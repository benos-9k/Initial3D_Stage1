package nz.net.initial3d;

import java.util.concurrent.atomic.AtomicInteger;

import nz.net.initial3d.util.IntQueue;


public class Profiler {

	public static final int MAX_ENTERED_SECTIONS = 2048;

	private IntQueue callid_queue = new IntQueue(2048);
	private long[] call_start = new long[MAX_ENTERED_SECTIONS];
	private int[] call_sec = new int[MAX_ENTERED_SECTIONS];

	private AtomicInteger secid_count = new AtomicInteger(0);

	public Profiler() {
		// push set of ids for specific section 'calls' into a synchronized queue
		for (int i = 0; i < MAX_ENTERED_SECTIONS; i++) {
			callid_queue.add(i);
		}
	}

	public int createSection() {
		return secid_count.getAndIncrement();
	}

	public int enter(int secid) {
		// FIXME
		int callid = callid_queue.remove();
		call_sec[callid] = secid;
		return callid;
	}

	public void exit(int callid) {
		// FIXME
		callid_queue.add(callid);
	}

	public void reset() {
		// FIXME
	}

}
