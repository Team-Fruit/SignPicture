package com.kamesuta.mc.signpic.http.shortening;

import javax.annotation.Nonnull;

import com.kamesuta.mc.signpic.state.State;

public class ShorteningRequest {
	protected final @Nonnull String name;
	protected final @Nonnull String longurl;
	protected final @Nonnull State state;

	public ShorteningRequest(final @Nonnull String name, final @Nonnull String longurl, final @Nonnull State state) {
		this.name = name;
		this.longurl = longurl;
		this.state = state;
	}

	public @Nonnull State getState(final @Nonnull String format) {
		final State state = getPendingState();
		state.setName(String.format(format, getName()));
		state.getProgress().setOverall(0);
		return state;
	}

	public @Nonnull State getPendingState() {
		return this.state;
	}

	public @Nonnull String getName() {
		return this.name;
	}

	public @Nonnull String getLongURL() {
		return this.longurl;
	}
}
