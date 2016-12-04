package com.kamesuta.mc.signpic.image.meta;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;
import com.kamesuta.mc.bnnwidget.position.Area;

public class ImageSize extends ImageMeta.MetaParser implements Cloneable {
	@Deprecated
	public static final float defaultSize = 1f;
	@Deprecated
	public static final float unknownSize = Float.NaN;

	public float width = unknownSize;
	public float height = unknownSize;

	public SizeData get() {
		return new SizeData(this.width, this.height);
	}

	public ImageSize defaultSize() {
		return setSize(defaultSize, defaultSize);
	}

	public ImageSize unknownSize() {
		return setSize(unknownSize, unknownSize);
	}

	public ImageSize setSize(final float width, final float height) {
		this.width = width;
		this.height = height;
		return this;
	}

	public ImageSize setWidth(final String width) {
		this.width = NumberUtils.toFloat(width, unknownSize);
		return this;
	}

	public ImageSize setHeight(final String height) {
		this.height = NumberUtils.toFloat(height, unknownSize);
		return this;
	}

	public ImageSize setSize(final String width, final String height) {
		return setWidth(width).setHeight(height);
	}

	public ImageSize setArea(final Area a) {
		return setSize(a.w(), a.h());
	}

	public ImageSize setImageSize(final ImageSize size) {
		return setSize(size.width, size.height);
	}

	@Override
	public ImageSize reset() {
		return unknownSize();
	}

	@Override
	public boolean parse(final String src, final String key, final String value) {
		if (StringUtils.equals(key, ""))
			this.width = NumberUtils.toFloat(value, unknownSize);
		else if (StringUtils.equals(key, "x"))
			this.height = NumberUtils.toFloat(value, unknownSize);
		else
			return false;
		return true;
	}

	@Override
	public String compose() {
		return (get().vaildWidth() ? ShortestFloatFormatter.format(this.width) : "")+(get().vaildHeight() ? "x"+ShortestFloatFormatter.format(this.height) : "");
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
			return new ImageSize().setImageSize(this);
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
