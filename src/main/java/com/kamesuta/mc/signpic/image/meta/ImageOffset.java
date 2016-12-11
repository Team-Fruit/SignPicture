package com.kamesuta.mc.signpic.image.meta;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;

public class ImageOffset implements MetaMovie<OffsetData, OffsetData> {
	public final String neg;
	public final String pos;

	private float offset;

	public ImageOffset(final String neg, final String pos) {
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
		/* @formatter:off */
		if (StringUtils.equals(key, this.neg)) if (StringUtils.isEmpty(value)) this.offset -= OffsetData.defaultOffset; else this.offset -= NumberUtils.toFloat(value, 0f);
		else if (StringUtils.equals(key, this.pos)) if (StringUtils.isEmpty(value)) this.offset += OffsetData.defaultOffset; else this.offset += NumberUtils.toFloat(value, 0f);
		else return false;
		return true;
		/* @formatter:on */
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
		/* @formatter:off */
		final StringBuilder stb = new StringBuilder();
		if (this.offset!=0)
			if (this.offset<0)
				if (this.offset==-OffsetData.defaultOffset) stb.append(this.neg);
				else stb.append(this.neg).append(ShortestFloatFormatter.format(-this.offset));
			else
				if (this.offset==OffsetData.defaultOffset) stb.append(this.pos);
				else stb.append(this.pos).append(ShortestFloatFormatter.format(this.offset));
		return stb.toString();
		/* @formatter:on */
	}
}
