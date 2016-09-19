package com.kamesuta.mc.signpic.image;

import com.kamesuta.mc.signpic.entry.EntryId;

import net.minecraft.util.ResourceLocation;

public class ResourceImage extends Image {
	protected McImageTexture texture;
	protected ResourceLocation location;

	public ResourceImage(final ResourceLocation location) {
		super(new EntryId("!" + location.toString()));
		this.location = location;
		this.texture = new McImageTexture(location);
		this.state = ImageState.AVAILABLE;
	}

	@Override
	public void process() {
	}

	@Override
	public float getProgress() {
		return 1f;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RemoteImage))
			return false;
		final Image other = (Image) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
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
}
