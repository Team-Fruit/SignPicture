package com.kamesuta.mc.signpic.image.meta;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.google.common.collect.Maps;
import com.kamesuta.mc.signpic.Reference;

public class ImageMeta {
	protected static final Pattern g = Pattern.compile("\\((?:([^\\)]*?)~)?(.*?)\\)");
	protected static final Pattern p = Pattern.compile("(?:([^\\d-\\+Ee\\.]?)([\\d-\\+Ee\\.]*)?)+?");

	public TreeMap<Float, FrameData> frames = Maps.newTreeMap();
	public TreeMap<Float, SizeData> sizes = Maps.newTreeMap();
	public TreeMap<Float, OffsetData> offsets = Maps.newTreeMap();
	public TreeMap<Float, RotationData> rotations = Maps.newTreeMap();
	public TreeMap<Float, TextureMapData> maps = Maps.newTreeMap();

	public SizeData getSize() {
		return this.sizes.get(0f);
	}

	public OffsetData getOffset() {
		return this.offsets.get(0f);
	}

	public RotationData getRotation() {
		return this.rotations.get(0f);
	}

	public TextureMapData getMap() {
		return this.maps.get(0f);
	}

	public final ImageFrame frame;
	public final ImageSize size;
	public final ImageOffset offset;
	public final ImageRotation rotation;
	public final ImageTextureMap map;
	private boolean hasInvalidMeta;

	public ImageMeta() {
		this.frame = new ImageFrame();
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
		this.frames.clear();
		this.sizes.clear();
		this.offsets.clear();
		this.rotations.clear();
		this.maps.clear();
		this.hasInvalidMeta = false;
		return this;
	}

	public ImageMeta parse(final String src) {
		Validate.notNull(src);

		final TreeMap<Float, String> timeline = Maps.newTreeMap();

		final Matcher mgb = g.matcher(src);
		final String s = mgb.replaceAll("");
		timeline.put(0f, s);

		final Matcher mg = g.matcher(src);
		while (mg.find()) {
			final int gcount = mg.groupCount();
			if (2<=gcount) {
				final String timesrc = mg.group(1);
				final float time;
				this.frame.reset();
				if (timesrc!=null) {
					final Matcher tmp = p.matcher(timesrc);
					while (tmp.find()) {
						final int tgcount = tmp.groupCount();
						if (1<=tgcount) {
							final String key = tmp.group(1);
							final String value = 2<=tgcount ? tmp.group(2) : "";
							if (!StringUtils.isEmpty(key)||!StringUtils.isEmpty(value))
								this.frame.parse(timesrc, key, value);
						}
					}
				}
				time = this.frame.time;
				this.frames.put(time, this.frame.get());
				final String before = timeline.get(time);
				String meta = mg.group(2);
				if (before!=null)
					meta = before+meta;
				timeline.put(time, meta);
			}
		}

		Reference.logger.info(timeline);

		boolean b = true;

		for (final Iterator<Entry<Float, String>> itr = timeline.entrySet().iterator(); itr.hasNext();) {
			final Entry<Float, String> entry = itr.next();
			final float time = entry.getKey();
			final String meta = entry.getValue();

			this.size.reset();
			this.offset.reset();
			this.rotation.reset();
			this.map.reset();

			final Matcher mp = p.matcher(meta);
			while (mp.find()) {
				final int gcount = mp.groupCount();
				if (1<=gcount) {
					final String key = mp.group(1);
					final String value = 2<=gcount ? mp.group(2) : "";
					if (!StringUtils.isEmpty(key)||!StringUtils.isEmpty(value))
						b = parseMeta(meta, key, value)&&b;
				}
			}

			this.sizes.put(time, this.size.get());
			this.offsets.put(time, this.offset.get());
			this.rotations.put(time, this.rotation.get());
			this.maps.put(time, this.map.get());
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
