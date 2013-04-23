package nz.net.initial3d.renderer;

import static nz.net.initial3d.renderer.Util.*;
import static nz.net.initial3d.renderer.Type.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import nz.net.initial3d.*;
import sun.misc.Unsafe;

public final class Initial3DImpl extends Initial3D {

	private static final Unsafe unsafe = getUnsafe();

	// exposed methods and enum constants
	List<String> exposed_methods = new ArrayList<String>();
	List<String> exposed_enums = new ArrayList<String>();

	// enum constants
	public static final int I3DX_FOG_A = 11199;
	public static final int I3DX_FOG_B = 11198;

	// pipes
	private final PolygonPipe polypipe;
	private final RasterPipe rasterpipe;

	// defaults
	private final FrameBufferImpl default_framebuffer;
	private final Texture2DImpl default_tex;
	private final VectorBufferImpl default_vbo, default_vbo_color;

	// state
	Stack<State> state = new Stack<State>();

	class State {

		final Buffer buf_base;
		final long pBase;

		FrameBufferImpl bound_framebuffer;
		VectorBufferImpl bound_vbo_v, bound_vbo_vt, bound_vbo_vn, bound_vbo_c0, bound_vbo_c1;
		int begin_mode = -1;
		final VectorBufferImpl begin_vbo_v, begin_vbo_vt, begin_vbo_vn, begin_vbo_c0, begin_vbo_c1;
		final int[] protopoly = new int[256];

		final Stack<Mat4> modelview;
		final Stack<Mat4> projection;
		Stack<Mat4> matrix_active;
		int matrix_mode;

		State() {
			buf_base = Buffer.alloc(i3d_t.SIZEOF(), -1);
			pBase = buf_base.getPointer();

			// bind default framebuffer
			bound_framebuffer = default_framebuffer;

			// bind default vector buffers
			bound_vbo_v = default_vbo;
			bound_vbo_vt = default_vbo;
			bound_vbo_vn = default_vbo;
			bound_vbo_c0 = default_vbo_color;
			bound_vbo_c1 = default_vbo_color;

			// create vbos used by begin / end
			begin_vbo_v = new VectorBufferImpl(1024);
			begin_vbo_vt = new VectorBufferImpl(1024);
			begin_vbo_vn = new VectorBufferImpl(1024);
			begin_vbo_c0 = new VectorBufferImpl(1024);
			begin_vbo_c1 = new VectorBufferImpl(1024);

			// setup matrices
			modelview = new Stack<Mat4>();
			projection = new Stack<Mat4>();
			matrix_active = modelview;
			matrix_mode = MODELVIEW;
			modelview.push(new Mat4());
			projection.push(new Mat4());
		}

		State(State other_) {
			buf_base = Buffer.alloc(i3d_t.SIZEOF(), -1);
			pBase = buf_base.getPointer();
			unsafe.copyMemory(other_.pBase, pBase, i3d_t.SIZEOF());

			// bind framebuffer
			bound_framebuffer = other_.bound_framebuffer;

			// bind vector buffers
			bound_vbo_v = other_.bound_vbo_v;
			bound_vbo_vt = other_.bound_vbo_vt;
			bound_vbo_vn = other_.bound_vbo_vn;
			bound_vbo_c0 = other_.bound_vbo_c0;
			bound_vbo_c1 = other_.bound_vbo_c1;

			// copy stuff used by begin / end
			begin_vbo_v = new VectorBufferImpl(other_.begin_vbo_v);
			begin_vbo_vt = new VectorBufferImpl(other_.begin_vbo_vt);
			begin_vbo_vn = new VectorBufferImpl(other_.begin_vbo_vn);
			begin_vbo_c0 = new VectorBufferImpl(other_.begin_vbo_c0);
			begin_vbo_c1 = new VectorBufferImpl(other_.begin_vbo_c1);
			begin_mode = other_.begin_mode;
			System.arraycopy(other_.protopoly, 0, protopoly, 0, protopoly.length);

			// copy matrices
			modelview = new Stack<Mat4>();
			for (Mat4 m : other_.modelview) {
				modelview.push(m.clone());
			}
			projection = new Stack<Mat4>();
			for (Mat4 m : other_.projection) {
				projection.push(m.clone());
			}
			// activate the correct matrix stack
			matrixMode(other_.matrix_mode);
		}

