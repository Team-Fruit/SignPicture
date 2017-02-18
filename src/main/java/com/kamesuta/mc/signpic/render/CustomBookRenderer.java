package com.kamesuta.mc.signpic.render;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.render.WRenderer;

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
			f.drawString(s1, p_78279_2_, p_78279_3_, p_78279_4_);
		}
		//f.drawSplitString(p_78279_1_, p_78279_2_, p_78279_3_, p_78279_4_, p_78279_5_);
	}
}
