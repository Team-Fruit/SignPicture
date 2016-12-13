package com.kamesuta.mc.signpic.gui;

import com.google.common.collect.Lists;
import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MButton;
import com.kamesuta.mc.bnnwidget.component.MLabel;
import com.kamesuta.mc.bnnwidget.component.MSelectButton;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.Motion;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VCommon;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryIdBuilder;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.render.OpenGL;
import com.kamesuta.mc.signpic.render.RenderHelper;

import net.minecraft.client.resources.I18n;

public class GuiSignOption extends WFrame {
	protected final Entry entry;

	public GuiSignOption(final Entry entry) {
		this.entry = entry;
		setGuiPauseGame(false);
	}

	@Override
	protected void initWidget() {
		add(new WPanel(new R()) {
			protected VMotion opa;
			protected VMotion opac;

			@Override
			protected void initOpacity() {
				setOpacity(this.opa = V.pm(1f).start());
			}

			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
				super.draw(ev, pgp, p, frame, popacity);
			}

			@Override
			public boolean mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				return this.opac.isFinished()&&super.mouseClicked(ev, pgp, p, button);
			}

			@Override
			protected void initWidget() {
				add(new WBase(new R()) {

					@Override
					protected void initOpacity() {
						setOpacity(opac = V.pm(0f).add(Easings.easeLinear.move(.5f, .5f)).start());
					}

					@Override
					public boolean onCloseRequest() {
						opac.stop().add(Easings.easeLinear.move(.2f, 0f)).start();
						opa.stop().add(Easings.easeLinear.move(.2f, 0f)).start();
						return false;
					}

					@Override
					public boolean onClosing(final WEvent ev, final Area pgp, final Point p) {
						return opac.isFinished();
					}

					@Override
					public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
						final Area a = getGuiPosition(pgp);
						RenderHelper.startShape();
						OpenGL.glColor4f(0f, 0f, 0f, getGuiOpacity(popacity));
						draw(a);
						super.draw(ev, pgp, p, frame, popacity);
					}
				});
				add(new WPanel(new R(Coord.pleft(.5f), Coord.ptop(.5f), Coord.width(200f), Coord.height(150f))) {
					@Override
					protected void initWidget() {
						add(new WPanel(new R(Coord.pleft(-.5f), Coord.ptop(-.5f))) {
							@Override
							protected void initWidget() {
								float top = -25f;
								final float d = 1f;
								final float n = .1f;
								final float od = .7f;
								float i = 0f;
								final VCommon v0 = V.pm(-.3f).add(Motion.blank(i += n)).add(Easings.easeOutBounce.move(d-i, 0f)).start();
								final VCommon o0 = V.pm(0f).add(Motion.blank(i)).add(Easings.easeLinear.move(od-i, 1f)).start();
								add(new MLabel(new R(Coord.left(v0), Coord.top(top += 25f), Coord.height(20f))) {
									@Override
									protected void initOpacity() {
										setOpacity(o0);
									}
								}.setText(I18n.format("signpic.gui.settings.sign")));
								final VCommon v1 = V.pm(-.3f).add(Motion.blank(i += n)).add(Easings.easeOutBounce.move(d-i, 0f)).start();
								final VCommon o1 = V.pm(0f).add(Motion.blank(i)).add(Easings.easeLinear.move(od-i, 1f)).start();
								add(new MSelectButton(new R(Coord.left(v1), Coord.top(top += 25f), Coord.height(20f)), 15) {
									@Override
									protected void initOpacity() {
										setOpacity(o1);
									}

									@Override
									protected void initWidget() {
										setSelector(new ButtonSelector() {
											{
												setList(Lists.<MButton> newArrayList(
														new MButton(new R()) {
															@Override
															protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
																loadAndOpen(true, true);
																return true;
															}
														}.setText(I18n.format("signpic.gui.settings.sign.load")),
														new MButton(new R()) {
															@Override
															protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
																loadAndOpen(true, false);
																return true;
															}
														}.setText(I18n.format("signpic.gui.settings.sign.load.content")),
														new MButton(new R()) {
															@Override
															protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
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
										// requestClose();
										Client.openEditor();
									}

									protected void load(final boolean content, final boolean meta) {
										final Entry old = CurrentMode.instance.getEntryId().entry();
										final EntryIdBuilder idb = new EntryIdBuilder();
										idb.setURI(content ? GuiSignOption.this.entry.contentId.getID() : old.contentId.getID());
										idb.setMeta(meta ? GuiSignOption.this.entry.getMeta() : old.getMeta());
										CurrentMode.instance.setEntryId(idb.build());
									}
								});
								final VCommon v2 = V.pm(-.3f).add(Motion.blank(i += n)).add(Easings.easeOutBounce.move(d-i, 0f)).start();
								final VCommon o2 = V.pm(0).add(Motion.blank(i)).add(Easings.easeLinear.move(od-i, 1f)).start();
								add(new MButton(new R(Coord.right(v2), Coord.top(top += 25f), Coord.height(20f))) {
									@Override
									protected void initOpacity() {
										setOpacity(o2);
									}

									@Override
									protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
										if (GuiSignOption.this.entry.isValid()) {
											GuiSignOption.this.entry.content().markDirty();
											return true;
										}
										return false;
									}
								}.setText(I18n.format("signpic.gui.settings.sign.reload")));
								final VCommon v3 = V.pm(-.3f).add(Motion.blank(i += n)).add(Easings.easeOutBounce.move(d-i, 0f)).start();
								final VCommon o3 = V.pm(0).add(Motion.blank(i)).add(Easings.easeLinear.move(od-i, 1f)).start();
								add(new MButton(new R(Coord.left(v3), Coord.top(top += 25f), Coord.height(20f))) {
									@Override
									protected void initOpacity() {
										setOpacity(o3);
									}

									@Override
									protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
										if (GuiSignOption.this.entry.isValid()) {
											GuiSignOption.this.entry.content().markDirtyWithCache();
											return true;
										}
										return false;
									}
								}.setText(I18n.format("signpic.gui.settings.sign.redownload")));
								final VCommon v4 = V.pm(-.3f).add(Motion.blank(i += n)).add(Easings.easeOutBounce.move(d-i, 0f)).start();
								final VCommon o4 = V.pm(0f).add(Motion.blank(i)).add(Easings.easeLinear.move(od-i, 1f)).start();
								add(new MButton(new R(Coord.right(v4), Coord.top(top += 25f), Coord.height(20f))) {
									@Override
									protected void initOpacity() {
										setOpacity(o4);
									}

									@Override
									protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
										if (GuiSignOption.this.entry.isValid()) {
											GuiSignOption.this.entry.content().cancel();
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
										setBlock(!GuiSignOption.this.entry.content().meta.isBlocked());
									}

									protected void setBlock(final boolean b) {
										if (b)
											setText(I18n.format("signpic.gui.settings.sign.block"));
										else
											setText(I18n.format("signpic.gui.settings.sign.unblock"));
									}

									@Override
									protected void initOpacity() {
										setOpacity(o5);
									}

									@Override
									protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
										if (GuiSignOption.this.entry.isValid()) {
											final boolean blocked = GuiSignOption.this.entry.content().meta.isBlocked();
											GuiSignOption.this.entry.content().meta.setBlocked(!blocked);
											setBlock();
											GuiSignOption.this.entry.content().markDirty();
											return true;
										}
										return false;
									}
								});
							}
						});
					}
				});
			}
		});
	}
}
