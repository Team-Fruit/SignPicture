package com.kamesuta.mc.signpic.image.meta;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
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

	public final ImageSize size = new ImageSize();
	public final ImageOffset offset = new ImageOffset();
	public final ImageRotation rotation = new ImageRotation();
	public final ImageTextureMap map = new ImageTextureMap();
	public final ImageAnimation animation = new ImageAnimation();
	private boolean hasInvalidMeta;

	public final Movie<SizeData> sizes = new Movie<SizeData>(this.size.get());
	public final Movie<OffsetData> offsets = new Movie<OffsetData>(this.offset.get());
	public final Movie<RotationData> rotations = new Movie<RotationData>(this.rotation.get());
	public final Movie<TextureMapData> maps = new Movie<TextureMapData>(this.map.get());
	public final Movie<AnimationData> animations = new Movie<AnimationData>(this.animation.get());

	public ImageMeta(final String src) {
		Validate.notNull(src);

		final TreeMap<Float, String> timeline = Maps.newTreeMap();

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

		boolean bb = true;

		for (final Iterator<Entry<Float, String>> itr = timeline.entrySet().iterator(); itr.hasNext();) {
			final Entry<Float, String> entry = itr.next();
			final float time = entry.getKey();
			final String meta = entry.getValue();

			this.size.reset();
			this.offset.reset();
			this.rotation.reset();
			this.map.reset();
			this.animation.reset();

			final Matcher mp = p.matcher(meta);
			while (mp.find()) {
				final int gcount = mp.groupCount();
				if (1<=gcount) {
					final String key = mp.group(1);
					final String value = 2<=gcount ? mp.group(2) : "";
					if (!StringUtils.isEmpty(key)||!StringUtils.isEmpty(value)) {
						final boolean a = this.size.parse(src, key, value);
						if (a)
							this.sizes.add(time, this.size.get());
						final boolean b = this.offset.parse(src, key, value);
						if (b)
							this.offsets.add(time, this.offset.get());
						final boolean c = this.rotation.parse(src, key, value);
						if (c)
							this.rotations.add(time, this.rotation.get());
						final boolean d = this.map.parse(src, key, value);
						if (d)
							this.maps.add(time, this.map.get());
						final boolean e = this.animation.parse(src, key, value);
						if (e)
							this.animations.add(time, this.animation.get());
						bb = (a||b||c||d||e)&&bb;
					}
				}
			}
		}

		this.hasInvalidMeta = this.hasInvalidMeta||!bb;
	}

	public boolean hasInvalidMeta() {
		return this.hasInvalidMeta;
	}

	public String compose() {
		return "{"+this.size+this.offset+this.rotation+this.map+this.animation+"}";
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
