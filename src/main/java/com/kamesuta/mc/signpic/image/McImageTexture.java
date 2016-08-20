package com.kamesuta.mc.signpic.image;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.util.ResourceLocation;

public class McImageTexture implements IImageTexture {
	protected ResourceLocation location;

	public McImageTexture(final ResourceLocation location) {
		this.location = location;
	}

	@Override
	public void bind() {
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(this.location);
	}

	@Override
	public ImageSize getSize() {
		return DefaultSize;
	}
}
