package net.teamfruit.signpic.image;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.util.ResourceLocation;
import net.teamfruit.signpic.entry.content.Content;
import net.teamfruit.signpic.state.StateType;

public class ResourceImage extends Image {
	protected @Nonnull ResourceLocation resource;
	protected @Nonnull ResourceImageTexture texture;

	public ResourceImage(final @Nonnull Content content) {
		super(content);
		this.resource = content.id.getResource();
		this.texture = new ResourceImageTexture(this.resource);
	}

	@Override
	public @Nonnull String toString() {
		return String.format("ResourceImage[%s]", this.content.id);
	}

	@Override
	public @Nonnull String getLocal() {
		return "Resource:"+StringUtils.substring(this.content.id.getID(), 1);
	}

	@Override
	public @Nonnull ImageTexture getTexture() {
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

	@Override
	public void cancel() {
	}
}
