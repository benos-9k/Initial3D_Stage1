package nz.net.initial3d.renderer;

final class PrimitivePipe {

	private RasterPipe rasterpipe = null;

	PrimitivePipe() {

	}

	void connectRasterPipe(RasterPipe rp) {
		rasterpipe = null;
	}

	void feed() {

	}

	private class WorkerThread extends Thread {

		private Queue<Buffer> work = new Queue<Buffer>(1024);

		WorkerThread() {

		}

		@Override
		public void run() {

		}

	}

}
