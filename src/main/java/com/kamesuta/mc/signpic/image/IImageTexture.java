package com.kamesuta.mc.signpic.image;

public interface IImageTexture {

	public static final ImageSize DefaultSize = new ImageSize(1, 1);

	void bind();

	ImageSize getSize();

}