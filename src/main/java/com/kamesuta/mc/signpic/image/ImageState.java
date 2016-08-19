package com.kamesuta.mc.signpic.image;

public enum ImageState {
	INIT("signpic.state.init"),
	INITALIZED("signpic.state.init"),
	DOWNLOAD("signpic.state.downloading"),
	DOWNLOADING("signpic.state.downloading"),
	DOWNLOADED("signpic.state.downloading"),
	IOLOAD("signpic.state.ioloading"),
	IOLOADING("signpic.state.ioloading"),
	IOLOADED("signpic.state.ioloading"),
	AVAILABLE("signpic.state.available"),
	FAILED("signpic.state.failed"),
	ERROR("signpic.state.error");

	String msg;
	ImageState(final String s) {
		this.msg = s;
	}
}
