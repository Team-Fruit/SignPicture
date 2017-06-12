package com.kamesuta.mc.signpic.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.RenderOption;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Apis;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.asm.ASMDeobfNames;
import com.kamesuta.mc.signpic.attr.AttrWriters;
import com.kamesuta.mc.signpic.attr.AttrWriters.AttrWriter;
import com.kamesuta.mc.signpic.attr.prop.OffsetData.OffsetBuilder;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationBuilder;
import com.kamesuta.mc.signpic.attr.prop.SizeData.SizeBuilder;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.EntryIdBuilder;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.gui.GuiMain.SignEditor.MainTextField;
import com.kamesuta.mc.signpic.gui.GuiRotation.RefRotation;
import com.kamesuta.mc.signpic.gui.file.McUiUpload;
import com.kamesuta.mc.signpic.handler.SignHandler;
import com.kamesuta.mc.signpic.http.shortening.ShortenerApiUtil;
import com.kamesuta.mc.signpic.information.Informations;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.reflect.lib.ReflectClass;
import com.kamesuta.mc.signpic.reflect.lib.ReflectMethod;
import com.kamesuta.mc.signpic.util.FileUtilitiy;
import com.kamesuta.mc.signpic.util.Sign;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntitySign;

public class GuiMain extends WFrame {
	private @Nonnull EntryId signid = CurrentMode.instance.getEntryId();
	private @Nullable SignEditor editor;

	public void setURL(final @Nonnull String url) {
		final SignEditor editor = this.editor;
		if (editor!=null) {
			editor.signbuilder.setURI(url);
			editor.field.setText(url);
			editor.field.apply();
		}
	}

	public void setId(final @Nonnull EntryId id) {
		this.signid = id;
		exportId();
	}

	public @Nonnull EntryId getId() {
		return this.signid;
	}

	public void exportId() {
		CurrentMode.instance.setEntryId(this.signid);
	}

	private @Nonnull GuiSettings settings;

	{
		this.settings = new GuiSettings(new R());
	}

	private @Nonnull final ReflectMethod<GuiScreenBook, Void> bookwriter = ReflectClass.fromClass(GuiScreenBook.class).getMethodFromName(ASMDeobfNames.GuiScreenBookPageInsertIntoCurrent, null, void.class, String.class);

	public GuiMain(final @Nullable GuiScreen parent) {
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
					public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
						WRenderer.startShape();
						OpenGL.glColor4f(0f, 0f, 0f, this.m.get());
						draw(getGuiPosition(pgp));
					}

					protected boolean b = !CurrentMode.instance.isState(CurrentMode.State.PREVIEW);

