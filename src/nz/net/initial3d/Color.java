package nz.net.initial3d;

public class Color {
	
	public static final Color WHITE = new Color(1, 1, 1);
	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color RED = new Color(1, 0, 0);
	public static final Color GREEN = new Color(0, 1, 0);
	public static final Color BLUE = new Color(0, 0, 1);
	public static final Color YELLOW = new Color(1, 1, 0);
	public static final Color CYAN = new Color(0, 1, 1);
	public static final Color MAGENTA = new Color(1, 0, 1);
	public static final Color ORANGE = new Color(1, 0.5, 0);

	/** Alpha component (0 == transparent, 1 == opaque). */
	public final float a;

	/** Red component. */
	public final float r;

	/** Green component. */
	public final float g;

	/** Blue component. */
	public final float b;

	public Color(float a_, float r_, float g_, float b_) {
		a = a_;
		r = r_;
		g = g_;
		b = b_;
	}
	
	public Color(float r_, float g_, float b_) {
		this(1f, r_, g_, b_);
	}

	public Color(double a_, double r_, double g_, double b_) {
		a = (float) a_;
		r = (float) r_;
		g = (float) g_;
		b = (float) b_;
	}
	
	public Color(double r_, double g_, double b_) {
		this(1d, r_, g_, b_);
	}

	public Color(int argb_) {
		b = (argb_ & 0xFF) / 255f;
		argb_ >>>= 8;
		g = (argb_ & 0xFF) / 255f;
		argb_ >>>= 8;
		r = (argb_ & 0xFF) / 255f;
		argb_ >>>= 8;
		a = (argb_ & 0xFF) / 255f;
	}

	public int toARGB() {
		return (((int) (a * 255f)) << 24) | (((int) (r * 255f)) << 16) | (((int) (g * 255f)) << 8) | ((int) (b * 255f));
	}

}
