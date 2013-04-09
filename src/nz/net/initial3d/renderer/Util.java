package nz.net.initial3d.renderer;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

import nz.net.initial3d.*;

final class Util {

	private static Unsafe unsafe;

	private Util() {
		throw new AssertionError("You're doing it wrong.");
	}

	/** Get the <code>sun.misc.Unsafe</code> singleton. */
	public static Unsafe getUnsafe() {
		if (unsafe == null) {
			try {
				Field field = Unsafe.class.getDeclaredField("theUnsafe");
				field.setAccessible(true);
				unsafe = (Unsafe) field.get(null);
			} catch (Throwable t) {
				throw new I3DException("Failed to obtain the sun.misc.Unsafe singleton.", t);
			}
		}
		return unsafe;
	}

	/**
	 * Fast approximation for inverse square root of a positive float. From <a
	 * href=http://en.wikipedia.org/wiki/Fast_inverse_square_root>Wikipedia</a>.
	 */
	public static float fastInverseSqrt(float x) {
		float x2 = x * 0.5f;
		// evil floating point bit level hacking
		x = Float.intBitsToFloat(0x5f3759df - (Float.floatToRawIntBits(x) >>> 1));
		// 1st iteration of newton's method
		x = x * (1.5f - (x2 * x * x));
		return x;
	}

	/**
	 * Fast approximation for inverse of a positive float. Identical to <code>fastInverseSqrt()</code>, except the
	 * operand is squared.
	 */
	public static float fastInverse(float x) {
		x *= x;
		float x2 = x * 0.5f;
		// evil floating point bit level hacking
		x = Float.intBitsToFloat(0x5f3759df - (Float.floatToRawIntBits(x) >>> 1));
		// 1st iteration of newton's method
		x = x * (1.5f - (x2 * x * x));
		return x;
	}

	public static double clamp(double value, double lower, double upper) {
		return value < lower ? lower : value > upper ? upper : value;
	}

	public static float clamp(float value, float lower, float upper) {
		return value < lower ? lower : value > upper ? upper : value;
	}

	public static long clamp(long value, long lower, long upper) {
		return value < lower ? lower : value > upper ? upper : value;
	}

	public static int clamp(int value, int lower, int upper) {
		return value < lower ? lower : value > upper ? upper : value;
	}

	/** Wrapper for <code>System.currentTimeMillis()</code>. */
	public static long time() {
		return System.currentTimeMillis();
	}

	/** Wrapper for <code>System.nanoTime()</code>. */
	public static long timenanos() {
		return System.nanoTime();
	}

	/** Wrapper for <code>System.out.printf()</code>. */
	public static void printf(String fmt, Object... args) {
		System.out.printf(fmt, args);
	}

	/** Wrapper for <code>String.format()</code>. */
	public static String sprintf(String fmt, Object... args) {
		return String.format(fmt, args);
	}

	/** Wrapper for <code>System.out.println()</code>. */
	public static void puts(Object s) {
		System.out.println(s);
	}

	/** Wrapper for <code>Thread.sleep()</code> that suppresses InterruptedException. */
	public static void pause(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// do nothing
		}
	}

	/** Throw a new I3DException with the specified message. Is declared to return I3DException for convenience only. */
	public static I3DException nope(String msg) {
		throw new I3DException(msg);
	}

	public static void writeVector(Unsafe unsafe, long pTarget, double x, double y, double z, double w) {
		unsafe.putDouble(pTarget, x);
		unsafe.putDouble(pTarget + 8, y);
		unsafe.putDouble(pTarget + 16, z);
		unsafe.putDouble(pTarget + 24, w);
	}

	public static double vectorDot(Unsafe unsafe, long pVec, double cx, double cy, double cz) {
		double dot = unsafe.getDouble(pVec) * cx;
		dot += unsafe.getDouble(pVec + 8) * cy;
		dot += unsafe.getDouble(pVec + 16) * cz;
		return dot;
	}

	public static double vectorDot(Unsafe unsafe, long pVecA, long pVecB) {
		double dot = unsafe.getDouble(pVecA) * unsafe.getDouble(pVecB);
		dot += unsafe.getDouble(pVecA + 8) * unsafe.getDouble(pVecB + 8);
		dot += unsafe.getDouble(pVecA + 16) * unsafe.getDouble(pVecB + 16);
		return dot;
	}

	public static void vectorPlaneNorm(Unsafe unsafe, long pTarget, long pVec0, long pVec1, long pVec2) {
		double dx01 = unsafe.getDouble(pVec1) - unsafe.getDouble(pVec0);
		double dy01 = unsafe.getDouble(pVec1 + 8) - unsafe.getDouble(pVec0 + 8);
		double dz01 = unsafe.getDouble(pVec1 + 16) - unsafe.getDouble(pVec0 + 16);
		double dx12 = unsafe.getDouble(pVec2) - unsafe.getDouble(pVec1);
		double dy12 = unsafe.getDouble(pVec2 + 8) - unsafe.getDouble(pVec1 + 8);
		double dz12 = unsafe.getDouble(pVec2 + 16) - unsafe.getDouble(pVec1 + 16);
		// now do d01 cross d12
		unsafe.putDouble(pTarget, dy01 * dz12 - dz01 * dy12);
		unsafe.putDouble(pTarget + 8, dz01 * dx12 - dx01 * dz12);
		unsafe.putDouble(pTarget + 16, dx01 * dy12 - dy01 * dx12);
		unsafe.putDouble(pTarget + 24, 1);
	}

	public static void vectorCross(Unsafe unsafe, long pTarget, long pVec0, long pVec1) {
		double x0 = unsafe.getDouble(pVec0);
		double y0 = unsafe.getDouble(pVec0 + 8);
		double z0 = unsafe.getDouble(pVec0 + 16);
		double x1 = unsafe.getDouble(pVec1);
		double y1 = unsafe.getDouble(pVec1 + 8);
		double z1 = unsafe.getDouble(pVec1 + 16);
		unsafe.putDouble(pTarget, y0 * z1 - z0 * y1);
		unsafe.putDouble(pTarget + 8, z0 * x1 - x0 * z1);
		unsafe.putDouble(pTarget + 16, x0 * y1 - y0 * x1);
		unsafe.putDouble(pTarget + 24, 1);
	}

}
