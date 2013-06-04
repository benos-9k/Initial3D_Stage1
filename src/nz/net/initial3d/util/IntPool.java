package nz.net.initial3d.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class IntPool {

	private final Set<Integer> used = new HashSet<Integer>();
	private int used_min, used_max;

	private final Set<Integer> pool = new HashSet<Integer>();
	private int next, max;

	public IntPool(int min_, int max_) {
		if (min_ >= max_) throw new IllegalArgumentException("min_ must be < max_");
		next = min_;
		max = max_;
	}

	public int alloc() {
		int m = 0;
		if (pool.isEmpty()) {
			// create new int
			if (next < max) {
				m = next++;
			} else {
				throw new IllegalStateException("Pool exhausted.");
			}
		} else {
			// get int from pool
			Iterator<Integer> it = pool.iterator();
			m = it.next();
			it.remove();
		}
		used.add(m);
		return m;
	}

	public void free(int m) {
		if (!used.remove(m)) {
			throw new IllegalStateException("Integer '" + m + "' not allocated.");
		}
		pool.add(m);
	}

	@Override
	public String toString() {
		return String.format("IntPool[used:%d, free:%d, next:%d, max:%d]", used.size(), pool.size(), next, max);
	}

}
