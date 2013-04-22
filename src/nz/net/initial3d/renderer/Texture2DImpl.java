package nz.net.initial3d.renderer;

import static nz.net.initial3d.renderer.Util.*;
import nz.net.initial3d.*;

import sun.misc.Unsafe;

final class Texture2DImpl extends Texture2D {

	private static final Unsafe unsafe = getUnsafe();

	final long pTex;
	final int stride_tex;
	final long pTop;
	final int alloc;
	final int levelu_top, levelv_top;
	final int sizeu, sizev;

	static int levelOffset(int stride, int levelu, int levelv) {
		// TODO faster texture level offset
		// stride in bytes
		return (stride << levelv) + (4 << levelu);
	}

	static int texelOffset(int stride, int levelu, int levelv, int u, int v) {
		// TODO faster texture texel offset
		// u and v as 16.16 fixed point

		// no it doesn't... v >>> 16 is a no-no
		int probablyworks = ((stride + (v >>> 16)) << levelv) + ((4 + (u >>> 14)) << levelu);
		return (stride << levelv) + (v >>> (16 - levelv)) + (4 << levelu) + (u >>> (14 - levelu));
	}

	Texture2DImpl(int sizeu_, int sizev_) {
		// number of u-levels
		short levelu_ = 0;
		switch (sizeu_) {
		case 1024:
			levelu_++;
		case 512:
			levelu_++;
		case 256:
			levelu_++;
		case 128:
			levelu_++;
		case 64:
			levelu_++;
		case 32:
			levelu_++;
		case 16:
			levelu_++;
		case 8:
			levelu_++;
		case 4:
			levelu_++;
		case 2:
			levelu_++;
		case 1:
			break;
		default:
			throw new IllegalArgumentException("Illegal u-size for texture: " + sizeu_);
		}
		// number of v-levels
		short levelv_ = 0;
		switch (sizev_) {
		case 1024:
			levelv_++;
		case 512:
			levelv_++;
		case 256:
			levelv_++;
		case 128:
			levelv_++;
		case 64:
			levelv_++;
		case 32:
			levelv_++;
		case 16:
			levelv_++;
		case 8:
			levelv_++;
		case 4:
			levelv_++;
		case 2:
			levelv_++;
		case 1:
			break;
		default:
			throw new IllegalArgumentException("Illegal v-size for texture: " + sizev_);
		}
		// ok to create texture
		levelu_top = levelu_;
		levelv_top = levelv_;
		sizeu = sizeu_;
		sizev = sizev_;
		// x2 for rip-maps, 4 bytes per pixel
		stride_tex = sizeu * 2 * 4;
		// x4 for rip-maps, 4 bytes per pixel
		alloc = sizeu * sizev * 4 * 4;
		pTex = unsafe.allocateMemory(alloc);
		pTop = pTex + levelOffset(stride_tex, levelu_top, levelv_top);
		// init texture to black
		clear();
	}

	@Override
	protected void finalize() {
		unsafe.freeMemory(pTex);
	}

	@Override
	public int sizeU() {
		return sizeu;
	}

	@Override
	public int sizeV() {
		return sizev;
	}

	@Override
	public void setMipMapsEnabled(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getMipMapsEnabled() {
		// TODO
		return false;
	}

	@Override
	public void setMipMapFloor(int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getMipMapFloor() {
		// TODO
		return 0;
	}

	@Override
	public void createMipMaps() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getTexel(int u, int v, boolean wrap) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getTexelComponentFloat(int u, int v, Channel ch, boolean wrap) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTexelComponent(int u, int v, Channel ch, boolean wrap) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTexel(int u, int v, int argb, boolean wrap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTexelComponent(int u, int v, Channel ch, float val, boolean wrap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTexelComponent(int u, int v, Channel ch, int val, boolean wrap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// zero out the texture data
		int offset = levelOffset(stride_tex, 0, 0);
		unsafe.setMemory(pTex + offset, alloc - offset, (byte) 0);
	}

}
