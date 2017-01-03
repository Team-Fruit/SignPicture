package com.kamesuta.mc.signpic.image;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.render.OpenGL;

import net.minecraft.client.renderer.texture.TextureUtil;

public abstract class DynamicImageTexture implements ImageTexture {
	public static final @Nonnull DynamicImageTexture NULL = new DynamicTexture(null, 1, 1);

	protected final int width;
	protected final int height;
	private final @Nonnull SizeData size;
	private int id = -1;

	public DynamicImageTexture(final int width, final int height) {
		this.width = width;
		this.height = height;
		this.size = SizeData.create(width, height);
	}

	public @Nonnull DynamicImageTexture load() {
		if (this.id==-1) {
			this.id = OpenGL.glGenTextures();
			loadDirect();
		}
		return this;
	}

	protected abstract @Nonnull DynamicImageTexture loadDirect();

	@Override
	public abstract boolean hasMipmap();

	@Override
	public @Nonnull SizeData getSize() {
		return this.size;
	}

	public int getId() {
		return this.id;
	}

	@Override
	public void bind() {
		if (this.id!=-1)
			OpenGL.glBindTexture(GL_TEXTURE_2D, this.id);
	}

	public void delete() {
		if (this.id!=-1)
			OpenGL.glDeleteTextures(this.id);
		this.id = -1;
	}

	public static @Nonnull DynamicImageTexture create(final @Nonnull BufferedImage image, final int width, final int height) {
		if (Config.getConfig().renderUseMipmap.get())
			return MipmapDynamicTexture.createFromImage(image, width, height, Client.mc.gameSettings.mipmapLevels);
		else
			return DynamicTexture.createFromImage(image, width, height);
	}

	public static class DynamicTexture extends DynamicImageTexture {
		private @Nullable int[] image;

		private DynamicTexture(final @Nullable int[] image, final int width, final int height) {
			super(width, height);
			this.image = image;
		}

		@Override
		protected @Nonnull DynamicImageTexture loadDirect() {
			if (this.image!=null) {
				final int id = getId();
				TextureUtil.allocateTexture(id, this.width, this.height);
				TextureUtil.uploadTexture(id, this.image, this.width, this.height);
				this.image = null;
			}
			return this;
		}

		@Override
		public boolean hasMipmap() {
			return false;
		}

		@Override
		public void delete() {
			super.delete();
			if (this.image!=null)
				this.image = null;
		}

		public static @Nonnull DynamicTexture createFromRawData(final @Nullable int[] image, final int width, final int height) {
			return new DynamicTexture(image, width, height);
		}

		public static @Nonnull DynamicTexture createFromSizedImage(final @Nonnull BufferedImage sizedImage) {
			final int width = sizedImage.getWidth();
			final int height = sizedImage.getHeight();
			return createFromRawData(sizedImage.getRGB(0, 0, width, height, null, 0, width), width, height);
		}

		public static @Nonnull DynamicTexture createFromImage(final @Nonnull BufferedImage image, final int width, final int height) {
			// return same size
			if (width==image.getWidth()&&height==image.getHeight())
				return createFromSizedImage(image);
			// resize
			final BufferedImage s = new BufferedImage(width, height, image.getType());
			final Graphics2D g = s.createGraphics();
			g.drawImage(image.getScaledInstance(width, height, Config.getConfig().imageResizeFast.get() ? java.awt.Image.SCALE_FAST : java.awt.Image.SCALE_SMOOTH), 0, 0, null);
			g.dispose();
			return createFromSizedImage(s);
		}
	}

	public static class MipmapDynamicTexture extends DynamicImageTexture {
		private @Nullable int[][] mipdata;
		private final int miplevel;

		private MipmapDynamicTexture(final @Nullable int[][] mipdata, final int width, final int height, final int miplevel) {
			super(width, height);
			this.mipdata = mipdata;
			this.miplevel = miplevel;
		}

		@Override
		public @Nonnull DynamicImageTexture loadDirect() {
			if (this.mipdata!=null) {
				TextureUtil.allocateTextureImpl(getId(), this.miplevel, this.width, this.height, Client.mc.gameSettings.anisotropicFiltering);
				TextureUtil.uploadTextureMipmap(this.mipdata, this.width, this.height, 0, 0, false, false);
				this.mipdata = null;
			}
			return this;
		}

		@Override
		public boolean hasMipmap() {
			return true;
		}

		@Override
		public void delete() {
			super.delete();
			if (this.mipdata!=null)
				this.mipdata = null;
		}

		public static @Nonnull MipmapDynamicTexture createFromRawMipmap(final @Nullable int[][] mipdata, final int width, final int height, final int miplevel) {
			return new MipmapDynamicTexture(mipdata, width, height, miplevel);
		}

		public static @Nonnull MipmapDynamicTexture createFromRawData(final @Nonnull int[] image, final int width, final int height, final int miplevel) {
			final int[][] aint = new int[miplevel+1][];
			aint[0] = image;
			final int[][] bint = TextureUtil.generateMipmapData(miplevel, width, aint);
			return new MipmapDynamicTexture(bint, width, height, miplevel);
		}

		public static @Nonnull MipmapDynamicTexture createFromSizedImage(final @Nonnull BufferedImage sizedImage, final int miplevel) {
			final int width = sizedImage.getWidth();
			final int height = sizedImage.getHeight();
			return createFromRawData(sizedImage.getRGB(0, 0, width, height, null, 0, width), width, height, miplevel);
		}

		public static @Nonnull MipmapDynamicTexture createFromImage(final @Nonnull BufferedImage image, final int width, final int height, final int miplevel) {
			// get resized mipmap size unit
			final int unitmip = 2<<miplevel;
			final int nwidth = (int) Math.ceil(width/(double) unitmip)*unitmip;
			final int nheight = (int) Math.ceil(height/(double) unitmip)*unitmip;
			// return same size
			if (nwidth==image.getWidth()&&nheight==image.getHeight())
				return createFromSizedImage(image, miplevel);
			// resize
			final BufferedImage s = new BufferedImage(nwidth, nheight, image.getType());
			final Graphics2D g = s.createGraphics();
			g.drawImage(image.getScaledInstance(nwidth, nheight, Config.getConfig().imageResizeFast.get() ? java.awt.Image.SCALE_FAST : java.awt.Image.SCALE_SMOOTH), 0, 0, null);
			g.dispose();
			return createFromSizedImage(s, miplevel);
		}

		public static @Nonnull MipmapDynamicTexture createFromImage(final @Nonnull BufferedImage image, final int miplevel) {
			return createFromImage(image, image.getWidth(), image.getHeight(), miplevel);
		}
	}
}
