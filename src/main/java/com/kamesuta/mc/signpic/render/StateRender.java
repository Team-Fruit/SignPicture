package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.Color;

import com.kamesuta.mc.signpic.entry.content.Content;

public class StateRender {
	public static enum LoadingCircle {
		INIT(new Color(0, 255, 255, 255), new Color(160, 160, 160, 255), new Color(120, 120, 120, 255)),
		DOWNLOAD(new Color(0, 255, 255, 255), new Color(0, 102, 204, 255), new Color(23, 121, 232, 255)),
		CONTENTLOAD(new Color(0, 255, 255, 255), new Color(238, 97, 35, 255), new Color(238, 134, 35, 255)),
		DEFAULT(new Color(), new Color(), new Color()) {
			@Override
			public void drawLoading(final Content content, final LoadingCircleType speed) {
			}
		}
		;

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
			glColor4b(color.getRedByte(), color.getGreenByte(), color.getBlueByte(), color.getAlphaByte());
		}

		public void drawLoading(final Content content, final LoadingCircleType speed) {
			glLineWidth(3f);
			RenderHelper.startShape();

			glPushMatrix();
			glScalef(.5f, .5f, 1f);

			// Loading Circle
			loadingColor();
			RenderHelper.drawLoadingCircle(speed.inner, speed.outer);

			// Design Circle
			designColor();
			RenderHelper.drawDesignCircle();

			// Progress Circle
			progressColor();
			final float progress = content.state.progress.getProgress();
			RenderHelper.drawProgressCircle(progress);

			glPopMatrix();
		}
	}

	public static enum LoadingCircleType {
		WAIT(627*-2, 893*-2),
		RUN(627, 893),
		DEFAULT(-1, -1),
		;

		public final int inner;
		public final int outer;

		LoadingCircleType(final int inner, final int outer) {
			this.inner = inner;
			this.outer = outer;
		}
	}
}
