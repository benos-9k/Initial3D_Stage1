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

	/*
	 * Enum Constants
	 */

	public static final int ZERO = 0;
	public static final int ONE = 1;

	// capabilities
	public static final int SCISSOR_TEST = 2;
	public static final int ALPHA_TEST = 3;
	public static final int DEPTH_TEST = 4;
	public static final int STENCIL_TEST = 5;
	public static final int CULL_FACE = 6;
	public static final int BLEND = 7;
	public static final int FOG = 8;
	public static final int LIGHTING = 9;
	public static final int TWO_SIDED_LIGHTING = 10;
	public static final int TEXTURE_2D = 11;
	public static final int MIPMAPS = 12;
	public static final int AUTO_ZFLIP = 13;
	public static final int WRITE_COLOR0 = 14;
	public static final int WRITE_COLOR1 = 15;
	public static final int WRITE_COLOR2 = 16;
	public static final int WRITE_COLOR3 = 17;
	public static final int WRITE_Z = 18;
	public static final int WRITE_STENCIL = 19;
	public static final int WRITE_ID = 20;
	public static final int ALPHAREF_RANDOM = 21;
	public static final int LIGHT0 = 22;
	public static final int LIGHT1 = 23;
	public static final int LIGHT2 = 24;
	public static final int LIGHT3 = 25;
	public static final int LIGHT4 = 26;
	public static final int LIGHT5 = 27;
	public static final int LIGHT6 = 28;
	public static final int LIGHT7 = 29;
	public static final int LIGHT_MAX = 1045;
	
	// or...
	public static enum Capability {
		SCISSOR_TEST, ALPHA_TEST, DEPTH_TEST, STENCIL_TEST;
	}
	
	// buffers
	public static final int BUFFER_COLOR0 = 1046;
	public static final int BUFFER_COLOR1 = 1047;
	public static final int BUFFER_COLOR2 = 1048;
	public static final int BUFFER_COLOR3 = 1049;
	public static final int BUFFER_Z = 1050;
	public static final int BUFFER_STENCIL = 1051;
	public static final int BUFFER_ID = 1052;
	
	public static enum Buffer {
		COLOR0, COLOR1, COLOR2, COLOR3, Z, STENCIL, ID;
	}

	// shademodels
	public static final int SHADEMODEL_FLAT = 1053;
	public static final int SHADEMODEL_SMOOTH = 1054;
	public static final int SHADEMODEL_PHONG = 1055;
	
	public static enum ShadeModel {
		FLAT, SMOOTH, PHONG;
	}

	// blend func parameters
	// share ZERO, ONE
	public static final int SRC_COLOR = 1056;
	public static final int ONE_MINUS_SRC_COLOR = 1057;
	public static final int DST_COLOR = 1058;
	public static final int ONE_MINUS_DST_COLOR = 1059;
	public static final int SRC_ALPHA = 1060;
	public static final int ONE_MINUS_SRC_ALPHA = 1061;
	public static final int DST_ALPHA = 1062;
	public static final int ONE_MINUS_DST_ALPHA = 1063;

	// comparison functions
	public static final int NEVER = 1064;
	public static final int LESS = 1065;
	public static final int LEQUAL = 1066;
	public static final int GREATER = 1067;
	public static final int GEQUAL = 1068;
	public static final int EQUAL = 1069;
	public static final int NOTEQUAL = 1070;
	public static final int ALWAYS = 1071;

	// stencil ops
	// stencil buffer as unsigned bytes
	// share ZERO
	public static final int KEEP = 1072;
	public static final int REPLACE = 1073;
	public static final int INCR = 1074; // clamp to 255
	public static final int INCR_WRAP = 1075; // wrap to 0
	public static final int DECR = 1076; // clamp to 0
	public static final int DECR_WRAP = 1077; // wrap to 255
	public static final int INVERT = 1078; // bitwise invert

	// faces
	public static final int FRONT = 1079;
	public static final int BACK = 1080;
	public static final int FRONT_AND_BACK = 1081;

	// light and material
	public static final int AMBIENT = 1082;
	public static final int DIFFUSE = 1083;
	public static final int SPECULAR = 1084;
	public static final int EMISSION = 1085;
	// opacity in diffuse alpha, shininess in specular alpha
	// but you can set them seperately of the color
	public static final int SHININESS = 1086;
	public static final int OPACITY = 1087;
	public static final int POSITION = 1088;
	public static final int SPOT_DIRECTION = 1089;
	public static final int CONSTANT_ATTENUATION = 1090;
	public static final int LINEAR_ATTENUATION = 1091;
	public static final int QUADRATIC_ATTENUATION = 1092;
	public static final int SPOT_CUTOFF = 1093;
	public static final int SPOT_EXPONENT = 1094;
	public static final int EFFECT_RADIUS = 1095;

	public abstract void enable(int cap);

	public abstract void disable(int cap);

	public abstract boolean isEnabled(int cap);

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

	public abstract void light(int light, int param, float v);

	public abstract void light(int light, int param, Color c);

	public abstract void light(int light, int param, Vec4 v);

	public abstract void light(int light, int param, Vec3 v);

	public abstract void sceneAmbient(Color c);

	public abstract void cullFace(int face);

	// matrices

	public abstract void matrixMode(long mode);

	public abstract Vec4 transformOne(Vec3 v);

	public abstract Vec4 transformOne(Vec4 v);

	public abstract void pushMatrix();

	public abstract void popMatrix();

	public void loadIdentity() {
		loadMatrix(new Mat4());
	}

	public void pushIdentity() {
		pushMatrix();
		loadIdentity();
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
