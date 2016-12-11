package com.kamesuta.mc.signpic.image.meta;

import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.Maps;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.image.meta.RotationData.DiffRotation;
import com.kamesuta.mc.signpic.image.meta.RotationData.KeyRotation;

public class ImageMeta {
	protected static final Pattern g = Pattern.compile("\\((?:([^\\)]*?)~)?(.*?)\\)");
	protected static final Pattern p = Pattern.compile("(?:([^\\d-\\+Ee\\.]?)([\\d-\\+Ee\\.]*)?)+?");

	private boolean hasInvalidMeta;

	public final MovieMeta<AnimationData, AnimationData, AnimationData> animations = new MovieMeta<AnimationData, AnimationData, AnimationData>() {
		@Override
		public MetaMovie<AnimationData, AnimationData> builder() {
			return new ImageAnimation();
		}
	};
	public final MovieMeta<SizeData, SizeData, SizeData> sizes = new MovieMeta<SizeData, SizeData, SizeData>() {
		@Override
		public ImageSize builder() {
			return new ImageSize();
		}
	};
	public final MovieMeta<OffsetData, OffsetData, OffsetData> offsets = new MovieMeta<OffsetData, OffsetData, OffsetData>() {
		@Override
		public MetaMovie<OffsetData, OffsetData> builder() {
			return new ImageOffset();
		}
	};
	public final MovieMeta<KeyRotation, RotationData, DiffRotation> rotations = new MovieMeta<KeyRotation, RotationData, DiffRotation>() {
		@Override
		public ImageRotation builder() {
			return new ImageRotation();
		}
	};
	public final MovieMeta<TextureMapData, TextureMapData, TextureMapData> maps = new MovieMeta<TextureMapData, TextureMapData, TextureMapData>() {
		@Override
		public MetaMovie<TextureMapData, TextureMapData> builder() {
			return new ImageTextureMap();
		}
	};

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

		for (final Entry<Float, String> entry : timeline.entrySet()) {
			final float time = entry.getKey();
			final String meta = entry.getValue();

			final Matcher mp = p.matcher(meta);
			while (mp.find()) {
				final int gcount = mp.groupCount();
				if (1<=gcount) {
					final String key = mp.group(1);
					final String value = 2<=gcount ? mp.group(2) : "";
					if (!StringUtils.isEmpty(key)||!StringUtils.isEmpty(value)) {
						final boolean ia = this.animations.parse(src, key, value);
						final boolean ib = this.sizes.parse(src, key, value);
						final boolean ic = this.offsets.parse(src, key, value);
						final boolean id = this.rotations.parse(src, key, value);
						final boolean ie = this.maps.parse(src, key, value);
						bb = (ia||ib||ic||id||ie)&&bb;
					}
				}
			}

			Easings easing = Easings.easeLinear;
			if (this.animations.isParsed()) {
				final AnimationData anim = this.animations.getDiff();
				easing = anim.easing;
			}
			this.animations.next(time, easing);
			this.sizes.next(time, easing);
			this.offsets.next(time, easing);
			this.rotations.next(time, easing);
			this.maps.next(time, easing);
		}

		this.hasInvalidMeta = this.hasInvalidMeta||!bb;
	}

	public boolean hasInvalidMeta() {
		return this.hasInvalidMeta;
	}
}
