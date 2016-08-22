package com.kamesuta.mc.signpic.placer;

public class PlacerMode {
	public static final PlacerMode INSTANCE = new PlacerMode();

	private PlacerMode() {
	}

	protected String[] signtext;

	public void enable(final String[] signtext) {
		this.signtext = signtext;
	}

	public void disable() {
		this.signtext = null;
	}

	public boolean isEnabled() {
		return this.signtext!=null;
	}

	public String[] getSignText() {
		return this.signtext;
	}
}
