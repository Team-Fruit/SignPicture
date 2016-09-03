package com.kamesuta.mc.signpic.gui;

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
import com.kamesuta.mc.guiwidget.position.R;
import com.kamesuta.mc.guiwidget.position.RArea;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.mode.Mode;
import com.kamesuta.mc.signpic.proxy.ClientProxy;

import net.minecraft.client.resources.I18n;

public class GuiSignPicture extends WFrame {
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
						add(new SignPictureLabel(new RArea(Coord.top(5), Coord.left(5), Coord.right(5), Coord.bottom(5)), ClientProxy.manager).setSign(CurrentMode.instance.getSign()));
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
				float i = 175;

				add(new MLabel(new RArea(Coord.right(5), Coord.bottom(i-=20), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.width")));
				add(new MNumber(new RArea(Coord.right(5), Coord.bottom(i-=15), Coord.left(5), Coord.height(15)), 15) {
					{
						if (CurrentMode.instance.getSign().isSizeVaild())
							setNumber(CurrentMode.instance.getSign().size.width);
					}

					@Override
					protected void onNumberChanged(final String oldText, final String newText) {
						CurrentMode.instance.getSign().setSize(CurrentMode.instance.getSign().size.imageWidth(newText));
					}
				});
				add(new MLabel(new RArea(Coord.right(5), Coord.bottom(i-=20), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.height")));
				add(new MNumber(new RArea(Coord.right(5), Coord.bottom(i-=15), Coord.left(5), Coord.height(15)), 15) {
					{
						if (CurrentMode.instance.getSign().isSizeVaild())
							setNumber(CurrentMode.instance.getSign().size.height);
					}

					@Override
					protected void onNumberChanged(final String oldText, final String newText) {
						CurrentMode.instance.getSign().setSize(CurrentMode.instance.getSign().size.imageHeight(newText));
					}
				});
				add(new FunnyButton(new RArea(Coord.right(5), Coord.bottom(i-=25), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.continue")) {
					@Override
					protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
						CurrentMode.instance.setContinue(!CurrentMode.instance.isContinue());
						return true;
					}

					@Override
					public boolean isEnabled() {
						state(CurrentMode.instance.isContinue());
						return true;
					}
				});
				add(new FunnyButton(new RArea(Coord.right(5), Coord.bottom(i-=25), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.copy")) {
					@Override
					protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
						CurrentMode.instance.setMode(Mode.COPY);
						requestClose();
						return true;
					}

					@Override
					public boolean isEnabled() {
						state(CurrentMode.instance.isMode(Mode.COPY));
						return true;
					}
				});
				add(new FunnyButton(new RArea(Coord.right(5), Coord.bottom(i-=25), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.place")) {
					@Override
					protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
						if (CurrentMode.instance.getSign().isVaild()) {
							CurrentMode.instance.setMode(Mode.PLACE);
							requestClose();
							return true;
						}
						return false;
					}

					@Override
					public boolean isEnabled() {
						state(CurrentMode.instance.isMode(Mode.PLACE));
						return CurrentMode.instance.getSign().isVaild() && !CurrentMode.instance.isMode(Mode.PLACE);
					}
				});
				add(new MButton(new RArea(Coord.right(5), Coord.bottom(i-=25), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.cancel")) {
					@Override
					protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
						if (CurrentMode.instance.isMode()) {
							CurrentMode.instance.setMode();
							return true;
						}
						return false;
					}

					@Override
					public boolean isEnabled() {
						return CurrentMode.instance.isMode();
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
			{
				if (CurrentMode.instance.getSign().id != null)
					setText(CurrentMode.instance.getSign().id);
			}

			@Override
			public void onFocusChanged() {
				super.onFocusChanged();
				CurrentMode.instance.getSign().setId(getText());
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

	public class FunnyButton extends MButton {
		public FunnyButton(final R position, final String text) {
			super(position, text);
		}

		boolean hover;
		MotionQueue m = new MotionQueue(0);
		MotionQueue s = new MotionQueue(1);

		protected void state(final boolean b) {
			if (b) {
				if (!this.hover) {
					this.hover = true;
					this.m.stop().add(Easings.easeOutElastic.move(.5f, 6f)).start();
					this.s.stop().add(Easings.easeOutElastic.move(.5f, 1.1f)).start();
				}
			} else {
				if (this.hover) {
					this.hover = false;
					this.m.stop().add(Easings.easeOutElastic.move(.5f, 0f)).start();
					this.s.stop().add(Easings.easeOutElastic.move(.5f, 1f)).start();
				}
			}
		}

		@Override
		public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
			final Area a = getGuiPosition(pgp);
			glPushMatrix();
			glTranslatef(a.x1()+a.w()/2, a.y1()+a.h()/2, 0);
			final float c = this.s.get();
			glScalef(c, c, 1f);
			glRotatef(this.m.get(), 0, 0, 1);
			glTranslatef(-a.x1()-a.w()/2, -a.y1()-a.h()/2, 0);
			super.draw(ev, pgp, p, frame);
			glPopMatrix();
		}
	}
}
