package com.kamesuta.mc.signpic.image.meta;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class ImageOffset implements ImageMeta.MetaParser {
	public static final float defaultOffset = 0.5f;

	public float x = 0f;
	public float y = 0f;
	public float z = 0f;

	/**
	 * L=left
	 * R=right
	 * D=down
	 * U=up
	 * B=back
	 * F=front
	 * @param src
	 */
	public ImageOffset parseOffset(final Map<String, String> meta, final String src) {
		float x = 0f;
		float y = 0f;
		float z = 0f;
		if (meta.containsKey("L")) if (StringUtils.isEmpty(meta.get("L"))) x -= defaultOffset; else x -= NumberUtils.toFloat(meta.get("L"), 0f);
		if (meta.containsKey("R")) if (StringUtils.isEmpty(meta.get("R"))) x += defaultOffset; else x += NumberUtils.toFloat(meta.get("R"), 0f);
		if (meta.containsKey("D")) if (StringUtils.isEmpty(meta.get("D"))) y -= defaultOffset; else y -= NumberUtils.toFloat(meta.get("D"), 0f);
		if (meta.containsKey("U")) if (StringUtils.isEmpty(meta.get("U"))) y += defaultOffset; else y += NumberUtils.toFloat(meta.get("U"), 0f);
		if (meta.containsKey("B")) if (StringUtils.isEmpty(meta.get("B"))) z -= defaultOffset; else z -= NumberUtils.toFloat(meta.get("B"), 0f);
		if (meta.containsKey("F")) if (StringUtils.isEmpty(meta.get("F"))) z += defaultOffset; else z += NumberUtils.toFloat(meta.get("F"), 0f);
		this.x = x;
		this.y = y;
		this.z = z;
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
	public String toString() {
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
}
