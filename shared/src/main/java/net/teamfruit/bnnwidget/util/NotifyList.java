package net.teamfruit.bnnwidget.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.common.collect.ForwardingList;

/**
 * 変更を確認することができるリストです
 *
 * @author TeamFruit
 */
public class NotifyList<E> extends ForwardingList<E> implements NotifyCollection<E> {
	private final List<E> delegate;
	private int modCount;

	public NotifyList(final List<E> list) {
		this.delegate = list;
		this.modCount = 0;
	}

	@Override
	public int getModCount() {
		return this.modCount;
	}

	@Override
	protected List<E> delegate() {
		return this.delegate;
	}

	@Override
	public boolean add(final E element) {
		return standardAdd(element);
	}

	@Override
	public void add(final int index, final E element) {
		this.modCount++;
		super.add(index, element);
	}

	@Override
	public boolean addAll(final Collection<? extends E> collection) {
		return standardAddAll(collection);
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends E> elements) {
		return standardAddAll(index, elements);
	}

	@Override
	public void clear() {
		this.modCount++;
		super.clear();
	}

	@Override
	public Iterator<E> iterator() {
		return standardIterator();
	}

	@Override
	public ListIterator<E> listIterator() {
		return standardListIterator();
	}

	@Override
	public ListIterator<E> listIterator(final int index) {
		return standardListIterator(index);
	}

	@Override
	public E remove(final int index) {
		this.modCount++;
		return super.remove(index);
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
	public E set(final int index, final E element) {
		this.modCount++;
		return super.set(index, element);
	}

	@Override
	public List<E> subList(final int fromIndex, final int toIndex) {
		return standardSubList(fromIndex, toIndex);
	}
}
