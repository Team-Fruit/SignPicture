package com.kamesuta.mc.signpic.placer;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

import com.kamesuta.mc.guiwidget.WBase;
import com.kamesuta.mc.guiwidget.WEvent;
import com.kamesuta.mc.guiwidget.WFrame;
import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.Point;
import com.kamesuta.mc.guiwidget.position.RArea;

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
		//		add(new WBase(RArea.diff(0, 0, 0, 0)) {
		//			@Override
		//			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		//				glColor4f(0f, 0f, 0f, 0.3f);
		//				glDisable(GL_TEXTURE_2D);
		//				draw(getGuiPosition(pgp), GL_QUADS);
		//				glEnable(GL_TEXTURE_2D);
		//			}
		//		});
		//		add(new WBase(new RArea(Coord.left(5), Coord.bottom(150), Coord.left(135), Coord.bottom(20))) {
		//			@Override
		//			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		//				glColor4f(0f, 0f, 0f, 0.3f);
		//				glDisable(GL_TEXTURE_2D);
		//				draw(getGuiPosition(pgp), GL_QUADS);
		//				glEnable(GL_TEXTURE_2D);
		//			}
		//		});
		//		add(new MTextField(new RArea(Coord.left(5), Coord.bottom(20), Coord.right(5), Coord.bottom(5)), "aaaa") {
		//			@Override
		//			public void onFocusChanged() {
		//				super.onFocusChanged();
		//				GuiSignPicture.this.url = getText();
		//			}
		//		});
		//		//add(new MButton(new LRArea(5, -21, 30, -6, true), "aaaa") {
		//		final Coord c = Coord.width(0);
		//		add(new MButton(new RArea(Coord.bottom(10), Coord.left(0), c.add(Easings.easeOutCirc.move(2f, 30)), Coord.height(20)), "aaaa") {
		//			@Override
		//			protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
		//				c.motion.stop().add(Easings.easeOutCirc.move(2f, c.motion.getLast() + 30));
		//				return true;
		//			}
		//		});
		add(new WBase(RArea.diff(0, 0, 0, 0)) {
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
				final Area a = new Area(75, 75, 150, 150);
				final Area b = new Area(85, 85, 160, 160);
				clip.clipArea(b);
				glColor4f(0f, 0f, 1f, 0.3f);
				glDisable(GL_TEXTURE_2D);
				draw(pgp, GL_QUADS);
				glEnable(GL_TEXTURE_2D);
				clip.end();
				clip.clipArea(a);
				glColor4f(1f, 0f, 0f, 0.3f);
				glDisable(GL_TEXTURE_2D);
				draw(pgp, GL_QUADS);
				glEnable(GL_TEXTURE_2D);
				clip.end();

				glDisable(GL_TEXTURE_2D);
				glColor4f(1f, 0f, 0f, 1f);
				draw(a, GL_LINE_LOOP);
				draw(b, GL_LINE_LOOP);
				glEnable(GL_TEXTURE_2D);
			}
		});
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}
}
