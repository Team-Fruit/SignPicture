package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.Color;

import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.state.Progress;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class StateRender {
	public static enum LoadingCircle {
		INIT(new Color(0, 255, 255, 255), new Color(160, 160, 160, 255), new Color(120, 120, 120, 255)), DOWNLOAD(new Color(0, 255, 255, 255), new Color(0, 102, 204, 255), new Color(23, 121, 232, 255)), CONTENTLOAD(new Color(0, 255, 255, 255), new Color(0, 144, 55), new Color(23, 177, 55, 255)), DEFAULT(new Color(), new Color(), new Color());

		private final Color loading;
		private final Color progress;
		private final Color design;

		private LoadingCircle(final Color loading, final Color progress, final Color design) {
			this.loading = loading;
			this.progress = progress;
			this.design = design;
		}

		public void loadingColor() {
			color(this.loading);
		}

		public void progressColor() {
			color(this.progress);
		}

		public void designColor() {
			color(this.design);
		}

		private static void color(final Color color) {
			glColor4ub(color.getRedByte(), color.getGreenByte(), color.getBlueByte(), color.getAlphaByte());
		}
	}

	public static void drawLoading(final Progress progress, final LoadingCircle type, final LoadingCircleType speed) {
		if (type!=LoadingCircle.DEFAULT) {
			glLineWidth(3f);
			RenderHelper.startShape();

			GlStateManager.pushMatrix();
			GlStateManager.scale(.5f, .5f, 1f);

			// Loading Circle
			type.loadingColor();
			RenderHelper.drawLoadingCircle(speed.inner, speed.outer);

			// Design Circle
			type.designColor();
			RenderHelper.drawDesignCircle();

			// Progress Circle
			type.progressColor();
			final float p = progress.getProgress();
			RenderHelper.drawProgressCircle(p);

			GlStateManager.popMatrix();
		}
	}

	public static void drawMessage(final Content content, final FontRenderer fontrenderer) {
		RenderHelper.startTexture();
		final float f1 = 0.6666667F;
		float f3 = 0.06666668F*f1;
		GlStateManager.translate(0f, 1f, 0f);
		GlStateManager.pushMatrix();
		GlStateManager.scale(f3, f3, 1f);
		final String msg1 = content.state.getStateMessage();
		fontrenderer.drawStringWithShadow(msg1, -fontrenderer.getStringWidth(msg1)/2, -fontrenderer.FONT_HEIGHT, 0xffffff);
		GlStateManager.popMatrix();
		f3 = 0.036666668F*f1;
		GlStateManager.pushMatrix();
		GlStateManager.scale(f3, f3, 1f);
		final String msg2 = content.id.id();
		fontrenderer.drawStringWithShadow(msg2, -fontrenderer.getStringWidth(msg2)/2, 0, 0xffffff);
		GlStateManager.popMatrix();
		final String msg3 = content.state.getMessage();
		if (msg3!=null) {
			GlStateManager.pushMatrix();
			GlStateManager.scale(f3, f3, 1f);
			fontrenderer.drawStringWithShadow(msg3, -fontrenderer.getStringWidth(msg3)/2, fontrenderer.FONT_HEIGHT, 0xffffff);
			GlStateManager.popMatrix();
		}
	}

	public static enum LoadingCircleType {
		WAIT(627*-2, 893*-2), RUN(627, 893), DEFAULT(-1, -1),
		;

		public final int inner;
		public final int outer;

		LoadingCircleType(final int inner, final int outer) {
			this.inner = inner;
			this.outer = outer;
		}
	}
}
