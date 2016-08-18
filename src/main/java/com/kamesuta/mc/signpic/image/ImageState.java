package com.kamesuta.mc.signpic.image;

public enum ImageState {
	INIT("signpic.state.init"),
	AVAILABLE("signpic.state.available"),
	LOADING("signpic.state.loading"),
	DOWNLOADING("signpic.state.downloading"),
	FAILED("signpic.state.failed"),
	ERROR("signpic.state.error");

	String msg;
	ImageState(final String s) {
		this.msg = s;
	}
}
