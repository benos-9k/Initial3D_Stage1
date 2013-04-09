package nz.net.initial3d.renderer;

import static nz.net.initial3d.renderer.Util.*;

public class Test2 {

	public static void main(String[] args) {

		long nano_start = timenanos();
		Buffer buf = Buffer.alloc(1024);
		long nanos = timenanos() - nano_start;

		System.out.println(buf);
		System.out.println("Alloc'd in " + nanos + "ns.");

		buf.release();

		nano_start = timenanos();
		buf = Buffer.alloc(1024);
		nanos = timenanos() - nano_start;

		System.out.println(buf);
		
		System.out.println("Re-alloc'd in " + nanos + "ns.");

		buf.putInt(0, 9001);

		buf.acquire();
		new Foo(buf).start();

		buf.acquire();
		new Foo(buf).start();

		buf.acquire();
		new Foo(buf).start();

		buf.acquire();
		new Foo(buf).start();

		buf.release();

		System.out.println("Main thread exiting.");
	}

	private static class Foo extends Thread {

		private Buffer b;

		public Foo(Buffer b_) {
			b = b_;
		}

		@Override
		public void run() {

			try {
				Thread.sleep((long) (Math.random() * 5000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println(b.getInt(0));

			b.release();

			System.out.println("Thread " + Thread.currentThread().getId() + " exiting.");
		}

	}

}
