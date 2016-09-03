package com.kamesuta.mc.signpic.image;

import java.text.DecimalFormat;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class ImageOffset {
	public static final float defaultOffset = 0.5f;
	public static final DecimalFormat signformat = new DecimalFormat("0.#");

	public float x;
	public float y;
	public float z;

	public ImageOffset() {
	}

	public ImageOffset parseOffset(final Map<String, String> meta) {
		if (meta.containsKey("l")) if (StringUtils.isEmpty(meta.get("l"))) this.x -= defaultOffset; else this.x -= NumberUtils.toFloat(meta.get("l"), 0f);
		if (meta.containsKey("r")) if (StringUtils.isEmpty(meta.get("r"))) this.x += defaultOffset; else this.x += NumberUtils.toFloat(meta.get("r"), 0f);
		if (meta.containsKey("d")) if (StringUtils.isEmpty(meta.get("d"))) this.y -= defaultOffset; else this.y -= NumberUtils.toFloat(meta.get("d"), 0f);
		if (meta.containsKey("u")) if (StringUtils.isEmpty(meta.get("u"))) this.y += defaultOffset; else this.y += NumberUtils.toFloat(meta.get("u"), 0f);
		if (meta.containsKey("b")) if (StringUtils.isEmpty(meta.get("b"))) this.z -= defaultOffset; else this.z -= NumberUtils.toFloat(meta.get("b"), 0f);
		if (meta.containsKey("f")) if (StringUtils.isEmpty(meta.get("f"))) this.z += defaultOffset; else this.z += NumberUtils.toFloat(meta.get("f"), 0f);
		return this;
	}

	public String text() {
		final StringBuilder stb = new StringBuilder();
		if (this.x>0)
			if (this.x==defaultOffset) stb.append("l");
			else stb.append("l").append(signformat.format(this.x));
		else if (this.x<0)
			if (-this.x==defaultOffset) stb.append("r");
			else stb.append("r").append(signformat.format(-this.x));
		if (this.y>0)
			if (this.y==defaultOffset) stb.append("d");
			else stb.append("d").append(signformat.format(this.y));
		else if (this.y<0)
			if (-this.y==defaultOffset) stb.append("u");
			else stb.append("u").append(signformat.format(-this.y));
		if (this.z>0)
			if (this.z==defaultOffset) stb.append("b");
			else stb.append("b").append(signformat.format(this.z));
		else if (this.z<0)
			if (-this.z==defaultOffset) stb.append("f");
			else stb.append("f").append(signformat.format(-this.z));
		return stb.toString();
	}

	@Override
	public String toString() {
		return text();
	}
}
