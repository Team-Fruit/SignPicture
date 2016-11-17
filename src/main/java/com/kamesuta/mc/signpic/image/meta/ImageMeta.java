package com.kamesuta.mc.signpic.image.meta;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ImageMeta {
	protected static final Pattern p = Pattern.compile("(?:([^\\d-\\+Ee\\.]?)([\\d-\\+Ee\\.]*)?)+?");

	public final ImageSize size;
	public final ImageOffset offset;
	public final ImageRotation rotation;
	public final ImageTextureMap map;
	private boolean hasInvalidMeta = false;

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

	public ImageMeta parse(final String src) {
		this.size.reset();
		this.offset.reset();
		this.rotation.reset();
		boolean b = true;
		final Matcher m = p.matcher(src);
		while (m.find()) {
			final int gcount = m.groupCount();
			if (1<=gcount) {
				final String key = m.group(1);
				final String value = 2<=gcount ? m.group(2) : "";
				if (!StringUtils.isEmpty(key)||!StringUtils.isEmpty(value))
					b = parseMeta(src, key, value)&&b;
			}
		}
		this.hasInvalidMeta = !b;
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
		private static final DecimalFormat signformat = new DecimalFormat(".##");

		public static String format(final float f) {
			if (f==0)
				return "0";

			final String str = signformat.format(f);

			final String cut = ".0";

			int end = str.length();
			int last = cut.length();

			while (end!=0&&last!=0)
				if (cut.charAt(last-1)==str.charAt(end-1))
					end--;
				else
					last--;
			return str.substring(0, end);
		}

		public abstract boolean parse(String src, String key, String value);

		public abstract MetaParser reset();

		public abstract String compose();
	}
}
