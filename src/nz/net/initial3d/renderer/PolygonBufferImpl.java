package nz.net.initial3d.renderer;

import nz.net.initial3d.*;

final class PolygonBufferImpl extends PolygonBuffer {

	final int[] pdata;
	final int capacity, maxv, stride;
	int count = 0;

	// idea:
	// vertex (array) struct, store vcount in each
	// can put flags into vcount if needed, only need < 16 bits for actual vcount
	// [vcount, v, vt, vn, vc0, vc1]

	// FIXME change array vertex struct impl!

	PolygonBufferImpl(int capacity_, int maxv_) {
		if (capacity_ < 1) throw new IllegalArgumentException("Minimum allowed capacity is 1.");
		if (maxv_ < 3) throw new IllegalArgumentException("Minimum allowed max vertices per polygon is 3.");
		capacity = capacity_;
		maxv = maxv_;
		stride = maxv * 4 + 4;
		pdata = new int[capacity * stride];
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
	public int maxPolyVertices() {
		return maxv;
	}

	@Override
	public void add(int[] v, int[] vt, int[] vn, int[] vc) {
		if (count == capacity) throw new IndexOutOfBoundsException();
		if (v == null) throw new IllegalArgumentException();
		if (v.length < 3) throw new IllegalArgumentException("Less than 3 vertices does not define a valid polygon.");
		if (vn != null && v.length != vn.length) {
			throw new IllegalArgumentException("Incorrect size for vn.");
		}
		if (vt != null && v.length != vt.length) {
			throw new IllegalArgumentException("Incorrect size for vt.");
		}
		if (vc != null && v.length != vc.length) {
			throw new IllegalArgumentException("Incorrect size for vc.");
		}
		if (v.length > maxv) throw new IllegalArgumentException("Too many vertices.");
		// write vertex count
		int i = count * stride;
		pdata[i] = v.length;
		// write vertex data
		i += 4;
		for (int j = 0; j < v.length; j++) {
			pdata[i] = v[j];
			if (vt != null) pdata[i + 1] = vt[j];
			if (vn != null) pdata[i + 2] = vn[j];
			if (vc != null) pdata[1 + 3] = vc[j];
			i += 4;
		}
		count++;
	}
}