		@Override
		protected void finalize() {
			buf_base.release();
		}

		private void initUnsafeState() {
			// this shouldn't require pipeline to be finished
			// init the 'unsafe' parts of the state that depend on 'safe' parts
			// specifically the matrices, and anything dependent on them, like the clipfuncs

		}

		public void bindFrameBuffer(FrameBuffer fb) {
			if (fb == null) {
				bound_framebuffer = default_framebuffer;
			} else {
				bound_framebuffer = (FrameBufferImpl) fb;
			}
		}

		public FrameBuffer getFrameBuffer() {
			return bound_framebuffer;
		}

		public void bindTexture(int target, Texture2D tex) {
			// TODO Auto-generated method stub

		}

		public void enable(int... caps) {
			for (int cap : caps) {
				// TODO enable
			}
		}

		public void disable(int... caps) {
			for (int cap : caps) {
				// TODO disable
			}
		}

		public boolean isEnabled(int cap) {
			// TODO is enabled
			return false;
		}

		public void projectionMode(int mode) {
			// TODO projection mode

		}

		public void polygonMode(int face, int mode) {
			// TODO polymode
			switch (face) {
			case FRONT:

				break;
			case BACK:

				break;
			case FRONT_AND_BACK:

				break;
			default:
				throw nope("Invalid enum.");
			}
		}

		public void shadeModel(int model) {
			// TODO shade model

		}

		public void viewport(int w, int h) {
			// TODO viewport

		}

		public void clear(int... buffers) {
			finish();
			// TODO Auto-generated method stub

		}

		public void flipZSign() {
			finish();
			System.out.println("flippity");
			// TODO flip zsign
		}

		public void bindVertexBuffer(int att, VectorBuffer vbuf) {
			if (vbuf == null) {
				// revert to default
				switch (att) {
				case VERTEX_POSITION:
					bound_vbo_v = default_vbo;
					break;
				case VERTEX_TEXCOORD:
					bound_vbo_vt = default_vbo;
					break;
				case VERTEX_NORMAL:
					bound_vbo_vn = default_vbo;
					break;
				case VERTEX_COLOR0:
					bound_vbo_c0 = default_vbo_color;
					break;
				case VERTEX_COLOR1:
					bound_vbo_c1 = default_vbo_color;
					break;
				default:
					throw nope("Invalid enum.");
				}
			} else {
				// bind new
				switch (att) {
				case VERTEX_POSITION:
					bound_vbo_v = (VectorBufferImpl) vbuf;
					break;
				case VERTEX_TEXCOORD:
					bound_vbo_vt = (VectorBufferImpl) vbuf;
					break;
				case VERTEX_NORMAL:
					bound_vbo_vn = (VectorBufferImpl) vbuf;
					break;
				case VERTEX_COLOR0:
					bound_vbo_c0 = (VectorBufferImpl) vbuf;
					break;
				case VERTEX_COLOR1:
					bound_vbo_c1 = (VectorBufferImpl) vbuf;
					break;
				default:
					throw nope("Invalid enum.");
				}
			}
		}

		public void drawPolygons(PolygonBuffer pbuf, int offset, int count) {
			// TODO draw polys

		}

		public void begin(int mode) {
			// clear buffers
			state.peek().begin_vbo_v.clear();
			state.peek().begin_vbo_vt.clear();
			state.peek().begin_vbo_vn.clear();
			state.peek().begin_vbo_c0.clear();
			state.peek().begin_vbo_c1.clear();
			// need valid first vector
			state.peek().begin_vbo_v.add(0, 0, 0, 0);
			state.peek().begin_vbo_vt.add(0, 0, 0, 0);
			state.peek().begin_vbo_vn.add(0, 0, 0, 0);
			state.peek().begin_vbo_c0.add(1, 1, 1, 1);
			state.peek().begin_vbo_c1.add(1, 1, 1, 1);

			switch (mode) {
			case POLYGON:
				// clear polygon
				state.peek().protopoly[0] = 0;
				break;
			case LINE_STRIP:
				nope("LINE_STRIP unimplimented.");
				break;
			case LINE_LOOP:
				nope("LINE_LOOP unimplimented.");
				break;
			default:
				throw nope("Invalid enum.");
			}

			state.peek().begin_mode = mode;
		}

