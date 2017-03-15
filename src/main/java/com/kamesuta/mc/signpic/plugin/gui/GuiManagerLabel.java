package com.kamesuta.mc.signpic.plugin.gui;

import javax.annotation.Nonnull;

import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.gui.SignPicLabel;

public class GuiManagerLabel extends SignPicLabel {

	public GuiManagerLabel(final @Nonnull R position) {
		super(position, ContentManager.instance);
	}

}
