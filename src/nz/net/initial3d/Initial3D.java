package nz.net.initial3d;

/**
 * Initial3D Rendering API.
 *
 * Yes, much of this does mimic OpenGL 1 / 2.
 *
 *
 * @author Ben Allen
 *
 */
public abstract class Initial3D {

	public abstract void viewport(int w, int h);

	public abstract void begin(int mode);

	public void vertex(Vec3 v) {
		vertex(v.x, v.y, v.z);
	}

	public abstract void vertex(double vx, double vy, double vz);

	public void normal(Vec3 n) {
		normal(n.x, n.y, n.z);
	}

	public abstract void normal(double nx, double ny, double nz);

	public void color(Color c) {
		color(c.r, c.g, c.b, c.a);
	}

	public void color(double r, double g, double b) {
		color(r, g, b, 1);
	}

	public abstract void color(double r, double g, double b, double a);

	public abstract void texCoord(double u, double v);

	public abstract void end();

	public abstract void material(int face, int param, float f);

	public abstract void material(int face, int param, Color c);

	public abstract void blendFunc(int sfactor, int dfactor);

	public abstract void alphaFunc(int func, float ref);

	public abstract void depthFunc(int func);

	public abstract void stencilFunc(int func, int ref, int mask);

	public abstract void stencilOp(int sfail, int dfail, int dpass);

	public abstract void light(long light, int param, float v);

	public abstract void light(long light, int param, Color c);

	public abstract void light(long light, int param, Vec4 v);

	public abstract void light(long light, int param, Vec3 v);

	public abstract void sceneAmbient(Color c);

	public abstract void cullFace(int face);

	public abstract void enable(long state);

	public abstract void disable(long state);

	public abstract boolean isEnabled(long state);

	// matrices

	public abstract void matrixMode(long mode);

	public abstract Vec4 transformOne(Vec3 v);

	public abstract Vec4 transformOne(Vec4 v);

	public abstract void pushMatrix();

	public abstract void popMatrix();

	public void loadIdentity() {
		loadMatrix(new Mat4());
	}

	public abstract void loadMatrix(Mat4 m);

	public abstract void multMatrix(Mat4 m);

	public abstract Mat4 getMatrix();

	public void translate(Vec3 d) {
		multMatrix(d.toMatrix());
	}

	public void translate(double dx, double dy, double dz) {
		multMatrix(Mat4.createTranslate(dx, dy, dz));
	}

	public void scale(double f) {
		multMatrix(Mat4.createScale(f, f, f));
	}

	public void scale(Vec3 f) {
		multMatrix(Mat4.createScale(f.x, f.y, f.z));
	}

	public void scale(double fx, double fy, double fz) {
		multMatrix(Mat4.createScale(fx, fy, fz));
	}

	public void rotate(Quat q) {
		multMatrix(q.toMatrix());
	}

	public void rotate(double angle, Vec3 axis) {
		multMatrix(new Quat(angle, axis).toMatrix());
	}

	public void rotate(double angle, double ax, double ay, double az) {
		multMatrix(new Quat(angle, new Vec3(ax, ay, az)).toMatrix());
	}

}
