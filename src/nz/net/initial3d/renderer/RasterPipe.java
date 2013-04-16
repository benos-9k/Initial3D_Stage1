package nz.net.initial3d.renderer;

import static nz.net.initial3d.renderer.Util.*;

final class RasterPipe {

	static final int TRIANGLE = 1;
	static final int LINE = 2;

	private final WorkerThread[] workers;

	RasterPipe(int threadcount_) {
		workers = new WorkerThread[threadcount_];
		for (int i = 0; i < threadcount_; i++) {
			workers[i] = new WorkerThread();
			workers[i].start();
		}

	}

	public void feed(Buffer wb) {
		for (WorkerThread w : workers) {
			// acquire for each worker thread
			wb.acquire();
			w.feed(wb);
		}
		// main thread is done with it
		wb.release();
	}

	private class WorkerThread extends Thread {

		private Queue<Buffer> work = new Queue<Buffer>(1024);

		public WorkerThread() {
			setDaemon(true);
		}

		public void feed(Buffer wb) {
			while (!work.offer(wb)) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					//
				}
			}
		}

		@Override
		public void run() {

			while (true) {
				try {

					Buffer wb = work.poll();
					while (wb == null) {
						Thread.sleep(1);
						wb = work.poll();
					}

					try {
						switch (wb.getTag()) {
						case TRIANGLE:
							rasterTriangles(wb);
							break;
						case LINE:
							rasterLines(wb);
							break;
						default:
							// whelp.
						}
					} finally {
						// this thread is done with it
						wb.release();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

	private static void rasterTriangles(Buffer wb) {

	}

	private static void rasterLines(Buffer wb) {

	}

}
