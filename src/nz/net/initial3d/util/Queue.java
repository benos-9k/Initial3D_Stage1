package nz.net.initial3d.util;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A (fast) synchronized queue with only non-blocking ops (that's why it's fast).
 *
 * @author Ben Allen
 *
 */
public class Queue<T> {

	private final Object[] data;
	private final int cap, mask;

	private final AtomicInteger count = new AtomicInteger(0);
	private final AtomicInteger i_read = new AtomicInteger(0);
	private final AtomicInteger i_write = new AtomicInteger(0);

	public Queue(int cap_) {
		if (cap_ < 1) throw new IllegalArgumentException();
		cap = cap_;
		int s = 1;
		for (cap_--; cap_ != 0; cap_ >>= 1, s <<= 1);
		mask = s - 1;
		data = new Object[s];
	}

	public int capacity() {
		return cap;
	}

	public void add(T t) {
		if (t == null) throw new NullPointerException();
		if (count.get() >= cap) throw new IllegalStateException();
		data[i_write.getAndIncrement() & mask] = t;
		count.incrementAndGet();
	}

	public boolean offer(T t) {
		if (t == null) throw new NullPointerException();
		if (count.get() >= cap) return false;
		data[i_write.getAndIncrement() & mask] = t;
		count.incrementAndGet();
		return true;
	}

	public T remove() {
		if (count.get() == 0) throw new NoSuchElementException();
		@SuppressWarnings("unchecked")
		T t = (T) data[i_read.getAndIncrement() & mask];
		count.decrementAndGet();
		return t;
	}

	public T poll() {
		if (count.get() == 0) return null;
		@SuppressWarnings("unchecked")
		T t = (T) data[i_read.getAndIncrement() & mask];
		count.decrementAndGet();
		return t;
	}

}
