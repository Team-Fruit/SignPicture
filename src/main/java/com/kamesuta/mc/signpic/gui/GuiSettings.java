package com.kamesuta.mc.signpic.gui;

import static org.lwjgl.opengl.GL11.*;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.kamesuta.mc.bnnwidget.WBox;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MLabel;
import com.kamesuta.mc.bnnwidget.component.MSelect;
import com.kamesuta.mc.bnnwidget.component.MSelectLabel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.MCoord;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Coords;
import com.kamesuta.mc.bnnwidget.position.PCoord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.Apis;
import com.kamesuta.mc.signpic.Apis.ImageUploaderFactory;
import com.kamesuta.mc.signpic.Apis.Setting;
import com.kamesuta.mc.signpic.render.RenderHelper;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class GuiSettings extends WPanel {
	public GuiSettings(final R area) {
		super(area);
	}

	@Override
	protected void initWidget() {
		add(new WPanel(new R()) {
			protected boolean show = true;
			protected MCoord bottom = MCoord.pbottom(0f);

			@Override
			protected void initWidget() {
				add(new WPanel(new R(PCoord.of(Coords.combine(Coord.pbottom(-1), Coord.bottom(2)), Coord.pbottom(0f), this.bottom))) {
					@Override
					public void update(final WEvent ev, final Area pgp, final Point p) {
						final Area a = getGuiPosition(pgp);
						final boolean b = a.pointInside(p);
						if (b) {
							if (!show) {
								bottom.stop().add(Easings.easeOutQuad.move(.5f, 1f)).start();
								mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("signpic", "gui.show"), 1.0F));
							}
							show = true;
						} else {
							if (show)
								bottom.stop().add(Easings.easeOutBounce.move(.6f, 0f)).start();
							show = false;
						}
						super.update(ev, pgp, p);
					}

					@Override
					public boolean mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
						final Area a = getGuiPosition(pgp);
						return super.mouseClicked(ev, pgp, p, button)||a.pointInside(p);
					}

					@Override
					protected void initWidget() {
						add(new WPanel(new R(Coord.top(2), Coord.bottom(0))) {
							protected R line = new R(Coord.top(0), Coord.top(2));

							@Override
							public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity) {
								final Area a = getGuiPosition(pgp);
								RenderHelper.startShape();
								glColor4f(0f, 0f, 0f, .6f);
								drawRect(a);
								glColor4f(0f/256f, 78f/256f, 155f/256f, 1f);
								drawRect(this.line.getAbsolute(a));
								super.draw(ev, pgp, p, frame, opacity);
							}

							@Override
							protected void initWidget() {
								add(new WPanel(new R(Coord.left(5), Coord.width(200), Coord.top(5), Coord.height(82))) {
									@Override
									public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity) {
										final Area a = getGuiPosition(pgp);
										RenderHelper.startShape();
										glColor4f(0f, 0f, 0f, .2f);
										draw(a, GL_LINE_LOOP);
										super.draw(ev, pgp, p, frame, opacity);
									}

									protected WBox box = new WBox(new R(Coord.left(1), Coord.right(1), Coord.top(1+15+32+1), Coord.height(32))) {
										@Override
										protected void initWidget() {
											final ImageUploaderFactory factory = Apis.instance.imageUploader.solve(Apis.instance.imageUploader.getSetting());
											Set<String> keys = null;
											if (factory!=null)
												keys = factory.keys();
											Apis.instance.imageUploaderKey = new Apis.KeySetting(keys);
											box.add(new Key(new R(), Apis.instance.imageUploaderKey));
										}
									};

									@Override
									protected void initWidget() {
										float top = 1;
										add(new MLabel(new R(Coord.left(1), Coord.right(1), Coord.top(top), Coord.height(15)), "Api Settings"));
										add(new WPanel(new R(Coord.left(1), Coord.right(1), Coord.top(top += 15), Coord.height(32))) {
											@Override
											public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity) {
												final Area a = getGuiPosition(pgp);
												RenderHelper.startShape();
												glColor4f(0f, 0f, 0f, .2f);
												drawRect(a);
												super.draw(ev, pgp, p, frame, opacity);
											}

											@Override
											protected void initWidget() {
												add(new MLabel(new R(Coord.left(1), Coord.right(1), Coord.top(1), Coord.height(15)), "Type"));
												add(new MSelectLabel(new R(Coord.left(1), Coord.right(1), Coord.top(16), Coord.height(15)), 15) {
													{
														setSelector(new ListSelector() {
															{
																setList(Lists.newArrayList(Apis.instance.imageUploader.getSettings()));
															}
														});
													}

													@Override
													protected void onChanged(final String oldText, final String newText) {
														if (!StringUtils.equals(oldText, newText)) {
															final ImageUploaderFactory factory = Apis.instance.imageUploader.solve(newText);
															Set<String> keys = null;
															if (factory!=null)
																keys = factory.keys();
															Apis.instance.imageUploaderKey = new Apis.KeySetting(keys);
															box.add(new Key(new R(), Apis.instance.imageUploaderKey));
															Apis.instance.imageUploader.solve(newText);
														}
													}
												});
											}
										});
										add(this.box);
									}

									class Key extends WPanel {
										protected Setting setting;

										public Key(final R position, final Setting setting) {
											super(position);
											this.setting = setting;
										}

										@Override
										public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity) {
											final Area a = getGuiPosition(pgp);
											RenderHelper.startShape();
											glColor4f(0f, 0f, 0f, .2f);
											drawRect(a);
											super.draw(ev, pgp, p, frame, opacity);
										}

										@Override
										protected void initWidget() {
											add(new MLabel(new R(Coord.left(1), Coord.right(1), Coord.top(1), Coord.height(15)), "Key"));
											add(new MSelect(new R(Coord.left(1), Coord.right(1), Coord.top(16), Coord.height(15)), 15) {
												{
													setSelector(new ListSelector() {
														{
															if (Key.this.setting!=null)
																setList(Lists.newArrayList(Key.this.setting.getSettings()));
															else
																setList(Lists.<String> newArrayList());
														}
													});
													this.field.setWatermark("Default");
												}

												@Override
												protected void onChanged(final String oldText, final String newText) {
													if (!StringUtils.equals(oldText, newText))
														Apis.instance.imageUploader.solve(newText);
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
		});
	}
}
