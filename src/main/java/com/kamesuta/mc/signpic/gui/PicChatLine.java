package com.kamesuta.mc.signpic.gui;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.CoreInvoke;
import com.kamesuta.mc.signpic.attr.CompoundAttr;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.attr.prop.SizeData.ImageSizes;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.state.StateType;

import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

@CoreInvoke
public class PicChatLine extends ChatLine {
	public final @Nonnull List<Entry> entrylist;
	public final int num;

	public PicChatLine(final int updateCounterCreated, @Nonnull final IChatComponent lineString, final int chatLineID, @Nonnull final List<Entry> entrylist, final int num) {
		super(updateCounterCreated, lineString, chatLineID);
		this.entrylist = entrylist;
		this.num = num;
	}

	public void draw(final float width, final float height) {
		float ix = 0;
		for (final Entry entry : this.entrylist) {
			final Content content = entry.getContent();

			final SizeData size1 = content!=null ? content.image.getSize() : SizeData.DefaultSize;
			final SizeData size2 = ImageSizes.INNER.defineSize(size1, SizeData.create(width, height*4f));
			OpenGL.glPushMatrix();
			OpenGL.glTranslatef(ix, -9, 0f);
			ix += size2.getWidth();

			OpenGL.glScalef(size2.getWidth(), size2.getHeight(), 1f);

			if (content!=null&&content.state.getType()==StateType.AVAILABLE) {
				final CompoundAttr meta = entry.getMeta();
				OpenGL.glScalef(1f, 1f/4f, 1f);
				final float iy = 1f-this.num/4f;
				content.image.draw(meta, 0f, iy, 1f, iy+1f/4f);
			}

			OpenGL.glPopMatrix();
		}
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

	private static int getChatWidth(final @Nonnull GuiNewChat chat) {
		return MathHelper.floor_float(chat.func_146228_f()/chat.func_146244_h());
	}

	@CoreInvoke
	public @Nullable IChatComponent onClicked(final float width, final float height, final int x) {
		float ix = 0;
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