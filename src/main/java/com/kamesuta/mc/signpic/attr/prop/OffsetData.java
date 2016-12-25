package com.kamesuta.mc.signpic.attr.prop;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;
import com.kamesuta.mc.signpic.attr.IPropBuilder;
import com.kamesuta.mc.signpic.attr.IPropInterpolatable;

public class OffsetData implements IPropInterpolatable<OffsetData> {
	public static final float defaultOffset = 0.5f;

	public final OffsetPropData x;
	public final OffsetPropData y;
	public final OffsetPropData z;

	public OffsetData(final OffsetPropData x, final OffsetPropData y, final OffsetPropData z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public OffsetData per() {
		return this;
	}

	@Override
	public OffsetData per(final float per, final @Nullable OffsetData before) {
		if (before==null)
			return this;
		else
			return new OffsetData(this.x.per(per, before.x), this.y.per(per, before.y), this.z.per(per, before.z));
	}

	@Override
	public String toString() {
		return "OffsetData [x="+this.x+", y="+this.y+", z="+this.z+"]";
	}

	public static class OffsetBuilder implements IPropBuilder<OffsetData, OffsetData> {
		public final OffsetPropBuilder x = new OffsetPropBuilder("L", "R");
		public final OffsetPropBuilder y = new OffsetPropBuilder("D", "U");
		public final OffsetPropBuilder z = new OffsetPropBuilder("B", "F");

		@Override
		public boolean parse(final String src, final String key, final String value) {
			final boolean a = this.x.parse(src, key, value);
			final boolean b = this.y.parse(src, key, value);
			final boolean c = this.z.parse(src, key, value);
			return a||b||c;
		}

		@Override
		public String compose() {
			return this.x.compose()+this.y.compose()+this.z.compose();
		}

		@Override
		public OffsetData diff(final @Nullable OffsetData base) {
			if (base==null)
				return new OffsetData(this.x.diff(null), this.y.diff(null), this.z.diff(null));
			else
				return new OffsetData(this.x.diff(base.x), this.y.diff(base.y), this.z.diff(base.z));
		}

		@Override
		public String toString() {
			return "OffsetBuilder [x="+this.x+", y="+this.y+", z="+this.z+"]";
		}
	}

	public static class OffsetPropData implements IPropInterpolatable<OffsetPropData> {
		public final float offset;

		public OffsetPropData(final float offset) {
			this.offset = offset;
		}

		@Override
		public OffsetPropData per() {
			return this;
		}

		@Override
		public OffsetPropData per(final float per, final @Nullable OffsetPropData before) {
			if (before==null)
				return this;
			else
				return new OffsetPropData(this.offset*per+before.offset*(1f-per));
		}

		@Override
		public String toString() {
			return "OffsetPropData [offset="+this.offset+"]";
		}
	}

	public static class OffsetPropBuilder implements IPropBuilder<OffsetPropData, OffsetPropData> {
		public final String neg;
		public final String pos;

		private float offset;

		public OffsetPropBuilder(final String neg, final String pos) {
			this.neg = neg;
			this.pos = pos;
		}

		@Override
		public OffsetPropData diff(@Nullable final OffsetPropData base) {
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
		public String toString() {
			return "OffsetPropBuilder [neg="+this.neg+", pos="+this.pos+", offset="+this.offset+"]";
		}

		/**
		 * L=left
		 * R=right
		 * D=down
		 * U=up
		 * B=back
		 * F=front
		 * @param src
		 */
		@Override
		public boolean parse(final String src, final String key, final String value) {
			if (StringUtils.equals(key, this.neg))
				if (StringUtils.isEmpty(value))
					this.offset -= OffsetData.defaultOffset;
				else
					this.offset -= NumberUtils.toFloat(value, 0f);
			else if (StringUtils.equals(key, this.pos))
				if (StringUtils.isEmpty(value))
					this.offset += OffsetData.defaultOffset;
				else
					this.offset += NumberUtils.toFloat(value, 0f);
			else
				return false;
			return true;
		}

		/**
		 * L=left
		 * R=right
		 * D=down
		 * U=up
		 * B=back
		 * F=front
		 */
		@Override
		public String compose() {
			final StringBuilder stb = new StringBuilder();
			if (this.offset!=0)
				if (this.offset<0)
					if (this.offset==-OffsetData.defaultOffset)
						stb.append(this.neg);
					else
						stb.append(this.neg).append(ShortestFloatFormatter.format(-this.offset));
				else if (this.offset==OffsetData.defaultOffset)
					stb.append(this.pos);
				else
					stb.append(this.pos).append(ShortestFloatFormatter.format(this.offset));
			return stb.toString();
		}
	}
}
