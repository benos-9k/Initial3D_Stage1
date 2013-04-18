package nz.net.initial3d;

public abstract class PolygonBuffer {

	public abstract int capacity();

	public abstract int count();

	public abstract void clear();

	public abstract int maxPolyVertices();

	public abstract void add(int[] v, int[] vt, int[] vn, int[] vc);

}
