package com.kamesuta.mc.signpic.image.meta;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.Maps;
import com.kamesuta.mc.signpic.Reference;

public class ImageMeta {
	protected static final Pattern g = Pattern.compile("\\((?:([^\\)]*?)~)?(.*?)\\)");
	protected static final Pattern p = Pattern.compile("(?:([^\\d-\\+Ee\\.]?)([\\d-\\+Ee\\.]*)?)+?");

	public final ImageSize size;
	public final ImageOffset offset;
	public final ImageRotation rotation;
	public final ImageTextureMap map;
	private boolean hasInvalidMeta;

	public ImageMeta() {
		this.size = new ImageSize();
		this.offset = new ImageOffset();
		this.rotation = new ImageRotation();
		this.map = new ImageTextureMap();
	}

	protected boolean parseMeta(final String src, final String key, final String value) {
		final boolean a = this.size.parse(src, key, value);
		final boolean b = this.offset.parse(src, key, value);
		final boolean c = this.rotation.parse(src, key, value);
		final boolean d = this.map.parse(src, key, value);
		return a||b||c||d;
	}

	public ImageMeta reset() {
		this.size.reset();
		this.offset.reset();
		this.rotation.reset();
		this.map.reset();
		this.hasInvalidMeta = false;
		return this;
	}

	public ImageMeta parse(final String src) {
		Validate.notNull(src);

		final Map<Float, String> timeline = Maps.newHashMap();

		final Matcher mgb = g.matcher(src);
		final String s = mgb.replaceAll("");
		timeline.put(0f, s);

		final Matcher mg = g.matcher(src);
		while (mg.find()) {
			final int gcount = mg.groupCount();
			if (2<=gcount) {
				final float time = NumberUtils.toFloat(mg.group(1));
				final String before = timeline.get(time);
				String meta = mg.group(2);
				if (before!=null)
					meta = before+meta;
				timeline.put(time, meta);
			}
		}

		Reference.logger.info(timeline);

		boolean b = true;
		final Matcher mp = p.matcher(s);
		while (mp.find()) {
			final int gcount = mp.groupCount();
			if (1<=gcount) {
				final String key = mp.group(1);
				final String value = 2<=gcount ? mp.group(2) : "";
				if (!StringUtils.isEmpty(key)||!StringUtils.isEmpty(value))
					b = parseMeta(s, key, value)&&b;
			}
		}
		this.hasInvalidMeta = this.hasInvalidMeta||!b;
		return this;
	}

	public ImageMeta init(final String src) {
		reset();
		parse(src);
		return this;
	}

	public boolean hasInvalidMeta() {
		return this.hasInvalidMeta;
	}

	public String compose() {
		return "{"+this.size+this.offset+this.rotation+this.map+"}";
	}

	@Override
	public String toString() {
		return compose();
	}

	public static abstract class MetaParser {
		public abstract boolean parse(String src, String key, String value);

		public abstract MetaParser reset();

		public abstract String compose();
	}
}
