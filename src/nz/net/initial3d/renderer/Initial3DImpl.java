package nz.net.initial3d.renderer;

import static nz.net.initial3d.renderer.Util.*;
import static nz.net.initial3d.renderer.Type.*;
import nz.net.initial3d.*;
import sun.misc.Unsafe;

public final class Initial3DImpl extends Initial3D {

	private static final Unsafe unsafe = getUnsafe();

	// this pointer is the key to EVERYTHING
	private final long pBase;

	private final PolygonPipe polypipe;
	private final RasterPipe rasterpipe;

	public Initial3DImpl() {
		this(Runtime.getRuntime().availableProcessors());
	}

	public Initial3DImpl(int rasterthreads_) {
		pBase = unsafe.allocateMemory(i3d_t.SIZEOF());
		rasterpipe = new RasterPipe(pBase, rasterthreads_);
		polypipe = new PolygonPipe(pBase);
		polypipe.connectRasterPipe(rasterpipe);

	}

	@Override
	protected void finalize() {
		unsafe.freeMemory(pBase);
	}

	@Override
	public int queryEnum(String name) {
		if ("I3DX_FOG_A".equals(name)) {
			return 11199;
		} else if ("I3DX_FOG_B".equals(name)) {
			return 11198;
		} else {
			return queryEnumAPI(name);
		}
	}

	@Override
	public Method queryMethod(String name, Class<?>... paramtypes) {
		if ("flipZSign".equals(name)) {
			if (paramtypes.length == 0) {
				return new Method() {

					@Override
					public Object call(Object... args) {
						Initial3DImpl.this.flipZSign();
						return null;
					}

				};
			} else {
				throw nope("Method flipZSign takes 0 parameters.");
			}
		} else {
			return queryMethodAPI(name, paramtypes);
		}
	}

	@Override
	public FrameBuffer createFrameBuffer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bindFrameBuffer(FrameBuffer fb) {
		// TODO Auto-generated method stub

	}

	@Override
	public FrameBuffer getFrameBuffer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Texture2D createTexture2D(int size_u, int size_v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bindTexture(int target, Texture2D tex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enable(int cap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disable(int cap) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEnabled(int cap) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void projectionMode(int mode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void polygonMode(int face, int mode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shadeModel(int model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void viewport(int w, int h) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear(int... buffers) {
		// TODO Auto-generated method stub

	}

	public void flipZSign() {
		System.out.println("flippity");
		// TODO flipZSign
	}

	@Override
	public void bindVertexBuffer(int att, VectorBuffer vbuf) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawPolygons(PolygonBuffer pbuf, int offset, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void begin(int mode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vertex(double x, double y, double z) {
		// TODO Auto-generated method stub

	}

	@Override
	public void normal(double x, double y, double z) {
		// TODO Auto-generated method stub

	}

	@Override
	public void color(double r, double g, double b, double a) {
		// TODO Auto-generated method stub

	}

	@Override
	public void secondaryColor(double r, double g, double b, double a) {
		// TODO Auto-generated method stub

	}

	@Override
	public void texCoord(double u, double v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void end() {
		// TODO Auto-generated method stub

	}

	@Override
	public void material(int face, int param, double f) {
		// TODO Auto-generated method stub

	}

	@Override
	public void material(int face, int param, double r, double g, double b, double a) {
		// TODO Auto-generated method stub

	}

	@Override
	public void blendFunc(int sfactor, int dfactor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void alphaFunc(int func, double ref) {
		// TODO Auto-generated method stub

	}

	@Override
	public void depthFunc(int func) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stencilFuncSeparate(int face, int func, int ref, int mask) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stencilOpSeparate(int face, int sfail, int dfail, int dpass) {
		// TODO Auto-generated method stub

	}

	@Override
	public void light(int light, int param, double v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void light(int light, int param, double f0, double f1, double f2, double f3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sceneAmbient(double r, double g, double b, double a) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fog(int param, double val) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fog(int param, double r, double g, double b, double a) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cullFace(int face) {
		// TODO Auto-generated method stub

	}

	@Override
	public void matrixMode(int mode) {
		// TODO Auto-generated method stub

	}

	@Override
	public Vec4 transformOne(Vec3 v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vec4 transformOne(Vec4 v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pushMatrix() {
		// TODO Auto-generated method stub

	}

	@Override
	public void popMatrix() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadMatrix(Mat4 m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void multMatrix(Mat4 m) {
		// TODO Auto-generated method stub

	}

	@Override
	public Mat4 getMatrix() {
		// TODO Auto-generated method stub
		return null;
	}

}
