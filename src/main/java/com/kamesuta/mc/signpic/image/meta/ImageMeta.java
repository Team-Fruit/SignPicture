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

	public ImageMeta() {
		this.size = new ImageSize();
		this.offset = new ImageOffset();
		this.rotation = new ImageRotation();
	}

	protected ImageMeta parseMeta(final String src, final String key, final String value) {
		this.size.parse(src, key, value);
		this.offset.parse(src, key, value);
		this.rotation.parse(src, key, value);
		return this;
	}

	public ImageMeta parse(final String src) {
		this.size.reset();
		this.offset.reset();
		this.rotation.reset();
		final Matcher m = p.matcher(src);
		while(m.find()){
			if (2 <= m.groupCount()) {
				final String key = m.group(1);
				final String value = m.group(2);
				if (!StringUtils.isEmpty(key) || !StringUtils.isEmpty(value))
					parseMeta(src, key, value);
			}
		}
		return this;
	}

	public String compose() {
		return "[" + this.size + this.offset + this.rotation + "]";
	}

	@Override
	public String toString() {
		return compose();
	}

	public static abstract class MetaParser {
		private static final DecimalFormat signformat = new DecimalFormat(".##");

		public static String format(final float f) {
			if (f == 0)
				return "0";

			final String str = signformat.format(f);

			final String cut = ".0";

			int end = str.length();
			int last = cut.length();

			while (end != 0 && last != 0) {
				if (cut.charAt(last - 1) == str.charAt(end - 1))
					end--;
				else
					last--;
			}
			return str.substring(0, end);
		}

		public abstract MetaParser parse(String src, String key, String value);

		public abstract MetaParser reset();

		public abstract String compose();
	}
}
