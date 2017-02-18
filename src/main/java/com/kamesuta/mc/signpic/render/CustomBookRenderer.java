package com.kamesuta.mc.signpic.render;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.gui.GuiImage;

import net.minecraft.client.gui.FontRenderer;

public class CustomBookRenderer {

	public CustomBookRenderer() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public static void renderBook() {

	}

	public static void hookDrawSplitString(@Nullable String p_78279_1_, final int p_78279_2_, int p_78279_3_, final int p_78279_4_, final int p_78279_5_) {
		final FontRenderer f = WRenderer.font();

		while (p_78279_1_!=null&&p_78279_1_.endsWith("\n"))
			p_78279_1_ = p_78279_1_.substring(0, p_78279_1_.length()-1);

		final List<?> list = f.listFormattedStringToWidth(p_78279_1_, p_78279_4_);

		for (final Iterator<?> iterator = list.iterator(); iterator.hasNext(); p_78279_3_ += f.FONT_HEIGHT) {
			final String s1 = (String) iterator.next();
			final int index = StringUtils.lastIndexOf(s1, "}");
			b: {
				if (index!=StringUtils.INDEX_NOT_FOUND) {
					final String s2 = StringUtils.substring(s1, 0, index+1);
					final Entry entry = EntryId.from(s2).entry();
					if (entry.isValid()) {
						final GuiImage gui = entry.getGui();
						OpenGL.glPushMatrix();
						OpenGL.glTranslatef(p_78279_2_, p_78279_3_, 0f);
						gui.drawScreen(0, 0, 0f, 1f, p_78279_4_, 50f);
						OpenGL.glPopMatrix();
						WRenderer.startTexture();
						break b;
					}
				}
				f.drawString(s1, p_78279_2_, p_78279_3_, p_78279_5_);
			}
		}
		// f.drawSplitString(p_78279_1_, p_78279_2_, p_78279_3_, p_78279_4_, p_78279_5_);
	}
}
