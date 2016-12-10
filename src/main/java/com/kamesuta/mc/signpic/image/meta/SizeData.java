package com.kamesuta.mc.signpic.image.meta;

import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;

public class SizeData implements IMotionFrame<SizeData>, IComposable {
	public static final float Default = 1f;
	public static final float Unknown = Float.NaN;

	public static final SizeData DefaultSize = new SizeData(Default, Default);
	public static final SizeData UnknownSize = new SizeData(Unknown, Unknown);

	private final float width;
	private final float height;

	public float getWidth() {
		return this.width;
	}

	public float getHeight() {
		return this.height;
	}

	public SizeData(final float width, final float height) {
		this.width = width;
		this.height = height;
	}

	public boolean vaildWidth() {
		return !Float.isNaN(getWidth());
	}

	public boolean vaildHeight() {
		return !Float.isNaN(getHeight());
	}

	public float max() {
		return Math.max(getWidth(), getHeight());
	}

	public float min() {
		return Math.min(getWidth(), getHeight());
	}

	public SizeData scale(final float scale) {
		return new SizeData(getWidth()*scale, getHeight()*scale);
	}

	@Override
	public SizeData per() {
		return this;
	}

	@Override
	public SizeData per(final float per, final SizeData before) {
		return new SizeData(getWidth()*per+before.getWidth()*(1f-per), getHeight()*per+before.getHeight()*(1f-per));
	}

	@Override
	public String compose() {
		return (vaildWidth() ? ShortestFloatFormatter.format(getWidth()) : "")+(vaildHeight() ? "x"+ShortestFloatFormatter.format(getHeight()) : "");
	}

	@Override
	public String toString() {
		return "SizeData [width="+getWidth()+", height="+getHeight()+"]";
	}

	public SizeData aspectSize(final SizeData availableaspect) {
		if (availableaspect==null)
			return this;
		else if (vaildWidth()&&vaildHeight())
			return this;
		else if (vaildWidth())
			return ImageSizes.WIDTH.defineSize(availableaspect, getWidth(), Unknown);
		else if (vaildHeight())
			return ImageSizes.HEIGHT.defineSize(availableaspect, Unknown, getHeight());
		else
			return ImageSizes.HEIGHT.defineSize(availableaspect, Unknown, 1);
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
			return defineSize(raw.getWidth(), raw.getHeight(), maxWidth, maxHeight);
		}

		public SizeData defineSize(final float rawWidth, final float rawHeight, final SizeData max) {
			if (max==null)
				return new SizeData(rawWidth, rawHeight);
			return defineSize(rawWidth, rawHeight, max.getWidth(), max.getHeight());
		}

		public SizeData defineSize(final SizeData raw, final SizeData max) {
			if (raw==null&&max==null)
				throw new IllegalArgumentException("No Size Defined");
			else if (raw==null)
				return max;
			else if (max==null)
				return raw;
			return defineSize(raw.getWidth(), raw.getHeight(), max.getWidth(), max.getHeight());
		}
	}
}
