package com.kamesuta.mc.signpic.placer;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

import com.kamesuta.mc.guiwidget.WBase;
import com.kamesuta.mc.guiwidget.WEvent;
import com.kamesuta.mc.guiwidget.WFrame;
import com.kamesuta.mc.guiwidget.WPanel;
import com.kamesuta.mc.guiwidget.animation.BlankMotion;
import com.kamesuta.mc.guiwidget.animation.Easings;
import com.kamesuta.mc.guiwidget.animation.MotionQueue;
import com.kamesuta.mc.guiwidget.component.MButton;
import com.kamesuta.mc.guiwidget.component.MLabel;
import com.kamesuta.mc.guiwidget.component.MNumber;
import com.kamesuta.mc.guiwidget.component.MPanel;
import com.kamesuta.mc.guiwidget.component.MTextField;
import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.Coord;
import com.kamesuta.mc.guiwidget.position.Point;
import com.kamesuta.mc.guiwidget.position.RArea;
import com.kamesuta.mc.signpic.image.ImageSize;
import com.kamesuta.mc.signpic.proxy.ClientProxy;
import com.kamesuta.mc.signpic.util.Sign;

public class GuiSignPicture extends WFrame {
	private final Sign sign = new Sign().setSize(ImageSize.UnknownSize);
	//	protected SignPictureLabel picture;

