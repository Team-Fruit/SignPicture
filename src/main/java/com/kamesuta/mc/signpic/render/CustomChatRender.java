package com.kamesuta.mc.signpic.render;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WGui;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.CoreInvoke;
import com.kamesuta.mc.signpic.attr.CompoundAttr;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.attr.prop.SizeData.ImageSizes;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
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
		public static final @Nonnull IChatComponent dummytext = new ChatComponentText("");
		private final @Nonnull PicChatNode node;
		public final int num;
		private float xpos;

		public PicChatLine(final @Nonnull PicChatNode node, final int num) {
			super(node.updateCounterCreated, dummytext, node.chatLineID);
			this.node = node;
			this.num = num;
		}

		public void draw(final float width, final float height) {
			float totalwidth = 0;
			final List<Entry> entries = this.node.getEntries();
			for (final Entry entry : entries) {
				final Content content = entry.getContent();
				final SizeData size1 = content!=null ? content.image.getSize() : SizeData.DefaultSize;
				final SizeData size2 = ImageSizes.INNER.defineSize(size1, SizeData.create(width, height*4f));
				totalwidth += size2.getWidth();
			}
			float ix = -totalwidth*(this.xpos/width);
			final Area trim = Area.size(0f, 0f, width, height);
			final Area vert = WGui.defaultTextureArea.translate(0f, -height*this.num);
			for (final Entry entry : entries) {
				if (ix>width)
					break;
				final Content content = entry.getContent();
				final SizeData size1 = content!=null ? content.image.getSize() : SizeData.DefaultSize;
				final SizeData size2 = ImageSizes.INNER.defineSize(size1, SizeData.create(width, height*4f));
				OpenGL.glPushMatrix();
				OpenGL.glTranslatef(0f, -9, 0f);
				WRenderer.startShape();
				OpenGL.glColor4f(1f, 1f, 1f, 1f);
				WGui.draw(trim, GL11.GL_LINE_LOOP);
				WRenderer.startTexture();

				final float w = size2.getWidth();
				final float h = size2.getHeight();
				//OpenGL.glScalef(w, h, 1f);
				final Area svert = vert.scaleSize(w, h).translate(ix, 0);
				ix += w;

				if (content!=null&&content.state.getType()==StateType.AVAILABLE) {
					final CompoundAttr meta = entry.getMeta();
					content.image.draw(meta, svert, trim);
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
			for (final Entry entry : this.node.getEntries()) {
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

	public static class PicChatID {
		public final int id;
		public final @Nonnull List<ChatLine> lines;

		public PicChatID(final int id, final @Nonnull List<ChatLine> lines) {
			this.id = id;
			this.lines = lines;
		}

		public @Nonnull PicChatNode getNode(final @Nonnull Map<PicChatID, PicChatNode> id2node) {
			PicChatNode node = id2node.get(this);
			if (node==null)
				id2node.put(this, node = new PicChatNode(this.lines));
			return node;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime*result+this.id;
			return result;
		}

		@Override
		public boolean equals(final @Nullable Object obj) {
			if (this==obj)
				return true;
			if (obj==null)
				return false;
			if (!(obj instanceof PicChatID))
				return false;
			final PicChatID other = (PicChatID) obj;
			if (this.id!=other.id)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return String.format("PicChatID [id=%s]", this.id);
		}
	}

	public static class PicChatNode {
		private final @Nonnull List<EntryId> pendentryids;
		private final @Nonnull List<Entry> entries;
		public final int updateCounterCreated;
		public final int chatLineID;;

		public PicChatNode(final @Nonnull List<ChatLine> lines) {
			this.pendentryids = Lists.newLinkedList();
			this.entries = Lists.newLinkedList();
			int updateCounterCreated = -1;
			int chatLineID = -1;
			{
				EntryId _lastentryid = null;
				for (final ChatLine line : lines) {
					updateCounterCreated = line.getUpdatedCounter();
					chatLineID = line.getChatLineID();
					final IChatComponent cc = line.func_151461_a();
					final List<ClickEvent> clinks = getLinksFromChat(cc);
					boolean first = true;
					for (final ClickEvent clink : clinks) {
						final EntryId entryid = new EntryIdBuilder().setURI(clink.getValue()).build();
						if (!first||!entryid.equals(_lastentryid))
							this.pendentryids.add(entryid);
						first = false;
						_lastentryid = entryid;
					}
				}
			}
			this.updateCounterCreated = updateCounterCreated;
			this.chatLineID = chatLineID;
		}

		public @Nonnull List<Entry> getEntries() {
			this.entries.clear();
			for (final EntryId pendentryid : this.pendentryids) {
				final Entry pendentry = pendentryid.entry();
				final Content content = pendentry.getContent();
				if (pendentry.isValid()&&content!=null&&content.state.getType()==StateType.AVAILABLE)
					this.entries.add(pendentry);
			}
			return this.entries;
		}

		@Override
		public String toString() {
			return String.format("PicChatNode [pendentryids=%s, updateCounterCreated=%s, chatLineID=%s]", this.pendentryids, this.updateCounterCreated, this.chatLineID);
		}

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
	}

	@CoreInvoke
	public static class PicChatHook {
		private final @Nonnull List<ChatLine> chatLinesHook;
		private final @Nonnull List<ChatLine> chatList = Lists.newArrayList();
		private final @Nonnull Map<PicChatID, PicChatNode> id2node = Maps.newHashMap();

		@CoreInvoke
		public PicChatHook(final @Nonnull List<ChatLine> chatLinesHook) {
			this.chatLinesHook = chatLinesHook;
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

			final List<PicChatID> lineunits = Lists.newLinkedList();
			{
				List<ChatLine> _lineunits = Lists.newLinkedList();
				int _lastlineunits = -1;
				for (final ChatLine line : clist) {
					final int id = line.getUpdatedCounter();

					if (id!=_lastlineunits) {
						lineunits.add(new PicChatID(_lastlineunits, _lineunits));
						_lineunits = Lists.newLinkedList();
					}
					_lineunits.add(line);
					_lastlineunits = id;
				}
				if (_lineunits!=null)
					lineunits.add(new PicChatID(_lastlineunits, _lineunits));
			}

			for (final PicChatID id : lineunits) {
				final PicChatNode node = id.getNode(this.id2node);

				for (final ChatLine line : id.lines)
					list.add(0, line);

				if (!node.getEntries().isEmpty())
					for (int i = 0; i<4; i++)
						list.add(0, new PicChatLine(node, i));
			}
		}
	}
}
