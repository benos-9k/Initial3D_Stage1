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
	
	public abstract void vertex(Vec3 v);
	
	public abstract void vertex(double vx, double vy, double vz);
	
	public abstract void normal(Vec3 n);
	
	public abstract void normal(double nx, double ny, double nz);
	
	public abstract void color(Color c);
	
	public abstract void color(double r, double g, double b);
	
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
	
	public abstract void loadIdentity();
	
	public abstract void loadMatrix(Mat4 m);
	
	public abstract void multMatrix(Mat4 m);
	
	public abstract Mat4 readMatrix();
	
	public abstract void translate(Vec3 d);
	
	public abstract void translate(double dx, double dy, double dz);
	
	public abstract void scale(double f);
	
	public abstract void scale(Vec3 f);
	
	public abstract void scale(double fx, double fy, double fz);
	
	public abstract void rotate(Quat q);
	
	public abstract void rotate(double angle, Vec3 axis);
	
	public abstract void rotate(double angle, double ax, double ay, double az);
	
}
