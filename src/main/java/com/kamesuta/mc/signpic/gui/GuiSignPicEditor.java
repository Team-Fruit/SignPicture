package com.kamesuta.mc.signpic.gui;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MButton;
import com.kamesuta.mc.bnnwidget.component.MChatTextField;
import com.kamesuta.mc.bnnwidget.component.MLabel;
import com.kamesuta.mc.bnnwidget.component.MNumber;
import com.kamesuta.mc.bnnwidget.component.MPanel;
import com.kamesuta.mc.bnnwidget.motion.BlankMotion;
import com.kamesuta.mc.bnnwidget.motion.EasingMotion;
import com.kamesuta.mc.bnnwidget.motion.MotionQueue;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.position.RArea;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.EntryIdBuilder;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.render.RenderHelper;
import com.kamesuta.mc.signpic.util.Sign;

import net.minecraft.client.resources.I18n;

public class GuiSignPicEditor extends WFrame {
	private final EntryIdBuilder signbuilder = new EntryIdBuilder(CurrentMode.instance.getEntryId());

	public GuiSignPicEditor() {
	}

	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	protected void init() {
		add(new WPanel(RArea.diff(0, 0, 0, 0)) {
			@Override
			protected void initWidget() {
				add(new WBase(RArea.diff(0, 0, 0, 0)) {
					MotionQueue m = new MotionQueue(0);
					@Override
					public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
						RenderHelper.startShape();
						glColor4f(0f, 0f, 0f, this.m.get());
						drawRect(getGuiPosition(pgp));
					}

					protected boolean b = !CurrentMode.instance.isState(CurrentMode.State.PREVIEW);
					@Override
					public void update(final WEvent ev, final Area pgp, final Point p) {
						if (CurrentMode.instance.isState(CurrentMode.State.PREVIEW)) {
							if (!this.b) {
								this.b = true;
								this.m.stop().add(EasingMotion.easeLinear.move(.2f, 0f)).start();
							}
						} else {
							if (this.b) {
								this.b = false;
								this.m.stop().add(EasingMotion.easeLinear.move(.2f, .5f)).start();
							}
						}
						super.update(ev, pgp, p);
					}

					@Override
					public void onCloseRequest(final WEvent ev, final Area pgp, final Point mouse) {
						this.m.stop().add(EasingMotion.easeLinear.move(.2f, 0f)).start();
					}

					@Override
					public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
						return this.m.isFinished();
					}
				});

				final GuiRotation guiRotation = new GuiRotation(new RArea(Coord.top(0), Coord.left(0), Coord.right(70), Coord.bottom(15))) {
					@Override
					protected void onUpdate() {
						super.onUpdate();
						Sign.updatePreview(GuiSignPicEditor.this.signbuilder.build());
					}
				};
				guiRotation.setRotation(GuiSignPicEditor.this.signbuilder.getMeta().rotation);
				add(guiRotation);

				final Coord m = Coord.ptop(-1f);
				add(new WPanel(new RArea(m, Coord.left(0), Coord.right(0), Coord.pheight(1f))) {
					@Override
					protected void initWidget() {
						add(new MPanel(new RArea(Coord.top(5), Coord.left(5), Coord.right(70), Coord.bottom(25))) {
							{
								add(new SignPicLabel(new RArea(Coord.top(5), Coord.left(5), Coord.right(5), Coord.bottom(5)), ContentManager.instance) {
									@Override
									public EntryId getEntryId() {
										return CurrentMode.instance.getEntryId();
									}
								});
							}

							protected boolean b = !CurrentMode.instance.isState(CurrentMode.State.PREVIEW);
							@Override
							public void update(final WEvent ev, final Area pgp, final Point p) {
								if (CurrentMode.instance.isState(CurrentMode.State.PREVIEW)) {
									if (!this.b) {
										this.b = true;
										m.motion.stop().add(EasingMotion.easeOutElastic.move(.5f, -1f)).start();
									}
								} else {
									if (this.b) {
										this.b = false;
										m.motion.stop().add(EasingMotion.easeOutElastic.move(.5f, 0f)).start();
									}
								}
								super.update(ev, pgp, p);
							}
						});
					}

					@Override
					public void onCloseRequest(final WEvent ev, final Area pgp, final Point mouse) {
						m.motion.stop().add(EasingMotion.easeOutElastic.move(.5f, -1f)).start();
					}

					@Override
					public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
						return m.motion.isFinished();
					}
				});

				final Coord p = Coord.right(-65).add(EasingMotion.easeOutBounce.move(.5f, 0)).start();
				add(new WPanel(new RArea(Coord.top(0), p, Coord.width(70), Coord.bottom(0))) {
					@Override
					protected void initWidget() {
						float top = -20f;

						add(new FunnyButton(new RArea(Coord.right(5), Coord.top(top+=25), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.see")) {
							@Override
							protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								CurrentMode.instance.setState(CurrentMode.State.SEE, !CurrentMode.instance.isState(CurrentMode.State.SEE));
								return true;
							}

							@Override
							public boolean isEnabled() {
								state(CurrentMode.instance.isState(CurrentMode.State.SEE));
								return true;
							}
						});
						add(new FunnyButton(new RArea(Coord.right(5), Coord.top(top+=25), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.preview")) {
							@Override
							protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								final boolean state = CurrentMode.instance.isState(CurrentMode.State.PREVIEW);
								CurrentMode.instance.setState(CurrentMode.State.PREVIEW, !state);
								if (!state) {
									CurrentMode.instance.setMode(CurrentMode.Mode.SETPREVIEW);
									requestClose();
								} else {
									Sign.preview.setVisible(false);
								}
								return true;
							}

							@Override
							public boolean isEnabled() {
								state(CurrentMode.instance.isState(CurrentMode.State.PREVIEW));
								return true;
							}
						});

						float bottom = 175;

						add(new MLabel(new RArea(Coord.right(5), Coord.bottom(bottom-=20), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.width")));
						add(new MNumber(new RArea(Coord.right(5), Coord.bottom(bottom-=15), Coord.left(5), Coord.height(15)), 15) {
							{
								if (GuiSignPicEditor.this.signbuilder.getMeta().size.vaildWidth())
									setNumber(GuiSignPicEditor.this.signbuilder.getMeta().size.width);
							}

							@Override
							protected void onNumberChanged(final String oldText, final String newText) {
								GuiSignPicEditor.this.signbuilder.getMeta().size.setWidth(newText);
								CurrentMode.instance.setEntryId(GuiSignPicEditor.this.signbuilder.build());
							}
						});
						add(new MLabel(new RArea(Coord.right(5), Coord.bottom(bottom-=20), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.height")));
						add(new MNumber(new RArea(Coord.right(5), Coord.bottom(bottom-=15), Coord.left(5), Coord.height(15)), 15) {
							{
								if (GuiSignPicEditor.this.signbuilder.getMeta().size.vaildHeight())
									setNumber(GuiSignPicEditor.this.signbuilder.getMeta().size.height);
							}

							@Override
							protected void onNumberChanged(final String oldText, final String newText) {
								GuiSignPicEditor.this.signbuilder.getMeta().size.setHeight(newText);
								CurrentMode.instance.setEntryId(GuiSignPicEditor.this.signbuilder.build());
							}
						});
						add(new FunnyButton(new RArea(Coord.right(5), Coord.bottom(bottom-=25), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.continue")) {
							@Override
							protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								CurrentMode.instance.setState(CurrentMode.State.CONTINUE, !CurrentMode.instance.isState(CurrentMode.State.CONTINUE));
								return true;
							}

							@Override
							public boolean isEnabled() {
								state(CurrentMode.instance.isState(CurrentMode.State.CONTINUE));
								return true;
							}
						});
						add(new FunnyButton(new RArea(Coord.right(5), Coord.bottom(bottom-=25), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.load")) {
							@Override
							protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								CurrentMode.instance.setMode(CurrentMode.Mode.LOAD);
								requestClose();
								return true;
							}

							@Override
							public boolean isEnabled() {
								state(CurrentMode.instance.isMode(CurrentMode.Mode.LOAD));
								return !CurrentMode.instance.isMode(CurrentMode.Mode.LOAD);
							}
						});
						add(new FunnyButton(new RArea(Coord.right(5), Coord.bottom(bottom-=25), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.place")) {
							@Override
							protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								final Entry entry = CurrentMode.instance.getEntryId().entry();
								if (entry.isValid() && Sign.isPlaceable(entry.id)) {
									CurrentMode.instance.setMode(CurrentMode.Mode.PLACE);
									requestClose();
									return true;
								}
								return false;
							}

							@Override
							public boolean isEnabled() {
								state(CurrentMode.instance.isMode(CurrentMode.Mode.PLACE));
								final Entry entry = CurrentMode.instance.getEntryId().entry();
								return entry.isValid() && Sign.isPlaceable(entry.id) && !CurrentMode.instance.isMode(CurrentMode.Mode.PLACE);
							}
						});
						add(new MButton(new RArea(Coord.right(5), Coord.bottom(bottom-=25), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.cancel")) {
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
						p.motion.stop().add(EasingMotion.easeOutBounce.move(.5f, -65)).add(new BlankMotion(.1f)).start();
					}

					@Override
					public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
						return p.motion.isFinished();
					}
				});

				final Coord d = Coord.bottom(-15).add(EasingMotion.easeOutElastic.move(.5f, 5)).start();
				add(new MChatTextField(new RArea(Coord.left(5), d, Coord.right(70), Coord.height(15))) {
					@Override
					public void init() {
						super.init();
						setMaxStringLength(Integer.MAX_VALUE);
						setWatermark(I18n.format("signpic.gui.editor.textfield"));
						final String id = GuiSignPicEditor.this.signbuilder.getURI();
						if (id != null) {
							setText(id);
						}
					}

					@Override
					public void onFocusChanged() {
						final EntryId entryId = new EntryId(getText());
						if (entryId.hasMeta())
							GuiSignPicEditor.this.signbuilder.setMeta(entryId.getMeta());
						if (entryId.hasContentId())
							GuiSignPicEditor.this.signbuilder.setURI(entryId.getContentId().getURI());
						CurrentMode.instance.setEntryId(GuiSignPicEditor.this.signbuilder.build());
					}

					@Override
					public void onCloseRequest(final WEvent ev, final Area pgp, final Point mouse) {
						super.onCloseRequest(ev, pgp, mouse);
						d.motion.stop().add(EasingMotion.easeOutElastic.move(1f, -15)).start();
					}

					@Override
					public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
						return d.motion.isFinished();
					}
				});
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
					this.m.stop().add(EasingMotion.easeOutElastic.move(.5f, 6f)).start();
					this.s.stop().add(EasingMotion.easeOutElastic.move(.5f, 1.1f)).start();
				}
			} else {
				if (this.hover) {
					this.hover = false;
					this.m.stop().add(EasingMotion.easeOutElastic.move(.5f, 0f)).start();
					this.s.stop().add(EasingMotion.easeOutElastic.move(.5f, 1f)).start();
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