	public GuiSignPicture() {
	}

	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	protected void initWidgets() {
		//		add(new WBase(RArea.diff(0, 0, 0, 0)) {
		//			MotionQueue m = new MotionQueue(0).add(Easings.linear.move(.5f, .5f)).start();
		//			@Override
		//			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		//				glColor4f(0f, 0f, 0f, this.m.get());
		//				drawRect(getGuiPosition(pgp));
		//			}
		//
		//			@Override
		//			public void onCloseRequest(final WEvent ev, final Area pgp, final Point mouse) {
		//				this.m.stop().add(Easings.linear.move(.5f, 0f)).add(new BlankMotion(.2f)).start();
		//			}
		//
		//			@Override
		//			public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
		//				return this.m.isFinished();
		//			}
		//		});
		//
		//		add(new MCheckBox(new RArea(Coord.top(5), Coord.left(5), Coord.height(15), Coord.width(15)), "aaaabbbccc"));
		//
		//		add(new MPanel(new RArea(Coord.top(5), Coord.right(5), Coord.height(80), Coord.width(100))));
		//
		//		add(new MNumber(new RArea(Coord.bottom(40), Coord.right(5), Coord.height(15), Coord.width(100)), 15));
		//
		//		final Coord s = Coord.left(-180).add(Easings.easeOutExpo.move(.5f, 5)).start();
		//		this.picture = new SignPictureLabel(new RArea(s, Coord.height(180), Coord.width(180), Coord.bottom(20)), ClientProxy.manager) {
		//			@Override
		//			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		//				final Area a = getGuiPosition(pgp);
		//				glColor4f(0f, 0f, 0f, 0.3f);
		//				drawRect(a);
		//				super.draw(ev, pgp, p, frame);
		//			}
		//
		//			@Override
		//			public void onCloseRequest(final WEvent ev, final Area pgp, final Point mouse) {
		//				s.motion.stop().add(Easings.easeOutExpo.move(.5f, -180)).start();
		//			}
		//
		//			@Override
		//			public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
		//				return s.motion.isFinished();
		//			}
		//		};
		//		add(this.picture);
		//		final Coord d = Coord.bottom(-15).add(Easings.easeOutCirc.move(.5f, 5)).start();
		//		add(new MTextField(new RArea(Coord.left(5), d, Coord.right(5), Coord.height(15)), "aaaa") {
		//			@Override
		//			public void onFocusChanged() {
		//				super.onFocusChanged();
		//				final String url = getText();
		//				GuiSignPicture.this.picture.setUrl(url);
		//				for (int i=0; i<4; i++) {
		//					if (16*i <= url.length())
		//						GuiSignPicture.this.signurl[i] = url.substring(16*i, Math.min(16*i+15, url.length()));
		//				}
		//			}
		//
		//			@Override
		//			public void onCloseRequest(final WEvent ev, final Area pgp, final Point mouse) {
		//				d.motion.stop().add(Easings.easeOutCirc.move(1f, -15)).start();
		//			}
		//
		//			@Override
		//			public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
		//				return d.motion.isFinished();
		//			}
		//		});
		//		//add(new MButton(new LRArea(5, -21, 30, -6, true), "aaaa") {
		//		final Coord c = Coord.left(-70);
		//		add(new MButton(new RArea(Coord.top(20), c, Coord.width(80), Coord.height(20)), "aaaa") {
		//			boolean hover;
		//			@Override
		//			public void mouseMoved(final WEvent ev, final Area pgp, final Point p, final int button) {
		//				super.mouseMoved(ev, pgp, p, button);
		//				final Area a = getGuiPosition(pgp);
		//				final boolean h = a.pointInside(p);
		//				if (h) {
		//					if (!this.hover) {
		//						this.hover = h;
		//						c.motion.stop().add(Easings.easeOutBounce.move(.5f, 0)).start();
		//					}
		//				} else {
		//					if (this.hover) {
		//						this.hover = h;
		//						c.motion.stop().add(Easings.easeOutBounce.move(.5f, -70)).start();
		//					}
		//				}
		//			}
		//
		//			@Override
		//			protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
		//				//c.motion.stop().add(Easings.easeOutCirc.move(2f, c.motion.getLast() + 30));
		//				return true;
		//			}
		//		});
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
		add(new WBase(RArea.diff(0, 0, 0, 0)) {
			MotionQueue m = new MotionQueue(0).add(Easings.linear.move(.2f, .5f)).start();
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
				glColor4f(0f, 0f, 0f, this.m.get());
				drawRect(getGuiPosition(pgp));
			}

			@Override
			public void onCloseRequest(final WEvent ev, final Area pgp, final Point mouse) {
				this.m.stop().add(Easings.linear.move(.2f, 0f)).add(new BlankMotion(.1f)).start();
			}

			@Override
			public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
				return this.m.isFinished();
			}
		});

		final Coord m = Coord.ptop(-1f).add(Easings.easeOutElastic.move(.5f, 0f)).start();
		add(new WPanel(new RArea(m, Coord.left(0), Coord.right(0), Coord.pheight(1f))) {
			{
				add(new MPanel(new RArea(Coord.top(5), Coord.left(5), Coord.right(70), Coord.bottom(25))) {
					{
						add(new SignPictureLabel(new RArea(Coord.top(5), Coord.left(5), Coord.right(5), Coord.bottom(5)), ClientProxy.manager).setSign(GuiSignPicture.this.sign));
					}
				});
			}

			@Override
			public void onCloseRequest(final WEvent ev, final Area pgp, final Point mouse) {
				m.motion.stop().add(Easings.easeOutElastic.move(.5f, -1f)).start();
			}

			@Override
			public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
				return m.motion.isFinished();
			}
		});

		final Coord p = Coord.right(-60).add(Easings.easeOutBounce.move(.5f, 0)).start();
		add(new WPanel(new RArea(Coord.top(0), p, Coord.width(70), Coord.bottom(0))) {
			{
				float i = 125;

				add(new MLabel(new RArea(Coord.right(5), Coord.bottom(i-=20), Coord.left(5), Coord.height(15)), "Width"));
				add(new MNumber(new RArea(Coord.right(5), Coord.bottom(i-=15), Coord.left(5), Coord.height(15)), 15) {
					@Override
					protected void onNumberChanged(final String oldText, final String newText) {
						GuiSignPicture.this.sign.setSize(GuiSignPicture.this.sign.size().imageWidth(newText));
					}
				});
				add(new MLabel(new RArea(Coord.right(5), Coord.bottom(i-=20), Coord.left(5), Coord.height(15)), "Height"));
				add(new MNumber(new RArea(Coord.right(5), Coord.bottom(i-=15), Coord.left(5), Coord.height(15)), 15) {
					@Override
					protected void onNumberChanged(final String oldText, final String newText) {
						GuiSignPicture.this.sign.setSize(GuiSignPicture.this.sign.size().imageHeight(newText));
					}
				});
				add(new MButton(new RArea(Coord.right(5), Coord.bottom(i-=25), Coord.left(5), Coord.height(15)), "Apply") {
					@Override
					protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
						if (GuiSignPicture.this.sign.isVaild()) {
							PlacerMode.instance.enable(GuiSignPicture.this.sign);
							requestClose();
							return true;
						}
						return false;
					}

					@Override
					public boolean isEnabled() {
						return GuiSignPicture.this.sign.isVaild() && !PlacerMode.instance.isEnabled();
					}
				});
				add(new MButton(new RArea(Coord.right(5), Coord.bottom(i-=25), Coord.left(5), Coord.height(15)), "Cancel") {
					@Override
					protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
						if (PlacerMode.instance.isEnabled()) {
							PlacerMode.instance.disable();
							return true;
						}
						return false;
					}

					@Override
					public boolean isEnabled() {
						return PlacerMode.instance.isEnabled();
					}
				});
			}

			@Override
			public void onCloseRequest(final WEvent ev, final Area pgp, final Point mouse) {
				p.motion.stop().add(Easings.easeOutBounce.move(.5f, -60)).start();
			}

			@Override
			public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
				return p.motion.isFinished();
			}
		});

		final Coord d = Coord.bottom(-15).add(Easings.easeOutElastic.move(.5f, 5)).start();
		add(new MTextField(new RArea(Coord.left(5), d, Coord.right(70), Coord.height(15)), "URL Here") {
			@Override
			public void onFocusChanged() {
				super.onFocusChanged();
				GuiSignPicture.this.sign.setId(getText());
			}

			@Override
			protected void onTextChanged(final String oldText) {
			}

			@Override
			public void onCloseRequest(final WEvent ev, final Area pgp, final Point mouse) {
				d.motion.stop().add(Easings.easeOutElastic.move(1f, -15)).start();
			}

			@Override
			public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
				return d.motion.isFinished();
			}
		});
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
