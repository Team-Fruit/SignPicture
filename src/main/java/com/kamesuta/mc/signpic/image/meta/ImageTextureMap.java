package com.kamesuta.mc.signpic.image.meta;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class ImageTextureMap extends ImageMeta.MetaParser {
	public static final float defaultStart = 0f;
	public static final float defaultEnd = 1f;

	public float l = defaultStart;
	public float t = defaultStart;
	public float b = defaultEnd;
	public float r = defaultEnd;

	@Override
	public ImageTextureMap reset() {
		this.l = defaultStart;
		this.t = defaultStart;
		this.b = defaultEnd;
		this.r = defaultEnd;
		return this;
	}

	@Override
	public boolean parse(final String src, final String key, final String value) {
		/* @formatter:off */
		if (StringUtils.equals(key, "l")) this.l = NumberUtils.toFloat(value, defaultStart);
		else if (StringUtils.equals(key, "t")) this.t = NumberUtils.toFloat(value, defaultStart);
		else if (StringUtils.equals(key, "b")) this.b = NumberUtils.toFloat(value, defaultEnd);
		else if (StringUtils.equals(key, "r")) this.r = NumberUtils.toFloat(value, defaultEnd);
		else return false;
		return true;
		/* @formatter:on */
	}

	@Override
	public String compose() {
		final StringBuilder stb = new StringBuilder();
		if (this.l!=defaultStart)
			stb.append("l").append(format(this.l));
		if (this.t!=defaultStart)
			stb.append("t").append(format(this.t));
		if (this.b!=defaultEnd)
			stb.append("b").append(format(this.b));
		if (this.r!=defaultEnd)
			stb.append("r").append(format(this.r));
		return stb.toString();
	}

	@Override
	public String toString() {
		return compose();
	}
}
