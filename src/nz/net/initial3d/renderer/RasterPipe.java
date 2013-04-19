package nz.net.initial3d.renderer;

import static nz.net.initial3d.renderer.Util.*;
import nz.net.initial3d.util.Queue;

final class RasterPipe {

	static final int TRIANGLES = 1;
	static final int LINES = 2;

	private final long pBase;
	private final WorkerThread[] workers;

	RasterPipe(long pBase_, int threadcount_) {
		pBase = pBase_;
		workers = new WorkerThread[threadcount_];
		for (int i = 0; i < threadcount_; i++) {
			workers[i] = new WorkerThread();
			workers[i].start();
		}

	}

	@Override
	protected void finalize() {

	}

	void setScanlines(int lines) {
		// scanline division cannot change while rasterisation in progress
		finish();
		// divide by blocks of 8 (for rasteriser compatibility)
		int[] worker_blocks = new int[workers.length];
		for (int blocks = lines / 8, w = 0; blocks > 0; blocks--, w = (w + 1) % workers.length) {
			worker_blocks[w]++;
		}
		for (int w = 0, yi = 0; w < workers.length; w++) {
			workers[w].setScanlines(yi, yi += worker_blocks[w] * 8);
		}
	}

	/**
	 * This pipe runs asynchronously, so must not access client-side state.
	 *
	 * @param wb
	 */
	void feed(Buffer wb) {
		for (WorkerThread w : workers) {
			// acquire for each worker thread
			wb.acquire();
			w.feed(wb);
		}
		// main thread is done with it
		wb.release();
	}

	void finish() {
		for (WorkerThread w : workers) {
			w.finish();
		}
	}

	private class WorkerThread extends Thread {

		private Queue<Buffer> work = new Queue<Buffer>(1024);
		private final Object waiter_begin = new Object();
		private final Object waiter_finish = new Object();
		private volatile boolean waiting = true;

		private volatile int yi = 0, yf = 0;

		public WorkerThread() {
			setDaemon(true);
		}

		public void setScanlines(int yi_, int yf_) {
			yi = yi_;
			yf = yf_;
		}

		public void feed(Buffer wb) {
			while (!work.offer(wb)) {
				try {
					// this hopefully shouldn't happen
					Thread.sleep(1);
				} catch (InterruptedException e) {
					//
				}
			}
			if (waiting) {
				synchronized (waiter_begin) {
					waiter_begin.notify();
				}
			}
		}

		public boolean isWaiting() {
			return waiting;
		}

		public void finish() {
			while (!waiting) {
				synchronized (waiter_finish) {
					try {
						waiter_finish.wait();
					} catch (InterruptedException e) {
						//
					}
				}
			}
		}

		@Override
		public void run() {

			while (true) {
				try {

					Buffer wb = work.poll();
					if (wb == null) {
						waiting = true;
						waiter_finish.notify();
						while (wb == null) {
							synchronized (waiter_begin) {
								waiter_begin.wait();
							}
							wb = work.poll();
						}
					}
					waiting = false;

					try {
						switch (wb.getTag()) {
						case TRIANGLES:
							rasterTriangles(wb);
							break;
						case LINES:
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
