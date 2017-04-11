package com.kamesuta.mc.signpic.plugin.gui;

import com.kamesuta.mc.bnnwidget.position.Area;

public interface Scrollable {

	void scroll(final int scroll, final GuiManager manager, final Area position);

	void scrollTo(final float to, final GuiManager manager, final Area position);

	float getNowHeight();

	float getAllHeight();
}