					@Override
					public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
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
					public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point mouse) {
						return this.m.isFinished();
					}
				});

				//				final MTab tab = new MTab(new R(Coord.left(0f), Coord.right(75f)), CoordSide.Top, 15, 15);
				//				tab.addTab("Sign", new WidgetBuilder<WCommon>() {
				//					@Override
				//					public WCommon build() {
				//						return GuiMain.this.editor = new SignEditor(new R());
				//					}
				//				});
				//				tab.addTab("E", new WPanel(new R()));
				//				add(tab);
				add(GuiMain.this.editor = new SignEditor(new R(Coord.left(0f), Coord.right(75f))));

				final VMotion p = V.am(-65).add(Easings.easeOutBack.move(.25f, 0)).start();
				add(new WPanel(new R(Coord.top(0), Coord.right(p), Coord.width(80), Coord.bottom(0))) {
					@Override
					protected void initWidget() {
						float top = -15f;

						add(new FunnyButton(new R(Coord.right(5), Coord.top(top += 20), Coord.left(5), Coord.height(15))) {
							@Override
							protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
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
							protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
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
							protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
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
							protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
								final SignEditor editor = GuiMain.this.editor;
								if (editor!=null)
									Client.openURL(editor.signbuilder.getURI());
								return true;
							}
						}.setText(I18n.format("signpic.gui.editor.openurl")));
						add(new MButton(new R(Coord.right(5), Coord.top(top += 20), Coord.left(5), Coord.height(15))) {
							@Override
							protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
								try {
									final Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
									FileUtilitiy.transfer(transferable, null);
								} catch (final Exception e) {
									Log.notice(I18n.format("signpic.gui.notice.paste.unsupported", e));
								}
								return true;
							}
						}.setText(I18n.format("signpic.gui.editor.paste")));

						float bottom = 20*3+5;

						add(new FunnyButton(new R(Coord.right(5), Coord.bottom(bottom -= 20), Coord.left(5), Coord.height(15))) {
							@Override
							protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
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
							protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
								CurrentMode.instance.setMode(CurrentMode.Mode.OPTION);
								CurrentMode.instance.setState(CurrentMode.State.SEE, true);
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
							protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
								final Entry entry = CurrentMode.instance.getEntryId().entry();
								if (entry.isValid()) {
									final GuiScreen screen = getParent();
									if (screen instanceof GuiChat) {
										final Content content = entry.getContent();
										if (content!=null) {
											final GuiChat chat = new GuiChat(content.id.getURI());
											chat.setWorldAndResolution(mc, (int) width(), (int) height());
											setParent(chat);
										}
									}
									if (screen instanceof GuiEditSign) {
										final GuiEditSign guiEditSign = (GuiEditSign) screen;
										final TileEntitySign entitySign = SignHandler.guiEditSignTileEntity.get(guiEditSign);
										entry.id.toEntity(entitySign);
									}
									if (screen instanceof GuiScreenBook) {
										final GuiScreenBook book = (GuiScreenBook) screen;
										GuiMain.this.bookwriter.invoke(book, entry.id.id());
									}
								}
								if (!entry.id.isPlaceable()) {
									final Content content = entry.getContent();
									if (content!=null)
										ShortenerApiUtil.requestShoretning(content.id);
								}
								CurrentMode.instance.setMode(CurrentMode.Mode.PLACE);
								CurrentMode.instance.setState(CurrentMode.State.PREVIEW, true);
								requestClose();
								return true;
							}

							@Override
							public boolean isHighlight() {
								return CurrentMode.instance.isMode(CurrentMode.Mode.PLACE);
							}

							@Override
							public boolean isEnabled() {
								return !CurrentMode.instance.isMode(CurrentMode.Mode.PLACE);
							}
						}.setText(I18n.format("signpic.gui.editor.place")));
					}

					@Override
					public boolean onCloseRequest() {
						p.stop().add(Easings.easeInBack.move(.25f, -65)).start();
						return false;
					}

					@Override
					public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point mouse) {
						return p.isFinished();
					}
				});

				add(GuiMain.this.settings);

				add(OverlayFrame.instance.pane);
			}
		});
		if (Informations.instance.shouldCheck(Config.getConfig().informationJoinBeta.get() ? TimeUnit.HOURS.toMillis(6) : TimeUnit.DAYS.toMillis(1l)))
			Informations.instance.onlineCheck(null);
		if (!Config.getConfig().guiExperienced.get())
			Config.getConfig().guiExperienced.set(true);
	}

	public @Nullable MainTextField getTextField() {
		if (this.editor!=null)
			return this.editor.field;
		return null;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
		OverlayFrame.instance.release();
	}

	public static boolean setContentId(final @Nonnull String id) {
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

	public static boolean setContentId(final @Nonnull EntryId id) {
		if (Client.mc.currentScreen instanceof GuiMain) {
			final GuiMain editor = (GuiMain) Client.mc.currentScreen;
			editor.setId(id);
			return true;
		} else {
			CurrentMode.instance.setEntryId(id);
			return false;
		}
	}

	public class SignEditor extends WPanel {
		private final @Nonnull EntryIdBuilder signbuilder = new EntryIdBuilder().load(getId());

		private @Nonnull MainTextField field;

		private final VMotion mfield = V.am(-15).start();

		{
			this.field = new MainTextField(new R(Coord.left(5), Coord.bottom(this.mfield), Coord.right(5), Coord.height(15))) {
				@Override
				public boolean onCloseRequest() {
					super.onCloseRequest();
					SignEditor.this.mfield.stop().add(Easings.easeInBack.move(.25f, -15)).start();
					return false;
				}

				@Override
				public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point mouse) {
					return SignEditor.this.mfield.isFinished();
				}
			};
		}

		public SignEditor(final R position) {
			super(position);
		}

		public void export() {
			setId(this.signbuilder.build());
			exportId();
		}

		@Override
		public void onAdded() {
			super.onAdded();
			this.mfield.stop().add(Easings.easeOutBack.move(.5f, 5)).start();
		}

		@Override
		protected void initWidget() {
			add(new GuiSize(new R(Coord.top(5), Coord.left(5), Coord.width(15*8), Coord.height(15*2))) {
				@Override
				protected void onUpdate() {
					export();
				}

				@Override
				protected @Nonnull SizeBuilder size() {
					final AttrWriter attr = SignEditor.this.signbuilder.getMeta().getFrame(0);
					return attr.add(attr.size).size;
				}
			});

			add(new GuiOffset(new R(Coord.top(15*3+10), Coord.left(5), Coord.width(15*8), Coord.height(15*3))) {
				@Override
				protected void onUpdate() {
					export();
				}

				@Override
				protected @Nonnull OffsetBuilder offset() {
					final AttrWriter attr = SignEditor.this.signbuilder.getMeta().getFrame(0);
					return attr.add(attr.offset).offset;
				}
			});

			add(new GuiRotation(new R(Coord.top(15*8), Coord.left(5), Coord.width(15*8), Coord.height(15*4)), new RefRotation() {
				@Override
				public @Nonnull RotationBuilder rotation() {
					final AttrWriter attr = SignEditor.this.signbuilder.getMeta().getFrame(0);
					return attr.add(attr.rotation).rotation;
				}

				@Override
				public boolean isFirst() {
					return true;
				}
			}) {
				@Override
				protected void onUpdate() {
					export();
				}
			});

			add(this.field);

			final VMotion m = V.pm(-1f);
			add(new WPanel(new R(Coord.top(m), Coord.left(15*8+5), Coord.right(0), Coord.pheight(1f))) {
				@Override
				protected void initWidget() {
					add(new MPanel(new R(Coord.top(5), Coord.left(5), Coord.right(5), Coord.bottom(25))) {
						{
							add(new SignPicLabel(new R(Coord.top(5), Coord.left(5), Coord.right(5), Coord.bottom(5)), ContentManager.instance) {
								@Override
								public @Nonnull EntryId getEntryId() {
									return CurrentMode.instance.getEntryId();
								}
							});
						}

						protected boolean b = !CurrentMode.instance.isState(CurrentMode.State.PREVIEW);

						@Override
						public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
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
						public boolean mouseClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
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
				public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point mouse) {
					return m.isFinished();
				}
			});

			//			add(new GuiVariable(new R(Coord.top(5), Coord.left(15*8+5), Coord.right(5), Coord.bottom(5)), this.signbuilder.getMeta()) {
			//				@Override
			//				protected void onUpdate() {
			//					export();
			//				}
			//			});
		}

		public class MainTextField extends MChatTextField {
			public MainTextField(final @Nonnull R position) {
				super(position);
			}

			@Override
			public void onAdded() {
				super.onAdded();
				setMaxStringLength(Integer.MAX_VALUE);
				setWatermark(I18n.format("signpic.gui.editor.textfield"));

				final EntryId id = CurrentMode.instance.getEntryId();
				Content content = null;
				if ((content = id.entry().getContent())!=null)
					setText(content.id.getID());
			}

			@Override
			public void onFocusChanged() {
				apply();
			}

			public void apply() {
				final EntryId entryId = EntryId.from(getText());
				final AttrWriters atb = entryId.getMetaBuilder();
				if (atb!=null)
					SignEditor.this.signbuilder.setMeta(atb);
				final ContentId cid = entryId.getContentId();
				if (cid!=null) {
					String url = cid.getURI();
					setText(url = Apis.instance.replaceURL(url));
					SignEditor.this.signbuilder.setURI(url);
				} else
					SignEditor.this.signbuilder.setURI("");
				export();
				if (atb!=null)
					GuiMain.this.event.bus.post(new PropertyChangeEvent());
			}

			@Override
			public boolean mouseClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
				final boolean b = super.mouseClicked(ev, pgp, p, button);
				final Area a = getGuiPosition(pgp);
				if (a.pointInside(p))
					if (ev.isDoubleClick()) {
						final String clip = GuiScreen.getClipboardString();
						if (clip!=null)
							setText(clip);
					}
				return b;
			}
		}
	}

	private @Nonnull final CompoundMotion closeCooldown = new CompoundMotion().start();

	@Override
	public void requestClose() {
		if (this.closeCooldown.isFinished()) {
			this.closeCooldown.add(Motion.blank(3f));
			super.requestClose();
		}
	}
}
