package com.kamesuta.mc.signpic.gui;

import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.handler.KeyHandler;

import net.minecraft.client.gui.GuiScreen;

public class GuiHub extends WFrame {
	public GuiHub(final @Nullable GuiScreen parent) {
		super(parent);
	}

	public GuiHub() {
	}

	{
		setGuiPauseGame(false);
	}

	@Override
	public void update(final WEvent ev, final Area pgp, final Point p) {
		KeyHandler.instance.keyHook(this);
		if (KeyHandler.keySignPicture.isKeyPressed())
			KeyHandler.instance.keyHook(this);
		else
			WRenderer.mc.displayGuiScreen(new GuiMain(this));
		if (!ev.isCurrent())
			requestClose();
		super.update(ev, pgp, p);
	}
}
