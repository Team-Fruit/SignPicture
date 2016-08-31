package com.kamesuta.mc.signpic.image;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

public class McImageTexture implements IImageTexture {
	protected static final Minecraft mc = FMLClientHandler.instance().getClient();
	protected static final ResourceLocation Null = mc.renderEngine.getDynamicTextureLocation("null", TextureUtil.missingTexture);
	protected ResourceLocation location;

	public McImageTexture(final ResourceLocation location) {
		this.location = location;
	}

	@Override
	public void bind() {
		if (!this.location.getResourcePath().isEmpty())
			mc.renderEngine.bindTexture(this.location);
		else {
			mc.renderEngine.bindTexture(Null);
		}
	}

	@Override
	public ImageSize getSize() {
		return DefaultSize;
	}
}
