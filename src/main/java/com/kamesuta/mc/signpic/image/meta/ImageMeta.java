package com.kamesuta.mc.signpic.image.meta;

import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.Maps;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.image.meta.RotationData.KeyRotation;

public class ImageMeta {
	protected static final Pattern g = Pattern.compile("\\((?:([^\\)]*?)~)?(.*?)\\)");
	protected static final Pattern p = Pattern.compile("(?:([^\\d-\\+Ee\\.]?)([\\d-\\+Ee\\.]*)?)+?");

	public final ImageSize size = new ImageSize();
	public final ImageOffset offset = new ImageOffset();
	public final ImageRotation rotation = new ImageRotation();
	public final ImageTextureMap map = new ImageTextureMap();
	public final ImageAnimation animation = new ImageAnimation();
	private boolean hasInvalidMeta;

	public final Movie<SizeData, SizeData> sizes = new Movie<SizeData, SizeData>(this.size.get());
	public final Movie<OffsetData, OffsetData> offsets = new Movie<OffsetData, OffsetData>(this.offset.get());
	private KeyRotation base;
	public final Movie<KeyRotation, RotationData> rotations = new Movie<KeyRotation, RotationData>(this.base = this.rotation.get(this.base));
	public final Movie<TextureMapData, TextureMapData> maps = new Movie<TextureMapData, TextureMapData>(this.map.get());
	public final Movie<AnimationData, AnimationData> animations = new Movie<AnimationData, AnimationData>(this.animation.get());

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

		this.size.reset();
		this.offset.reset();
		this.map.reset();
		this.animation.reset();

		for (final Entry<Float, String> entry : timeline.entrySet()) {
			final float time = entry.getKey();
			final String meta = entry.getValue();

			this.rotation.reset();

			boolean a = false;
			boolean b = false;
			boolean c = false;
			boolean d = false;
			boolean e = false;

			final Matcher mp = p.matcher(meta);
			while (mp.find()) {
				final int gcount = mp.groupCount();
				if (1<=gcount) {
					final String key = mp.group(1);
					final String value = 2<=gcount ? mp.group(2) : "";
					if (!StringUtils.isEmpty(key)||!StringUtils.isEmpty(value)) {
						final boolean ia = this.size.parse(src, key, value);
						final boolean ib = this.offset.parse(src, key, value);
						final boolean ic = this.rotation.parse(src, key, value);
						final boolean id = this.map.parse(src, key, value);
						final boolean ie = this.animation.parse(src, key, value);
						bb = (ia||ib||ic||id||ie)&&bb;
						a = a||ia;
						b = b||ib;
						c = c||ic;
						d = d||id;
						e = e||ie;
					}
				}
			}

			if (a)
				this.sizes.add(time, this.size.get());
			if (b)
				this.offsets.add(time, this.offset.get());
			if (c)
				this.rotations.add(time, this.base = this.rotation.get(this.base));
			if (d)
				this.maps.add(time, this.map.get());
			if (e)
				this.animations.add(time, this.animation.get());
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
