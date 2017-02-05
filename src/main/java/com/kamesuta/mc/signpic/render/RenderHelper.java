package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.bnnwidget.render.WRenderer.WVertex;

public class RenderHelper {
	public static void drawLoadingCircle(final int msPerRoundInner, final int msPerRoundOuter) {
		final long time = System.currentTimeMillis();
		final float time1 = time%Math.abs(msPerRoundOuter)/(float) msPerRoundOuter;
		final WVertex vertex1 = WRenderer.begin(GL_LINE_LOOP);
		addCircleVertex(vertex1, time1, time1+0.2f, 1.07f);
		addCircleVertex(vertex1, time1+0.2f, time1, 1.09f);
		vertex1.draw();
		final float time2 = time%Math.abs(msPerRoundInner)/(float) msPerRoundInner;
		final WVertex vertex2 = WRenderer.begin(GL_LINE_LOOP);
		addCircleVertex(vertex2, time2, time2+0.1f, 1.03f);
		addCircleVertex(vertex2, time2+0.1f, time2, 1.05f);
		vertex2.draw();
	}

	public static void drawDesignCircle() {
		final WVertex vertex = WRenderer.begin(GL_LINE_LOOP);
		addCircleVertex(vertex, 0f, 1f, 1f);
		vertex.draw();
	}

	public static void drawProgressCircle(final float progress) {
		final WVertex vertex = WRenderer.begin(GL_POLYGON);
		vertex.pos(0f, 0f, 0f);
		addCircleVertex(vertex, progress, 0f, 1f);
		vertex.draw();
	}

	public static void drawProgressCircle(final int mode, final float r) {
		final WVertex vertex = WRenderer.begin(mode);
		addCircleVertex(vertex, 0f, 1f, r);
		vertex.draw();
	}

	public static void addCircleVertex(final WVertex vertex, final float start, final float end, final float r, final float acc) {
		final double sangle = Math.PI*(2d*start-.5);
		final double sx = Math.cos(sangle);
		final double sy = Math.sin(sangle);
		final double eangle = Math.PI*(2d*end-.5);
		final double ex = Math.cos(eangle);
		final double ey = Math.sin(eangle);

		vertex.pos(sx*r, sy*r, 0);
		for (int i = (int) (end<start ? Math.floor(start*acc) : Math.ceil(start*acc)); end<start ? i>end*acc : i<end*acc; i += end<start ? -1 : 1) {
			final double angle = Math.PI*(2d*i/acc-.5);
			final double ix = Math.cos(angle);
			final double iy = Math.sin(angle);
			vertex.pos(ix*r, iy*r, 0);
		}
		vertex.pos(ex*r, ey*r, 0);
	}

	public static void addCircleVertex(final WVertex vertex, final float start, final float end, final float r) {
		addCircleVertex(vertex, start, end, r, 32f);
	}

	public static void drawRect(final int mode) {
		drawRect(mode, 0, 0, 1, 1);
	}

	public static void drawRect(final int mode, final float x1, final float y1, final float x2, final float y2) {
		final WVertex vertex = WRenderer.begin(mode);
		addRectVertex(vertex, x1, y1, x2, y2);
		vertex.draw();
	}

	public static void addRectVertex(final WVertex vertex, final float x1, final float y1, final float x2, final float y2) {
		vertex.pos(x1, y2, 0);
		vertex.pos(x2, y2, 0);
		vertex.pos(x2, y1, 0);
		vertex.pos(x1, y1, 0);
	}

	public static void drawRectTexture(final int mode) {
		drawRectTexture(mode, 0, 0, 1, 1, 0, 0, 1, 1);
	}

	public static void drawRectTexture(final int mode, final float x1, final float y1, final float x2, final float y2, final float u1, final float v1, final float u2, final float v2) {
		final WVertex vertex = WRenderer.beginTexture(mode);
		addRectVertexTexture(vertex, x1, y1, x2, y2, u1, v1, u2, v2);
		vertex.draw();
	}

	public static void addRectVertexTexture(final WVertex vertex, final float x1, final float y1, final float x2, final float y2, final float u1, final float v1, final float u2, final float v2) {
		vertex
				.pos(x1, y2, 0).tex(u1, v2)
				.pos(x2, y2, 0).tex(u2, v2)
				.pos(x2, y1, 0).tex(u2, v1)
				.pos(x1, y1, 0).tex(u1, v1);
	}
}
