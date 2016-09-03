package com.kamesuta.mc.signpic.mode;

import com.kamesuta.mc.signpic.image.ImageSize;
import com.kamesuta.mc.signpic.util.Sign;

public class CurrentMode {
	public static final CurrentMode instance = new CurrentMode();

	private CurrentMode() {
	}

	protected Sign sign = new Sign().setSize(ImageSize.UnknownSize);
	protected Mode mode = Mode.NONE;
	protected boolean continueEnable;

	public void setMode(final Mode mode) {
		this.mode = mode;
	}

	public void setMode() {
		this.mode = Mode.NONE;
	}

	public void setContinue(final boolean continueMode) {
		this.continueEnable = continueMode;
	}

	public void setSign(final Sign sign) {
		this.sign = sign;
	}

	public boolean isMode() {
		return getMode() != Mode.NONE;
	}

	public boolean isMode(final Mode mode) {
		return getMode() == mode;
	}

	public Mode getMode() {
		return this.mode;
	}

	public boolean isContinue() {
		return this.continueEnable;
	}

	public Sign getSign() {
		return this.sign;
	}
}
