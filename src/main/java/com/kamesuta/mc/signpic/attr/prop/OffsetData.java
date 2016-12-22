package com.kamesuta.mc.signpic.attr.prop;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;
import com.kamesuta.mc.signpic.attr.IPropInterpolatable;
import com.kamesuta.mc.signpic.attr.IPropBuilder;

public class OffsetData implements IPropInterpolatable<OffsetData> {
	public static final float defaultOffset = 0.5f;

	public final float offset;

	public OffsetData(final float offset) {
		this.offset = offset;
	}

	@Override
	public OffsetData per() {
		return this;
	}

	@Override
	public OffsetData per(final float per, final OffsetData before) {
		return new OffsetData(this.offset*per+before.offset*(1f-per));
	}

	@Override
	public String toString() {
		return "OffsetData [offset="+this.offset+"]";
	}

	public static class OffsetBuilder implements IPropBuilder<OffsetData, OffsetData> {
		public final String neg;
		public final String pos;

		private float offset;

		public OffsetBuilder(final String neg, final String pos) {
			this.neg = neg;
			this.pos = pos;
		}

		@Override
		public OffsetData diff(@Nullable final OffsetData base) {
			if (base==null)
				return new OffsetData(this.offset);
			else
				return new OffsetData(base.offset+this.offset);
		}

		public void set(final float offset) {
			this.offset = offset;
		}

		public float get() {
			return this.offset;
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
