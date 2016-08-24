package com.kamesuta.mc.signpic.placer;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;

import org.lwjgl.input.Keyboard;

import com.kamesuta.mc.guiwidget.WBase;
import com.kamesuta.mc.guiwidget.WEvent;
import com.kamesuta.mc.guiwidget.WFrame;
import com.kamesuta.mc.guiwidget.component.MButton;
import com.kamesuta.mc.guiwidget.component.MTextField;
import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.Coord;
import com.kamesuta.mc.guiwidget.position.Point;
import com.kamesuta.mc.guiwidget.position.RArea;
import com.kamesuta.mc.guiwidget.position.relative.LRArea;
import com.kamesuta.mc.signpic.Reference;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class GuiSignPicture extends WFrame {
	protected String url;
	protected String[] signurl;

	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	protected void initWidgets() {
		add(new WBase(LRArea.createFit(true)) {
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
				glColor4f(0f, 0f, 0f, 0.3f);
				glDisable(GL_TEXTURE_2D);
				draw(getGuiPosition(pgp), GL_QUADS);
				glEnable(GL_TEXTURE_2D);
			}
		});
		add(new WBase(new LRArea(5, -151, 135, -21, true)) {
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
				glColor4f(0f, 0f, 0f, 0.3f);
				glDisable(GL_TEXTURE_2D);
				draw(getGuiPosition(pgp), GL_QUADS);
				glEnable(GL_TEXTURE_2D);
			}
		});
		add(new MTextField(new LRArea(5, -21, -6, -6, true), "aaaa") {
			@Override
			public void onFocusChanged() {
				super.onFocusChanged();
				GuiSignPicture.this.url = getText();
			}
		});
		// add(new MButton(new LRArea(5, -21, 30, -6, true), "aaaa") {
		add(new MButton(new RArea(Coord.bottom(10), Coord.left(0), Coord.width(20), Coord.height(20), true), "aaaa") {
			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				final ModContainer container = Loader.instance().getIndexedModList().get("signpic");
				if (container != null) {
					final File f = container.getSource();
					Reference.logger.info(f);
					Reference.logger.info(f.isFile());
				} else
					Reference.logger.info("Not Found");
				return true;
			}
		});
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}
}
