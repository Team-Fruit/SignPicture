package com.kamesuta.mc.signpic.gui;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MButton;
import com.kamesuta.mc.bnnwidget.component.MLabel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.Motion;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VCommon;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.entry.Entry;
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

			@Override
			protected void initOpacity() {
				setOpacity(this.opa = V.pm(1f).start());
			}

			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
				super.draw(ev, pgp, p, frame, popacity);
			}

			@Override
			protected void initWidget() {
				add(new WBase(new R()) {
					protected VMotion opac;

					@Override
					protected void initOpacity() {
						setOpacity(this.opac = V.pm(0f).add(Easings.easeLinear.move(.5f, .5f)).start());
					}

					@Override
					public boolean onCloseRequest() {
						this.opac.stop().add(Easings.easeLinear.move(.2f, 0f)).start();
						opa.stop().add(Easings.easeLinear.move(.2f, 0f)).start();
						return false;
					}

					@Override
					public boolean onClosing(final WEvent ev, final Area pgp, final Point p) {
						return this.opac.isFinished();
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
				add(new WPanel(new R(Coord.pleft(.5f), Coord.ptop(.5f), Coord.width(100f), Coord.height(100f))) {
					@Override
					protected void initWidget() {
						add(new WPanel(new R(Coord.pleft(-.5f), Coord.ptop(-.5f))) {
							@Override
							protected void initWidget() {
								final float d = 1f;
								final float n = .1f;
								final float od = .7f;
								float i = 0f;
								final VCommon v1 = V.pm(-.3f).add(Motion.blank(i += n)).add(Easings.easeOutBounce.move(d-i, 0f)).start();
								final VCommon o1 = V.pm(0f).add(Motion.blank(i)).add(Easings.easeLinear.move(od-i, 1f)).start();
								add(new MLabel(new R(Coord.left(v1), Coord.top(0f), Coord.height(20f))) {
									@Override
									protected void initOpacity() {
										setOpacity(o1);
									}
								}.setText(I18n.format("signpic.gui.settings.sign")));
								final VCommon v2 = V.pm(-.3f).add(Motion.blank(i += n)).add(Easings.easeOutBounce.move(d-i, 0f)).start();
								final VCommon o2 = V.pm(0).add(Motion.blank(i)).add(Easings.easeLinear.move(od-i, 1f)).start();
								add(new MButton(new R(Coord.right(v2), Coord.top(25f), Coord.height(20f))) {
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
								add(new MButton(new R(Coord.left(v3), Coord.top(50f), Coord.height(20f))) {
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
								add(new MButton(new R(Coord.right(v4), Coord.top(75f), Coord.height(20f))) {
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
							}
						});
					}
				});
			}
		});
	}
}
