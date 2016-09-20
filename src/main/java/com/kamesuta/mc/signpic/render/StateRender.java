package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.Color;

import com.kamesuta.mc.signpic.entry.content.ContentState.Progress;

public class StateRender {
	public static enum LoadingCircle {
		INIT(new Color(0, 255, 255, 255), new Color(160, 160, 160, 255), new Color(120, 120, 120, 255)),
		DOWNLOAD(new Color(0, 255, 255, 255), new Color(0, 102, 204, 255), new Color(23, 121, 232, 255)),
		CONTENTLOAD(new Color(0, 255, 255, 255), new Color(238, 97, 35, 255), new Color(238, 134, 35, 255)),
		DEFAULT(new Color(), new Color(), new Color())
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
	}


	public static void drawLoading(final Progress progress, final LoadingCircle type, final LoadingCircleType speed) {
		if (type != LoadingCircle.DEFAULT) {
			glLineWidth(3f);
			RenderHelper.startShape();

			glPushMatrix();
			glScalef(.5f, .5f, 1f);

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
