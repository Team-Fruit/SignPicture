package com.kamesuta.mc.signpic.gui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WBox;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.FontLabel;
import com.kamesuta.mc.bnnwidget.component.FunnyButton;
import com.kamesuta.mc.bnnwidget.component.MButton;
import com.kamesuta.mc.bnnwidget.component.MCheckBox;
import com.kamesuta.mc.bnnwidget.component.MScaledLabel;
import com.kamesuta.mc.bnnwidget.component.MSelectField;
import com.kamesuta.mc.bnnwidget.component.MSelectLabel;
import com.kamesuta.mc.bnnwidget.component.MTab;
import com.kamesuta.mc.bnnwidget.font.WFont;
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
import com.kamesuta.mc.bnnwidget.var.VCommon;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Apis;
import com.kamesuta.mc.signpic.Apis.ApiFactory;
import com.kamesuta.mc.signpic.Apis.ImageUploaderFactory;
import com.kamesuta.mc.signpic.Apis.MapSetting;
import com.kamesuta.mc.signpic.Apis.Setting;
import com.kamesuta.mc.signpic.Apis.URLShortenerFactory;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.Config.ConfigProperty;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.gui.config.ConfigGui;
import com.kamesuta.mc.signpic.information.Informations;
import com.kamesuta.mc.signpic.mode.CurrentMode;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiSettings extends WPanel {
	public static final @Nonnull ResourceLocation settings = new ResourceLocation("signpic", "textures/gui/settings.png");
	public static final @Nonnull ResourceLocation update = new ResourceLocation("signpic", "textures/gui/update.png");

	protected boolean show = true;
	protected @Nonnull VMotion bottom = V.pm(0f);
	protected boolean closing;

	public void show() {
		this.bottom.stop().add(Motion.blank(.25f).setAfter(new Runnable() {
			@Override
			public void run() {
				mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("signpic", "gui.show"), 1.0F));
			}
		})).add(Easings.easeOutQuad.move(.7f, 1f)).start();
	}

	public void hide() {
		this.bottom.stop().add(Easings.easeOutBounce.move(.7f, 0f)).start();
	}

	public GuiSettings(final @Nonnull R area) {
		super(area);
	}

	@Override
	protected void initWidget() {
		final boolean isUpdateRequired = Informations.instance.isUpdateRequired()&&Config.getConfig().informationUpdateGui.get();
		final int updatepanelHeight = isUpdateRequired ? 40 : 0;
		final float hitarea = 5f;
		add(new WPanel(new R(Coord.bottom(0), Coord.height(122+updatepanelHeight))) {

			@Override
			protected void initWidget() {
				add(new WPanel(new R(Coord.bottom(V.per(V.combine(V.p(-1), V.a(hitarea)), V.p(0f), GuiSettings.this.bottom)))) {
					@Override
					public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
						final Area a = getGuiPosition(pgp);
						final boolean b = a.pointInside(p);
						if (!GuiSettings.this.closing)
							if (b) {
								if (!GuiSettings.this.show)
									show();
								GuiSettings.this.show = true;
							} else {
								if (GuiSettings.this.show)
									hide();
								GuiSettings.this.show = false;
							}
						super.update(ev, pgp, p);
					}

					@Override
					public boolean onCloseRequest() {
						GuiSettings.this.closing = true;
						GuiSettings.this.bottom.stop().add(Easings.easeInBack.move(.25f, 0f)).start();
						return false;
					}

					@Override
					public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
						return GuiSettings.this.bottom.isFinished();
					}

					@Override
					public boolean mouseClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
						final Area a = getGuiPosition(pgp);
						return super.mouseClicked(ev, pgp, p, button)||a.pointInside(p);
					}

					@Override
					protected void initWidget() {
						add(new WPanel(new R(Coord.top(hitarea), Coord.bottom(0))) {
							protected R line = new R(Coord.top(0), Coord.top(2));

							@Override
							public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float opacity, final @Nonnull RenderOption opt) {
								final Area a = getGuiPosition(pgp);
								WRenderer.startShape();
								OpenGL.glColor4f(0f, 0f, 0f, .6f);
								draw(a);
								OpenGL.glColor4f(0f/256f, 78f/256f, 155f/256f, 1f);
								draw(this.line.getAbsolute(a));
								super.draw(ev, pgp, p, frame, opacity, opt);
							}

							@Override
							protected void initWidget() {
								add(new WBase(new R(Coord.top(5), Coord.left(3), Coord.width(90), Coord.height(26))) {
									@Override
									public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float opacity, final @Nonnull RenderOption opt) {
										final Area a = getGuiPosition(pgp);
										texture().bindTexture(settings);
										OpenGL.glColor4f(1, 1, 1, 1);
										WRenderer.startTexture();
										drawTexture(a, null, null);
									}
								});
								final MTab tab = new MTab(new R(Coord.left(5), Coord.width(250), Coord.top(33), Coord.height(82)), Coord.CoordSide.Left, 15f, 15f);
								tab.addTab(I18n.format("signpic.gui.settings.api.upimage"), new GuiApis<ImageUploaderFactory>(new R(), Apis.instance.imageUploaders, I18n.format("signpic.gui.settings.api.upimage.desc")));
								tab.addTab(I18n.format("signpic.gui.settings.api.urlshortener"), new GuiApis<URLShortenerFactory>(new R(), Apis.instance.urlShorteners, I18n.format("signpic.gui.settings.api.urlshortener.desc")));
								add(tab);
								add(new WPanel(new R(Coord.bottom(5+updatepanelHeight+80), Coord.right(5), Coord.width(100), Coord.height(15))) {
									@Override
									protected void initWidget() {
										add(new MCheckBox(new R(Coord.left(0), Coord.width(15))) {
											{
												check(getConfig().get());
											}

											private @Nonnull ConfigProperty<Boolean> getConfig() {
												return Config.getConfig().multiplayPAAS;
											}

											@Override
											protected void onCheckChanged(final boolean oldCheck) {
												getConfig().set(isCheck());
											}
										});
										add(new FontLabel(new R(Coord.left(15), Coord.right(0)), WFont.fontRenderer).setText(I18n.format("signpic.gui.settings.paas")));
									}
								});
								add(new FunnyButton(new R(Coord.bottom(5+updatepanelHeight+60), Coord.right(5), Coord.width(100), Coord.height(15))) {
									@Override
									protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
										CurrentMode.instance.toggleState(CurrentMode.State.HIDE);
										return true;
									}

									@Override
									public boolean isHighlight() {
										return CurrentMode.instance.isState(CurrentMode.State.HIDE);
									}
								}.setText(I18n.format("signpic.gui.settings.hide")));
								add(new MButton(new R(Coord.bottom(5+updatepanelHeight+40), Coord.right(5), Coord.width(100), Coord.height(15))) {
									@Override
									protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
										ContentManager.instance.reloadAll();
										return true;
									}
								}.setText(I18n.format("signpic.gui.settings.sign.reloadall")));
								add(new MButton(new R(Coord.bottom(5+updatepanelHeight+20), Coord.right(5), Coord.width(100), Coord.height(15))) {
									@Override
									protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
										ContentManager.instance.redownloadAll();
										OverlayFrame.instance.pane.task.show(2f);
										return true;
									}
								}.setText(I18n.format("signpic.gui.settings.sign.redownloadall")));
								add(new MButton(new R(Coord.bottom(5+updatepanelHeight), Coord.right(5), Coord.width(100), Coord.height(15))) {
									@Override
									protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
										Client.mc.displayGuiScreen(new ConfigGui(ev.owner));
										return true;
									}
								}.setText(I18n.format("signpic.gui.settings.config")));

								if (isUpdateRequired) {

									final VMotion state = V.pm(0f);
									final VCommon vstart = V.a(32);
									final VCommon vend = V.a(37);
									final VCommon hstart = V.a(30);
									final VCommon hend = V.a(10);

									add(new WPanel(new R(Coord.bottom(0), Coord.height(updatepanelHeight))) {
										@Override
										public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
											final Area a = getGuiPosition(pgp);
											OpenGL.glColor4f(0f, 0f, 0f, .4f);
											WRenderer.startShape();
											draw(a);
											super.draw(ev, pgp, p, frame, popacity, opt);
										}

										@Override
										protected void initWidget() {
											add(new WPanel(new R(Coord.left(V.per(hstart, hend, state)), Coord.right(V.per(hstart, hend, state)), Coord.height(V.per(vstart, vend, state)), Coord.ptop(.5f)).child(Coord.ptop(-.5f), Coord.pheight(1f))) {
												protected boolean in;

												protected VMotion rot = V.pm(0).add(Easings.easeLinear.move(8.04f/4f, 1f)).setLoop(true).start();
												protected float orot = 0f;

												@Override
												public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
													final Area a = getGuiPosition(pgp);
													if (a.pointInside(p)) {
														if (!this.in) {
															state.stop().add(Easings.easeOutCubic.move(1f, 1f)).start();
															this.orot += this.rot.get();
															this.rot.stopFirst().reset().add(Easings.easeLinear.move(8.04f/4f, 1f));
														}
														this.in = true;
													} else {
														if (this.in) {
															state.stop().add(Easings.easeOutCubic.move(1f, 0f)).start();
															this.orot += this.rot.get();
															this.rot.stopFirst().reset().add(Easings.easeOutSine.move(1.5f, .5f)).add(Motion.of(Motion.move(0), Easings.easeInOutSine.move(2.87f, 1f), Motion.blank(0.58f)).setLoop(true));
														}
														this.in = false;
													}
													super.update(ev, pgp, p);
												}

												@Override
												public boolean mouseClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
													final Area a = getGuiPosition(pgp);
													if (a.pointInside(p)) {
														Informations.instance.runUpdate();
														return true;
													}
													return false;
												}

												@Override
												protected void initWidget() {
													add(new WBase(new R(Coord.width(V.per(vstart, vend, state)), Coord.pleft(.5f)).child(Coord.pleft(-.5f))) {
														@Override
														public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float opacity, final @Nonnull RenderOption opt) {
															final Area a = getGuiPosition(pgp);
															texture().bindTexture(update);
															// glColor4f(1, 1, 1, 1);
															final float f = state.get();
															OpenGL.glColor4f(256f*(1-f)/256f+144*f/256f, 256f*(1-f)/256f+191*f/256f, 256f*(1-f)/256f+48*f/256f, 1f);
															WRenderer.startTexture();
															OpenGL.glPushMatrix();
															OpenGL.glTranslatef(a.x1()+a.w()/2, a.y1()+a.h()/2, 0f);
															OpenGL.glRotatef((orot+rot.get())*360, 0, 0, 1);
															OpenGL.glTranslatef(-a.x1()-a.w()/2, -a.y1()-a.h()/2, 0f);
															drawTexture(a, null, null);
															OpenGL.glPopMatrix();
														}
													});
													final VMotion o = V.pm(0).add(Easings.easeLinear.move(.8f, 1f)).add(Motion.blank(2f)).add(Easings.easeLinear.move(.8f, 0f)).add(Motion.blank(2f)).setLoop(true).start();
													final String message = I18n.format("signpic.gui.update.message");
													final String update = Informations.instance.getUpdateMessage();
													add(new MScaledLabel(new R(Coord.pheight(.4f), Coord.ptop(.5f)).child(Coord.ptop(-.5f), Coord.pheight(1f))) {
														@Override
														protected @Nonnull VCommon initOpacity() {
															return o;
														}

														@Override
														public int getColor() {
															final float f = state.get();
															return new Color(256f*(1-f)/256f+144*f/256f, 256f*(1-f)/256f+191*f/256f, 256f*(1-f)/256f+48*f/256f).getRGB();
														}

														@Override
														public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
															OpenGL.glPushMatrix();
															OpenGL.glTranslatef(0, 0, 10f);
															super.draw(ev, pgp, p, frame, popacity, opt);
															OpenGL.glPopMatrix();
														}
													}.setShadow(true).setText(message));
													add(new MScaledLabel(new R(Coord.pheight(.4f), Coord.ptop(.5f)).child(Coord.ptop(-.5f), Coord.pheight(1f))) {
														@Override
														protected @Nonnull VCommon initOpacity() {
															return V.per(V.p(1f), V.p(0f), o);
														}

														@Override
														public int getColor() {
															final float f = state.get();
															return new Color(256f*(1-f)/256f+144*f/256f, 256f*(1-f)/256f+191*f/256f, 256f*(1-f)/256f+48*f/256f).getRGB();
														}

														@Override
														public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
															OpenGL.glPushMatrix();
															OpenGL.glTranslatef(0, 0, 10f);
															super.draw(ev, pgp, p, frame, popacity, opt);
															OpenGL.glPopMatrix();
														}
													}.setShadow(true).setText(!StringUtils.isEmpty(update) ? I18n.format("signpic.gui.update.message.changelog", update) : message));
												}
											});
										}
									});
								}
							}
						});
					}
				});
			}
		});
	}

	static class GuiApis<E extends ApiFactory> extends WPanel {
		protected @Nonnull MapSetting<E> typesetting;
		protected @Nonnull String title;

		public GuiApis(final @Nonnull R position, final @Nonnull MapSetting<E> typesetting, final @Nonnull String title) {
			super(position);
			this.typesetting = typesetting;
			this.title = title;
		}

		@Override
		public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float opacity, final @Nonnull RenderOption opt) {
			final Area a = getGuiPosition(pgp);
			WRenderer.startShape();
			OpenGL.glColor4f(0f, 0f, 0f, .2f);
			OpenGL.glLineWidth(.5f);
			draw(a, GL_LINE_LOOP);
			super.draw(ev, pgp, p, frame, opacity, opt);
		}

		protected WBox box = new WBox(new R(Coord.left(1), Coord.right(1), Coord.top(1+15+32+1), Coord.height(32))) {
			@Override
			protected void initWidget() {
				final ApiFactory factory = GuiApis.this.typesetting.solve(GuiApis.this.typesetting.getConfig());
				if (factory!=null)
					GuiApis.this.box.set(new Key(new R(), factory.keySettings()));
				else
					GuiApis.this.box.set(null);
			}
		};

		@Override
		protected void initWidget() {
			float top = 1;
			add(new FontLabel(new R(Coord.left(1), Coord.right(1), Coord.top(top), Coord.height(15)), WFont.fontRenderer).setText(this.title));
			add(new WPanel(new R(Coord.left(1), Coord.right(1), Coord.top(top += 15), Coord.height(32))) {
				@Override
				public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float opacity, final @Nonnull RenderOption opt) {
					final Area a = getGuiPosition(pgp);
					WRenderer.startShape();
					OpenGL.glColor4f(0f, 0f, 0f, .2f);
					OpenGL.glLineWidth(.5f);
					draw(a, GL_LINE_LOOP);
					super.draw(ev, pgp, p, frame, opacity, opt);
				}

				@Override
				protected void initWidget() {
					add(new FontLabel(new R(Coord.left(1), Coord.right(1), Coord.top(1), Coord.height(15)), WFont.fontRenderer).setText(I18n.format("signpic.gui.settings.api.type")));
					add(new MSelectLabel(new R(Coord.left(1), Coord.right(1), Coord.top(16), Coord.height(15)), 15) {
						@Override
						protected void initWidget() {
							setSelector(new StringSelector() {
								{
									final List<String> settings = Lists.newArrayList("");
									settings.addAll(GuiApis.this.typesetting.getSettings());
									setList(settings);
								}
							});
							this.field.setWatermark(I18n.format("signpic.gui.settings.default"));
							setText(GuiApis.this.typesetting.getConfig());
						}

						@Override
						protected void onChanged(final @Nonnull String oldText, final @Nonnull String newText) {
							if (!StringUtils.equals(oldText, newText)) {
								if (!StringUtils.equals(newText, GuiApis.this.typesetting.getConfig()))
									GuiApis.this.typesetting.setConfig(newText);
								final ApiFactory factory = GuiApis.this.typesetting.solve(newText);
								if (factory!=null)
									GuiApis.this.box.set(new Key(new R(), factory.keySettings()));
								else
									GuiApis.this.box.set(null);
							}
						}
					});
				}
			});
			add(this.box);
		}
	}

	static class Key extends WPanel {
		protected @Nonnull Setting setting;

		public Key(final @Nonnull R position, final @Nonnull Setting setting) {
			super(position);
			this.setting = setting;
		}

		@Override
		public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float opacity, final @Nonnull RenderOption opt) {
			final Area a = getGuiPosition(pgp);
			WRenderer.startShape();
			OpenGL.glColor4f(0f, 0f, 0f, .2f);
			OpenGL.glLineWidth(.5f);
			draw(a, GL_LINE_LOOP);
			super.draw(ev, pgp, p, frame, opacity, opt);
		}

		@Override
		protected void initWidget() {
			add(new FontLabel(new R(Coord.left(1), Coord.right(1), Coord.top(1), Coord.height(15)), WFont.fontRenderer).setText(I18n.format("signpic.gui.settings.api.key")));
			add(new MSelectField(new R(Coord.left(1), Coord.right(1), Coord.top(16), Coord.height(15)), 15) {
				@Override
				protected void initWidget() {
					setSelector(new StringSelector() {
						{
							final List<String> settings = Lists.newArrayList("");
							settings.addAll(Key.this.setting.getSettings());
							setList(settings);
						}
					});
					this.field.setWatermark(I18n.format("signpic.gui.settings.default"));
					this.field.setMaxStringLength(256);
					setText(Key.this.setting.getConfig());
				}

				@Override
				protected void onChanged(final @Nonnull String oldText, final @Nonnull String newText) {
					if (!StringUtils.equals(oldText, newText)&&!StringUtils.equals(newText, Key.this.setting.getConfig()))
						Key.this.setting.setConfig(newText);
				}
			});
		}
	}
}