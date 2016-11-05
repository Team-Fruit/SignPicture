package com.kamesuta.mc.signpic.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class NotifyCollections {
	public static interface IModCount<E> extends Collection<E> {
		int getModCount();
	}

	public static class NotifyArrayList<E> extends ArrayList<E> implements IModCount<E> {
		public NotifyArrayList(final int initialCapacity) {
			super(initialCapacity);
		}

		public NotifyArrayList() {
			super();
		}

		public NotifyArrayList(final Collection<? extends E> c) {
			super(c);
		}

		@Override
		public int getModCount() {
			return this.modCount;
		}
	}

	public static class NotifyLinkedList<E> extends LinkedList<E> implements IModCount<E> {
		public NotifyLinkedList() {
			super();
		}

		public NotifyLinkedList(final Collection<? extends E> c) {
			super(c);
		}

		@Override
		public int getModCount() {
			return this.modCount;
		}
	}

	public static class NotifyArrayDeque<E> extends ArrayDeque<E> implements IModCount<E> {
		public NotifyArrayDeque() {
			super();
		}

		public NotifyArrayDeque(final Collection<? extends E> c) {
			super(c);
		}

		@Override
		public int getModCount() {
			return size();
		}
	}
}
