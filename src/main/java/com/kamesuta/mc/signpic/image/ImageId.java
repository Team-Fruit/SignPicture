package com.kamesuta.mc.signpic.image;

public class ImageId {
	private final String id;

	public ImageId(final String id) {
		this.id = id;
	}

	public String id() {
		return this.id;
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
		if (!(obj instanceof ImageId))
			return false;
		final ImageId other = (ImageId) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("ImageId [id=%s]", this.id);
	}
}
