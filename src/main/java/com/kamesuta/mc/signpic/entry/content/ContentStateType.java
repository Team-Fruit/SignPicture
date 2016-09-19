package com.kamesuta.mc.signpic.entry.content;

public enum ContentStateType {
	INIT("signpic.state.init"),
	INITALIZED("signpic.state.initalized"),
	LOADING("signpic.state.loading"),
	LOADED("signpic.state.loaded"),
	AVAILABLE("signpic.state.available"),
	ERROR("signpic.state.error"),
	;

	public final String msg;
	ContentStateType(final String s) {
		this.msg = s;
	}
}
