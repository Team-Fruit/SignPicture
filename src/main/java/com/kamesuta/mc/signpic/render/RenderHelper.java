package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import net.minecraft.client.renderer.Tessellator;

public class RenderHelper {
	protected static final Tessellator t = Tessellator.instance;

	public static void addCircleVertex(final float start, final float end, final float r) {
		final float acc = 32f;
		final double sangle = Math.PI*(2d*start-.5);
		final double sx = Math.cos(sangle);
		final double sy = Math.sin(sangle);
		final double eangle = Math.PI*(2d*end-.5);
		final double ex = Math.cos(eangle);
		final double ey = Math.sin(eangle);

		t.addVertex(sx*r, sy*r, 0);
		for(int i=(int)((end<start)?Math.floor(start*acc):Math.ceil(start*acc)); (end<start)?i>end*acc:i<end*acc; i+=(end<start)?-1:1) {
			final double angle = Math.PI*(2d*i/acc-.5);
			final double ix = Math.cos(angle);
			final double iy = Math.sin(angle);
			t.addVertex(ix*r, iy*r, 0);
		}
		t.addVertex(ex*r, ey*r, 0);
	}

	public static void drawLoadingCircle(final int msPerRoundInner, final int msPerRoundOuter) {
		final long time = System.currentTimeMillis();
		final float time1 = time % Math.abs(msPerRoundOuter) / (float)msPerRoundOuter;
		t.startDrawing(GL_LINE_LOOP);
		addCircleVertex(time1, time1+0.2f, 1.07f);
		addCircleVertex(time1+0.2f, time1, 1.09f);
		t.draw();
		final float time2 = time % Math.abs(msPerRoundInner) / (float)msPerRoundInner;
		t.startDrawing(GL_LINE_LOOP);
		addCircleVertex(time2, time2+0.1f, 1.03f);
		addCircleVertex(time2+0.1f, time2, 1.05f);
		t.draw();
	}

	public static void drawDesignCircle() {
		t.startDrawing(GL_LINE_LOOP);
		addCircleVertex(0f, 1f, 1f);
		t.draw();
	}

	public static void drawProgressCircle(final float progress) {
		t.startDrawing(GL_POLYGON);
		t.addVertex(0f, 0f, 0f);
		addCircleVertex(progress, 0f, 1f);
		t.draw();
	}
}
