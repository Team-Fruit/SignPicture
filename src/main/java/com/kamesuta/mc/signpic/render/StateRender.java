package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

public class StateRender {
	public static enum Color {
		DOWNLOAD {
			@Override
			public void progressColor() {
				glColor4f(0f/256f, 102f/256f, 204f/256f, 1f);
			}

			@Override
			public void designColor() {
				glColor4f(23f/256f, 121f/256f, 232f/256f, 1f);
			}
		},
		IOLOAD {
			@Override
			public void progressColor() {
				glColor4f(0f/256f, 144f/256f, 55f/256f, 1f);
			}

			@Override
			public void designColor() {
				glColor4f(23f/256f, 177f/256f, 55f/256f, 1f);
			}
		},
		TEXTURELOAD {
			@Override
			public void progressColor() {
				glColor4f(238f/256f, 97f/256f, 35f/256f, 1f);
			}

			@Override
			public void designColor() {
				glColor4f(238f/256f, 134f/256f, 35f/256f, 1f);
			}
		},
		DEFAULT
		;

		public void loadingColor() {
			glColor4f(0.0F, 1.0F, 1.0F, 1.0F);
		}

		public void progressColor() {
			glColor4f(160/256f, 160f/256f, 160f/256f, 1f);
		}

		public void designColor() {
			glColor4f(120/256f, 120f/256f, 120f/256f, 1f);
		}
	}

	public static enum Speed {
		WAIT(627*-2, 893*-2),
		RUN(627, 893),
		;

		public final int inner;
		public final int outer;

		Speed(final int inner, final int outer) {
			this.inner = inner;
			this.outer = outer;
		}
	}
}
