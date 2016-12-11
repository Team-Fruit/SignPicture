package com.kamesuta.mc.signpic.image.meta;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class ImageTextureMap implements MetaMovie<TextureMapData, TextureMapData> {
	private float u = TextureMapData.defaultUV;
	private float v = TextureMapData.defaultUV;
	private float w = TextureMapData.defaultWH;
	private float h = TextureMapData.defaultWH;
	private float c = TextureMapData.defaultCS;
	private float s = TextureMapData.defaultCS;
	private float o = TextureMapData.defaultOpacity;
	private boolean r = TextureMapData.defaultRepeat;
	private boolean m = TextureMapData.defaultMipMap;

	@Override
	public TextureMapData diff(final TextureMapData base) {
		if (base==null)
			return new TextureMapData(this.u, this.v, this.w, this.h, this.c, this.s, this.o, this.r, this.m);
		else
			return new TextureMapData(base.u+this.u, base.v+this.v, base.w+this.w, base.h+this.h, base.c+this.c, base.s+this.s, base.o+this.o, this.r, this.m);
	}

	@Override
	public boolean parse(final String src, final String key, final String value) {
		/* @formatter:off */
		if (StringUtils.equals(key, "u")) this.u = NumberUtils.toFloat(value, TextureMapData.defaultUV);
		else if (StringUtils.equals(key, "v")) this.v = NumberUtils.toFloat(value, TextureMapData.defaultUV);
		else if (StringUtils.equals(key, "w")) this.w = NumberUtils.toFloat(value, TextureMapData.defaultWH);
		else if (StringUtils.equals(key, "h")) this.h = NumberUtils.toFloat(value, TextureMapData.defaultWH);
		else if (StringUtils.equals(key, "c")) this.c = NumberUtils.toFloat(value, TextureMapData.defaultCS);
		else if (StringUtils.equals(key, "s")) this.s = NumberUtils.toFloat(value, TextureMapData.defaultCS);
		else if (StringUtils.equals(key, "o")) this.o = NumberUtils.toFloat(value, TextureMapData.defaultOpacity);
		else if (StringUtils.equals(key, "r")) this.r = !TextureMapData.defaultRepeat;
		else if (StringUtils.equals(key, "m")) this.m = !TextureMapData.defaultMipMap;
		else return false;
		return true;
		/* @formatter:on */
	}

	@Override
	public String compose() {
		return diff(null).compose();
	}

	@Override
	public String toString() {
		return compose();
	}
}
