package com.kamesuta.mc.signpic.image.meta;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class ImageTextureMap extends ImageMeta.MetaParser {
	public static final float defaultUV = 0f;
	// Width Height
	public static final float defaultWH = 1f;
	// Crossing Slitting
	public static final float defaultCS = 1f;
	public static final boolean defaultRepeat = true;
	public static final boolean defaultMipMap = true;

	public float u = defaultUV;
	public float v = defaultUV;
	public float w = defaultWH;
	public float h = defaultWH;
	public float c = defaultCS;
	public float s = defaultCS;
	public boolean r = defaultRepeat;
	public boolean m = defaultMipMap;

	@Override
	public ImageTextureMap reset() {
		this.u = defaultUV;
		this.v = defaultUV;
		this.w = defaultWH;
		this.h = defaultWH;
		this.c = defaultCS;
		this.s = defaultCS;
		this.r = defaultRepeat;
		this.m = defaultMipMap;
		return this;
	}

	@Override
	public boolean parse(final String src, final String key, final String value) {
		/* @formatter:off */
		if (StringUtils.equals(key, "u")) this.u = NumberUtils.toFloat(value, defaultUV);
		else if (StringUtils.equals(key, "v")) this.v = NumberUtils.toFloat(value, defaultUV);
		else if (StringUtils.equals(key, "w")) this.w = NumberUtils.toFloat(value, defaultWH);
		else if (StringUtils.equals(key, "h")) this.h = NumberUtils.toFloat(value, defaultWH);
		else if (StringUtils.equals(key, "c")) this.c = NumberUtils.toFloat(value, defaultCS);
		else if (StringUtils.equals(key, "s")) this.s = NumberUtils.toFloat(value, defaultCS);
		else if (StringUtils.equals(key, "r")) this.r = !defaultRepeat;
		else if (StringUtils.equals(key, "m")) this.m = !defaultMipMap;
		else return false;
		return true;
		/* @formatter:on */
	}

	@Override
	public String compose() {
		final StringBuilder stb = new StringBuilder();
		if (this.u!=defaultUV)
			stb.append("u").append(format(this.u));
		if (this.v!=defaultUV)
			stb.append("v").append(format(this.v));
		if (this.w!=defaultWH)
			stb.append("w").append(format(this.w));
		if (this.h!=defaultWH)
			stb.append("h").append(format(this.h));
		if (this.c!=defaultCS)
			stb.append("c").append(format(this.c));
		if (this.s!=defaultCS)
			stb.append("s").append(format(this.s));
		if (this.r!=defaultRepeat)
			stb.append("r");
		if (this.m!=defaultMipMap)
			stb.append("m");
		return stb.toString();
	}

	@Override
	public String toString() {
		return compose();
	}
}
