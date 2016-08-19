package com.kamesuta.mc.signpic.image;

public class ImageSize {
	public final float width;
	public final float height;

	public ImageSize(final float width, final float height) {
		this.width = width;
		this.height = height;
	}

	public static ImageSize createSize(final ImageSizes s, final float rawWidth, final float rawHeight, final float maxWidth, final float maxHeight) {
		return s.size(rawWidth, rawHeight, maxWidth, maxHeight);
	}

	public static ImageSize createSize(final ImageSizes s, final ImageSize raw, final float maxWidth, final float maxHeight) {
		if (raw == null) return new ImageSize(maxWidth, maxHeight);
		return createSize(s, raw.width, raw.height, maxWidth, maxHeight);
	}

	public static ImageSize createSize(final ImageSizes s, final float rawWidth, final float rawHeight, final ImageSize raw, final ImageSize max) {
		if (max == null) return new ImageSize(rawWidth, rawHeight);
		return createSize(s, rawWidth, rawHeight, max.width, max.height);
	}

	public static ImageSize createSize(final ImageSizes s, final ImageSize raw, final ImageSize max) {
		if (raw == null && max == null) throw new IllegalArgumentException("No Size Defined");
		else if (raw == null) return max;
		else if (max == null) return raw;
		return createSize(s, raw.width, raw.height, max.width, max.height);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(this.height);
		result = prime * result + Float.floatToIntBits(this.width);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ImageSize))
			return false;
		final ImageSize other = (ImageSize) obj;
		if (Float.floatToIntBits(this.height) != Float.floatToIntBits(other.height))
			return false;
		if (Float.floatToIntBits(this.width) != Float.floatToIntBits(other.width))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("ImageSize[width:%s, height:%s]", this.width, this.height);
	}
}
