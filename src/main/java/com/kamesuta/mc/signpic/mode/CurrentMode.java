package com.kamesuta.mc.signpic.mode;

import java.util.EnumSet;

import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.util.Sign;

public class CurrentMode {
	public static final CurrentMode instance = new CurrentMode();

	private CurrentMode() {
	}

	private EntryId handSign = EntryId.blank;
	private EntryId entryId = EntryId.blank;
	private Mode mode = Mode.NONE;
	private final EnumSet<State> states = EnumSet.noneOf(State.class);

	public void setMode(final Mode mode) {
		this.mode = mode;
	}

	public void setMode() {
		this.mode = Mode.NONE;
	}

	public boolean isMode() {
		return getMode()!=Mode.NONE;
	}

	public boolean isMode(final Mode mode) {
		return getMode()==mode;
	}

	public Mode getMode() {
		return this.mode;
	}

	public void setState(final State state, final boolean enable) {
		if (enable)
			this.states.add(state);
		else
			this.states.remove(state);
	}

	public boolean isState() {
		return !this.states.isEmpty();
	}

	public boolean isState(final State state) {
		return this.states.contains(state);
	}

	public void setEntryId(final EntryId sign) {
		this.entryId = sign;
		Sign.updatePreview(sign);
	}

	public EntryId getEntryId() {
		return this.entryId;
	}

	public void setHandSign(final EntryId sign) {
		this.handSign = sign;
	}

	public EntryId getHandSign() {
		return this.handSign;
	}

	public static enum Mode {
		PLACE("signpic.over.mode.place"), LOAD("signpic.over.mode.load"), SETPREVIEW("signpic.over.mode.setpreview"), NONE("signpic.over.mode.none"),
		;

		public final String message;

		private Mode(final String message) {
			this.message = message;
		}
	}

	public static enum State {
		CONTINUE, SEE, PREVIEW, LOAD_CONTENT, LOAD_META,
		;
	}
}
