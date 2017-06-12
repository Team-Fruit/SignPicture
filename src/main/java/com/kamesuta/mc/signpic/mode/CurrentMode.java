package com.kamesuta.mc.signpic.mode;

import java.util.EnumSet;

import javax.annotation.Nonnull;

import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.util.Sign;

public class CurrentMode {
	public static final @Nonnull CurrentMode instance = new CurrentMode();

	private CurrentMode() {
	}

	private boolean isShortening;
	private @Nonnull EntryId handSign = EntryId.from("#{}");
	private @Nonnull EntryId entryId = EntryId.from("#{}");
	private @Nonnull Mode mode = Mode.NONE;
	private final @Nonnull EnumSet<State> states = EnumSet.noneOf(State.class);

	public void setShortening(final boolean isShortening) {
		this.isShortening = isShortening;
	}

	public boolean isShortening() {
		return this.isShortening;
	}

	public void setMode(final @Nonnull Mode mode) {
		this.mode = mode;
	}

	public void setMode() {
		this.mode = Mode.NONE;
	}

	public boolean isMode() {
		return getMode()!=Mode.NONE;
	}

	public boolean isMode(final @Nonnull Mode mode) {
		return getMode()==mode;
	}

	public @Nonnull Mode getMode() {
		return this.mode;
	}

	public void setState(final @Nonnull State state, final boolean enable) {
		if (enable)
			this.states.add(state);
		else
			this.states.remove(state);
	}

	public boolean isState() {
		return !this.states.isEmpty();
	}

	public boolean isState(final @Nonnull State state) {
		return this.states.contains(state);
	}

	public void toggleState(final @Nonnull State state) {
		setState(state, !isState(state));
	}

	public void setEntryId(final @Nonnull EntryId sign) {
		this.entryId = sign;
		Sign.updatePreview(sign);
	}

	public @Nonnull EntryId getEntryId() {
		return this.entryId;
	}

	public void setHandSign(final @Nonnull EntryId sign) {
		this.handSign = sign;
	}

	public @Nonnull EntryId getHandSign() {
		return this.handSign;
	}

	public static enum Mode {
		PLACE("signpic.over.mode.place"),
		OPTION("signpic.over.mode.option"),
		SETPREVIEW("signpic.over.mode.setpreview"),
		NONE("signpic.over.mode.none"),
		;

		public final @Nonnull String message;

		private Mode(final @Nonnull String message) {
			this.message = message;
		}
	}

	public static enum State {
		CONTINUE,
		SEE,
		PREVIEW,
		HIDE,
		;
	}
}
