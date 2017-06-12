package com.kamesuta.mc.signpic.gui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.FontLabel;
import com.kamesuta.mc.bnnwidget.component.MButton;
import com.kamesuta.mc.bnnwidget.component.MSelectButton;
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
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryIdBuilder;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.mode.CurrentMode;

import net.minecraft.client.resources.I18n;

public class GuiSignOption extends WFrame {
	protected final @Nonnull Entry entry;

	public GuiSignOption(final @Nonnull Entry entry) {
		this.entry = entry;
		setGuiPauseGame(false);
	}

	@Override
	protected void initWidget() {
		add(new WPanel(new R()) {
			protected @Nullable VMotion opa;
			protected @Nullable VMotion opac;

			@Override
			protected @Nonnull VCommon initOpacity() {
				return this.opa = V.pm(1f).start();
			}

			@Override
			public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
				super.draw(ev, pgp, p, frame, popacity, opt);
			}

			@Override
			public boolean mouseClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
				return this.opac!=null&&this.opac.isFinished()&&super.mouseClicked(ev, pgp, p, button);
			}

			@Override
			protected void initWidget() {
				add(new WBase(new R()) {

					@Override
					protected @Nonnull VCommon initOpacity() {
						return opac = V.pm(0f).add(Easings.easeLinear.move(.5f, .5f)).start();
					}

					@Override
					public boolean onCloseRequest() {
						if (opac!=null)
							opac.stop().add(Easings.easeLinear.move(.2f, 0f)).start();
						if (opa!=null)
							opa.stop().add(Easings.easeLinear.move(.2f, 0f)).start();
						return false;
					}

					@Override
					public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
						if (opac!=null)
							return opac.isFinished();
						return true;
					}

					@Override
					public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
						final Area a = getGuiPosition(pgp);
						WRenderer.startShape();
						OpenGL.glColor4f(0f, 0f, 0f, getGuiOpacity(popacity));
						draw(a);
						super.draw(ev, pgp, p, frame, popacity, opt);
					}
				});
				add(new WPanel(new R(Coord.pleft(.5f), Coord.ptop(.5f), Coord.width(200f), Coord.height(150f))) {
					@Override
					protected void initWidget() {
						add(new WPanel(new R(Coord.pleft(-.5f), Coord.ptop(-.5f))) {
							@Override
							protected void initWidget() {
								float top = -25f*1.5f;
								final float d = 1f;
								final float n = .1f;
								final float od = .7f;
								float i = 0f;
								final VCommon v0 = V.pm(-.3f).add(Motion.blank(i += n)).add(Easings.easeOutBounce.move(d-i, 0f)).start();
								final VCommon o0 = V.pm(0f).add(Motion.blank(i)).add(Easings.easeLinear.move(od-i, 1f)).start();
								add(new FontLabel(new R(Coord.left(v0), Coord.top(top += 25f), Coord.height(20f)), WFont.fontRenderer) {
									@Override
									protected @Nonnull VCommon initOpacity() {
										return o0;
									}
								}.setText(I18n.format("signpic.gui.settings.sign")));
								if (!GuiSignOption.this.entry.isValid()) {
									final VCommon v1 = V.pm(-.3f).add(Motion.blank(i += n)).add(Easings.easeOutBounce.move(d-i, 0f)).start();
									final VCommon o1 = V.pm(0f).add(Motion.blank(i)).add(Easings.easeLinear.move(od-i, 1f)).start();
									add(new MButton(new R(Coord.right(v1), Coord.top(top += 25f), Coord.height(20f))) {
										@Override
										protected @Nonnull VCommon initOpacity() {
											return o1;
										}

										@Override
										protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
											CurrentMode.instance.setEntryId(GuiSignOption.this.entry.id);
											requestClose();
											Client.openEditor();
											return true;
										}
									}.setText(I18n.format("signpic.gui.settings.sign.load.text")));
								} else {
									final VCommon v1 = V.pm(-.3f).add(Motion.blank(i += n)).add(Easings.easeOutBounce.move(d-i, 0f)).start();
									final VCommon o1 = V.pm(0f).add(Motion.blank(i)).add(Easings.easeLinear.move(od-i, 1f)).start();
									add(new MSelectButton(new R(Coord.left(v1), Coord.top(top += 25f), Coord.height(20f)), 15) {
										@Override
										protected @Nonnull VCommon initOpacity() {
											return o1;
										}

										@Override
										protected void initWidget() {
											setSelector(new ButtonSelector() {
												{
													setList(Lists.<MButton> newArrayList(
															new MButton(new R()) {
																@Override
																protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
																	loadAndOpen(true, true);
																	return true;
																}
															}.setText(I18n.format("signpic.gui.settings.sign.load")),
															new MButton(new R()) {
																@Override
																protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
																	loadAndOpen(true, false);
																	return true;
																}
															}.setText(I18n.format("signpic.gui.settings.sign.load.content")),
															new MButton(new R()) {
																@Override
																protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
																	loadAndOpen(false, true);
																	return true;
																}
															}.setText(I18n.format("signpic.gui.settings.sign.load.meta"))));
												}
											});
											super.initWidget();
										}

										protected void loadAndOpen(final boolean content, final boolean meta) {
											load(content, meta);
											requestClose();
											Client.openEditor();
										}

										protected void load(final boolean content, final boolean meta) {
											final Entry oldentry = CurrentMode.instance.getEntryId().entry();
											final Entry newentry = GuiSignOption.this.entry;
											final EntryIdBuilder idb = new EntryIdBuilder();
											if (newentry.contentId!=null&&content)
												idb.setURI(newentry.contentId.getID());
											else if (oldentry.contentId!=null)
												idb.setURI(oldentry.contentId.getID());
											idb.setMeta(meta ? GuiSignOption.this.entry.getMetaBuilder() : oldentry.getMetaBuilder());
											CurrentMode.instance.setEntryId(idb.build());
										}
									});
									final VCommon v02 = V.pm(-.3f).add(Motion.blank(i += n)).add(Easings.easeOutBounce.move(d-i, 0f)).start();
									final VCommon o02 = V.pm(0).add(Motion.blank(i)).add(Easings.easeLinear.move(od-i, 1f)).start();
									add(new MButton(new R(Coord.right(v02), Coord.top(top += 25f), Coord.height(20f))) {
										@Override
										protected @Nonnull VCommon initOpacity() {
											return o02;
										}

										@Override
										protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
											final Content c = GuiSignOption.this.entry.getContent();
											if (c!=null)
												Client.openURL(c.id.getURI());
											return false;
										}
									}.setText(I18n.format("signpic.gui.settings.sign.openurl")));
									final VCommon v2 = V.pm(-.3f).add(Motion.blank(i += n)).add(Easings.easeOutBounce.move(d-i, 0f)).start();
									final VCommon o2 = V.pm(0).add(Motion.blank(i)).add(Easings.easeLinear.move(od-i, 1f)).start();
									add(new MButton(new R(Coord.right(v2), Coord.top(top += 25f), Coord.height(20f))) {
										@Override
										protected @Nonnull VCommon initOpacity() {
											return o2;
										}

										@Override
										protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
											final Content c = GuiSignOption.this.entry.getContent();
											if (c!=null) {
												c.markDirty();
												return true;
											}
											return false;
										}
									}.setText(I18n.format("signpic.gui.settings.sign.reload")));
									final VCommon v3 = V.pm(-.3f).add(Motion.blank(i += n)).add(Easings.easeOutBounce.move(d-i, 0f)).start();
									final VCommon o3 = V.pm(0).add(Motion.blank(i)).add(Easings.easeLinear.move(od-i, 1f)).start();
									add(new MButton(new R(Coord.left(v3), Coord.top(top += 25f), Coord.height(20f))) {
										@Override
										protected @Nonnull VCommon initOpacity() {
											return o3;
										}

										@Override
										protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
											final Content c = GuiSignOption.this.entry.getContent();
											if (c!=null) {
												c.markDirtyWithCache();
												return true;
											}
											return false;
										}
									}.setText(I18n.format("signpic.gui.settings.sign.redownload")));
									final VCommon v4 = V.pm(-.3f).add(Motion.blank(i += n)).add(Easings.easeOutBounce.move(d-i, 0f)).start();
									final VCommon o4 = V.pm(0f).add(Motion.blank(i)).add(Easings.easeLinear.move(od-i, 1f)).start();
									add(new MButton(new R(Coord.right(v4), Coord.top(top += 25f), Coord.height(20f))) {
										@Override
										protected @Nonnull VCommon initOpacity() {
											return o4;
										}

										@Override
										protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
											final Content c = GuiSignOption.this.entry.getContent();
											if (c!=null) {
												c.cancel();
												return true;
											}
											return false;
										}
									}.setText(I18n.format("signpic.gui.settings.sign.loadcancel")));
									final VCommon v5 = V.pm(-.3f).add(Motion.blank(i += n)).add(Easings.easeOutBounce.move(d-i, 0f)).start();
									final VCommon o5 = V.pm(0f).add(Motion.blank(i)).add(Easings.easeLinear.move(od-i, 1f)).start();
									add(new MButton(new R(Coord.left(v5), Coord.top(top += 25f), Coord.height(20f))) {
										{
											setBlock();
										}

										protected void setBlock() {
											final Content c = GuiSignOption.this.entry.getContent();
											if (c!=null)
												setBlock(!c.meta.isBlocked());
										}

										protected void setBlock(final boolean b) {
											if (b)
												setText(I18n.format("signpic.gui.settings.sign.block"));
											else
												setText(I18n.format("signpic.gui.settings.sign.unblock"));
										}

										@Override
										protected @Nonnull VCommon initOpacity() {
											return o5;
										}

										@Override
										protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
											final Content c = GuiSignOption.this.entry.getContent();
											if (c!=null) {
												final boolean blocked = c.meta.isBlocked();
												c.meta.setBlocked(!blocked);
												setBlock();
												c.markDirty();
												return true;
											}
											return false;
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
}
