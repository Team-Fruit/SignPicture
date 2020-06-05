package net.teamfruit.signpic.image;

import javax.annotation.Nonnull;

import net.teamfruit.signpic.attr.prop.SizeData;

public interface ImageTexture {

	void bind();

	@Nonnull
	SizeData getSize();

	boolean hasMipmap();

}