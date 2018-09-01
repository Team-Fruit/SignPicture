package com.kamesuta.mc.signpic.attr.prop;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.signpic.attr.IPropBuilder;
import com.kamesuta.mc.signpic.attr.IPropComposable;
import com.kamesuta.mc.signpic.attr.IPropInterpolatable;

public abstract class SizeData implements IPropInterpolatable<SizeData>, IPropComposable {
	public static final float Default = 1f;
	public static final float Unknown = Float.NaN;

	public static final @Nonnull SizeData DefaultSize = new AbsSizeData(Default, Default);
	public static final @Nonnull SizeData UnknownSize = new AbsSizeData(Unknown, Unknown);

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

	public @Nonnull SizeData scale(final float scale) {
		return new AbsSizeData(getWidth()*scale, getHeight()*scale);
	}

	@Override
	public @Nonnull SizeData per() {
		return this;
	}

	@Override
	public @Nonnull SizeData per(final float per, final @Nullable SizeData before) {
		// return new SizeData(getWidth()*per+before.getWidth()*(1f-per), getHeight()*per+before.getHeight()*(1f-per));
		if (before!=null)
			return new PerSizeData(this, before, per);
		return this;
	}

	@Override
	public @Nonnull String compose() {
		return (vaildWidth() ? PropSyntax.SIZE_W.id+ShortestFloatFormatter.format(getWidth()) : "")+(vaildHeight() ? PropSyntax.SIZE_H.id+ShortestFloatFormatter.format(getHeight()) : "");
	}

	public abstract @Nonnull SizeData aspectSize(final @Nullable SizeData availableaspect);

	public static @Nonnull SizeData create(final float width, final float height) {
		return new AbsSizeData(width, height);
	}

	public static @Nonnull SizeData create(final @Nullable SizeData base, final @Nonnull SizeData diff) {
		if (base==null)
			return diff;
		else
			return new DiffSizeData(base, diff);
	}

