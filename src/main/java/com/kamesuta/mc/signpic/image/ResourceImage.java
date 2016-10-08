package com.kamesuta.mc.signpic.image;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.state.StateType;

import net.minecraft.util.ResourceLocation;

public class ResourceImage extends Image {
	protected ResourceLocation resource;
	protected ResourceImageTexture texture;

	public ResourceImage(final Content content) {
		super(content);
		this.resource = content.id.getResource();
		this.texture = new ResourceImageTexture(this.resource);
	}

	@Override
	public String toString() {
		return String.format("ResourceImage[%s]", this.content.id);
	}

	@Override
	public String getLocal() {
		return "Resource:" + StringUtils.substring(this.content.id.id(), 1);
	}

	@Override
	public IImageTexture getTexture() {
		return this.texture;
	}

	@Override
	public void onInit() {
		this.content.state.setType(StateType.AVAILABLE);
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
