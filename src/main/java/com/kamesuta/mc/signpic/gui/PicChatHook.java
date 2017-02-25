package com.kamesuta.mc.signpic.gui;

import java.util.List;

import javax.annotation.Nonnull;

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
		final List<ChatLine> list = this.chatLinesHook;
		list.clear();

		final List<List<ChatLine>> lineunits = Lists.newLinkedList();
		{
			List<ChatLine> _lineunits = Lists.newLinkedList();
			int _lastlineunits = -1;
			for (final ChatLine line : this.chatList) {
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
}
