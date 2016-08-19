package com.kamesuta.mc.signpic.image;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;

import net.minecraft.client.renderer.texture.TextureUtil;

public class ImageTexture {
	public static final ImageTexture NULL = new ImageTexture(null);
	public static float DefaultDelay = .05f;

	protected BufferedImage temp;
	protected int id = -1;
	protected float delay;

	public ImageTexture(final BufferedImage image, final float delay) {
		this.temp = image;
		this.delay = delay;
	}

	public ImageTexture(final BufferedImage image) {
		this(image, DefaultDelay);
	}

	public ImageTexture(final float delay) {
		this(null, delay);
	}

	public ImageTexture() {
		this(null, DefaultDelay);
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

	public boolean setImage(final BufferedImage image) {
		if (this.id == -1) {
			this.temp = image;
			return true;
		}
		return false;
	}

	public int getId() {
		return this.id;
	}

	public void setDelay(final float delay) {
		this.delay = delay;
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
