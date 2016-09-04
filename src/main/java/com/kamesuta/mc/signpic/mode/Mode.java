package com.kamesuta.mc.signpic.mode;

public enum Mode {
	PLACE("signpic.over.mode.place"),
	LOAD("signpic.over.mode.load"),
	NONE("signpic.over.mode.none"),
	;

	public final String message;
	private Mode(final String message) {
		this.message = message;
	}
}