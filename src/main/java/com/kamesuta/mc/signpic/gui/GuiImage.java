package com.kamesuta.mc.signpic.gui;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MScaledLabel;
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
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.image.meta.ImageTextureMap;
import com.kamesuta.mc.signpic.information.Informations;
import com.kamesuta.mc.signpic.render.RenderHelper;
import com.kamesuta.mc.signpic.render.StateRender;
import com.kamesuta.mc.signpic.state.StateType;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiImage extends WFrame {
	protected Entry entry;

	public static final ResourceLocation resError = new ResourceLocation("signpic", "textures/state/error.png");

	public GuiImage(final Entry entry) {
		this.entry = entry;
		setWorldAndResolution(Client.mc, 0, 0);
	}

	@Override
	protected void initWidget() {
		add(new WPanel(new R()) {
			@Override
			protected void initWidget() {
				add(new WBase(new R()) {
					@Override
					public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, float opacity) {
						final Area a = getGuiPosition(pgp);
						final Content content = GuiImage.this.entry.content();

						GlStateManager.pushMatrix();
						if (GuiImage.this.entry.isNotSupported())
							opacity *= .5f;
						GlStateManager.pushMatrix();
						GlStateManager.scale(a.w(), a.h(), 1f);
						if (content.state.getType()==StateType.AVAILABLE) {
							GlStateManager.color(1.0F, 1.0F, 1.0F, opacity*1.0F);
							final ImageTextureMap map = GuiImage.this.entry.meta.map;
							content.image.draw(map.u, map.v, map.w, map.h, map.c, map.s, map.r, map.m);
						} else {
							RenderHelper.startShape();
							glLineWidth(1f);
							GlStateManager.color(1.0F, 0.0F, 0.0F, opacity*Config.instance.renderSeeOpacity);
							draw(0, 0, 1, 1, GL_LINE_LOOP);
						}
						GlStateManager.popMatrix();

						if (a.w()<1.5f||a.h()<1.5) {
							GlStateManager.scale(.5f, .5f, .5f);
							GlStateManager.translate(a.w()/2, a.h()/4, 0);
						}
						GlStateManager.translate(a.w()/2, a.h()/2, 0);
						GlStateManager.scale(.5f, .5f, 1f);
						if (content.state.getType()!=StateType.AVAILABLE) {
							if (content.state.getType()==StateType.ERROR) {
								GlStateManager.pushMatrix();
								;
								GlStateManager.scale(-.5f, -.5f, 0f);
								RenderHelper.startTexture();
								texture().bindTexture(resError);
								RenderHelper.drawRectTexture(GL_QUADS);
								GlStateManager.popMatrix();
							}
							StateRender.drawLoading(content.state.getProgress(), content.state.getType().circle, content.state.getType().speed);
							StateRender.drawMessage(content, font());
						}
						GlStateManager.popMatrix();
					}
				});
				add(new WPanel(new R()) {
					@Override
					protected void initWidget() {
						final VCommon var = V.range(V.a(.5f), V.a(.8f), V.p(.5f));
						add(new UpdateLogo(new R(Coord.width(var), Coord.height(var), Coord.pleft(.5f), Coord.ptop(.5f)).child(Coord.pleft(-.5f), Coord.ptop(-.5f))));
						add(new MScaledLabel(new R(Coord.pleft(.5f), Coord.top(0), Coord.pheight(.4f), Coord.width(2)).child(Coord.pleft(-.5f))).setText(I18n.format("signpic.advmsg.format.unsupported")).setColor(0xff9900).setShadow(true));
						if (Informations.instance.isUpdateRequired())
							add(new MScaledLabel(new R(Coord.pleft(.5f), Coord.bottom(0), Coord.pheight(.4f), Coord.width(2)).child(Coord.pleft(-.5f))).setText(I18n.format("signpic.advmsg.format.needupdate")).setColor(0xff9900).setShadow(true));
					}

					@Override
					public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
						if (GuiImage.this.entry.isNotSupported()) {
							RenderHelper.startShape();
							glLineWidth(1f);
							GlStateManager.color(1f, 1f, 1f, 1f);
							GlStateManager.pushMatrix();
							GlStateManager.translate(0f, 0f, .002f);
							super.draw(ev, pgp, p, frame, popacity);
							GlStateManager.popMatrix();
						}
					}
				});
			}
		});
	}

	protected class UpdateLogo extends WBase {
		protected VMotion rot = V.pm(0).add(Motion.of(0, Easings.easeInOutSine.move(2.87f, 1f), Motion.blank(0.58f)).setLoop(true)).setLoop(true).start();

		public UpdateLogo(final R position) {
			super(position);
		}

		@Override
		public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity) {
			final Area a = getGuiPosition(pgp);
			texture().bindTexture(GuiSettings.update);
			glColor4f(144f/256f, 191f/256f, 48f/256f, 1f);
			RenderHelper.startTexture();
			glPushMatrix();
			glTranslatef(a.x1()+a.w()/2, a.y1()+a.h()/2, 0f);
			glRotatef(this.rot.get()*360, 0, 0, 1);
			glTranslatef(-a.x1()-a.w()/2, -a.y1()-a.h()/2, -.001f);
			drawTexture(a);
			glTranslatef(0f, 0f, -.002f);
			drawTexture(a);
			glPopMatrix();
		}
	};

	public void drawScreen(final int mousex, final int mousey, final float f, final float opacity, final float width, final float height) {
		setWidth(width).setHeight(height);
		super.drawScreen(mousex, mousey, f, opacity);
	}
}
