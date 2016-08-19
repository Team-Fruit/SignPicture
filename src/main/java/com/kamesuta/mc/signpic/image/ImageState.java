package com.kamesuta.mc.signpic.image;

import static org.lwjgl.opengl.GL11.*;

public enum ImageState {
	INIT("signpic.state.init"),
	INITALIZED("signpic.state.initalized"),
	DOWNLOADING("signpic.state.downloading") {
		@Override
		public void themeColor() {
			glColor4f(0f/256f, 102f/256f, 204f/256f, 1f);
		}
	},
	DOWNLOADED("signpic.state.downloaded"),
	IOLOADING("signpic.state.ioloading") {
		@Override
		public void themeColor() {
			glColor4f(0f/256f, 144f/256f, 55f/256f, 1f);
		}
	},
	IOLOADED("signpic.state.ioloaded"),
	TEXTURELOADING("signpic.state.textureloading") {
		@Override
		public void themeColor() {
			glColor4f(238f/256f, 97f/256f, 35f/256f, 1f);
		}
	},
	TEXTURELOADED("signpic.state.textureloaded"),
	AVAILABLE("signpic.state.available"),
	FAILED("signpic.state.failed"),
	ERROR("signpic.state.error"),
	;

	public final String msg;
	ImageState(final String s) {
		this.msg = s;
	}

	public void themeColor() {}
}
