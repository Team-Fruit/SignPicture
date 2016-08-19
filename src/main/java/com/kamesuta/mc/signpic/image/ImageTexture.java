package com.kamesuta.mc.signpic.image;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;

import net.minecraft.client.renderer.texture.TextureUtil;

public class ImageTexture {
	public static final ImageTexture NULL = new ImageTexture(null);
	public static float DefaultDelay = .05f;

	protected BufferedImage temp;
	protected int id = -1;
	protected final float delay;

	public ImageTexture(final BufferedImage image, final float delay) {
		this.temp = image;
		this.delay = delay;
	}

	public ImageTexture(final BufferedImage image) {
		this(image, DefaultDelay);
	}

	public ImageTexture load() {
		if (this.id == -1 && this.temp != null) {
			this.id = TextureUtil.glGenTextures();
			TextureUtil.allocateTexture(this.id, this.temp.getWidth(), this.temp.getHeight());
			TextureUtil.uploadTextureImage(this.id, this.temp);
			this.temp = null;
		}
		return this;
	}

	public int getId() {
		return this.id;
	}

	public float getDelay() {
		return this.delay;
	}

	public void bind() {
		if (this.id != -1)
			glBindTexture(GL_TEXTURE_2D, this.id);
	}

	public void delete() {
		if (this.id != -1)
			TextureUtil.deleteTexture(this.id);
	}
}
