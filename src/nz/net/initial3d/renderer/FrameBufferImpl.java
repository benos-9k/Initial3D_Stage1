package nz.net.initial3d.renderer;

import nz.net.initial3d.*;

final class FrameBufferImpl extends FrameBuffer {

	Object obj_color0 = null;
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

	}

	@Override
	public void attachBuffer(int type, int[] buf, int offset, int stride) {
		if (type != Initial3D.BUFFER_COLOR0)
			throw new IllegalArgumentException("Can only attach array to BUFFER_COLOR0.");

	}

}
