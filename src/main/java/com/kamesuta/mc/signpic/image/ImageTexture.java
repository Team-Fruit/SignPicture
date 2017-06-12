package com.kamesuta.mc.signpic.image;

import javax.annotation.Nonnull;

import com.kamesuta.mc.signpic.attr.prop.SizeData;

public interface ImageTexture {

	void bind();

	@Nonnull
	SizeData getSize();

	boolean hasMipmap();

}