package com.kamesuta.mc.signpic.image;

import com.kamesuta.mc.signpic.entry.content.ContentId;

import net.minecraft.util.ResourceLocation;

public class ResourceImage extends Image {
	protected McImageTexture texture;
	protected ResourceLocation location;

	public ResourceImage(final ContentId path) {
		super(path);
		this.location = path.getResource();
		this.texture = new McImageTexture(this.location);
	}

	@Override
	public String toString() {
		return String.format("ResourceImage[%s]", this.path);
	}

	@Override
	public String getLocal() {
		return "Resource:" + this.location;
	}

	@Override
	public IImageTexture getTexture() {
		return this.texture;
	}

	@Override
	public void onAsyncProcess() {
	}

	@Override
	public boolean onDivisionProcess() {
		return true;
	}

	@Override
	public void onCollect() {
	}
}
