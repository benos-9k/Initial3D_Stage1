package nz.net.initial3d.renderer;

import static nz.net.initial3d.renderer.Util.*;
import sun.misc.Unsafe;
import nz.net.initial3d.*;

final class FrameBufferImpl extends FrameBuffer {

	private static final Unsafe unsafe = getUnsafe();

	Object obj_color0;
	long pColor0;
	int stride_color0;
	long pColor1;
	int stride_color1;
	long pZ;
	int stride_z;
	long pSZ;
	int stride_sz;
	long pStencil;
	int stride_stencil;
	long pID;
	int stride_id;

	FrameBufferImpl() {

	}

	@Override
	public void attachBuffer(int type, Texture2D tex) {
		Texture2DImpl texim = (Texture2DImpl) tex;
		switch (type) {
		case Initial3D.BUFFER_COLOR0:
			obj_color0 = null;
			pColor0 = texim.pTop;
			stride_color0 = texim.stride_tex;
			break;
		case Initial3D.BUFFER_COLOR1:
			pColor1 = texim.pTop;
			stride_color1 = texim.stride_tex;
			break;
		case Initial3D.BUFFER_Z:
			// attach the top level as the z buffer
			pZ = texim.pTop;
			stride_z = texim.stride_tex;
			// and one level down as the 'small' z buffer
			pSZ = texim.pTex + Texture2DImpl.levelOffset(texim.stride_tex, texim.levelu_top - 1, texim.levelv_top - 1);
			stride_sz = texim.stride_tex;
			break;
		case Initial3D.BUFFER_STENCIL:
			pStencil = texim.pTop;
			stride_stencil = texim.stride_tex;
			break;
		case Initial3D.BUFFER_ID:
			pID = texim.pTop;
			stride_id = texim.stride_tex;
			break;
		default:
			throw nope("Invalid enum.");
		}
	}

	@Override
	public void attachBuffer(int type, int[] buf, int offset, int stride) {
		if (type != Initial3D.BUFFER_COLOR0)
			throw new IllegalArgumentException("Can only attach array to BUFFER_COLOR0.");
		obj_color0 = buf;
		pColor0 = unsafe.arrayBaseOffset(int[].class) + offset * 4;
		stride_color0 = stride * 4;
	}

	@Override
	public void detachBuffer(int type) {
		switch (type) {
		case Initial3D.BUFFER_COLOR0:
			obj_color0 = null;
			pColor0 = 0;
			break;
		case Initial3D.BUFFER_COLOR1:
			pColor1 = 0;
			break;
		case Initial3D.BUFFER_Z:
			pZ = 0;
			break;
		case Initial3D.BUFFER_STENCIL:
			pStencil = 0;
			break;
		case Initial3D.BUFFER_ID:
			pID = 0;
			break;
		default:
			throw nope("Invalid enum.");
		}
	}

	@Override
	public boolean hasBuffer(int type) {
		switch (type) {
		case Initial3D.BUFFER_COLOR0:
			return (obj_color0 != null) || (pColor0 != 0);
		case Initial3D.BUFFER_COLOR1:
			return pColor1 != 0;
		case Initial3D.BUFFER_Z:
			return pZ != 0;
		case Initial3D.BUFFER_STENCIL:
			return pStencil != 0;
		case Initial3D.BUFFER_ID:
			return pID != 0;
		default:
			throw nope("Invalid enum.");
		}
	}

}
