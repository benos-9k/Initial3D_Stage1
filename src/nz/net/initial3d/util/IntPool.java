package nz.net.initial3d.util;

import java.util.NoSuchElementException;
import java.util.Set;

public class IntPool {

	private class Node {

		// min inclusive, max exclusive
		private final int min, max;
		private Node left, right;
		private boolean full;
		private int count;

		public Node(int min_, int max_, boolean full_) {
			min = min_;
			max = max_;
			full = full_;
			count = full ? range() : 0;
		}

		public int range() {
			return max - min;
		}

		public int count() {
			return count;
		}

		public boolean isFull() {
			return full || ((left != null && left.isFull()) && (right != null && right.isFull()));
		}

		public boolean isEmpty() {
			return !full && (left == null || left.isEmpty()) && (right == null || right.isEmpty());
		}

		public int alloc() {
			if (isEmpty()) {
				throw new NoSuchElementException();
			}
			if (isFull()) {
				full = false;
				// split if possible
				int split = (min + max) / 2;
				if (split == min) {
					// can't subdivide further
					left = null;
					right = null;
					count--;
					return min;
				} else {
					left = new Node(min, split, true);
					right = new Node(split, max, true);
				}
			}
			// allocate from subnode
			int m = 0;
			try {
				m = left.alloc();
			} catch (NoSuchElementException e) {
				m = right.alloc();
			}
			count--;
			// check if simplify-able
			if (isEmpty()) {
				full = false;
				left = null;
				right = null;
			}
			// return allocated value
			return m;
		}

		public boolean free(int m) {
			if (isFull()) {
				return false;
			}
			if (isEmpty()) {
				// split if possible
				int split = (min + max) / 2;
				if (split == min) {
					// can't subdivide further
					if (min != m) throw new AssertionError("Something broke.");
					full = true;
					left = null;
					right = null;
					count++;
					return true;
				} else {
					left = new Node(min, split, false);
					right = new Node(split, max, false);
				}
			}
			// free into appropriate subnode
			boolean b = false;
			if (m < right.min) {
				b = left.free(m);
			} else {
				b = right.free(m);
			}
			if (b) count++;
			// check if simplify-able
			if (isFull()) {
				full = true;
				left = null;
				right = null;
			}
			return b;
		}

		public void print(String indent) {
			System.out.printf("%smin=%d max=%d count=%d full=%b\n", indent, min, max, count, full);
			if (left != null) left.print(indent + "    ");
			if (right != null) right.print(indent + "    ");
		}

	}

	private Node root;

	public IntPool(int min_, int max_) {
		if (min_ >= max_) throw new IllegalArgumentException("min_ must be < max_");
		root = new Node(min_, max_, true);
	}

	public int alloc() {
		return root.alloc();
	}

	public boolean free(int m) {
		return root.free(m);
	}

	public void print() {
		root.print("");
	}

}
