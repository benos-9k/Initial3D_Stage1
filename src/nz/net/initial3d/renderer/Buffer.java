package nz.net.initial3d.renderer;

import java.util.concurrent.atomic.AtomicInteger;

import sun.misc.Unsafe;

public class Buffer {

	private static final Unsafe unsafe = Util.getUnsafe();

	@SuppressWarnings("unchecked")
	private static final Queue<Buffer>[] buf_queues = new Queue[31];

	static {
		for (int i = 0; i < buf_queues.length; i++) {
			buf_queues[i] = new Queue<Buffer>(2048 >> (i / 3));
			// System.out.println(i + " : " + buf_queues[i].capacity());
		}
	}

	private static int sizeindex(int b) {
		if (b < 1) return 0;
		int s = 0;
		for (b--; b != 0; b >>= 1, s++);
		return s;
	}

	public static Buffer alloc(int bytes) {
		if (bytes < 1) throw new IllegalArgumentException();
		int s = sizeindex(bytes);
		Buffer buf = buf_queues[s].poll();
		if (buf == null) {
			// System.out.println("Buffer malloc");
			long p = unsafe.allocateMemory(bytes);
			if (p == 0) {
				// malloc failed
				// possibly free all buffers currently in the queues?
				throw new OutOfMemoryError("Malloc failed for " + bytes + " bytes.");
			}
			buf = new Buffer(s, p);
		}
		buf.acquire();
		return buf;
	}

	private int sidx;
	private AtomicInteger refcount;
	private long pBuffer;

	private Buffer(int sidx_, long pBuffer_) {
		sidx = sidx_;
		pBuffer = pBuffer_;
		refcount = new AtomicInteger(0);
	}

	public int getSize() {
		return 1 << sidx;
	}

	public long getPointer() {
		return pBuffer;
	}

	public void acquire() {
		refcount.incrementAndGet();
	}

	public void release() {
		if (refcount.decrementAndGet() == 0) {
			// return to pool
			if (!buf_queues[sidx].offer(this)) {
				// no space in pool queue
				unsafe.freeMemory(pBuffer);
			}
			// System.out.println("Returning " + this + " to pool.");
		}
	}

	public int getInt(long q) {
		return unsafe.getInt(pBuffer + q);
	}

	public void putInt(long q, int val) {
		unsafe.putInt(pBuffer + q, val);
	}

	@Override
	public String toString() {
		return String.format("Buffer[%d bytes]@%x", getSize(), getPointer());
	}

}