		public void vertex(double x, double y, double z) {
			state.peek().begin_vbo_v.add(x, y, z, 1);
			// bind to previously set normal / texcoord
			switch (state.peek().begin_mode) {
			case POLYGON:
				int vcount = state.peek().protopoly[0] + 1;
				state.peek().protopoly[0] = vcount;
				state.peek().protopoly[vcount * 4] = state.peek().begin_vbo_v.count() - 1;
				state.peek().protopoly[vcount * 4 + 1] = state.peek().begin_vbo_vt.count() - 1;
				state.peek().protopoly[vcount * 4 + 2] = state.peek().begin_vbo_vn.count() - 1;
				state.peek().protopoly[vcount * 4 + 3] = state.peek().begin_vbo_c0.count() - 1;
				break;
			default:
				throw nope("WTF?!");
			}
		}

		public void normal(double x, double y, double z) {
			begin_vbo_vn.add(x, y, z, 0);
		}

		public void color(double r, double g, double b, double a) {
			begin_vbo_c0.add(a, r, g, b);
		}

		public void secondaryColor(double r, double g, double b, double a) {
			begin_vbo_c1.add(a, r, g, b);
		}

		public void texCoord(double u, double v) {
			begin_vbo_vt.add(u, v, 0, 1);
		}

		public void end() {
			// TODO Auto-generated method stub

		}

		public void material(int face, int param, double f) {
			// TODO Auto-generated method stub

		}

		public void material(int face, int param, double r, double g, double b, double a) {
			// TODO Auto-generated method stub

		}

		public void blendFunc(int sfactor, int dfactor) {
			// TODO Auto-generated method stub

		}

		public void alphaFunc(int func, double ref) {
			// TODO Auto-generated method stub

		}

		public void depthFunc(int func) {
			// TODO Auto-generated method stub

		}

		public void stencilFuncSeparate(int face, int func, int ref, int mask) {
			// TODO Auto-generated method stub

		}

		public void stencilOpSeparate(int face, int sfail, int dfail, int dpass) {
			// TODO Auto-generated method stub

		}

		public void light(int light, int param, double v) {
			// TODO Auto-generated method stub

		}

		public void light(int light, int param, double f0, double f1, double f2, double f3) {
			// TODO Auto-generated method stub

		}

		public void sceneAmbient(double r, double g, double b, double a) {
			// TODO Auto-generated method stub

		}

		public void fog(int param, double val) {
			// TODO Auto-generated method stub

		}

		public void fog(int param, double r, double g, double b, double a) {
			// TODO Auto-generated method stub

		}

		public void initFog() {
			finish();
			System.out.println("foggity");
			// TODO init fog
		}

		public void cullFace(int face) {
			// TODO Auto-generated method stub

		}

		public void finish() {
			// TODO Auto-generated method stub

		}

		public void matrixMode(int mode) {
			switch (mode) {
			case MODELVIEW:
				matrix_active = modelview;
				break;
			case PROJECTION:
				matrix_active = projection;
				break;
			default:
				throw nope("Invalid enum.");
			}
			matrix_mode = mode;
		}

		public Vec3 transformOne(Vec3 v) {
			return matrix_active.peek().mul(v);
		}

		public Vec4 transformOne(Vec4 v) {
			return matrix_active.peek().mul(v);
		}

		public void pushMatrix() {
			matrix_active.push(matrix_active.peek().clone());
		}

		public void popMatrix() {
			if (matrix_active.size() > 1) {
				matrix_active.pop();
			} else {
				throw nope("Can't pop the last matrix off the stack!");
			}
		}

		public void loadMatrix(Mat4 m) {
			matrix_active.peek().set(m);
		}

