package com.kamesuta.mc.signpic.image.meta;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class ImageRotation implements ImageMeta.MetaParser {
	public static final float defaultOffset = 0.5f;

	public float h = 0f;
	public float s = 0f;
	public float t = 0f;

	/**
	 * h=phi
	 * s=psi
	 * t=theta
	 */
	public ImageRotation parseRotation(final Map<String, String> meta) {
		if (meta.containsKey("h")) if (StringUtils.isEmpty(meta.get("h"))) this.h += defaultOffset; else this.h += NumberUtils.toFloat(meta.get("h"), 0f);
		if (meta.containsKey("s")) if (StringUtils.isEmpty(meta.get("s"))) this.s += defaultOffset; else this.s += NumberUtils.toFloat(meta.get("s"), 0f);
		if (meta.containsKey("t")) if (StringUtils.isEmpty(meta.get("t"))) this.t += defaultOffset; else this.t += NumberUtils.toFloat(meta.get("t"), 0f);
		return this;
	}

	/**
	 * h=phi
	 * s=psi
	 * t=theta
	 */
	@Override
	public String toString() {
		final StringBuilder stb = new StringBuilder();
		if (this.h>0)
			if (this.h==defaultOffset) stb.append("l");
			else stb.append("l").append(signformat.format(this.h));
		else if (this.h<0)
			if (-this.h==defaultOffset) stb.append("r");
			else stb.append("r").append(signformat.format(-this.h));
		if (this.s>0)
			if (this.s==defaultOffset) stb.append("d");
			else stb.append("d").append(signformat.format(this.s));
		else if (this.s<0)
			if (-this.s==defaultOffset) stb.append("u");
			else stb.append("u").append(signformat.format(-this.s));
		if (this.t>0)
			if (this.t==defaultOffset) stb.append("b");
			else stb.append("b").append(signformat.format(this.t));
		else if (this.t<0)
			if (-this.t==defaultOffset) stb.append("f");
			else stb.append("f").append(signformat.format(-this.t));
		return stb.toString();
	}
}
