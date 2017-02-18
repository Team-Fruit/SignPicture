package com.kamesuta.mc.signpic.render;

import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.render.WRenderer;

public class CustomBookRenderer {

	public CustomBookRenderer() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public static void renderBook() {

	}

	public static void hookDrawSplitString(final @Nullable String p_78279_1_, final int p_78279_2_, final int p_78279_3_, final int p_78279_4_, final int p_78279_5_) {
		WRenderer.font().drawSplitString(p_78279_1_, p_78279_2_, p_78279_3_, p_78279_4_, p_78279_5_);
	}
}
