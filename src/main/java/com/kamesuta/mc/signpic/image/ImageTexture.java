package com.kamesuta.mc.signpic.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class ImageTexture {

	protected int glTextureId = -1;

	public ImageTexture(final BufferedImage image) {
		TextureUtil.allocateTexture(getGlTextureId(), image.getWidth(), image.getHeight());
		TextureUtil.uploadTextureImage(getGlTextureId(), image);
	}

	public ImageTexture(final int width, final int height, final int[] data) {
		TextureUtil.allocateTexture(getGlTextureId(), width, height);
		TextureUtil.uploadTexture(getGlTextureId(), data, width, height);
	}

	public ImageTexture(final File file) throws IOException {
		this(ImageIO.read(file));
	}

	public ImageTexture(final InputStream in) throws IOException {
		this(ImageIO.read(in));
	}

	public ImageTexture(final IResourceManager manager, final ResourceLocation location) throws IOException {
		this(ImageIO.read(manager.getResource(location).getInputStream()));
	}

	public ImageTexture(final ResourceLocation location) throws IOException {
		this(FMLClientHandler.instance().getClient().getResourceManager(), location);
	}

	public int getGlTextureId() {
		if (this.glTextureId == -1) {
			this.glTextureId = TextureUtil.glGenTextures();
		}

		return this.glTextureId;
	}

	public void deleteGlTexture() {
		if (this.glTextureId != -1) {
			TextureUtil.deleteTexture(this.glTextureId);
			this.glTextureId = -1;
		}
	}
}