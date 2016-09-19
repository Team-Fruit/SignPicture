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
		result = prime * result + ((this.path == null) ? 0 : this.path.hashCode());
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
		if (this.path == null) {
			if (other.path != null)
				return false;
		} else if (!this.path.equals(other.path))
			return false;
		return true;
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
}
