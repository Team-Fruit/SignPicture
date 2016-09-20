package com.kamesuta.mc.signpic.image;

import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.entry.content.ContentLocation;
import com.kamesuta.mc.signpic.entry.content.ContentState;

import net.minecraft.util.ResourceLocation;

public class ResourceImage extends Image {
	protected final ContentLocation location;
	protected final ContentState state;
	protected ResourceLocation resource;
	protected McImageTexture texture;

	public ResourceImage(final ContentLocation location, final ContentId id, final ContentState state) {
		super(id);
		this.location = location;
		this.state = state;
		this.resource = id.getResource();
		this.texture = new McImageTexture(this.resource);
	}

	@Override
	public String toString() {
		return String.format("ResourceImage[%s]", this.id);
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
