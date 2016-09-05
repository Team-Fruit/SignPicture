package com.kamesuta.mc.signpic.image.meta;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ImageMeta {
	protected static final Pattern p = Pattern.compile("(?:([^\\d-\\+Ee\\.]?)([\\d-\\+Ee\\.]*)?)+?");

	public final ImageSize size;
	public final ImageOffset offset;
	public final ImageRotation rotation;

	public ImageMeta() {
		this.size = new ImageSize();
		this.offset = new ImageOffset();
		this.rotation = new ImageRotation();
	}

	public ImageMeta parseMeta(final String src) {
		final Map<String, String> meta = parse(src);
		this.size.parseSize(meta);
		this.offset.parseOffset(meta);
		this.rotation.parseRotation(meta);
		return this;
	}

	protected static Map<String, String> parse(final String src) {
		final Matcher m = p.matcher(src);
		final Map<String, String> map = new HashMap<String, String>();
		while(m.find()){
			if (2 <= m.groupCount()) {
				final String key = m.group(1);
				final String value = m.group(2);
				if (!StringUtils.isEmpty(key) || !StringUtils.isEmpty(value))
					map.put(key, value);
			}
		}
		return map;
	}

	@Override
	public String toString() {
		return "[" + this.size + this.offset + this.rotation + "]";
	}

	public static interface MetaParser {
		public static final DecimalFormat signformat = new DecimalFormat("0.#");
	}
}
