package com.kamesuta.mc.signpic.image.meta;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class ImageOffset implements ImageMeta.MetaParser {
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
	public ImageOffset parse(final String src, final String key, final String value) {
		if (StringUtils.equals(key, "L")) if (StringUtils.isEmpty(value)) this.x -= defaultOffset; else this.x -= NumberUtils.toFloat(value, 0f);
		else if (StringUtils.equals(key, "R")) if (StringUtils.isEmpty(value)) this.x += defaultOffset; else this.x += NumberUtils.toFloat(value, 0f);
		else if (StringUtils.equals(key, "D")) if (StringUtils.isEmpty(value)) this.y -= defaultOffset; else this.y -= NumberUtils.toFloat(value, 0f);
		else if (StringUtils.equals(key, "U")) if (StringUtils.isEmpty(value)) this.y += defaultOffset; else this.y += NumberUtils.toFloat(value, 0f);
		else if (StringUtils.equals(key, "B")) if (StringUtils.isEmpty(value)) this.z -= defaultOffset; else this.z -= NumberUtils.toFloat(value, 0f);
		else if (StringUtils.equals(key, "F")) if (StringUtils.isEmpty(value)) this.z += defaultOffset; else this.z += NumberUtils.toFloat(value, 0f);
		return this;
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
		if (this.x>0)
			if (this.x==defaultOffset) stb.append("L");
			else stb.append("L").append(signformat.format(this.x));
		else if (this.x<0)
			if (-this.x==defaultOffset) stb.append("R");
			else stb.append("R").append(signformat.format(-this.x));
		if (this.y>0)
			if (this.y==defaultOffset) stb.append("D");
			else stb.append("D").append(signformat.format(this.y));
		else if (this.y<0)
			if (-this.y==defaultOffset) stb.append("U");
			else stb.append("U").append(signformat.format(-this.y));
		if (this.z>0)
			if (this.z==defaultOffset) stb.append("B");
			else stb.append("B").append(signformat.format(this.z));
		else if (this.z<0)
			if (-this.z==defaultOffset) stb.append("F");
			else stb.append("F").append(signformat.format(-this.z));
		return stb.toString();
	}

	@Override
	public String toString() {
		return compose();
	}
}
