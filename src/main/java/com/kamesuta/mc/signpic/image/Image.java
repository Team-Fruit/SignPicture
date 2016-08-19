package com.kamesuta.mc.signpic.image;

public abstract class Image {
	protected String id;
	protected ImageTextures texture;
	protected ImageState state = ImageState.INIT;
	protected ImageLocation location;
	protected String advmsg;

	public Image(final String id) {
		this.id = id;
	}

	public abstract void init();

	public abstract void preload();

	public abstract float getProgress();

	public abstract String getStatusMessage();

	public abstract ImageState getState();

	public abstract ImageTextures getTexture();

	public abstract String getId();

	public abstract String getLocal();

	public abstract String advMessage();

	public abstract void load();

}