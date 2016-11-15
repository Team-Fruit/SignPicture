package com.kamesuta.mc.signpic.image;

import com.kamesuta.mc.signpic.image.meta.ImageSize;

public interface IImageTexture {

	void bind();

	ImageSize getSize();

	boolean hasMipmap();

}