package nz.net.initial3d.renderer;

import static nz.net.initial3d.renderer.Util.*;
import nz.net.initial3d.util.Queue;

final class RasterPipe {

	static final int TRIANGLES = 1;
	static final int LINES = 2;

	private final WorkerThread[] workers;

	RasterPipe(int threadcount_) {
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
		// IDEA: interleaved division for better load distribution
		// block-0 : thread-0
		// block-1 : thread-1
		// block-2 : thread-2
		// block-3 : thread-0 ...and so on
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
	 * All enablers, bound textures etc must be specified at the start of the buffer.
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
			setName("I3D-raster-" + getId());
		}

		public void setScanlines(int yi_, int yf_) {
			yi = yi_;
			yf = yf_;
		}

		public void feed(Buffer wb) {
			while (!work.offer(wb)) {
				// this hopefully shouldn't happen
				Thread.yield();
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
						synchronized (waiter_finish) {
							waiter_finish.notify();
						}
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
							rasteriseTriangles(wb);
							break;
						case LINES:
							rasteriseLines(wb);
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

	private void rasteriseTriangles(Buffer wb) {
		Object obj_color0 = wb.getExtra("OBJ_COLOR0");
		
	}

	private void rasteriseLines(Buffer wb) {

	}

}
