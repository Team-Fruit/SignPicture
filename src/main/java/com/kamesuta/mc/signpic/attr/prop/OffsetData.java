package com.kamesuta.mc.signpic.attr.prop;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;
import com.kamesuta.mc.signpic.attr.IPropBuilder;
import com.kamesuta.mc.signpic.attr.IPropInterpolatable;

public class OffsetData implements IPropInterpolatable<OffsetData> {
	public static final float defaultOffset = 0.5f;

	public final @Nonnull OffsetPropData x;
	public final @Nonnull OffsetPropData y;
	public final @Nonnull OffsetPropData z;

	public OffsetData(final @Nonnull OffsetPropData x, final @Nonnull OffsetPropData y, final @Nonnull OffsetPropData z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public @Nonnull OffsetData per() {
		return this;
	}

	@Override
	public @Nonnull OffsetData per(final float per, final @Nullable OffsetData before) {
		if (before==null)
			return this;
		else
			return new OffsetData(this.x.per(per, before.x), this.y.per(per, before.y), this.z.per(per, before.z));
	}

	@Override
	public @Nonnull String toString() {
		return "OffsetData [x="+this.x+", y="+this.y+", z="+this.z+"]";
	}

	public static class OffsetBuilder implements IPropBuilder<OffsetData, OffsetData> {
		public final @Nonnull OffsetPropBuilder x;
		public final @Nonnull OffsetPropBuilder y;
		public final @Nonnull OffsetPropBuilder z;

		public OffsetBuilder(final @Nonnull OffsetPropBuilder x, final @Nonnull OffsetPropBuilder y, final @Nonnull OffsetPropBuilder z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public boolean parse(final @Nonnull String src, final @Nonnull String key, final @Nonnull String value) {
			final boolean a = this.x.parse(src, key, value);
			final boolean b = this.y.parse(src, key, value);
			final boolean c = this.z.parse(src, key, value);
			return a||b||c;
		}

		@Override
		public @Nonnull String compose() {
			return this.x.compose()+this.y.compose()+this.z.compose();
		}

		@Override
		public @Nonnull OffsetData diff(final @Nullable OffsetData base) {
			if (base==null)
				return new OffsetData(this.x.diff(null), this.y.diff(null), this.z.diff(null));
			else
				return new OffsetData(this.x.diff(base.x), this.y.diff(base.y), this.z.diff(base.z));
		}

		@Override
		public @Nonnull String toString() {
			return "OffsetBuilder [x="+this.x+", y="+this.y+", z="+this.z+"]";
		}
	}

	public static class OffsetPropData implements IPropInterpolatable<OffsetPropData> {
		public final float offset;

		public OffsetPropData(final float offset) {
			this.offset = offset;
		}

		@Override
		public @Nonnull OffsetPropData per() {
			return this;
		}

		@Override
		public @Nonnull OffsetPropData per(final float per, final @Nullable OffsetPropData before) {
			if (before==null)
				return this;
			else
				return new OffsetPropData(this.offset*per+before.offset*(1f-per));
		}

		@Override
		public @Nonnull String toString() {
			return "OffsetPropData [offset="+this.offset+"]";
		}
	}

	public static class OffsetPropBuilder implements IPropBuilder<OffsetPropData, OffsetPropData> {
		public final @Nonnull String pos;

		protected float offset;

		public OffsetPropBuilder(final @Nonnull String pos) {
			this.pos = pos;
		}

		@Override
		public @Nonnull OffsetPropData diff(final @Nullable OffsetPropData base) {
			if (base==null)
				return new OffsetPropData(this.offset);
			else
				return new OffsetPropData(base.offset+this.offset);
		}

		public void set(final float offset) {
			this.offset = offset;
		}

		public float get() {
			return this.offset;
		}

		@Override
		public @Nonnull String toString() {
			return "OffsetPropBuilder [pos="+this.pos+", offset="+this.offset+"]";
		}

		@Override
		public boolean parse(final @Nonnull String src, final @Nonnull String key, final @Nonnull String value) {
			if (StringUtils.equals(key, this.pos))
				if (StringUtils.isEmpty(value))
					this.offset += OffsetData.defaultOffset;
				else
					this.offset += NumberUtils.toFloat(value, 0f);
			else
				return false;
			return true;
		}

		@Override
		public @Nonnull String compose() {
			if (this.offset!=0)
				if (this.offset==OffsetData.defaultOffset)
					return this.pos;
				else
					return this.pos+ShortestFloatFormatter.format(this.offset);
			return "";
		}
	}

	public static class OffsetDoublePropBuilder extends OffsetPropBuilder {
		public final @Nonnull String neg;

		public OffsetDoublePropBuilder(final @Nonnull String neg, final @Nonnull String pos) {
			super(pos);
			this.neg = neg;
		}

		@Override
		public @Nonnull String toString() {
			return "OffsetPropBuilder [neg="+this.neg+", pos="+this.pos+", offset="+this.offset+"]";
		}

		@Override
		public boolean parse(final @Nonnull String src, final @Nonnull String key, final @Nonnull String value) {
			if (StringUtils.equals(key, this.neg))
				if (StringUtils.isEmpty(value))
					this.offset -= OffsetData.defaultOffset;
				else
					this.offset -= NumberUtils.toFloat(value, 0f);
			else
				return super.parse(src, key, value);
			return true;
		}

		@Override
		public @Nonnull String compose() {
			if (this.offset!=0)
				if (this.offset<0)
					if (this.offset==-OffsetData.defaultOffset)
						return this.neg;
					else
						return this.neg+ShortestFloatFormatter.format(-this.offset);
				else
					return super.compose();
			return "";
		}
	}
}