		public void multMatrix(Mat4 m) {
			matrix_active.peek().setMul(m);
		}

		public Mat4 getMatrix() {
			return matrix_active.peek().clone();
		}

	}

	public Initial3DImpl() {
		this(Runtime.getRuntime().availableProcessors());
	}

	public Initial3DImpl(int rasterthreads_) {
		rasterpipe = new RasterPipe(rasterthreads_);
		polypipe = new PolygonPipe();
		polypipe.connectRasterPipe(rasterpipe);

		// expose methods
		exposed_methods.add("flipZSign");
		exposed_methods.add("initFog");

		// expose enum constants
		exposed_enums.add("I3DX_FOG_A");
		exposed_enums.add("I3DX_FOG_B");

		// create default framebuffer
		default_framebuffer = new FrameBufferImpl();
		default_framebuffer.attachBuffer(BUFFER_COLOR0, new Texture2DImpl(1024, 1024));
		default_framebuffer.attachBuffer(BUFFER_Z, new Texture2DImpl(1024, 1024));
		default_framebuffer.setZSign(1);

		// create default texture (1x1 texel black)
		default_tex = new Texture2DImpl(1, 1);

		// create default vector buffers
		default_vbo = new VectorBufferImpl(1);
		default_vbo.add(0, 0, 0, 0);
		default_vbo_color = new VectorBufferImpl(1);
		default_vbo_color.add(1, 1, 1, 1);

		// setup initial state
		state.push(new State());

		projectionMode(ORTHOGRAPHIC);
	}

	@Override
	protected void finalize() {
		// don't think anything needs to be here?
	}

	@Override
	public void pushState() {
		state.push(new State(state.peek()));
	}

	@Override
	public void popState() {
		if (state.size() > 1) {
			state.pop();
		} else {
			throw nope("Can't pop the last state off the stack!");
		}
	}

	@Override
	public int queryEnum(String name) {
		if (exposed_enums.contains(name)) {
			return getEnumWithReflection(name);
		}
		// else just use the api base method
		else {
			return super.queryEnum(name);
		}
	}

	private int getEnumWithReflection(String name) {
		try {
			java.lang.reflect.Field f = Initial3DImpl.class.getField(name);
			return (Integer) f.get(null);
		} catch (Throwable t) {
			throw new I3DException("Unable to get implementation enum constant " + name, t);
		}
	}

	@Override
	public Method queryMethod(String name, Class<?>... paramtypes) {
		if (exposed_methods.contains(name)) {
			return getMethodWithReflection(name, paramtypes);
		}
		// else just use the api base method
		else {
			return super.queryMethod(name, paramtypes);
		}
	}

	private Method getMethodWithReflection(String name, Class<?>... paramtypes) {
		try {
			final java.lang.reflect.Method m = Initial3DImpl.class.getMethod(name, paramtypes);
			return new Method() {

				@Override
				public Object call(Object... args) {
					try {
						return m.invoke(Initial3DImpl.this, args);
					} catch (Throwable t) {
						throw nope("Unable to call implementation method " + m.getName(), t);
					}
				}

			};
		} catch (Throwable t) {
			throw nope("Unable to get implementation method " + name, t);
		}
	}

	@Override
	public FrameBuffer createFrameBuffer() {
		return new FrameBufferImpl();
	}

	@Override
	public void bindFrameBuffer(FrameBuffer fb) {
		state.peek().bindFrameBuffer(fb);
	}

	@Override
	public FrameBuffer getFrameBuffer() {
		return state.peek().getFrameBuffer();
	}

	@Override
	public Texture2D createTexture2D(int size_u, int size_v) {
		return new Texture2DImpl(size_u, size_v);
	}

	@Override
	public void bindTexture(int target, Texture2D tex) {
		state.peek().bindTexture(target, tex);
	}

	@Override
	public void enable(int... caps) {
		state.peek().enable(caps);
	}

	@Override
	public void disable(int... caps) {
		state.peek().disable(caps);
	}

	@Override
	public boolean isEnabled(int cap) {
		return state.peek().isEnabled(cap);
	}

