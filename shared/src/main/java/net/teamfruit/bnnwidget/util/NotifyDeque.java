package net.teamfruit.bnnwidget.util;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

import com.google.common.collect.ForwardingDeque;
import com.google.common.collect.ForwardingIterator;

/**
 * 変更を確認することができるリストです
 *
 * @author TeamFruit
 */
public class NotifyDeque<E> extends ForwardingDeque<E> implements NotifyCollection<E> {
	private final Deque<E> delegate;
	private int modCount;

	public NotifyDeque(final Deque<E> deque) {
		this.delegate = deque;
		this.modCount = 0;
	}

	@Override
	public int getModCount() {
		return this.modCount;
	}

	@Override
	protected Deque<E> delegate() {
		return this.delegate;
	}

	@Override
	public void clear() {
		this.modCount++;
		super.clear();
	}

	private class NotifyIterator extends ForwardingIterator<E> {
		private final Iterator<E> iterator;

		public NotifyIterator(final Iterator<E> iterator) {
			this.iterator = iterator;
		}

		@Override
		protected Iterator<E> delegate() {
			return this.iterator;
		}

		@Override
		public void remove() {
			NotifyDeque.this.modCount++;
			super.remove();
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new NotifyIterator(super.iterator());
	}

	@Override
	public Iterator<E> descendingIterator() {
		return new NotifyIterator(super.descendingIterator());
	}

	@Override
	public boolean remove(final Object object) {
		return standardRemove(object);
	}

	@Override
	public boolean removeAll(final Collection<?> collection) {
		return standardRemoveAll(collection);
	}

	@Override
	public boolean retainAll(final Collection<?> collection) {
		return standardRetainAll(collection);
	}

	@Override
	public boolean add(final E element) {
		this.modCount++;
		return super.add(element);
	}

	@Override
	public void addFirst(final E e) {
		this.modCount++;
		super.addFirst(e);
	}

	@Override
	public void addLast(final E e) {
		this.modCount++;
		super.addLast(e);
	}

	@Override
	public boolean addAll(final Collection<? extends E> collection) {
		return standardAddAll(collection);
	}

	@Override
	public boolean offer(final E o) {
		return standardOffer(o);
	}

	@Override
	public boolean offerFirst(final E e) {
		this.modCount++;
		return super.offerFirst(e);
	}

	@Override
	public boolean offerLast(final E e) {
		this.modCount++;
		return super.offerLast(e);
	}

	@Override
	public E poll() {
		this.modCount++;
		return super.poll();
	}

	@Override
	public E pollFirst() {
		this.modCount++;
		return super.pollFirst();
	}

	@Override
	public E pollLast() {
		this.modCount++;
		return super.pollLast();
	}

	@Override
	public E pop() {
		this.modCount++;
		return super.pop();
	}

	@Override
	public void push(final E e) {
		this.modCount++;
		super.push(e);
	}

	@Override
	public E remove() {
		this.modCount++;
		return super.remove();
	}

	@Override
	public E removeFirst() {
		this.modCount++;
		return super.removeFirst();
	}

	@Override
	public E removeLast() {
		this.modCount++;
		return super.removeLast();
	}

	@Override
	public boolean removeFirstOccurrence(final Object o) {
		this.modCount++;
		return super.removeFirstOccurrence(o);
	}

	@Override
	public boolean removeLastOccurrence(final Object o) {
		this.modCount++;
		return super.removeLastOccurrence(o);
	}
}
