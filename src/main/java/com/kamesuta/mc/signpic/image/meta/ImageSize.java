package com.kamesuta.mc.signpic.image.meta;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.position.Area;

public class ImageSize extends ImageMeta.MetaParser implements Cloneable {
	public float width = SizeData.Unknown;
	public float height = SizeData.Unknown;

	public SizeData get() {
		return new SizeData(this.width, this.height);
	}

	public ImageSize setSize(final float width, final float height) {
		this.width = width;
		this.height = height;
		return this;
	}

	public ImageSize setWidth(final float width) {
		this.width = width;
		return this;
	}

	public ImageSize setHeight(final float height) {
		this.height = height;
		return this;
	}

	public ImageSize setWidth(final String width) {
		this.width = NumberUtils.toFloat(width, SizeData.Unknown);
		return this;
	}

	public ImageSize setHeight(final String height) {
		this.height = NumberUtils.toFloat(height, SizeData.Unknown);
		return this;
	}

	public ImageSize setSize(final String width, final String height) {
		return setWidth(width).setHeight(height);
	}

	public ImageSize setSize(final Area a) {
		return setSize(a.w(), a.h());
	}

	public ImageSize setSize(final SizeData size) {
		return setSize(size.getWidth(), size.getHeight());
	}

	public ImageSize setSize(final ImageSize size) {
		return setSize(size.width, size.height);
	}

	@Override
	public ImageSize reset() {
		this.width = SizeData.Unknown;
		this.height = SizeData.Unknown;
		return this;
	}

	@Override
	public boolean parse(final String src, final String key, final String value) {
		if (StringUtils.equals(key, ""))
			this.width = NumberUtils.toFloat(value, SizeData.Unknown);
		else if (StringUtils.equals(key, "x"))
			this.height = NumberUtils.toFloat(value, SizeData.Unknown);
		else
			return false;
		return true;
	}

	@Override
	public String compose() {
		return get().compose();
	}

	@Override
	public String toString() {
		return compose();
	}

	@Override
	public ImageSize clone() {
		try {
			return (ImageSize) super.clone();
		} catch (final Exception e) {
			return new ImageSize().setSize(this);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime*result+Float.floatToIntBits(this.height);
		result = prime*result+Float.floatToIntBits(this.width);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this==obj)
			return true;
		if (obj==null)
			return false;
		if (!(obj instanceof ImageSize))
			return false;
		final ImageSize other = (ImageSize) obj;
		if (Float.floatToIntBits(this.height)!=Float.floatToIntBits(other.height))
			return false;
		if (Float.floatToIntBits(this.width)!=Float.floatToIntBits(other.width))
			return false;
		return true;
	}
}
