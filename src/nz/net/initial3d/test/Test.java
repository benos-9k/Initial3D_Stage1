package nz.net.initial3d.test;

public class Test {

	public static void main(String[] args) {

		long nanostart = System.nanoTime();

		long foo = 0;
		for (long i = 0; i < 10000000000L; i++) {
			foo += Thread.currentThread().getId();
		}

		long nanos = System.nanoTime() - nanostart;

		System.out.println(foo);
		System.out.println(nanos / 10000000000d);

		for (int i = 0; i < 20; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					System.out.println(Thread.currentThread().getId());
				}

			}).start();
		}

	}

}
