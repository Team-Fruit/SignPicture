package com.kamesuta.mc.signpic.render;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.attr.CompoundAttr;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.attr.prop.SizeData.ImageSizes;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.gui.GuiImage;

import net.minecraft.client.gui.FontRenderer;

public class CustomBookRenderer {

	public CustomBookRenderer() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public static void renderBook() {

	}

	public static void hookDrawSplitString(@Nullable String str, final int x, int y, final int width, final int color) {
		final FontRenderer f = WRenderer.font();

		while (str!=null&&str.endsWith("\n"))
			str = str.substring(0, str.length()-1);

		final List<?> list = f.listFormattedStringToWidth(str, width);

		for (final Iterator<?> iterator = list.iterator(); iterator.hasNext(); y += f.FONT_HEIGHT) {
			final String s1 = (String) iterator.next();
			final int index = StringUtils.lastIndexOf(s1, "}");
			b: {
				if (index!=StringUtils.INDEX_NOT_FOUND) {
					final String s2 = StringUtils.substring(s1, 0, index+1);
					final Entry entry = EntryId.from(s2).entry();
					if (entry.isValid()) {
						final GuiImage gui = entry.getGui();
						final CompoundAttr attr = entry.getMeta();
						final Content content = entry.getContent();
						final SizeData size00 = attr.sizes.getMovie().get();
						final SizeData size01 = content!=null ? content.image.getSize() : SizeData.DefaultSize;
						final SizeData size1 = size00.aspectSize(size01);
						final SizeData size2 = ImageSizes.INNER.defineSize(size1, SizeData.create(f.FONT_HEIGHT, f.FONT_HEIGHT));
						OpenGL.glPushMatrix();
						OpenGL.glTranslatef(x, y, 0f);
						OpenGL.glScalef(size2.getWidth(), size2.getHeight(), 1f);
						OpenGL.glTranslatef(size1.getWidth()/2, size1.getHeight()+(size1.getHeight()>=0 ? 0 : -size1.getHeight())-.5f, 0f);
						OpenGL.glScalef(1f, -1f, 1f);
						gui.renderSignPicture(1f);
						;
						OpenGL.glPopMatrix();
						WRenderer.startTexture();
						break b;
					}
				}
				f.drawString(s1, x, y, color);
			}
		}
		// f.drawSplitString(p_78279_1_, p_78279_2_, p_78279_3_, p_78279_4_, p_78279_5_);
	}
}
