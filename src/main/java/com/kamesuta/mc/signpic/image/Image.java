package com.kamesuta.mc.signpic.image;

public interface Image {

	void preload();

	void load();

	float getProgress();

	String getStatusMessage();

	ImageState getState();

	ImageTexture getTexture();

	String getId();

	String getLocal();

	String advMessage();

}