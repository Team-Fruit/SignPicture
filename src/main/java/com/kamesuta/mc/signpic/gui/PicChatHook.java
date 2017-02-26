package com.kamesuta.mc.signpic.gui;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.kamesuta.mc.signpic.CoreInvoke;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryIdBuilder;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.state.StateType;

import net.minecraft.client.gui.ChatLine;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class PicChatHook {
	private final @Nonnull List<ChatLine> chatLinesHook;
	private final @Nonnull List<ChatLine> chatList = Lists.newArrayList();

	@CoreInvoke
	public PicChatHook(final @Nonnull List<ChatLine> chatLinesHook) {
		this.chatLinesHook = chatLinesHook;
	}

	@CoreInvoke
	public @Nonnull List<ClickEvent> getLinksFromChat(final @Nonnull IChatComponent chat) {
		final List<ClickEvent> list = Lists.newLinkedList();
		getLinksFromChat0(list, chat);
		return list;
	}

	private void getLinksFromChat0(final @Nonnull List<ClickEvent> list, final @Nonnull IChatComponent pchat) {
		final List<?> chats = pchat.getSiblings();
		for (final Object o : chats) {
			final IChatComponent chat = (IChatComponent) o;
			final ClickEvent ev = chat.getChatStyle().getChatClickEvent();
			if (ev!=null&&ev.getAction()==ClickEvent.Action.OPEN_URL)
				list.add(ev);
			getLinksFromChat0(list, chat);
		}
	}

	@CoreInvoke
	public void addLine(final @Nonnull ChatLine line) {
		this.chatList.add(line);
		updateLines();
	}

	@CoreInvoke
	public void updateLines() {
		final List<ChatLine> clist = this.chatList;
		clist.clear();
		final List<ChatLine> list = this.chatLinesHook;
		for (final ChatLine line : list)
			if (!(line instanceof PicChatLine))
				clist.add(line);
		list.clear();

		final List<List<ChatLine>> lineunits = Lists.newLinkedList();
		{
			List<ChatLine> _lineunits = Lists.newLinkedList();
			int _lastlineunits = -1;
			for (final ChatLine line : clist) {
				final int id = line.getUpdatedCounter();

				if (id!=_lastlineunits) {
					lineunits.add(_lineunits);
					_lineunits = Lists.newLinkedList();
				}
				_lineunits.add(line);
				_lastlineunits = id;
			}
			if (_lineunits!=null)
				lineunits.add(_lineunits);
		}

		for (final List<ChatLine> lines : lineunits) {
			final List<Entry> entries = Lists.newLinkedList();
			int updateCounterCreated = -1;
			int chatLineID = -1;
			{
				Entry _lastentries = null;
				for (final ChatLine line : lines) {
					updateCounterCreated = line.getUpdatedCounter();
					chatLineID = line.getChatLineID();
					final IChatComponent cc = line.func_151461_a();
					final List<ClickEvent> clinks = getLinksFromChat(cc);
					boolean first = true;
					for (final ClickEvent clink : clinks) {
						final Entry entry = new EntryIdBuilder().setURI(clink.getValue()).build().entry();
						if (!first||!entry.equals(_lastentries)) {
							final Content content = entry.getContent();
							if (entry.isValid()&&content!=null&&content.state.getType()==StateType.AVAILABLE)
								entries.add(entry);
						}
						first = false;
						_lastentries = entry;
					}
				}
			}

			for (final ChatLine line : lines)
				list.add(0, line);

			if (!entries.isEmpty()) {
				final IChatComponent chattext = new ChatComponentText("");
				for (int i = 0; i<4; i++)
					list.add(0, new PicChatLine(updateCounterCreated, chattext, chatLineID, entries, i));
			}
		}
	}

	public static class PicChatList<T> implements List<T>, RandomAccess {
		public final List<T> list;

		public PicChatList(final @Nonnull List<T> list) {
			this.list = list;
		}

		@Override
		public int size() {
			return this.list.size();
		}

		@Override
		public boolean isEmpty() {
			return this.list.isEmpty();
		}

		@Override
		public boolean contains(@Nullable final Object o) {
			return this.list.contains(o);
		}

		@Override
		public @Nonnull Iterator<T> iterator() {
			return this.list.iterator();
		}

		@Override
		public @Nonnull Object[] toArray() {
			return this.list.toArray();
		}

		@Override
		public @Nonnull <E> E[] toArray(@Nullable final E[] a) {
			return this.list.toArray(a);
		}

		@Override
		public boolean add(@Nullable final T e) {
			return this.list.add(e);
		}

		@Override
		public boolean remove(@Nullable final Object o) {
			return this.list.remove(o);
		}

		@Override
		public boolean containsAll(@Nullable final Collection<?> c) {
			return this.list.containsAll(c);
		}

		@Override
		public boolean addAll(@Nullable final Collection<? extends T> c) {
			return this.list.addAll(c);
		}

		@Override
		public boolean addAll(final int index, @Nullable final Collection<? extends T> c) {
			return this.list.addAll(index, c);
		}

		@Override
		public boolean removeAll(@Nullable final Collection<?> c) {
			return this.list.removeAll(c);
		}

		@Override
		public boolean retainAll(@Nullable final Collection<?> c) {
			return this.list.retainAll(c);
		}

		@Override
		public void clear() {
			this.list.clear();
		}

		@Override
		public boolean equals(@Nullable final Object o) {
			return this.list.equals(o);
		}

		@Override
		public int hashCode() {
			return this.list.hashCode();
		}

		@Override
		public T get(final int index) {
			return this.list.get(index);
		}

		@Override
		public T set(final int index, @Nullable final T element) {
			return this.list.set(index, element);
		}

		@Override
		public void add(final int index, @Nullable final T element) {
			this.list.add(index, element);
		}

		@Override
		public @Nonnull T remove(final int index) {
			return this.list.remove(index);
		}

		@Override
		public int indexOf(@Nullable final Object o) {
			return this.list.indexOf(o);
		}

		@Override
		public int lastIndexOf(@Nullable final Object o) {
			return this.list.lastIndexOf(o);
		}

		@Override
		public @Nonnull ListIterator<T> listIterator() {
			return this.list.listIterator();
		}

		@Override
		public @Nonnull ListIterator<T> listIterator(final int index) {
			return this.list.listIterator(index);
		}

		@Override
		public @Nonnull List<T> subList(final int fromIndex, final int toIndex) {
			return this.list.subList(fromIndex, toIndex);
		}
	}
}
