package com.kamesuta.mc.signpic.image;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.render.OpenGL;

import net.minecraft.client.renderer.texture.TextureUtil;

public class DynamicImageTexture implements ImageTexture {
	public static final DynamicImageTexture NULL = new DynamicImageTexture(null);
	public static float DefaultDelay = .05f;

	protected BufferedImage temp;
	protected ImageSize size;
	protected int id = -1;
	protected float delay;

	public DynamicImageTexture(final BufferedImage image, final float delay) {
		setImage(image);
		this.delay = delay;
	}

	public DynamicImageTexture(final BufferedImage image) {
		this(image, DefaultDelay);
	}

	public DynamicImageTexture(final float delay) {
		this(null, delay);
	}

	public DynamicImageTexture() {
		this(null, DefaultDelay);
	}

	private boolean hasMipmap;

	public DynamicImageTexture load() {
		if (this.id==-1&&this.temp!=null) {
			// generate id
			this.id = OpenGL.glGenTextures();
			// get size
			final int width = this.temp.getWidth();
			final int height = this.temp.getHeight();
			if (OpenGL.openGl30()&&Config.instance.renderUseMipmap.get()) {
				final int lvmip = Client.mc.gameSettings.mipmapLevels;
				final int unitmip = 2<<lvmip;
				// get resized mipmap size unit
				final int nwidth = (int) Math.ceil(width/(double) unitmip)*unitmip;
				final int nheight = (int) Math.ceil(height/(double) unitmip)*unitmip;
				// resize
				final BufferedImage s = new BufferedImage(nwidth, nheight, this.temp.getType());
				final Graphics2D g = s.createGraphics();
				g.drawImage(this.temp.getScaledInstance(nwidth, nheight, java.awt.Image.SCALE_FAST), 0, 0, null);
				g.dispose();
				// generate mipmap
				final int[][] aint = new int[lvmip+1][];
				aint[0] = s.getRGB(0, 0, nwidth, nheight, null, 0, nwidth);
				final int[][] bint = TextureUtil.generateMipmapData(lvmip, nwidth, aint);
				// apply
				TextureUtil.allocateTextureImpl(this.id, lvmip, nwidth, nheight);
				TextureUtil.uploadTextureMipmap(bint, nwidth, nheight, 0, 0, false, false);
				this.hasMipmap = true;
			} else {
				// apply
				TextureUtil.allocateTexture(this.id, width, height);
				TextureUtil.uploadTextureImage(this.id, this.temp);
			}
			this.temp = null;
		}
		return this;
	}

	@Override
	public boolean hasMipmap() {
		return this.hasMipmap;
	}

	@Override
	public ImageSize getSize() {
		return this.size;
	}

	public boolean setImage(final BufferedImage image) {
		if (this.id==-1) {
			this.temp = image;
			if (image!=null)
				this.size = new ImageSize().setSize(image.getWidth(), image.getHeight());
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

	@Override
	public void bind() {
		if (this.id!=-1)
			OpenGL.glBindTexture(GL_TEXTURE_2D, this.id);
	}

	public void delete() {
		if (this.id!=-1)
			OpenGL.glDeleteTextures(this.id);
		if (this.temp!=null)
			this.temp = null;
	}
}
