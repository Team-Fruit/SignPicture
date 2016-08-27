package com.kamesuta.mc.signpic.placer;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

import com.kamesuta.mc.guiwidget.WBase;
import com.kamesuta.mc.guiwidget.WEvent;
import com.kamesuta.mc.guiwidget.WFrame;
import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.Point;
import com.kamesuta.mc.guiwidget.position.RArea;
import com.kamesuta.mc.signpic.Reference;

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
		//add(new WBase(RArea.diff(0, 0, 0, 0)) {
		//	@Override
		//	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		//		glColor4f(0f, 0f, 0f, 0.3f);
		//		glDisable(GL_TEXTURE_2D);
		//		draw(getGuiPosition(pgp), GL_QUADS);
		//		glEnable(GL_TEXTURE_2D);
		//	}
		//});
		//add(new WBase(new RArea(Coord.left(5), Coord.bottom(150), Coord.left(135), Coord.bottom(20))) {
		//	@Override
		//	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		//		glColor4f(0f, 0f, 0f, 0.3f);
		//		glDisable(GL_TEXTURE_2D);
		//		draw(getGuiPosition(pgp), GL_QUADS);
		//		glEnable(GL_TEXTURE_2D);
		//	}
		//});
		//add(new MTextField(new RArea(Coord.left(5), Coord.bottom(20), Coord.right(5), Coord.bottom(5)), "aaaa") {
		//	@Override
		//	public void onFocusChanged() {
		//		super.onFocusChanged();
		//		GuiSignPicture.this.url = getText();
		//	}
		//});
		////add(new MButton(new LRArea(5, -21, 30, -6, true), "aaaa") {
		//final Coord c = Coord.width(0);
		//add(new MButton(new RArea(Coord.bottom(10), Coord.left(0), c.add(Easings.easeOutCirc.move(2f, 30)), Coord.height(20), true), "aaaa") {
		//	@Override
		//	protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
		//		c.motion.stop().add(Easings.easeOutCirc.move(2f, c.motion.getLast() + 30));
		//		return true;
		//	}
		//});
		Reference.logger.info("stencil: " + glGetInteger(GL_STENCIL_BITS));
		add(new WBase(RArea.diff(0, 0, 0, 0)) {
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
				glClear(GL_DEPTH_BUFFER_BIT);
				glEnable(GL_STENCIL_TEST);
				glColorMask(false, false, false, false);
				glDepthMask(false);
				glStencilFunc(GL_NEVER, 1, 0xFF);
				glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);  // draw 1s on test fail (always)

				// draw stencil pattern
				glStencilMask(0xFF);
				glClear(GL_STENCIL_BUFFER_BIT);  // needs mask=0xFF

				glColor4f(1f, 1f, 1f, 0.7f);
				glDisable(GL_TEXTURE_2D);
				draw(new Area(75, 75, 250, 250), GL_QUADS);
				glEnable(GL_TEXTURE_2D);

				glColorMask(true, true, true, true);
				glDepthMask(true);
				glStencilMask(0x00);
				// draw where stencil's value is 0
				glStencilFunc(GL_EQUAL, 0, 0xFF);
				/* (nothing to draw) */
				// draw only where stencil's value is 1
				glStencilFunc(GL_EQUAL, 1, 0xFF);

				glColor4f(0f, 0f, 0f, 0.3f);
				glDisable(GL_TEXTURE_2D);
				draw(new Area(0, 0, 150, 150), GL_QUADS);
				glEnable(GL_TEXTURE_2D);

				glDisable(GL_STENCIL_TEST);
			}
		});
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}
}
