package com.kamesuta.mc.signpic.placer;

import com.kamesuta.mc.signpic.image.ImageSize;
import com.kamesuta.mc.signpic.util.Sign;

public class PlacerMode {
	public static final PlacerMode instance = new PlacerMode();

	private PlacerMode() {
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

	public static enum Mode {
		PLACE("signpic.over.picturemode.place"),
		COPY("signpic.over.picturemode.copy"),
		NONE("signpic.over.picturemode.none"),
		;

		public final String message;
		private Mode(final String message) {
			this.message = message;
		}
	}
}
