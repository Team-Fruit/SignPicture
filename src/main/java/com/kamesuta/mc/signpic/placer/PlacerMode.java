package com.kamesuta.mc.signpic.placer;

import com.kamesuta.mc.signpic.util.Sign;

public class PlacerMode {
	public static final PlacerMode instance = new PlacerMode();

	private PlacerMode() {
	}

	protected Sign sign;

	public void enable(final Sign sign) {
		this.sign = sign;
	}

	public void disable() {
		this.sign = null;
	}

	public boolean isEnabled() {
		return this.sign!=null;
	}

	public Sign getSign() {
		return this.sign;
	}
}
