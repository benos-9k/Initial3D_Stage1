package nz.net.initial3d;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import nz.net.initial3d.util.IntQueue;

/**
 * Profiling helper. Supports multi-threaded use and recursive section entry.
 * 
 * @author Ben Allen
 * 
 */
public final class Profiler {

	private static final int MAX_SECTIONS = 1024;
	private static final int MAX_CALLS = 1024;
	private static final int MAX_THREADS = 1024;

	// section names
	private static final String[] sec_name = new String[MAX_SECTIONS];
	// cumulative section times
	private static final AtomicLong[] sec_time = new AtomicLong[MAX_SECTIONS];

	// how many sections have been allocated
	private static final AtomicInteger secid_count = new AtomicInteger(0);

	// available call ids
	private static final IntQueue callid_queue = new IntQueue(MAX_CALLS);
	// entry / exit locks for calls
	private static final Lock[] call_lock = new Lock[MAX_CALLS];
	// time a specific call began
	private static final long[] call_start = new long[MAX_CALLS];
	// section a call is for
	private static final int[] call_sec = new int[MAX_CALLS];
	// if a call is in progress
	private static final boolean[] call_inprogress = new boolean[MAX_CALLS];

	// per-thread table of how many times a section has been entered
	// relies on thread ids being sane
	private static int[][] entry_count = new int[MAX_THREADS][];

	static {
		// push set of ids for specific section 'calls' into a synchronized queue
		for (int i = 0; i < MAX_CALLS; i++) {
			callid_queue.add(i);
		}
		// initialise section times
		for (int i = 0; i < MAX_SECTIONS; i++) {
			sec_time[i] = new AtomicLong(0);
		}
		// initialise call locks
		for (int i = 0; i < MAX_CALLS; i++) {
			call_lock[i] = new ReentrantLock();
		}
	}

	private Profiler() {
		throw new AssertionError();
	}

	/**
	 * Register a new section.
	 * 
	 * @return The ID of the newly-created section.
	 */
	public static int createSection(String name) {
		if (name == null) throw new NullPointerException();
		int id = secid_count.getAndIncrement();
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
	 * Enter a section.
	 * 
	 * @param secid
	 *            Section ID, as returned by <code>Profiler.createSection()</code>.
	 * @return An ID for this entry into the specified section.
	 */
	public static int enter(int secid) {
		int threadid = (int) Thread.currentThread().getId();
		if (entry_count[threadid] == null) {
			// need to create the entry count table for this thread
			entry_count[threadid] = new int[MAX_SECTIONS];
		}
		// acquire call id
		int callid = callid_queue.remove();
		// take the call lock
		while (!call_lock[callid].tryLock())
			;
		try {
			call_sec[callid] = secid;
			call_start[callid] = System.nanoTime();
			call_inprogress[callid] = true;
			entry_count[threadid][secid]++;
			return callid;
		} finally {
			call_lock[callid].unlock();
		}
	}

	/**
	 * Exit a section.
	 * 
	 * @param callid
	 *            The ID returned by the corresponding call to <code>Profiler.enter()</code>.
	 */
	public static void exit(int callid) {
		int threadid = (int) Thread.currentThread().getId();
		while (!call_lock[callid].tryLock())
			;
		try {
			int secid = call_sec[callid];
			if (--entry_count[threadid][secid] == 0) {
				// initial entry from this thread now exiting
				sec_time[secid].addAndGet(System.nanoTime() - call_start[callid]);
			}
			call_inprogress[callid] = false;
		} finally {
			call_lock[callid].unlock();
		}
		// release call id
		callid_queue.add(callid);
	}

	public static void reset() {
		// copy section times and clear them
		
		// go through call table
		// -- lock
		// -- if inprogress
		// -- -- add delta to copy of section times
		// -- -- reset call start time
		// -- unlock
		
		// print section times
	}

}
