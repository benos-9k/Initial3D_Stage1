package nz.net.initial3d;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import nz.net.initial3d.util.Queue;

/**
 * Profiling helper. Supports multi-threaded use and recursive section entry.
 * 
 * @author Ben Allen
 * 
 */
public final class Profiler {

	// note: this all falls down if System.nanoTime() is not consistent across threads

	private static final int MAX_SECTIONS = 1024;
	private static final int MAX_THREADS = 1024;

	// if profiler should auto reset
	private static boolean do_autoreset = true;

	// time between auto-resets, in nanoseconds
	private static volatile long reset_interval = 10 * 1000 * 1000 * 1000;

	// time of last reset
	private static long reset_last = System.nanoTime();

	// if reset should not print profile info
	private static boolean reset_mute = false;

	// start time
	private static final long time_init = System.nanoTime();

	// how many sections have been allocated
	private static int secid_count = 0;

	// section names
	private static final String[] sec_name = new String[MAX_SECTIONS];

	// per-thread profiler data
	// relies on thread ids being sane
	private static final ThreadData[] thread_data = new ThreadData[MAX_THREADS];

	// thread data for dead threads. processed by reset().
	private static final Queue<ThreadData> thread_dead = new Queue<ThreadData>(MAX_THREADS);

	private static final long[][] reset_sec_time = new long[MAX_THREADS][MAX_SECTIONS];
	private static final String[] reset_tname = new String[MAX_THREADS];

	static {
		Thread reset_thread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(reset_interval / 1000000);
						if (do_autoreset) Profiler.reset();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		reset_thread.setDaemon(true);
		reset_thread.start();
	}

	private static class ThreadData {

		public final Thread thread;
		public final long[] sec_time = new long[MAX_SECTIONS];
		public final long[] sec_start = new long[MAX_SECTIONS];
		public final int[] sec_entry = new int[MAX_SECTIONS];

		// is this fast enough?
		private final Lock lock = new ReentrantLock();

		public ThreadData() {
			thread = Thread.currentThread();
		}

		public void lock() {
			while (!lock.tryLock())
				;
		}

		public void unlock() {
			lock.unlock();
		}

	}

	private Profiler() {
		throw new AssertionError();
	}

	/**
	 * Check that the thread data object for the caller is initialised.
	 * 
	 * @return The thread ID of the caller.
	 */
	private static int checkThreadInit() {
		int threadid = (int) Thread.currentThread().getId();
		if (thread_data[threadid] == null) {
			// no thread data
			thread_data[threadid] = new ThreadData();
		} else if (thread_data[threadid].thread != Thread.currentThread()) {
			// thread data not for this thread
			thread_dead.add(thread_data[threadid]);
			thread_data[threadid] = new ThreadData();
		}
		return threadid;
	}

	/**
	 * Register a new section.
	 * 
	 * @return The ID of the newly-created section.
	 */
	public static synchronized int createSection(String name) {
		if (name == null) throw new NullPointerException();
		int id = secid_count++;
		sec_name[id] = name;
		return id;
	}

	/**
	 * Get the registered name for a section ID.
	 * 
	 * @param secid
	 *            Section ID.
	 * @return The name.
	 */
	public static String getSectionName(int secid) {
		return sec_name[secid];
	}

	/**
	 * Enter a section. Must be one of a pair with <code>Profiler.exit()</code>.
	 * 
	 * @param secid
	 *            Section ID, as returned by <code>Profiler.createSection()</code>.
	 */
	public static void enter(int secid) {
		int threadid = checkThreadInit();
		ThreadData td = thread_data[threadid];
		td.lock();
		try {
			// increment entry count
			if (td.sec_entry[secid]++ == 0) {
				// initial entry from this thread
				td.sec_start[secid] = System.nanoTime();
			}
		} finally {
			td.unlock();
		}
	}

