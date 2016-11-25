package com.kamesuta.mc.signpic.image.meta;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.position.Area;

public class ImageSize extends ImageMeta.MetaParser implements Cloneable {
	public static final float defaultSize = 1f;
	public static final float unknownSize = Float.NaN;

	public float width = unknownSize;
	public float height = unknownSize;

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

	public boolean vaildWidth() {
		return !Float.isNaN(this.width);
	}

	public boolean vaildHeight() {
		return !Float.isNaN(this.height);
	}

	public float max() {
		return Math.max(this.width, this.height);
	}

	public float min() {
		return Math.min(this.width, this.height);
	}

	public ImageSize setAspectSize(final ImageSize imagesize, final ImageSize availableaspect) {
		if (imagesize==null)
			return setImageSize(availableaspect);
		if (availableaspect==null)
			return setImageSize(imagesize);
		else if (imagesize.vaildWidth()&&imagesize.vaildHeight())
			return setImageSize(imagesize);
		else if (imagesize.vaildWidth())
			return setSize(ImageSizes.WIDTH, availableaspect, imagesize.width, unknownSize);
		else if (imagesize.vaildHeight())
			return setSize(ImageSizes.HEIGHT, availableaspect, unknownSize, imagesize.height);
		else
			return setSize(ImageSizes.HEIGHT, availableaspect, unknownSize, 1);
	}

	public ImageSize setSize(final ImageSizes s, float rawWidth, float rawHeight, float maxWidth, float maxHeight) {
		if (Float.isNaN(rawWidth)&&Float.isNaN(maxWidth)||Float.isNaN(rawHeight)&&Float.isNaN(maxHeight))
			throw new IllegalArgumentException("No Size Defined");
		if (Float.isNaN(rawWidth))
			rawWidth = maxWidth;
		if (Float.isNaN(rawHeight))
			rawHeight = maxHeight;
		if (Float.isNaN(maxWidth))
			maxWidth = rawWidth;
		if (Float.isNaN(maxHeight))
			maxHeight = rawHeight;
		s.size(this, rawWidth, rawHeight, maxWidth, maxHeight);
		return this;
	}

	public ImageSize setSize(final ImageSizes s, final ImageSize raw, final float maxWidth, final float maxHeight) {
		if (raw==null)
			return setSize(maxWidth, maxHeight);
		return setSize(s, raw.width, raw.height, maxWidth, maxHeight);
	}

	public ImageSize setSize(final ImageSizes s, final float rawWidth, final float rawHeight, final ImageSize max) {
		if (max==null)
			return setSize(rawWidth, rawHeight);
		return setSize(s, rawWidth, rawHeight, max.width, max.height);
	}

	public ImageSize setSize(final ImageSizes s, final ImageSize raw, final ImageSize max) {
		if (raw==null&&max==null)
			throw new IllegalArgumentException("No Size Defined");
		else if (raw==null)
			return max;
		else if (max==null)
			return raw;
		return setSize(s, raw.width, raw.height, max.width, max.height);
	}

	public ImageSize scale(final float scale) {
		this.width *= scale;
		this.height *= scale;
		return this;
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
		return (vaildWidth() ? format(this.width) : "")+(vaildHeight() ? "x"+format(this.height) : "");
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

	public static enum ImageSizes {
		RAW {
			@Override
			public void size(final ImageSize s, final float w, final float h, final float maxw, final float maxh) {
				s.setSize(w, h);
			}
		},
		MAX {
			@Override
			public void size(final ImageSize s, final float w, final float h, final float maxw, final float maxh) {
				s.setSize(maxw, maxh);
			}
		},
		WIDTH {
			@Override
			public void size(final ImageSize s, final float w, final float h, final float maxw, final float maxh) {
				s.setSize(maxw, h*maxw/w);
			}
		},
		HEIGHT {
			@Override
			public void size(final ImageSize s, final float w, final float h, final float maxw, final float maxh) {
				s.setSize(w*maxh/h, maxh);
			}
		},
		INNER {
			@Override
			public void size(final ImageSize s, final float w, final float h, float maxw, float maxh) {
				if (w<0)
					maxw *= -1;
				if (h<0)
					maxh *= -1;
				final boolean b = w/maxw>h/maxh;
				s.setSize(b ? maxw : w*maxh/h, b ? h*maxw/w : maxh);
			}
		},
		OUTER {
			@Override
			public void size(final ImageSize s, final float w, final float h, float maxw, float maxh) {
				if (w<0)
					maxw *= -1;
				if (h<0)
					maxh *= -1;
				final boolean b = w/maxw<h/maxh;
				s.setSize(b ? maxw : w*maxh/h, b ? h*maxw/w : maxh);
			}
		},
		WIDTH_LIMIT {
			@Override
			public void size(final ImageSize s, final float w, final float h, final float maxw, final float maxh) {
				if (w<maxw)
					s.setSize(w, h);
				else
					s.setSize(maxw, maxw*h/w);
			}
		},
		HEIGHT_LIMIT {
			@Override
			public void size(final ImageSize s, final float w, final float h, final float maxw, final float maxh) {
				if (h<maxh)
					s.setSize(w, h);
				else
					s.setSize(maxh*w/h, maxh);
			}
		},
		LIMIT {
			@Override
			public void size(final ImageSize s, final float w, final float h, final float maxw, final float maxh) {
				if (w>h)
					if (w<maxw)
						s.setSize(w, h);
					else
						s.setSize(maxw, maxw*h/w);
				else if (h<maxh)
					s.setSize(w, h);
				else
					s.setSize(maxh*w/h, maxh);
			}
		},
		;

		public abstract void size(final ImageSize s, float w, float h, float maxw, float maxh);
	}
}
