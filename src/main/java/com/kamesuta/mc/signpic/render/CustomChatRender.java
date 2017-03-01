package com.kamesuta.mc.signpic.render;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WGui;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.CoreInvoke;
import com.kamesuta.mc.signpic.attr.CompoundAttr;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.attr.prop.SizeData.ImageSizes;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryIdBuilder;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.state.StateType;

import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

@CoreInvoke
public class CustomChatRender {
	@CoreInvoke
	public static class PicChatLine extends ChatLine {
		private final @Nonnull List<Entry> entrylist;
		public final int num;
		private float xpos;

		public PicChatLine(final int updateCounterCreated, @Nonnull final IChatComponent lineString, final int chatLineID, @Nonnull final List<Entry> entrylist, final int num) {
			super(updateCounterCreated, lineString, chatLineID);
			this.entrylist = entrylist;
			this.num = num;
		}

		public void draw(final float width, final float height) {
			float ix = 0;
			for (final Entry entry : this.entrylist) {
				if (ix>width)
					break;
				final Content content = entry.getContent();

				final SizeData size1 = content!=null ? content.image.getSize() : SizeData.DefaultSize;
				final SizeData size2 = ImageSizes.INNER.defineSize(size1, SizeData.create(width, height*4f));
				OpenGL.glPushMatrix();
				OpenGL.glTranslatef(ix, -9, 0f);
				float w = size2.getWidth();
				final float ix0 = ix+w;
				float wmap = 1f;
				WRenderer.startShape();
				OpenGL.glColor4f(1f, 1f, 1f, 1f);
				WGui.draw(Area.size(0f, 0f, width, height));
				WRenderer.startTexture();
				if (ix0>width) {
					final float cut = ix0-width;
					wmap = ix0/width;
				}

				w *= wmap;
				OpenGL.glScalef(w, size2.getHeight(), 1f);
				ix += w;

				if (content!=null&&content.state.getType()==StateType.AVAILABLE) {
					final CompoundAttr meta = entry.getMeta();
					OpenGL.glScalef(1f, 1f/4f, 1f);
					final float iy = 1f-this.num/4f;
					content.image.draw(meta, 0f, iy, wmap, iy+1f/4f);
				}

				OpenGL.glPopMatrix();
			}
		}

		@CoreInvoke
		public @Nullable IChatComponent onClicked(final @Nonnull GuiNewChat chat, final int x) {
			this.xpos = x;
			final int width = getChatWidth(chat)/2;
			float ix = 0;
			final int height = WRenderer.font().FONT_HEIGHT;
			for (final Entry entry : this.entrylist) {
				final Content content = entry.getContent();

				final SizeData size1 = content!=null ? content.image.getSize() : SizeData.DefaultSize;
				final SizeData size2 = ImageSizes.INNER.defineSize(size1, SizeData.create(width, height*4f));

				ix += size2.getWidth();

				if (x<ix)
					if (content!=null)
						return new ChatComponentText("").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/signpic image open "+content.id.getURI())));
			}
			return null;
		}
	}

	private static int getChatWidth(final @Nonnull GuiNewChat chat) {
		return MathHelper.floor_float(chat.func_146228_f()/chat.func_146244_h());
	}

	@CoreInvoke
	public static int hookDrawStringWithShadow(final @Nonnull FontRenderer font, final @Nonnull String str, final int x, final int y, final int color, final @Nonnull GuiNewChat chat, final @Nonnull ChatLine chatline, final int j2, final int opacity) {
		if (chatline instanceof PicChatLine) {
			final PicChatLine cline = (PicChatLine) chatline;
			OpenGL.glPushMatrix();
			OpenGL.glTranslatef(0f, j2, 0f);
			OpenGL.glColor4i(255, 255, 255, opacity);
			cline.draw(getChatWidth(chat), font.FONT_HEIGHT);
			OpenGL.glPopMatrix();
			WRenderer.startTexture();
		} else
			// this.mc.fontRenderer.drawStringWithShadow(s, b0, j2-8, 16777215+(i2<<24));
			font.drawStringWithShadow(str, x, y, color);
		return 0;
	}

	@CoreInvoke
	public static class PicChatHook {
		private final @Nonnull List<ChatLine> chatLinesHook;
		private final @Nonnull List<ChatLine> chatList = Lists.newArrayList();

		@CoreInvoke
		public PicChatHook(final @Nonnull List<ChatLine> chatLinesHook) {
			this.chatLinesHook = chatLinesHook;
		}

		@CoreInvoke
		public static @Nonnull List<ClickEvent> getLinksFromChat(final @Nonnull IChatComponent chat) {
			final List<ClickEvent> list = Lists.newLinkedList();
			getLinksFromChat0(list, chat);
			return list;
		}

		private static void getLinksFromChat0(final @Nonnull List<ClickEvent> list, final @Nonnull IChatComponent pchat) {
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
		public void updateLines() {
			final List<ChatLine> clist = this.chatList;
			clist.clear();
			final List<ChatLine> list = this.chatLinesHook;
			for (final ChatLine line : list)
				if (!(line instanceof PicChatLine))
					clist.add(0, line);
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
	}
}