	/**
	 * Exit a section. Must be one of a pair with <code>Profiler.enter()</code>.
	 * 
	 * @param secid
	 *            Section ID, as returned by <code>Profiler.createSection()</code>.
	 */
	public static void exit(int secid) {
		// shouldn't need to use checkThreadInit here
		int threadid = (int) Thread.currentThread().getId();
		ThreadData td = thread_data[threadid];
		td.lock();
		try {
			// decrement entry count
			if (--td.sec_entry[secid] == 0) {
				// exiting initial entry from this thread
				long delta = System.nanoTime() - td.sec_start[secid];
				td.sec_time[secid] += delta;
			}
		} finally {
			td.unlock();
		}
	}

	public static synchronized void setAutoResetEnabled(boolean enabled) {
		do_autoreset = enabled;
	}

	public static synchronized boolean getAutoResetEnabled() {
		return do_autoreset;
	}

	public static synchronized void setResetInterval(long nanos) {
		reset_interval = nanos;
	}

	public static synchronized long getResetInterval() {
		return reset_interval;
	}

	public static synchronized void setResetMute(boolean mute) {
		reset_mute = mute;
	}

	public static synchronized boolean getResetMute() {
		return reset_mute;
	}

	public static synchronized void reset() {
		long reset_time = System.nanoTime();
		// safe to use these because reset is synchronised
		long[][] sec_time = reset_sec_time;
		Arrays.fill(sec_time[0], 0);
		String[] tname = reset_tname;
		Arrays.fill(tname, null);
		tname[0] = "ALL";
		int ti = 1;

		// collect data on possibly live threads
		for (int i = 0; i < MAX_THREADS; i++) {
			ThreadData td = thread_data[i];
			if (td != null) {
				td.lock();
				try {
					tname[ti] = td.thread.getName();
					for (int j = 0; j < MAX_SECTIONS; j++) {
						long st = td.sec_time[j];
						td.sec_time[j] = 0;
						if (td.sec_entry[j] > 0) {
							// section in progress
							st += reset_time - td.sec_start[j];
							td.sec_start[j] = reset_time;
						}
						sec_time[0][j] += st;
						sec_time[ti][j] = st;
					}
					if (!td.thread.isAlive()) {
						// thread is dead, remove its data
						thread_data[i] = null;
					}
					ti++;
				} finally {
					td.unlock();
				}
			}
		}

		// collect data on definitely dead threads (ones whose ids had been recycled)
		// just hope dead ThreadData objects aren't continuously being generated
		for (ThreadData td = thread_dead.poll(); td != null; td = thread_dead.poll()) {
			td.lock();
			try {
				tname[ti] = td.thread.getName();
				for (int j = 0; j < MAX_SECTIONS; j++) {
					long st = td.sec_time[j];
					// can't possibly have a section in progress
					sec_time[0][j] += st;
					sec_time[ti][j] = st;
				}
				ti++;
			} finally {
				td.unlock();
			}
		}

		if (!reset_mute) {
			// prepare to print profile
			// find width of section name column
			int secname_width = 7;
			for (int i = 0; i < secid_count; i++) {
				if (sec_name[i].length() > secname_width) {
					secname_width = sec_name[i].length();
				}
			}

			// print
			final int thread_minwidth = 4;
			System.out.printf("\n ========== Profile @ %.2fs (%.2fs) ==========\n",
					(reset_time - time_init) / 1000000000d, (reset_time - reset_last) / 1000000000d);
			System.out.printf(" %" + secname_width + "s", "Section");
			for (int i = 0; i < ti; i++) {
				System.out.printf(" | %" + Math.max(tname[i].length(), thread_minwidth) + "s", tname[i]);
			}
			System.out.printf("\n ");
			for (int i = 0; i < secname_width; i++) {
				System.out.printf("-");
			}
			for (int i = 0; i < ti; i++) {
				System.out.printf("-+-");
				for (int j = 0; j < Math.max(tname[i].length(), thread_minwidth); j++) {
					System.out.printf("-");
				}
			}
			System.out.printf("\n");
			for (int i = 0; i < secid_count; i++) {
				System.out.printf(" %" + secname_width + "s", sec_name[i]);
				for (int j = 0; j < ti; j++) {
					System.out.printf(" | %" + Math.max(tname[j].length(), thread_minwidth) + ".2f", sec_time[j][i]
							/ (double) (reset_time - reset_last));
				}
				System.out.printf("\n");
			}
		}
		reset_last = reset_time;
	}

}