	public static @Nonnull SizeData create(final @Nonnull Area area) {
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
		public @Nonnull SizeData aspectSize(final @Nullable SizeData availableaspect) {
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

		@Override
		public String toString() {
			return String.format("AbsSizeData [width=%s, height=%s]", getWidth(), getHeight());
		}
	}

	public static class PerSizeData extends SizeData {
		private final @Nonnull SizeData after;
		private final @Nonnull SizeData before;
		private final float per;

		public PerSizeData(final @Nonnull SizeData after, final @Nonnull SizeData before, final float per) {
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
		public @Nonnull SizeData aspectSize(final @Nullable SizeData availableaspect) {
			return new PerSizeData(this.after.aspectSize(availableaspect), this.before.aspectSize(availableaspect), this.per);
		}

		@Override
		public @Nonnull String toString() {
			return "PerSizeData [after="+this.after+", before="+this.before+", per="+this.per+", (width="+getWidth()+", height="+getHeight()+")]";
		}
	}

	public static class DiffSizeData extends SizeData {
		private final @Nonnull SizeData base;
		private final @Nonnull SizeData diff;

		public DiffSizeData(final @Nonnull SizeData base, final @Nonnull SizeData diff) {
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
		public @Nonnull SizeData aspectSize(final @Nullable SizeData availableaspect) {
			return this.diff.aspectSize(this.base.aspectSize(availableaspect));
		}

		@Override
		public @Nonnull String toString() {
			return "PerSizeData [base="+this.base+", diff="+this.diff+", (width="+getWidth()+", height="+getHeight()+")]";
		}
	}

	public static enum ImageSizes {
		RAW {
			@Override
			public @Nonnull SizeData size(final float w, final float h, final float maxw, final float maxh) {
				return new AbsSizeData(w, h);
			}
		},
		MAX {
			@Override
			public @Nonnull SizeData size(final float w, final float h, final float maxw, final float maxh) {
				return new AbsSizeData(maxw, maxh);
			}
		},
		WIDTH {
			@Override
			public @Nonnull SizeData size(final float w, final float h, final float maxw, final float maxh) {
				return new AbsSizeData(maxw, h*maxw/w);
			}
		},
		HEIGHT {
			@Override
			public @Nonnull SizeData size(final float w, final float h, final float maxw, final float maxh) {
				return new AbsSizeData(w*maxh/h, maxh);
			}
		},
		INNER {
			@Override
			public @Nonnull SizeData size(final float w, final float h, float maxw, float maxh) {
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
			public @Nonnull SizeData size(final float w, final float h, float maxw, float maxh) {
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
			public @Nonnull SizeData size(final float w, final float h, final float maxw, final float maxh) {
				if (w<maxw)
					return new AbsSizeData(w, h);
				else
					return new AbsSizeData(maxw, maxw*h/w);
			}
		},
		HEIGHT_LIMIT {
			@Override
			public @Nonnull SizeData size(final float w, final float h, final float maxw, final float maxh) {
				if (h<maxh)
					return new AbsSizeData(w, h);
				else
					return new AbsSizeData(maxh*w/h, maxh);
			}
		},
		LIMIT {
			@Override
			public @Nonnull SizeData size(final float w, final float h, final float maxw, final float maxh) {
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

		public abstract @Nonnull SizeData size(float w, float h, float maxw, float maxh);

		public @Nonnull SizeData defineSize(float rawWidth, float rawHeight, float maxWidth, float maxHeight) {
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

		public @Nonnull SizeData defineSize(final @Nullable SizeData raw, final float maxWidth, final float maxHeight) {
			if (raw==null)
				return new AbsSizeData(maxWidth, maxHeight);
			return defineSize(raw.getWidth(), raw.getHeight(), maxWidth, maxHeight);
		}

		public @Nonnull SizeData defineSize(final float rawWidth, final float rawHeight, final @Nullable SizeData max) {
			if (max!=null)
				return defineSize(rawWidth, rawHeight, max.getWidth(), max.getHeight());
			return new AbsSizeData(rawWidth, rawHeight);
		}

		public @Nonnull SizeData defineSize(final @Nullable SizeData raw, final @Nullable SizeData max) {
			if (raw!=null&&max!=null)
				return defineSize(raw.getWidth(), raw.getHeight(), max.getWidth(), max.getHeight());
			else if (raw==null&&max!=null)
				return max;
			else if (max==null&&raw!=null)
				return raw;
			else
				throw new IllegalArgumentException("No Size Defined");
		}
	}

	public static class SizeBuilder implements IPropBuilder<SizeData, SizeData>, Cloneable {
		public float width = SizeData.Unknown;
		public float height = SizeData.Unknown;

		@Override
		public @Nonnull SizeData diff(final @Nullable SizeData base) {
			return SizeData.create(base, SizeData.create(this.width, this.height));
		}

		public @Nonnull SizeBuilder setSize(final float width, final float height) {
			this.width = width;
			this.height = height;
			return this;
		}

		public @Nonnull SizeBuilder setWidth(final float width) {
			this.width = width;
			return this;
		}

		public @Nonnull SizeBuilder setHeight(final float height) {
			this.height = height;
			return this;
		}

		public @Nonnull SizeBuilder setWidth(final @Nullable String width) {
			this.width = NumberUtils.toFloat(width, SizeData.Unknown);
			return this;
		}

		public @Nonnull SizeBuilder setHeight(final @Nullable String height) {
			this.height = NumberUtils.toFloat(height, SizeData.Unknown);
			return this;
		}

		public @Nonnull SizeBuilder setSize(final @Nullable String width, final @Nullable String height) {
			return setWidth(width).setHeight(height);
		}

		public @Nonnull SizeBuilder setSize(final @Nonnull Area a) {
			return setSize(a.w(), a.h());
		}

		public @Nonnull SizeBuilder setSize(final @Nonnull SizeData size) {
			return setSize(size.getWidth(), size.getHeight());
		}

		public @Nonnull SizeBuilder setSize(final @Nonnull SizeBuilder size) {
			return setSize(size.width, size.height);
		}

		@Override
		public boolean parse(final @Nonnull String src, final @Nonnull String key, final @Nonnull String value) {
			if (StringUtils.equals(key, PropSyntax.SIZE_W.id))
				this.width = NumberUtils.toFloat(value, SizeData.Unknown);
			else if (StringUtils.equals(key, PropSyntax.SIZE_H.id))
				this.height = NumberUtils.toFloat(value, SizeData.Unknown);
			else
				return false;
			return true;
		}

		@Override
		public @Nonnull String compose() {
			return diff(null).compose();
		}

		@Override
		public String toString() {
			return String.format("SizeBuilder [width=%s, height=%s]", this.width, this.height);
		}

		@Override
		public @Nonnull SizeBuilder clone() {
			try {
				return (SizeBuilder) super.clone();
			} catch (final Exception e) {
				return new SizeBuilder().setSize(this);
			}
		}
	}
}