	@Override
	public void projectionMode(int mode) {
		state.peek().projectionMode(mode);
	}

	@Override
	public void polygonMode(int face, int mode) {
		state.peek().polygonMode(face, mode);
	}

	@Override
	public void shadeModel(int model) {
		state.peek().shadeModel(model);
	}

	@Override
	public void viewport(int w, int h) {
		state.peek().viewport(w, h);
	}

	@Override
	public void clear(int... buffers) {
		state.peek().clear(buffers);
	}

	public void flipZSign() {
		state.peek().flipZSign();
	}

	@Override
	public void bindVertexBuffer(int att, VectorBuffer vbuf) {
		state.peek().bindVertexBuffer(att, vbuf);
	}

	@Override
	public void drawPolygons(PolygonBuffer pbuf, int offset, int count) {
		state.peek().drawPolygons(pbuf, offset, count);
	}

	@Override
	public void begin(int mode) {
		state.peek().begin(mode);
	}

	@Override
	public void vertex(double x, double y, double z) {
		state.peek().vertex(x, y, z);
	}

	@Override
	public void normal(double x, double y, double z) {
		state.peek().normal(x, y, z);
	}

	@Override
	public void color(double r, double g, double b, double a) {
		state.peek().color(r, g, b, a);
	}

	@Override
	public void secondaryColor(double r, double g, double b, double a) {
		state.peek().secondaryColor(r, g, b, a);
	}

	@Override
	public void texCoord(double u, double v) {
		state.peek().texCoord(u, v);
	}

	@Override
	public void end() {
		state.peek().end();
	}

	@Override
	public void material(int face, int param, double f) {
		state.peek().material(face, param, f);
	}

	@Override
	public void material(int face, int param, double r, double g, double b, double a) {
		state.peek().material(face, param, r, g, b, a);
	}

	@Override
	public void blendFunc(int sfactor, int dfactor) {
		state.peek().blendFunc(sfactor, dfactor);
	}

	@Override
	public void alphaFunc(int func, double ref) {
		state.peek().alphaFunc(func, ref);
	}

	@Override
	public void depthFunc(int func) {
		state.peek().depthFunc(func);
	}

	@Override
	public void stencilFuncSeparate(int face, int func, int ref, int mask) {
		state.peek().stencilFuncSeparate(face, func, ref, mask);
	}

	@Override
	public void stencilOpSeparate(int face, int sfail, int dfail, int dpass) {
		state.peek().stencilOpSeparate(face, sfail, dfail, dpass);
	}

	@Override
	public void light(int light, int param, double v) {
		state.peek().light(light, param, v);
	}

	@Override
	public void light(int light, int param, double f0, double f1, double f2, double f3) {
		state.peek().light(light, param, f0, f1, f2, f3);
	}

	@Override
	public void sceneAmbient(double r, double g, double b, double a) {
		state.peek().sceneAmbient(r, g, b, a);
	}

	@Override
	public void fog(int param, double val) {
		state.peek().fog(param, val);
	}

	@Override
	public void fog(int param, double r, double g, double b, double a) {
		state.peek().fog(param, r, g, b, a);
	}

	public void initFog() {
		state.peek().initFog();
	}

	@Override
	public void cullFace(int face) {
		state.peek().cullFace(face);
	}

	@Override
	public void finish() {
		state.peek().finish();
	}

	@Override
	public void matrixMode(int mode) {
		state.peek().matrixMode(mode);
	}

	@Override
	public Vec3 transformOne(Vec3 v) {
		return state.peek().transformOne(v);
	}

	@Override
	public Vec4 transformOne(Vec4 v) {
		return state.peek().transformOne(v);
	}

	@Override
	public void pushMatrix() {
		state.peek().pushMatrix();
	}

	@Override
	public void popMatrix() {
		state.peek().popMatrix();
	}

	@Override
	public void loadMatrix(Mat4 m) {
		state.peek().loadMatrix(m);
	}

	@Override
	public void multMatrix(Mat4 m) {
		state.peek().multMatrix(m);
	}

	@Override
	public Mat4 getMatrix() {
		return state.peek().getMatrix();
	}

}
