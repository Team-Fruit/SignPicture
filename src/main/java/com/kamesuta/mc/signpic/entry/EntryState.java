package com.kamesuta.mc.signpic.entry;

public enum EntryState {
	INIT("signpic.state.init"),
	INITALIZED("signpic.state.initalized"),
	LOADING("signpic.state.loading"),
	LOADED("signpic.state.loaded"),
	AVAILABLE("signpic.state.available"),
	ERROR("signpic.state.error"),
	;

	public final String msg;
	EntryState(final String s) {
		this.msg = s;
	}
}
