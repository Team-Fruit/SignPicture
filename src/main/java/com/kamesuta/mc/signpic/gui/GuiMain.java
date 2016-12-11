package com.kamesuta.mc.signpic.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.util.concurrent.TimeUnit;

import org.lwjgl.input.Keyboard;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.FunnyButton;
import com.kamesuta.mc.bnnwidget.component.MButton;
import com.kamesuta.mc.bnnwidget.component.MChatTextField;
import com.kamesuta.mc.bnnwidget.component.MPanel;
import com.kamesuta.mc.bnnwidget.motion.CompoundMotion;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.Motion;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Apis;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.EntryIdBuilder;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.gui.file.McUiUpload;
import com.kamesuta.mc.signpic.http.shortening.ShortenerApiUtil;
import com.kamesuta.mc.signpic.information.Informations;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.render.OpenGL;
import com.kamesuta.mc.signpic.render.RenderHelper;
import com.kamesuta.mc.signpic.util.FileUtilitiy;
import com.kamesuta.mc.signpic.util.Sign;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiMain extends WFrame {
	private final EntryIdBuilder signbuilder = new EntryIdBuilder().load(CurrentMode.instance.getEntryId());

	public void setURL(final String url) {
		this.signbuilder.setURI(url);
		final MainTextField field = getTextField();
		field.setText(url);
		field.apply();
	}

	public void export() {
		CurrentMode.instance.setEntryId(GuiMain.this.signbuilder.build());
	}

	private MainTextField field;
	private GuiSettings settings;

	public GuiMain(final GuiScreen parent) {
		super(parent);
	}

	public GuiMain() {
	}

	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		OverlayFrame.instance.delegate();
		setGuiPauseGame(false);
	}

	@Override
	protected void initWidget() {
		if (CurrentMode.instance.isMode(CurrentMode.Mode.PLACE))
			CurrentMode.instance.setState(CurrentMode.State.PREVIEW, false);
		CurrentMode.instance.setMode();
		add(new WPanel(new R()) {
			@Override
			protected void initWidget() {
				add(new WBase(new R()) {
					VMotion m = V.pm(0);

					@Override
					public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity) {
						RenderHelper.startShape();
						OpenGL.glColor4f(0f, 0f, 0f, this.m.get());
						draw(getGuiPosition(pgp));
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

				add(new GuiSize(new R(Coord.top(5), Coord.left(5), Coord.width(15*8), Coord.height(15*2)), GuiMain.this.signbuilder.getMeta().size) {
					@Override
					protected void onUpdate() {
						super.onUpdate();
						export();
						;
					}
				});

				add(new GuiOffset(new R(Coord.top(15*3+10), Coord.left(5), Coord.width(15*8), Coord.height(15*3)), GuiMain.this.signbuilder.getMeta().offset) {
					@Override
					protected void onUpdate() {
						super.onUpdate();
						export();
						;
					}
				});

				add(new GuiRotation(new R(Coord.top(15*8), Coord.left(5), Coord.width(15*8), Coord.height(15*4)), GuiMain.this.signbuilder.getMeta().rotation) {
					@Override
					protected void onUpdate() {
						super.onUpdate();
						export();
						;
					}
				});

				final VMotion m = V.pm(-1f);
				add(new WPanel(new R(Coord.top(m), Coord.left(15*8+5), Coord.right(0), Coord.pheight(1f))) {
					@Override
					protected void initWidget() {
						add(new MPanel(new R(Coord.top(5), Coord.left(5), Coord.right(80), Coord.bottom(25))) {
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

							@Override
							public boolean mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								final Area a = getGuiPosition(pgp);
								if (a.pointInside(p))
									if (Informations.instance.isUpdateRequired()) {
										GuiMain.this.settings.show();
										return true;
									}
								return false;
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

				final VMotion p = V.am(-65).add(Easings.easeOutBack.move(.25f, 0)).start();
				add(new WPanel(new R(Coord.top(0), Coord.right(p), Coord.width(80), Coord.bottom(0))) {
					@Override
					protected void initWidget() {
						float top = -15f;

						add(new FunnyButton(new R(Coord.right(5), Coord.top(top += 20), Coord.left(5), Coord.height(15))) {
							@Override
							protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								CurrentMode.instance.setState(CurrentMode.State.SEE, !CurrentMode.instance.isState(CurrentMode.State.SEE));
								return true;
							}

							@Override
							public boolean isHighlight() {
								return CurrentMode.instance.isState(CurrentMode.State.SEE);
							}
						}.setText(I18n.format("signpic.gui.editor.see")));
						add(new FunnyButton(new R(Coord.right(5), Coord.top(top += 20), Coord.left(5), Coord.height(15))) {
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
						}.setText(I18n.format("signpic.gui.editor.preview")));
						add(new FunnyButton(new R(Coord.right(5), Coord.top(top += 20), Coord.left(5), Coord.height(15))) {
							@Override
							protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								McUiUpload.instance.setVisible(!McUiUpload.instance.isVisible());
								return true;
							}

							@Override
							public boolean isHighlight() {
								return McUiUpload.instance.isVisible();
							}
						}.setText(I18n.format("signpic.gui.editor.file")));
						add(new MButton(new R(Coord.right(5), Coord.top(top += 20), Coord.left(5), Coord.height(15))) {
							@Override
							protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								try {
									final Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
									FileUtilitiy.transfer(transferable);
								} catch (final Exception e) {
									Client.notice(I18n.format("signpic.gui.notice.paste.unsupported", e), 2);
								}
								return true;
							}
						}.setText(I18n.format("signpic.gui.editor.paste")));

						float bottom = 20*3+5;

						add(new FunnyButton(new R(Coord.right(5), Coord.bottom(bottom -= 20), Coord.left(5), Coord.height(15))) {
							@Override
							protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								CurrentMode.instance.setState(CurrentMode.State.CONTINUE, !CurrentMode.instance.isState(CurrentMode.State.CONTINUE));
								return true;
							}

							@Override
							public boolean isHighlight() {
								return CurrentMode.instance.isState(CurrentMode.State.CONTINUE);
							}
						}.setText(I18n.format("signpic.gui.editor.continue")));
						add(new FunnyButton(new R(Coord.right(5), Coord.bottom(bottom -= 20), Coord.left(5), Coord.height(15))) {
							@Override
							protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								CurrentMode.instance.setMode(CurrentMode.Mode.OPTION);
								requestClose();
								return true;
							}

							@Override
							public boolean isHighlight() {
								return CurrentMode.instance.isMode(CurrentMode.Mode.OPTION);
							}
						}.setText(I18n.format("signpic.gui.editor.option")));
						add(new FunnyButton(new R(Coord.right(5), Coord.bottom(bottom -= 20), Coord.left(5), Coord.height(15))) {
							@Override
							protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
								final Entry entry = CurrentMode.instance.getEntryId().entry();
								if (entry.isValid()) {
									if (!entry.id.isPlaceable())
										ShortenerApiUtil.requestShoretning(entry.content().id);
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
								return entry.isValid()&&!CurrentMode.instance.isMode(CurrentMode.Mode.PLACE);
							}
						}.setText(I18n.format("signpic.gui.editor.place")));
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

				final VMotion d = V.am(-15).add(Easings.easeOutBack.move(.5f, 5)).start();
				GuiMain.this.field = new MainTextField(new R(Coord.left(5), Coord.bottom(d), Coord.right(80), Coord.height(15))) {
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
				add(GuiMain.this.field);

				GuiMain.this.settings = new GuiSettings(new R());

				add(GuiMain.this.settings);

				add(OverlayFrame.instance.pane);
			}
		});
		if (Informations.instance.shouldCheck(Config.instance.informationJoinBeta.get() ? TimeUnit.HOURS.toMillis(6) : TimeUnit.DAYS.toMillis(1l)))
			Informations.instance.onlineCheck(null);
		if (!Config.instance.guiExperienced.get())
			Config.instance.guiExperienced.set(true);
	}

	public MainTextField getTextField() {
		return this.field;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
		OverlayFrame.instance.release();
	}

	public static boolean setContentId(final String id) {
		if (Client.mc.currentScreen instanceof GuiMain) {
			final GuiMain editor = (GuiMain) Client.mc.currentScreen;
			editor.setURL(id);
			return true;
		} else {
			final EntryId entryId = CurrentMode.instance.getEntryId();
			final EntryIdBuilder signbuilder = new EntryIdBuilder().load(entryId);
			signbuilder.setURI(id);
			CurrentMode.instance.setEntryId(signbuilder.build());
			return false;
		}
	}

	public class MainTextField extends MChatTextField {
		public MainTextField(final R position) {
			super(position);
		}

		@Override
		public void onAdded() {
			super.onAdded();
			setMaxStringLength(Integer.MAX_VALUE);
			setWatermark(I18n.format("signpic.gui.editor.textfield"));

			final EntryId id = CurrentMode.instance.getEntryId();
			if (id.hasContentId())
				setText(id.getContentId().getID());
		}

		@Override
		public void onFocusChanged() {
			apply();
		}

		public void apply() {
			final EntryId entryId = EntryId.from(getText());
			if (entryId.hasMeta())
				GuiMain.this.signbuilder.setMeta(entryId.getMetaBuilder());
			if (entryId.hasContentId()) {
				String url = entryId.getContentId().getURI();
				setText(url = Apis.instance.replaceURL(url));
				GuiMain.this.signbuilder.setURI(url);
			} else
				GuiMain.this.signbuilder.setURI("");
			export();
		}

		@Override
		public boolean mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
			final int cursor1 = getCursorPosition();
			final boolean focused1 = isFocused();
			final boolean b = super.mouseClicked(ev, pgp, p, button);
			final int cursor2 = getCursorPosition();
			final boolean focused2 = isFocused();
			final Area a = getGuiPosition(pgp);
			if (a.pointInside(p))
				if (focused1&&focused2&&cursor1==cursor2)
					setText(GuiScreen.getClipboardString());
			return b;
		}
	}

	private CompoundMotion closeCooldown = new CompoundMotion().start();

	@Override
	public void requestClose() {
		if (this.closeCooldown.isFinished()) {
			this.closeCooldown.add(Motion.blank(3f));
			super.requestClose();
		}
	}
}
