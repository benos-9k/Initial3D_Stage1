package nz.net.initial3d.renderer;

import static nz.net.initial3d.renderer.Util.*;
import static nz.net.initial3d.renderer.Type.*;
import sun.misc.Unsafe;

public final class Initial3DImpl {

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

}
