package nz.net.initial3d.renderer;

import static nz.net.initial3d.renderer.Util.*;
import nz.net.initial3d.*;

import sun.misc.Unsafe;

final class VectorBufferImpl extends VectorBuffer {

	private static final Unsafe unsafe = getUnsafe();
	
	final long pBuf;
	final int capacity;
	int count;
	
	VectorBufferImpl(int capacity_) {
		if (capacity_ < 1) throw new IllegalArgumentException("Capacity must be at least 1.");
		capacity = capacity_;
		pBuf = unsafe.allocateMemory(capacity * 32);
		count = 0;
	}
	
	@Override
	protected void finalize() {
		unsafe.freeMemory(pBuf);
	}
	
	@Override
	public int capacity() {
		return capacity;
	}

	@Override
	public int count() {
		return count;
	}

	@Override
	public void clear() {
		count = 0;
	}

	@Override
	public void add(double x, double y, double z, double w) {
		if (count >= capacity) throw new IndexOutOfBoundsException("Buffer is full.");
		long p = pBuf + count * 32;
		unsafe.putDouble(p, x);
		unsafe.putDouble(p + 8, y);
		unsafe.putDouble(p + 16, z);
		unsafe.putDouble(p + 24, w);
		count++;
	}

}
