package com.kamesuta.mc.signpic.entry;

public enum EntryState {
	INIT("signpic.state.init"),
	LOADING("signpic.state.loading"),
	AVAILABLE("signpic.state.available"),
	;

	public final String msg;
	EntryState(final String s) {
		this.msg = s;
	}
}
