package com.kamesuta.mc.signpic.image.meta;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class ImageOffset extends ImageMeta.MetaParser {
	public static final float defaultOffset = 0.5f;

	public float x;
	public float y;
	public float z;

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
		if (StringUtils.equals(key, "L")) if (StringUtils.isEmpty(value)) this.x -= defaultOffset; else this.x -= NumberUtils.toFloat(value, 0f);
		else if (StringUtils.equals(key, "R")) if (StringUtils.isEmpty(value)) this.x += defaultOffset; else this.x += NumberUtils.toFloat(value, 0f);
		else if (StringUtils.equals(key, "D")) if (StringUtils.isEmpty(value)) this.y -= defaultOffset; else this.y -= NumberUtils.toFloat(value, 0f);
		else if (StringUtils.equals(key, "U")) if (StringUtils.isEmpty(value)) this.y += defaultOffset; else this.y += NumberUtils.toFloat(value, 0f);
		else if (StringUtils.equals(key, "B")) if (StringUtils.isEmpty(value)) this.z -= defaultOffset; else this.z -= NumberUtils.toFloat(value, 0f);
		else if (StringUtils.equals(key, "F")) if (StringUtils.isEmpty(value)) this.z += defaultOffset; else this.z += NumberUtils.toFloat(value, 0f);
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
		if (this.x!=0)
			if (this.x<0)
				if (this.x==-defaultOffset) stb.append("L");
				else stb.append("L").append(format(-this.x));
			else
				if (this.x==defaultOffset) stb.append("R");
				else stb.append("R").append(format(this.x));
		if (this.y!=0)
			if (this.y<0)
				if (this.y==-defaultOffset) stb.append("D");
				else stb.append("D").append(format(-this.y));
			else
				if (this.y==defaultOffset) stb.append("U");
				else stb.append("U").append(format(this.y));
		if (this.z!=0)
			if (this.z<0)
				if (this.z==-defaultOffset) stb.append("B");
				else stb.append("B").append(format(-this.z));
			else
				if (this.z==defaultOffset) stb.append("F");
				else stb.append("F").append(format(this.z));
		return stb.toString();
		/* @formatter:on */
	}

	@Override
	public String toString() {
		return compose();
	}
}
