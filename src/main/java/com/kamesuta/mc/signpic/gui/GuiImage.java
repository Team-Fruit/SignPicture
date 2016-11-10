package com.kamesuta.mc.signpic.gui;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.render.RenderHelper;
import com.kamesuta.mc.signpic.render.StateRender;
import com.kamesuta.mc.signpic.state.StateType;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class GuiImage extends WFrame {
	protected Content content;

	public static final ResourceLocation resError = new ResourceLocation("signpic", "textures/state/error.png");

	public GuiImage(final Content content) {
		this.content = content;
		setWorldAndResolution(Client.mc, 0, 0);
	}

	@Override
	protected void initWidget() {
		add(new WPanel(R.diff(0, 0, 0, 0)) {
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity) {
				final Area a = getGuiPosition(pgp);

				glPushMatrix();
				glScalef(a.w(), a.h(), 1f);
				if (GuiImage.this.content.state.getType()==StateType.AVAILABLE) {
					glColor4f(1.0F, 1.0F, 1.0F, opacity*1.0F);
					GuiImage.this.content.image.draw();
				} else {
					final Tessellator t = Tessellator.getInstance();
					final WorldRenderer w = t.getWorldRenderer();
					RenderHelper.startShape();
					glLineWidth(1f);
					glColor4f(1.0F, 0.0F, 0.0F, opacity*1.0F);

					w.begin(GL_LINE_LOOP, DefaultVertexFormats.POSITION_TEX);
					w.pos(0, 0, 0).tex(0, 0).endVertex();
					w.pos(0, 1, 0).tex(0, 1).endVertex();
					w.pos(1, 1, 0).tex(1, 1).endVertex();
					w.pos(1, 0, 0).tex(1, 0).endVertex();
					t.draw();
				}
				glPopMatrix();

				if (a.w()<1.5f||a.h()<1.5) {
					glScalef(.5f, .5f, .5f);
					glTranslatef(a.w()/2, a.h()/4, 0);
				}
				glTranslatef(a.w()/2, a.h()/2, 0);
				glScalef(.5f, .5f, 1f);
				if (GuiImage.this.content.state.getType()!=StateType.AVAILABLE) {
					if (GuiImage.this.content.state.getType()==StateType.ERROR) {
						RenderHelper.startShape();
						glPushMatrix();
						glTranslatef(-.5f, -.5f, 0f);
						RenderHelper.startTexture();
						texture().bindTexture(resError);
						RenderHelper.drawRectTexture(GL_QUADS);
						glPopMatrix();
					}
					StateRender.drawLoading(GuiImage.this.content.state.getProgress(), GuiImage.this.content.state.getType().circle, GuiImage.this.content.state.getType().speed);
					StateRender.drawMessage(GuiImage.this.content, font());
				}
				super.draw(ev, pgp, p, frame, opacity);
			}
		});
	}

	public void drawScreen(final int mousex, final int mousey, final float f, final float opacity, final float width, final float height) {
		setWidth(width).setHeight(height);
		super.drawScreen(mousex, mousey, f, opacity);
	}
}
