package nz.net.initial3d.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nz.net.initial3d.util.IntPool;

public class IntPoolTest {

	public static void main(String[] args) {

		IntPool pool = new IntPool(0, 1024);

		List<Integer> ids = new ArrayList<Integer>();

		for (int i = 0; i < 100; i++) {
			ids.add(pool.alloc());
		}

		System.out.println("After alloc:");
		System.out.println(pool);

		Random ran = new Random();
		while (!ids.isEmpty()) {
			int id = ids.remove(ran.nextInt(ids.size()));
			pool.free(id);
			if (ids.size() == 50) {
				System.out.println("During free:");
				System.out.println(pool);
			}
		}

		System.out.println("After free:");
		System.out.println(pool);

		System.out.println("Next id: " + pool.alloc());

	}

}
