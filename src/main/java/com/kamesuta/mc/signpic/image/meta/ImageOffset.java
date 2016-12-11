package com.kamesuta.mc.signpic.image.meta;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class ImageOffset implements MetaParser, MetaBuilder<OffsetData, OffsetData> {

	private float x;
	private float y;
	private float z;

	@Override
	public OffsetData get(final OffsetData base) {
		if (base==null)
			return new OffsetData(this.x, this.y, this.z);
		else
			return new OffsetData(base.x+this.x, base.y+this.y, base.z+this.z);
	}

	public void setX(final float x) {
		this.x = x;
	}

	public void setY(final float y) {
		this.y = y;
	}

	public void setZ(final float z) {
		this.z = z;
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public float getZ() {
		return this.z;
	}

	@Override
	public ImageOffset reset() {
		this.x = 0f;
		this.y = 0f;
		this.z = 0f;
		return this;
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
		if (StringUtils.equals(key, "L")) if (StringUtils.isEmpty(value)) this.x -= OffsetData.defaultOffset; else this.x -= NumberUtils.toFloat(value, 0f);
		else if (StringUtils.equals(key, "R")) if (StringUtils.isEmpty(value)) this.x += OffsetData.defaultOffset; else this.x += NumberUtils.toFloat(value, 0f);
		else if (StringUtils.equals(key, "D")) if (StringUtils.isEmpty(value)) this.y -= OffsetData.defaultOffset; else this.y -= NumberUtils.toFloat(value, 0f);
		else if (StringUtils.equals(key, "U")) if (StringUtils.isEmpty(value)) this.y += OffsetData.defaultOffset; else this.y += NumberUtils.toFloat(value, 0f);
		else if (StringUtils.equals(key, "B")) if (StringUtils.isEmpty(value)) this.z -= OffsetData.defaultOffset; else this.z -= NumberUtils.toFloat(value, 0f);
		else if (StringUtils.equals(key, "F")) if (StringUtils.isEmpty(value)) this.z += OffsetData.defaultOffset; else this.z += NumberUtils.toFloat(value, 0f);
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
	@Deprecated
	@Override
	public String compose() {
		return get(new OffsetData(0, 0, 0)).compose();
	}

	@Deprecated
	@Override
	public String toString() {
		return compose();
	}
}
