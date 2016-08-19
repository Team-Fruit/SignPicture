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

	public String getId() {
		return this.id;
	}

	public ImageTextures getTexture() {
		if (this.state == ImageState.AVAILABLE)
			return this.texture;
		else
			throw new IllegalStateException("Not Available");
	}

	public ImageState getState() {
		return this.state;
	}

	public abstract float getProgress();

	public abstract String getStatusMessage();

	public abstract String getLocal();

	public abstract String advMessage();

	public abstract void process();

	public boolean processTexture() {
		return true;
	}
}