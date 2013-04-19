package nz.net.initial3d.util;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A version of Queue for primitive ints.
 *
 * @author Ben Allen
 *
 */
public class IntQueue {

	private final int[] data;
	private final int cap, mask;

	private final AtomicInteger count = new AtomicInteger(0);
	private final AtomicInteger i_read = new AtomicInteger(0);
	private final AtomicInteger i_write = new AtomicInteger(0);

	public IntQueue(int cap_) {
		if (cap_ < 1) throw new IllegalArgumentException();
		cap = cap_;
		int s = 1;
		for (cap_--; cap_ != 0; cap_ >>= 1, s <<= 1)
			;
		mask = s - 1;
		data = new int[s];
	}

	public int capacity() {
		return cap;
	}

	public void add(int t) {
		if (count.get() >= cap) throw new IllegalStateException();
		data[i_write.getAndIncrement() & mask] = t;
		count.incrementAndGet();
	}

	public boolean offer(int t) {
		if (count.get() >= cap) return false;
		data[i_write.getAndIncrement() & mask] = t;
		count.incrementAndGet();
		return true;
	}

	public int remove() {
		if (count.get() == 0) throw new NoSuchElementException();
		int t = data[i_read.getAndIncrement() & mask];
		count.decrementAndGet();
		return t;
	}

}