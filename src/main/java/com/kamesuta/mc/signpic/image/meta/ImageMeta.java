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
import com.kamesuta.mc.signpic.image.meta.MovieMeta.SizeMovieMeta;
import com.kamesuta.mc.signpic.image.meta.RotationData.KeyRotation;

public class ImageMeta {
	protected static final Pattern g = Pattern.compile("\\((?:([^\\)]*?)~)?(.*?)\\)");
	protected static final Pattern p = Pattern.compile("(?:([^\\d-\\+Ee\\.]?)([\\d-\\+Ee\\.]*)?)+?");

	private boolean hasInvalidMeta;

	public final SizeMovieMeta sizes = new SizeMovieMeta();
	public final Movie<OffsetData, OffsetData> offsets;
	public final Movie<KeyRotation, RotationData> rotations;
	public final Movie<TextureMapData, TextureMapData> maps;
	public final Movie<AnimationData, AnimationData> animations;

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

		OffsetData baseoffset;
		KeyRotation baserotation;
		TextureMapData basemap;
		AnimationData baseanimation;

		this.offsets = new Movie<OffsetData, OffsetData>(baseoffset = new ImageOffset().diff(null));
		this.rotations = new Movie<KeyRotation, RotationData>(baserotation = new ImageRotation().diff(null));
		this.maps = new Movie<TextureMapData, TextureMapData>(basemap = new ImageTextureMap().diff(null));
		this.animations = new Movie<AnimationData, AnimationData>(baseanimation = new ImageAnimation().diff(null));

		boolean bb = true;

		for (final Entry<Float, String> entry : timeline.entrySet()) {
			final float time = entry.getKey();
			final String meta = entry.getValue();

			final ImageOffset offset = new ImageOffset();
			final ImageRotation rotation = new ImageRotation();
			final ImageTextureMap map = new ImageTextureMap();
			final ImageAnimation animation = new ImageAnimation();

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
						final boolean ia = this.sizes.parse(src, key, value);
						final boolean ib = offset.parse(src, key, value);
						final boolean ic = rotation.parse(src, key, value);
						final boolean id = map.parse(src, key, value);
						final boolean ie = animation.parse(src, key, value);
						bb = (ia||ib||ic||id||ie)&&bb;
						b = b||ib;
						c = c||ic;
						d = d||id;
						e = e||ie;
					}
				}
			}

			Easings easing = Easings.easeLinear;
			if (e) {
				final AnimationData anim = baseanimation = animation.diff(baseanimation);
				easing = anim.easing;
				this.animations.add(time, anim, easing);
			}
			this.sizes.next(time, easing);
			if (b)
				this.offsets.add(time, baseoffset = offset.diff(baseoffset), easing);
			if (c)
				this.rotations.add(time, baserotation = rotation.diff(baserotation), easing);
			if (d)
				this.maps.add(time, basemap = map.diff(basemap), easing);
		}

		this.hasInvalidMeta = this.hasInvalidMeta||!bb;
	}

	public boolean hasInvalidMeta() {
		return this.hasInvalidMeta;
	}
}
