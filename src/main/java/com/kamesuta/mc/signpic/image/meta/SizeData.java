package com.kamesuta.mc.signpic.image.meta;

import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;

public class SizeData {
	public static final float defaultSize = 1f;
	public static final float unknownSize = Float.NaN;

	public final float width;
	public final float height;

	public SizeData(final float width, final float height) {
		this.width = width;
		this.height = height;
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

	public SizeData scale(final float scale) {
		return new SizeData(this.width*scale, this.height*scale);
	}

	public String compose() {
		return (vaildWidth() ? ShortestFloatFormatter.format(this.width) : "")+(vaildHeight() ? "x"+ShortestFloatFormatter.format(this.height) : "");
	}

	public static SizeData aspectSize(final SizeData imagesize, final SizeData availableaspect) {
		if (imagesize==null)
			return availableaspect;
		if (availableaspect==null)
			return imagesize;
		else if (imagesize.vaildWidth()&&imagesize.vaildHeight())
			return imagesize;
		else if (imagesize.vaildWidth())
			return ImageSizes.WIDTH.defineSize(availableaspect, imagesize.width, unknownSize);
		else if (imagesize.vaildHeight())
			return ImageSizes.HEIGHT.defineSize(availableaspect, unknownSize, imagesize.height);
		else
			return ImageSizes.HEIGHT.defineSize(availableaspect, unknownSize, 1);
	}

	public static enum ImageSizes {
		RAW {
			@Override
			public SizeData size(final float w, final float h, final float maxw, final float maxh) {
				return new SizeData(w, h);
			}
		},
		MAX {
			@Override
			public SizeData size(final float w, final float h, final float maxw, final float maxh) {
				return new SizeData(maxw, maxh);
			}
		},
		WIDTH {
			@Override
			public SizeData size(final float w, final float h, final float maxw, final float maxh) {
				return new SizeData(maxw, h*maxw/w);
			}
		},
		HEIGHT {
			@Override
			public SizeData size(final float w, final float h, final float maxw, final float maxh) {
				return new SizeData(w*maxh/h, maxh);
			}
		},
		INNER {
			@Override
			public SizeData size(final float w, final float h, float maxw, float maxh) {
				if (w<0)
					maxw *= -1;
				if (h<0)
					maxh *= -1;
				final boolean b = w/maxw>h/maxh;
				return new SizeData(b ? maxw : w*maxh/h, b ? h*maxw/w : maxh);
			}
		},
		OUTER {
			@Override
			public SizeData size(final float w, final float h, float maxw, float maxh) {
				if (w<0)
					maxw *= -1;
				if (h<0)
					maxh *= -1;
				final boolean b = w/maxw<h/maxh;
				return new SizeData(b ? maxw : w*maxh/h, b ? h*maxw/w : maxh);
			}
		},
		WIDTH_LIMIT {
			@Override
			public SizeData size(final float w, final float h, final float maxw, final float maxh) {
				if (w<maxw)
					return new SizeData(w, h);
				else
					return new SizeData(maxw, maxw*h/w);
			}
		},
		HEIGHT_LIMIT {
			@Override
			public SizeData size(final float w, final float h, final float maxw, final float maxh) {
				if (h<maxh)
					return new SizeData(w, h);
				else
					return new SizeData(maxh*w/h, maxh);
			}
		},
		LIMIT {
			@Override
			public SizeData size(final float w, final float h, final float maxw, final float maxh) {
				if (w>h)
					if (w<maxw)
						return new SizeData(w, h);
					else
						return new SizeData(maxw, maxw*h/w);
				else if (h<maxh)
					return new SizeData(w, h);
				else
					return new SizeData(maxh*w/h, maxh);
			}
		},
		;

		public abstract SizeData size(float w, float h, float maxw, float maxh);

		public SizeData defineSize(float rawWidth, float rawHeight, float maxWidth, float maxHeight) {
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
			return size(rawWidth, rawHeight, maxWidth, maxHeight);
		}

		public SizeData defineSize(final SizeData raw, final float maxWidth, final float maxHeight) {
			if (raw==null)
				return new SizeData(maxWidth, maxHeight);
			return defineSize(raw.width, raw.height, maxWidth, maxHeight);
		}

		public SizeData defineSize(final float rawWidth, final float rawHeight, final SizeData max) {
			if (max==null)
				return new SizeData(rawWidth, rawHeight);
			return defineSize(rawWidth, rawHeight, max.width, max.height);
		}

		public SizeData defineSize(final SizeData raw, final SizeData max) {
			if (raw==null&&max==null)
				throw new IllegalArgumentException("No Size Defined");
			else if (raw==null)
				return max;
			else if (max==null)
				return raw;
			return defineSize(raw.width, raw.height, max.width, max.height);
		}
	}
}
