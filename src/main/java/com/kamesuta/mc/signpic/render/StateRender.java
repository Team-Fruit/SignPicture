package com.kamesuta.mc.signpic.render;

import javax.annotation.Nonnull;

import org.lwjgl.util.Color;

import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.state.Progress;

import net.minecraft.client.gui.FontRenderer;

public class StateRender {
	public static enum LoadingCircle {
		INIT(new Color(0, 255, 255, 255), new Color(160, 160, 160, 255), new Color(120, 120, 120, 255)), DOWNLOAD(new Color(0, 255, 255, 255), new Color(0, 102, 204, 255), new Color(23, 121, 232, 255)), CONTENTLOAD(new Color(0, 255, 255, 255), new Color(0, 144, 55), new Color(23, 177, 55, 255)), DEFAULT(new Color(), new Color(), new Color());

		private final @Nonnull Color loading;
		private final @Nonnull Color progress;
		private final @Nonnull Color design;

		private LoadingCircle(final @Nonnull Color loading, final @Nonnull Color progress, final @Nonnull Color design) {
			this.loading = loading;
			this.progress = progress;
			this.design = design;
		}

		public void loadingColor() {
			OpenGL.glColor(this.loading);
		}

		public void progressColor() {
			OpenGL.glColor(this.progress);
		}

		public void designColor() {
			OpenGL.glColor(this.design);
		}
	}

	public static void drawLoading(final @Nonnull Progress progress, final @Nonnull LoadingCircle type, final @Nonnull LoadingCircleType speed) {
		if (type!=LoadingCircle.DEFAULT) {
			OpenGL.glLineWidth(3f);
			WRenderer.startShape();

			OpenGL.glPushMatrix();
			OpenGL.glScalef(.5f, .5f, 1f);

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

			OpenGL.glPopMatrix();
		}
	}

	public static void drawMessage(final @Nonnull Content content, final @Nonnull FontRenderer fontrenderer) {
		WRenderer.startTexture();
		final float f1 = 0.6666667F;
		float f3 = 0.06666668F*f1;
		OpenGL.glTranslatef(0f, 1f, 0f);
		OpenGL.glPushMatrix();
		OpenGL.glScalef(f3, f3, 1f);
		final String msg1 = content.state.getStateMessage();
		fontrenderer.drawStringWithShadow(msg1, -fontrenderer.getStringWidth(msg1)/2, -fontrenderer.FONT_HEIGHT, 0xffffff);
		OpenGL.glPopMatrix();
		f3 = 0.036666668F*f1;
		OpenGL.glPushMatrix();
		OpenGL.glScalef(f3, f3, 1f);
		final String msg2 = content.id.getID();
		fontrenderer.drawStringWithShadow(msg2, -fontrenderer.getStringWidth(msg2)/2, 0, 0xffffff);
		OpenGL.glPopMatrix();
		final String msg3 = content.state.getMessage();
		OpenGL.glPushMatrix();
		OpenGL.glScalef(f3, f3, 1f);
		fontrenderer.drawStringWithShadow(msg3, -fontrenderer.getStringWidth(msg3)/2, fontrenderer.FONT_HEIGHT, 0xffffff);
		OpenGL.glPopMatrix();
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
