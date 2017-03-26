package com.kamesuta.mc.signpic.render;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.RenderOption;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.CoreInvoke;
import com.kamesuta.mc.signpic.attr.AttrReaders;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.gui.GuiImage;

import net.minecraft.client.gui.FontRenderer;

@CoreInvoke
public class CustomBookRenderer {
	@CoreInvoke
	public static void hookDrawSplitString(@Nonnull final String s, final int x, final int y, final int width, final int color) {
		final FontRenderer f = WRenderer.font();

		final String str = StringUtils.removeEnd(s, "\n");

		final String[] lines = str.split("\n");
		final List<String> newlines = Lists.newLinkedList();
		boolean flag = false;
		for (String line : lines) {
			int num = 0;
			final int index = StringUtils.lastIndexOf(line, "}");
			if (index!=StringUtils.INDEX_NOT_FOUND) {
				final String s2 = StringUtils.substring(line, 0, index+1);
				final Entry entry = EntryId.from(s2).entry();
				if (entry.isValid()) {
					flag = true;
					newlines.add(s2);
					final List<?> list = f.listFormattedStringToWidth(line, width);
					line = StringUtils.substringAfterLast(line, "}");
					num = list.size();
				}
			}
			final List<?> list = f.listFormattedStringToWidth(line, width);
			for (final Object o : list) {
				newlines.add((String) o);
				num--;
			}
			for (int i = 0; i<num; i++)
				newlines.add("");
		}
		if (flag) {
			int iy = y;
			for (final Iterator<String> itr = newlines.iterator(); itr.hasNext();) {
				final String s1 = itr.next();
				final int index = StringUtils.lastIndexOf(s1, "}");
				c: {
					if (index!=StringUtils.INDEX_NOT_FOUND) {
						final String s2 = StringUtils.substring(s1, 0, index+1);
						final Entry entry = EntryId.from(s2).entry();
						if (entry.isValid()) {
							final GuiImage gui = entry.getGui();
							final AttrReaders attr = entry.getMeta();
							final Content content = entry.getContent();
							final SizeData size00 = attr.sizes.getMovie().get();
							final SizeData size01 = content!=null ? content.image.getSize() : SizeData.DefaultSize;
							final SizeData size1 = size00.aspectSize(size01);
							OpenGL.glPushMatrix();
							OpenGL.glTranslatef(x, iy-f.FONT_HEIGHT/2f, 0f);
							OpenGL.glScalef(f.FONT_HEIGHT, f.FONT_HEIGHT, 1f);
							OpenGL.glTranslatef((size1.getWidth()>=0 ? 1f : -1f)*size1.getWidth()/2f, (size1.getHeight()>=0 ? 1f : -1f)*size1.getHeight(), 0f);
							OpenGL.glScalef(1f, -1f, 1f);
							gui.renderSignPicture(1f, 4f, new RenderOption());

							OpenGL.glPopMatrix();
							WRenderer.startTexture();
							break c;
						}
					}
					f.drawString(s1, x, iy, color);
					iy += f.FONT_HEIGHT;
				}
			}
		} else
			f.drawSplitString(s, x, y, width, color);
	}
}
