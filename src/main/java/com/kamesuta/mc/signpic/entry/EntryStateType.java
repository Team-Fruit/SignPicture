package com.kamesuta.mc.signpic.entry;

public enum EntryStateType {
	INIT("signpic.state.init"),
	INITALIZED("signpic.state.initalized"),
	LOADING("signpic.state.loading"),
	LOADED("signpic.state.loaded"),
	AVAILABLE("signpic.state.available"),
	ERROR("signpic.state.error"),
	;

	public final String msg;
	EntryStateType(final String s) {
		this.msg = s;
	}
}
