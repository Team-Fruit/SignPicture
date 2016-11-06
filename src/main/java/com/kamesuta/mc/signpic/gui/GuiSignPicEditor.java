package com.kamesuta.mc.signpic.gui;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;
import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.FunnyButton;
import com.kamesuta.mc.bnnwidget.component.MButton;
import com.kamesuta.mc.bnnwidget.component.MChatTextField;
import com.kamesuta.mc.bnnwidget.component.MPanel;
import com.kamesuta.mc.bnnwidget.component.MSelect;
import com.kamesuta.mc.bnnwidget.component.MSelectLabel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.MCoord;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.EntryIdBuilder;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.gui.file.McUiUpload;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.render.RenderHelper;
import com.kamesuta.mc.signpic.util.Sign;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiSignPicEditor extends WFrame {
	private final EntryIdBuilder signbuilder = new EntryIdBuilder(CurrentMode.instance.getEntryId());
	private MChatTextField field;

	public GuiSignPicEditor(final GuiScreen parent) {
		super(parent);
	}

	public GuiSignPicEditor() {
	}

	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	protected void initWidget() {
		add(new WPanel(R.diff(0, 0, 0, 0)) {
			@Override
			protected void initWidget() {
				add(new WBase(R.diff(0, 0, 0, 0)) {
					MCoord m = new MCoord(0);

					@Override
					public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity) {
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
								this.m.stop().add(Easings.easeLinear.move(.2f, 0f)).start();
							}
						} else if (this.b) {
							this.b = false;
							this.m.stop().add(Easings.easeLinear.move(.2f, .5f)).start();
						}
						super.update(ev, pgp, p);
					}

					@Override
					public boolean onCloseRequest() {
						this.m.stop().add(Easings.easeLinear.move(.25f, 0f)).start();
						return false;
					}

					@Override
					public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
						return this.m.isFinished();
					}
				});

				add(new GuiSize(new R(Coord.top(5), Coord.left(5), Coord.width(15*8), Coord.height(15*2)), GuiSignPicEditor.this.signbuilder.getMeta().size) {
					@Override
					protected void onUpdate() {
						super.onUpdate();
						CurrentMode.instance.setEntryId(GuiSignPicEditor.this.signbuilder.build());
					}
				});

				add(new GuiOffset(new R(Coord.top(15*3+10), Coord.left(5), Coord.width(15*8), Coord.height(15*3)), GuiSignPicEditor.this.signbuilder.getMeta().offset) {
					@Override
					protected void onUpdate() {
						super.onUpdate();
						CurrentMode.instance.setEntryId(GuiSignPicEditor.this.signbuilder.build());
					}
				});

				add(new GuiRotation(new R(Coord.top(15*8), Coord.left(5), Coord.width(15*8), Coord.height(15*4)), GuiSignPicEditor.this.signbuilder.getMeta().rotation) {
					@Override
					protected void onUpdate() {
						super.onUpdate();
						CurrentMode.instance.setEntryId(GuiSignPicEditor.this.signbuilder.build());
					}
				});

				final MCoord m = MCoord.ptop(-1f);
				add(new WPanel(new R(m, Coord.left(15*8+5), Coord.right(0), Coord.pheight(1f))) {
					@Override
					protected void initWidget() {
						add(new MPanel(new R(Coord.top(5), Coord.left(5), Coord.right(70), Coord.bottom(25))) {
							{
								add(new SignPicLabel(new R(Coord.top(5), Coord.left(5), Coord.right(5), Coord.bottom(5)), ContentManager.instance) {
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
										m.stop().add(Easings.easeInBack.move(.25f, -1f)).start();
									}
								} else if (this.b) {
									this.b = false;
									m.stop().add(Easings.easeOutBack.move(.25f, 0f)).start();
								}
								super.update(ev, pgp, p);
							}
						});
					}

					@Override
					public boolean onCloseRequest() {
						m.stop().add(Easings.easeInBack.move(.25f, -1f)).start();
						return false;
					}

					@Override
					public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
						return m.isFinished();
					}
				});

				final MCoord p = MCoord.right(-65).add(Easings.easeOutBack.move(.25f, 0)).start();
				add(new WPanel(new R(Coord.top(0), p, Coord.width(70), Coord.bottom(0))) {
					@Override
					protected void initWidget() {
						float top = -20f;

						add(new FunnyButton(new R(Coord.right(5), Coord.top(top += 25), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.see")) {
							@Override
							protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								CurrentMode.instance.setState(CurrentMode.State.SEE, !CurrentMode.instance.isState(CurrentMode.State.SEE));
								return true;
							}

							@Override
							public boolean isHighlight() {
								return CurrentMode.instance.isState(CurrentMode.State.SEE);
							}
						});
						add(new FunnyButton(new R(Coord.right(5), Coord.top(top += 25), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.preview")) {
							@Override
							protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								final boolean state = CurrentMode.instance.isState(CurrentMode.State.PREVIEW);
								CurrentMode.instance.setState(CurrentMode.State.PREVIEW, !state);
								if (!state) {
									CurrentMode.instance.setMode(CurrentMode.Mode.SETPREVIEW);
									requestClose();
								} else {
									if (CurrentMode.instance.isMode(CurrentMode.Mode.SETPREVIEW))
										CurrentMode.instance.setMode();
									Sign.preview.setVisible(false);
								}
								return true;
							}

							@Override
							public boolean isHighlight() {
								return CurrentMode.instance.isState(CurrentMode.State.PREVIEW);
							}
						});
						add(new FunnyButton(new R(Coord.right(5), Coord.top(top += 25), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.file")) {
							@Override
							protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								McUiUpload.instance.setVisible(!McUiUpload.instance.isVisible());
								return true;
							}

							@Override
							public boolean isHighlight() {
								return McUiUpload.instance.isVisible();
							}
						});
						add(new MSelect(new R(Coord.right(5), Coord.top(top += 25), Coord.left(5), Coord.height(15)), 15) {
							{
								setSelector(new ListSelector() {
									{
										setList(Lists.newArrayList("uhgnva", "azulghn", "zaghnvu", "iuhvgn"));
									}
								});
							}
						});
						add(new MSelectLabel(new R(Coord.right(5), Coord.top(top += 25), Coord.left(5), Coord.height(15)), 15) {
							{
								setSelector(new ListSelector() {
									{
										setList(Lists.newArrayList("uhgnva", "azulghn", "zaghnvu", "iuhvgn"));
									}
								});
							}
						});

						float bottom = 25*4+5;

						add(new FunnyButton(new R(Coord.right(5), Coord.bottom(bottom -= 25), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.continue")) {
							@Override
							protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								CurrentMode.instance.setState(CurrentMode.State.CONTINUE, !CurrentMode.instance.isState(CurrentMode.State.CONTINUE));
								return true;
							}

							@Override
							public boolean isHighlight() {
								return CurrentMode.instance.isState(CurrentMode.State.CONTINUE);
							}
						});
						add(new FunnyButton(new R(Coord.right(5), Coord.bottom(bottom -= 25), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.load")) {
							@Override
							protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								CurrentMode.instance.setMode(CurrentMode.Mode.LOAD);
								requestClose();
								return true;
							}

							@Override
							public boolean isHighlight() {
								return CurrentMode.instance.isMode(CurrentMode.Mode.LOAD);
							}

							@Override
							public boolean isEnabled() {
								return !CurrentMode.instance.isMode(CurrentMode.Mode.LOAD);
							}
						});
						add(new FunnyButton(new R(Coord.right(5), Coord.bottom(bottom -= 25), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.place")) {
							@Override
							protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								final Entry entry = CurrentMode.instance.getEntryId().entry();
								if (entry.isValid()&&entry.id.isPlaceable()) {
									CurrentMode.instance.setMode(CurrentMode.Mode.PLACE);
									CurrentMode.instance.setState(CurrentMode.State.PREVIEW, true);
									requestClose();
									return true;
								}
								return false;
							}

							@Override
							public boolean isHighlight() {
								return CurrentMode.instance.isMode(CurrentMode.Mode.PLACE);
							}

							@Override
							public boolean isEnabled() {
								final Entry entry = CurrentMode.instance.getEntryId().entry();
								return entry.isValid()&&entry.id.isPlaceable()&&!CurrentMode.instance.isMode(CurrentMode.Mode.PLACE);
							}
						});
						add(new MButton(new R(Coord.right(5), Coord.bottom(bottom -= 25), Coord.left(5), Coord.height(15)), I18n.format("signpic.gui.editor.cancel")) {
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
					public boolean onCloseRequest() {
						p.stop().add(Easings.easeInBack.move(.25f, -65)).start();
						return false;
					}

					@Override
					public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
						return p.isFinished();
					}
				});

				final MCoord d = MCoord.bottom(-15).add(Easings.easeOutBack.move(.5f, 5)).start();
				GuiSignPicEditor.this.field = new MChatTextField(new R(Coord.left(5), d, Coord.right(70), Coord.height(15))) {
					@Override
					public void onAdded() {
						super.onAdded();
						setMaxStringLength(Integer.MAX_VALUE);
						setWatermark(I18n.format("signpic.gui.editor.textfield"));
						final String id = GuiSignPicEditor.this.signbuilder.getURI();
						if (id!=null)
							setText(id);
					}

					@Override
					public void onFocusChanged() {
						final EntryId entryId = new EntryId(getText());
						if (entryId.hasMeta())
							GuiSignPicEditor.this.signbuilder.setMeta(entryId.getMeta());
						if (entryId.hasContentId())
							GuiSignPicEditor.this.signbuilder.setURI(entryId.getContentId().getURI());
						else
							GuiSignPicEditor.this.signbuilder.setURI("");
						CurrentMode.instance.setEntryId(GuiSignPicEditor.this.signbuilder.build());
					}

					@Override
					public boolean onCloseRequest() {
						super.onCloseRequest();
						d.stop().add(Easings.easeInBack.move(.25f, -15)).start();
						return false;
					}

					@Override
					public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
						return d.isFinished();
					}
				};
				add(GuiSignPicEditor.this.field);

				// add(new GuiFileDD(new R(Coord.left(100), Coord.right(100), Coord.top(100), Coord.bottom(100)), field));

				OverlayFrame.instance.delegate();
				add(OverlayFrame.instance.pane);
			}
		});
	}

	public MChatTextField getTextField() {
		return this.field;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
		OverlayFrame.instance.release();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return super.sDoesGuiPauseGame();
	}
}
