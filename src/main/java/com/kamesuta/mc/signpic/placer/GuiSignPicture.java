package com.kamesuta.mc.signpic.placer;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

import com.kamesuta.mc.guiwidget.WBase;
import com.kamesuta.mc.guiwidget.WEvent;
import com.kamesuta.mc.guiwidget.WFrame;
import com.kamesuta.mc.guiwidget.animation.BlankMotion;
import com.kamesuta.mc.guiwidget.animation.Easings;
import com.kamesuta.mc.guiwidget.animation.MotionQueue;
import com.kamesuta.mc.guiwidget.component.MButton;
import com.kamesuta.mc.guiwidget.component.MTextField;
import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.Coord;
import com.kamesuta.mc.guiwidget.position.Point;
import com.kamesuta.mc.guiwidget.position.RArea;
import com.kamesuta.mc.signpic.proxy.ClientProxy;

public class GuiSignPicture extends WFrame {
	protected String[] signurl = new String[4];
	protected SignPictureLabel picture;

	public GuiSignPicture() {
	}

	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	protected void initWidgets() {
		add(new WBase(RArea.diff(0, 0, 0, 0)) {
			MotionQueue m = new MotionQueue(0).add(Easings.linear.move(.5f, .5f)).start();
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
				glColor4f(0f, 0f, 0f, this.m.get());
				glDisable(GL_TEXTURE_2D);
				draw(getGuiPosition(pgp), GL_QUADS);
				glEnable(GL_TEXTURE_2D);
			}

			@Override
			public void onCloseRequest(final WEvent ev, final Area pgp, final Point mouse) {
				this.m.stop().add(Easings.linear.move(.5f, 0f)).add(new BlankMotion(.2f)).start();
			}

			@Override
			public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
				return this.m.isFinished();
			}
		});
		final Coord s = Coord.left(-130).add(Easings.easeOutExpo.move(.5f, 5)).start();
		this.picture = new SignPictureLabel(new RArea(s, Coord.bottom(150), Coord.width(130), Coord.bottom(20)), ClientProxy.manager) {
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
				final Area a = getGuiPosition(pgp);
				glColor4f(0f, 0f, 0f, 0.3f);
				glDisable(GL_TEXTURE_2D);
				draw(a, GL_QUADS);
				glEnable(GL_TEXTURE_2D);
				super.draw(ev, pgp, p, frame);
			}

			@Override
			public void onCloseRequest(final WEvent ev, final Area pgp, final Point mouse) {
				s.motion.stop().add(Easings.easeOutExpo.move(.5f, -130)).start();
			}

			@Override
			public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
				return s.motion.isFinished();
			}
		};
		add(this.picture);
		final Coord d = Coord.bottom(-15).add(Easings.easeOutCirc.move(.5f, 5)).start();
		add(new MTextField(new RArea(Coord.left(5), d, Coord.right(5), Coord.height(15)), "aaaa") {
			@Override
			public void onFocusChanged() {
				super.onFocusChanged();
				final String url = getText();
				GuiSignPicture.this.picture.setUrl(url);
				for (int i=0; i<4; i++) {
					if (16*i <= url.length())
						GuiSignPicture.this.signurl[i] = url.substring(16*i, Math.min(16*i+15, url.length()));
				}
			}

			@Override
			public void onCloseRequest(final WEvent ev, final Area pgp, final Point mouse) {
				d.motion.stop().add(Easings.easeOutCirc.move(1f, -15)).start();
			}

			@Override
			public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
				return d.motion.isFinished();
			}
		});
		//add(new MButton(new LRArea(5, -21, 30, -6, true), "aaaa") {
		final Coord c = Coord.left(-70);
		add(new MButton(new RArea(Coord.top(20), c, Coord.width(80), Coord.height(20)), "aaaa") {
			boolean hover;
			@Override
			public void mouseMoved(final WEvent ev, final Area pgp, final Point p, final int button) {
				super.mouseMoved(ev, pgp, p, button);
				final Area a = getGuiPosition(pgp);
				final boolean h = a.pointInside(p);
				if (h) {
					if (!this.hover) {
						this.hover = h;
						c.motion.stop().add(Easings.easeOutBounce.move(.5f, 0)).start();
					}
				} else {
					if (this.hover) {
						this.hover = h;
						c.motion.stop().add(Easings.easeOutBounce.move(.5f, -70)).start();
					}
				}
			}

			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				//c.motion.stop().add(Easings.easeOutCirc.move(2f, c.motion.getLast() + 30));
				return true;
			}
		});
		//		add(new WBase(RArea.diff(0, 0, 0, 0)) {
		//			@Override
		//			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		//				final Area a = new Area(75, 75, 150, 150);
		//				final Area b = new Area(85, 85, 160, 160);
		//				clip.clipArea(b);
		//				glColor4f(0f, 0f, 1f, 0.3f);
		//				glDisable(GL_TEXTURE_2D);
		//				draw(pgp, GL_QUADS);
		//				glEnable(GL_TEXTURE_2D);
		//				clip.end();
		//				clip.clipArea(a);
		//				glColor4f(1f, 0f, 0f, 0.3f);
		//				glDisable(GL_TEXTURE_2D);
		//				draw(pgp, GL_QUADS);
		//				glEnable(GL_TEXTURE_2D);
		//				clip.end();
		//
		//				glDisable(GL_TEXTURE_2D);
		//				glColor4f(1f, 0f, 0f, 1f);
		//				draw(a, GL_LINE_LOOP);
		//				draw(b, GL_LINE_LOOP);
		//				glEnable(GL_TEXTURE_2D);
		//			}
		//		});
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}
}
