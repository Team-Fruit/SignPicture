package com.kamesuta.mc.signpic.gui;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VCommon;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.attr.CompoundAttr;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.information.Informations;
import com.kamesuta.mc.signpic.render.RenderHelper;
import com.kamesuta.mc.signpic.render.StateRender;
import com.kamesuta.mc.signpic.state.StateType;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiImage extends WFrame {
	protected @Nonnull Entry entry;

	public static final @Nonnull ResourceLocation resError = new ResourceLocation("signpic", "textures/state/error.png");

	public GuiImage(final @Nonnull Entry entry) {
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
					public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity) {
						final Area a = getGuiPosition(pgp);
						float opacity = getGuiOpacity(popacity);

						OpenGL.glPushMatrix();
						if (GuiImage.this.entry.isNotSupported())
							opacity *= .5f;
						OpenGL.glPushMatrix();
						OpenGL.glScalef(a.w(), a.h(), 1f);

						@Nullable
						final Content content = GuiImage.this.entry.getContent();
						@Nonnull
						CompoundAttr meta;
						if (content!=null&&content.state.getType()==StateType.AVAILABLE&&!(meta = GuiImage.this.entry.getMeta()).hasInvalidMeta()) {
							final float o = meta.o.getMovie().get().data*0.1f;
							OpenGL.glColor4f(1.0F, 1.0F, 1.0F, opacity*o);
							content.image.draw(
									meta.u.getMovie().get().data,
									meta.v.getMovie().get().data,
									meta.w.getMovie().get().data,
									meta.h.getMovie().get().data,
									meta.c.getMovie().get().data,
									meta.s.getMovie().get().data,
									meta.b.getMovie().get().data,
									meta.d.getMovie().get().data,
									meta.r.getMovie().get().data,
									meta.m.getMovie().get().data);
						} else {
							WRenderer.startShape();
							OpenGL.glLineWidth(1f);
							OpenGL.glColor4f(1.0F, 0.0F, 0.0F, opacity*1.0F);
							drawAbs(0, 0, 1, 1, GL_LINE_LOOP);
						}
						OpenGL.glPopMatrix();

						if (a.w()<1.5f||a.h()<1.5) {
							OpenGL.glScalef(.5f, .5f, .5f);
							OpenGL.glTranslatef(a.w()/2, a.h()/4, 0);
						}
						OpenGL.glTranslatef(a.w()/2, a.h()/2, 0);
						OpenGL.glScalef(.5f, .5f, 1f);
						if (content!=null&&content.state.getType()!=StateType.AVAILABLE) {
							if (content.state.getType()==StateType.ERROR) {
								OpenGL.glPushMatrix();
								OpenGL.glTranslatef(-.5f, -.5f, 0f);
								WRenderer.startTexture();
								texture().bindTexture(resError);
								RenderHelper.drawRectTexture(GL_QUADS);
								OpenGL.glPopMatrix();
							}
							StateRender.drawLoading(content.state.getProgress(), content.state.getType().circle, content.state.getType().speed);
							StateRender.drawMessage(content, font());
						}
						OpenGL.glPopMatrix();
					}
				});
				add(new WPanel(new R()) {
					@Override
					protected void initWidget() {
						final VCommon var = V.a(.8f);
						add(new UpdateLogo(new R(Coord.width(var), Coord.height(var), Coord.pleft(.5f), Coord.ptop(.5f)).child(Coord.pleft(-.5f), Coord.ptop(-.5f))));
						add(new MScaledLabel(new R(Coord.pleft(.5f), Coord.top(0), Coord.pheight(.4f), Coord.width(2)).child(Coord.pleft(-.5f))).setText(I18n.format("signpic.advmsg.format.unsupported")).setColor(0xff9900).setShadow(true));
						if (Informations.instance.isUpdateRequired())
							add(new MScaledLabel(new R(Coord.pleft(.5f), Coord.bottom(0), Coord.pheight(.4f), Coord.width(2)).child(Coord.pleft(-.5f))).setText(I18n.format("signpic.advmsg.format.needupdate")).setColor(0xff9900).setShadow(true));
					}

					@Override
					public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity) {
						if (GuiImage.this.entry.isNotSupported()) {
							WRenderer.startShape();
							OpenGL.glLineWidth(1f);
							OpenGL.glColor4f(1f, 1f, 1f, 1f);
							OpenGL.glPushMatrix();
							OpenGL.glTranslatef(0f, 0f, .002f);
							super.draw(ev, pgp, p, frame, popacity);
							OpenGL.glPopMatrix();
						}
					}
				});
			}
		});
	}

	protected class UpdateLogo extends WBase {
		protected @Nonnull VMotion rot = V.pm(0).add(Motion.of(0, Easings.easeInOutSine.move(2.87f, 1f), Motion.blank(0.58f)).setLoop(true)).setLoop(true).start();

		public UpdateLogo(final @Nonnull R position) {
			super(position);
		}

		@Override
		public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float opacity) {
			final Area a = getGuiPosition(pgp);
			texture().bindTexture(GuiSettings.update);
			OpenGL.glColor4f(144f/256f, 191f/256f, 48f/256f, 1f);
			WRenderer.startTexture();
			OpenGL.glPushMatrix();
			OpenGL.glTranslatef(a.x1()+a.w()/2, a.y1()+a.h()/2, 0f);
			OpenGL.glRotatef(this.rot.get()*360, 0, 0, 1);
			OpenGL.glTranslatef(-a.x1()-a.w()/2, -a.y1()-a.h()/2, -.001f);
			drawTexture(a);
			OpenGL.glTranslatef(0f, 0f, -.002f);
			drawTexture(a);
			OpenGL.glPopMatrix();
		}
	};

	public void drawScreen(final int mousex, final int mousey, final float f, final float opacity, final float width, final float height) {
		setWidth(width).setHeight(height);
		super.drawScreen(mousex, mousey, f, opacity);
	}
}