package com.kamesuta.mc.signpic.image;

import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.entry.content.ContentLocation;
import com.kamesuta.mc.signpic.entry.content.ContentState;
import com.kamesuta.mc.signpic.entry.content.ContentStateType;

import net.minecraft.util.ResourceLocation;

public class ResourceImage extends Image {
	protected ResourceLocation resource;
	protected ResourceImageTexture texture;

	public ResourceImage(final ContentLocation location, final ContentId id, final ContentState state) {
		super(location, id, state);
		this.resource = id.getResource();
		this.texture = new ResourceImageTexture(this.resource);
		state.setType(ContentStateType.AVAILABLE);
		this.isAvailable = true;
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
