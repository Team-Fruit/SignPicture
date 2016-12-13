package com.kamesuta.mc.signpic.http.shortening;

import com.kamesuta.mc.signpic.state.State;

public class ShorteningRequest {
	protected final String name;
	protected final String longurl;
	protected final State state;

	public ShorteningRequest(final String name, final String longurl, final State state) {
		this.name = name;
		this.longurl = longurl;
		this.state = state;
	}

	public State getState(final String format) {
		final State state = getPendingState();
		state.setName(String.format(format, getName()));
		state.getProgress().setOverall(0);
		return state;
	}

	public State getPendingState() {
		return this.state;
	}

	public String getName() {
		return this.name;
	}

	public String getLongURL() {
		return this.longurl;
	}
}
