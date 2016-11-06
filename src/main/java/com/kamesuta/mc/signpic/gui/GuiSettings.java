package com.kamesuta.mc.signpic.gui;

import static org.lwjgl.opengl.GL11.*;

import com.google.common.collect.Lists;
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
								bottom.stop().add(Easings.easeOutQuart.move(.7f, 1f)).start();
								mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("signpic", "gui.show"), 1.0F));
							}
							show = true;
						} else {
							if (show)
								bottom.stop().add(Easings.easeOutQuart.move(.7f, 0f)).start();
							show = false;
						}
						super.update(ev, pgp, p);
					}

					@Override
					protected void initWidget() {
						add(new WPanel(new R(Coord.top(2), Coord.bottom(0))) {

							@Override
							public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity) {
								final Area a = getGuiPosition(pgp);
								RenderHelper.startShape();
								glColor4f(0f, 0f, 0f, .6f);
								drawRect(a);
								super.draw(ev, pgp, p, frame, opacity);
							}

							@Override
							protected void initWidget() {
								add(new WPanel(new R(Coord.left(5), Coord.width(200), Coord.top(5), Coord.height(100))) {
									@Override
									protected void initWidget() {
										float top = 5f;
										add(new MLabel(new R(Coord.left(5), Coord.right(5), Coord.top(top += 25), Coord.height(15)), "Type"));
										add(new MSelectLabel(new R(Coord.left(5), Coord.right(5), Coord.top(top += 25), Coord.height(15)), 15) {
											{
												setSelector(new ListSelector() {
													{
														setList(Lists.newArrayList("Gyazo", "Imgur"));
													}
												});
											}
										});
										add(new MLabel(new R(Coord.left(5), Coord.right(5), Coord.top(top += 25), Coord.height(15)), "Key"));
										add(new MSelect(new R(Coord.left(5), Coord.right(5), Coord.top(top += 25), Coord.height(15)), 15) {
											{
												setSelector(new ListSelector() {
													{
														setList(Lists.newArrayList("hlunsghvl", "ihulagvtiun"));
													}
												});
											}
										});
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
