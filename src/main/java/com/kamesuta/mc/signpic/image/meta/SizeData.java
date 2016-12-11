package com.kamesuta.mc.signpic.image.meta;

import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;
import com.kamesuta.mc.bnnwidget.position.Area;

public abstract class SizeData implements IMotionFrame<SizeData>, IComposable {
	public static final float Default = 1f;
	public static final float Unknown = Float.NaN;

	public static final SizeData DefaultSize = new AbsSizeData(Default, Default);
	public static final SizeData UnknownSize = new AbsSizeData(Unknown, Unknown);

	public abstract float getWidth();

	public abstract float getHeight();

	public abstract boolean vaildWidth();

	public abstract boolean vaildHeight();

	public float max() {
		return Math.max(getWidth(), getHeight());
	}

	public float min() {
		return Math.min(getWidth(), getHeight());
	}

	public SizeData scale(final float scale) {
		return new AbsSizeData(getWidth()*scale, getHeight()*scale);
	}

	@Override
	public SizeData per() {
		return this;
	}

	@Override
	public SizeData per(final float per, final SizeData before) {
		// return new SizeData(getWidth()*per+before.getWidth()*(1f-per), getHeight()*per+before.getHeight()*(1f-per));
		return new PerSizeData(this, before, per);
	}

	@Override
	public String compose() {
		return (vaildWidth() ? ShortestFloatFormatter.format(getWidth()) : "")+(vaildHeight() ? "x"+ShortestFloatFormatter.format(getHeight()) : "");
	}

	@Override
	public String toString() {
		return "SizeData [width="+getWidth()+", height="+getHeight()+"]";
	}

	public abstract SizeData aspectSize(final SizeData availableaspect);

	public static SizeData create(final float width, final float height) {
		return new AbsSizeData(width, height);
	}

	public static SizeData create(final SizeData base, final SizeData diff) {
		if (base==null)
			return create(diff.getWidth(), diff.getHeight());
		else
			return new DiffSizeData(base, diff);
	}

	public static SizeData create(final Area area) {
		return create(area.w(), area.h());
	}

	public static class AbsSizeData extends SizeData {
		private final float width;
		private final float height;

		public AbsSizeData(final float width, final float height) {
			this.width = width;
			this.height = height;
		}

		@Override
		public float getWidth() {
			return this.width;
		}

		@Override
		public float getHeight() {
			return this.height;
		}

		@Override
		public boolean vaildWidth() {
			return !Float.isNaN(getWidth());
		}

		@Override
		public boolean vaildHeight() {
			return !Float.isNaN(getHeight());
		}

		@Override
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
	}

	public static class PerSizeData extends SizeData {
		private final SizeData after;
		private final SizeData before;
		private final float per;

		public PerSizeData(final SizeData after, final SizeData before, final float per) {
			this.after = after;
			this.before = before;
			this.per = per;
		}

		@Override
		public float getWidth() {
			return this.after.getWidth()*this.per+this.before.getWidth()*(1f-this.per);
		}

		@Override
		public float getHeight() {
			return this.after.getHeight()*this.per+this.before.getHeight()*(1f-this.per);
		}

		@Override
		public boolean vaildWidth() {
			return (this.after.vaildWidth()||this.before.vaildWidth())&&(this.after.vaildHeight()||this.before.vaildHeight());
		}

		@Override
		public boolean vaildHeight() {
			return (this.after.vaildWidth()||this.before.vaildWidth())&&(this.after.vaildHeight()||this.before.vaildHeight());
		}

		@Override
		public SizeData aspectSize(final SizeData availableaspect) {
			return new PerSizeData(this.after.aspectSize(availableaspect), this.before.aspectSize(availableaspect), this.per);
		}
	}

	public static class DiffSizeData extends SizeData {
		private final SizeData base;
		private final SizeData diff;

		public DiffSizeData(final SizeData base, final SizeData diff) {
			this.base = base;
			this.diff = diff;
		}

		@Override
		public float getWidth() {
			return this.base.getWidth()+this.diff.getWidth();
		}

		@Override
		public float getHeight() {
			return this.base.getHeight()+this.diff.getHeight();
		}

		@Override
		public boolean vaildWidth() {
			return this.base.vaildWidth();
		}

		@Override
		public boolean vaildHeight() {
			return this.base.vaildHeight();
		}

		@Override
		public SizeData aspectSize(final SizeData availableaspect) {
			final SizeData baseaspect = this.base.aspectSize(availableaspect);
			return new DiffSizeData(baseaspect, this.diff.aspectSize(baseaspect));
		}
	}

	public static enum ImageSizes {
		RAW {
			@Override
			public SizeData size(final float w, final float h, final float maxw, final float maxh) {
				return new AbsSizeData(w, h);
			}
		},
		MAX {
			@Override
			public SizeData size(final float w, final float h, final float maxw, final float maxh) {
				return new AbsSizeData(maxw, maxh);
			}
		},
		WIDTH {
			@Override
			public SizeData size(final float w, final float h, final float maxw, final float maxh) {
				return new AbsSizeData(maxw, h*maxw/w);
			}
		},
		HEIGHT {
			@Override
			public SizeData size(final float w, final float h, final float maxw, final float maxh) {
				return new AbsSizeData(w*maxh/h, maxh);
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
				return new AbsSizeData(b ? maxw : w*maxh/h, b ? h*maxw/w : maxh);
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
				return new AbsSizeData(b ? maxw : w*maxh/h, b ? h*maxw/w : maxh);
			}
		},
		WIDTH_LIMIT {
			@Override
			public SizeData size(final float w, final float h, final float maxw, final float maxh) {
				if (w<maxw)
					return new AbsSizeData(w, h);
				else
					return new AbsSizeData(maxw, maxw*h/w);
			}
		},
		HEIGHT_LIMIT {
			@Override
			public SizeData size(final float w, final float h, final float maxw, final float maxh) {
				if (h<maxh)
					return new AbsSizeData(w, h);
				else
					return new AbsSizeData(maxh*w/h, maxh);
			}
		},
		LIMIT {
			@Override
			public SizeData size(final float w, final float h, final float maxw, final float maxh) {
				if (w>h)
					if (w<maxw)
						return new AbsSizeData(w, h);
					else
						return new AbsSizeData(maxw, maxw*h/w);
				else if (h<maxh)
					return new AbsSizeData(w, h);
				else
					return new AbsSizeData(maxh*w/h, maxh);
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
				return new AbsSizeData(maxWidth, maxHeight);
			return defineSize(raw.getWidth(), raw.getHeight(), maxWidth, maxHeight);
		}

		public SizeData defineSize(final float rawWidth, final float rawHeight, final SizeData max) {
			if (max==null)
				return new AbsSizeData(rawWidth, rawHeight);
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
