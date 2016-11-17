package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderHelper {
	public static final Tessellator t = Tessellator.getInstance();
	public static final WorldRenderer w = t.getWorldRenderer();

	public static void drawLoadingCircle(final int msPerRoundInner, final int msPerRoundOuter) {
		final long time = System.currentTimeMillis();
		final float time1 = time % Math.abs(msPerRoundOuter) / (float)msPerRoundOuter;
		w.begin(GL_LINE_LOOP, DefaultVertexFormats.POSITION);
		addCircleVertex(time1, time1+0.2f, 1.07f);
		addCircleVertex(time1+0.2f, time1, 1.09f);
		t.draw();
		final float time2 = time % Math.abs(msPerRoundInner) / (float)msPerRoundInner;
		w.begin(GL_LINE_LOOP, DefaultVertexFormats.POSITION);
		addCircleVertex(time2, time2+0.1f, 1.03f);
		addCircleVertex(time2+0.1f, time2, 1.05f);
		t.draw();
	}

	public static void drawDesignCircle() {
		w.begin(GL_LINE_LOOP, DefaultVertexFormats.POSITION);
		addCircleVertex(0f, 1f, 1f);
		t.draw();
	}

	public static void drawProgressCircle(final float progress) {
		w.begin(GL_POLYGON, DefaultVertexFormats.POSITION);
		w.pos(0f, 0f, 0f).endVertex();
		addCircleVertex(progress, 0f, 1f);
		t.draw();
	}

	public static void drawProgressCircle(final int mode, final float r) {
		w.begin(mode, DefaultVertexFormats.POSITION);
		addCircleVertex(0f, 1f, r);
		t.draw();
	}

	public static void addCircleVertex(final float start, final float end, final float r, final float acc) {
		final double sangle = Math.PI*(2d*start-.5);
		final double sx = Math.cos(sangle);
		final double sy = Math.sin(sangle);
		final double eangle = Math.PI*(2d*end-.5);
		final double ex = Math.cos(eangle);
		final double ey = Math.sin(eangle);

		w.pos(sx*r, sy*r, 0).endVertex();
		for (int i = (int) (end<start ? Math.floor(start*acc) : Math.ceil(start*acc)); end<start ? i>end*acc : i<end*acc; i += end<start ? -1 : 1) {
			final double angle = Math.PI*(2d*i/acc-.5);
			final double ix = Math.cos(angle);
			final double iy = Math.sin(angle);
			w.pos(ix*r, iy*r, 0).endVertex();
		}
		w.pos(ex*r, ey*r, 0).endVertex();
	}

	public static void addCircleVertex(final float start, final float end, final float r) {
		addCircleVertex(start, end, r, 32f);
	}

	public static void drawRect(final int mode) {
		drawRect(mode, 0, 0, 1, 1);
	}

	public static void drawRect(final int mode, final float x1, final float y1, final float x2, final float y2) {
		w.begin(mode, DefaultVertexFormats.POSITION_TEX);
		addRectVertex(x1, y1, x2, y2);
		t.draw();
	}

	public static void addRectVertex(final float x1, final float y1, final float x2, final float y2) {
		w.pos(x1, y1, 0).tex(0, 0).endVertex();
		w.pos(x1, y2, 0).tex(0, 1).endVertex();
		w.pos(x2, y2, 0).tex(1, 1).endVertex();
		w.pos(x2, y1, 0).tex(1, 0).endVertex();
	}

	public static void drawRectTexture(final int mode) {
		drawRectTexture(mode, 0, 0, 1, 1, 0, 0, 1, 1);
	}

	public static void drawRectTexture(final int mode, final float x1, final float y1, final float x2, final float y2, final float u1, final float v1, final float u2, final float v2) {
		w.begin(mode, DefaultVertexFormats.POSITION_TEX);
		addRectVertexTexture(x1, y1, x2, y2, u1, v1, u2, v2);
		t.draw();
	}

	public static void addRectVertexTexture(final float x1, final float y1, final float x2, final float y2, final float u1, final float v1, final float u2, final float v2) {
		w.pos(x1, y2, 0).tex(u1, v2).endVertex();
		w.pos(x2, y2, 0).tex(u2, v2).endVertex();
		w.pos(x2, y1, 0).tex(u2, v1).endVertex();
		w.pos(x1, y1, 0).tex(u1, v1).endVertex();
	}

	public static void startTexture() {
		GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableBlend();
		GlStateManager.enableTexture2D();
	}

	public static void startShape() {
		GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
	}
}
