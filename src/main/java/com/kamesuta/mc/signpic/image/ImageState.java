package com.kamesuta.mc.signpic.image;

public enum ImageState {
	INIT("signpic.state.init"),
	DOWNLOADING("signpic.state.downloading"),
	LOADING("signpic.state.loading"),
	IOLOADING("signpic.state.ioloading"),
	POSTLOADING("signpic.state.postloading"),
	AVAILABLE("signpic.state.available"),
	FAILED("signpic.state.failed"),
	ERROR("signpic.state.error");

	String msg;
	ImageState(final String s) {
		this.msg = s;
	}
}
