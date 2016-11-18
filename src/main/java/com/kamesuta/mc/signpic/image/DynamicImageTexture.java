package com.kamesuta.mc.signpic.image;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;

import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;

import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.image.meta.ImageSize;

import net.minecraft.client.renderer.GlStateManager;
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

	private static ContextCapabilities capabilities;

	public static boolean openGl30() {
		return capabilities!=null&&capabilities.OpenGL30;
	}

	private boolean hasMipmap;

	public DynamicImageTexture load() {
		if (this.id==-1&&this.temp!=null) {
			this.id = TextureUtil.glGenTextures();
			TextureUtil.allocateTexture(this.id, this.temp.getWidth(), this.temp.getHeight());
			TextureUtil.uploadTextureImage(this.id, this.temp);
			if (capabilities==null)
				capabilities = GLContext.getCapabilities();
			if (openGl30()&&Config.instance.renderUseMipmap) {
				GL30.glGenerateMipmap(GL_TEXTURE_2D);
				this.hasMipmap = true;
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
			GlStateManager.bindTexture(this.id);
	}

	public void delete() {
		if (this.id!=-1)
			TextureUtil.deleteTexture(this.id);
		if (this.temp!=null)
			this.temp = null;
	}
}
