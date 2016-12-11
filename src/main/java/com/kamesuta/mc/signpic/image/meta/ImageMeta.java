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
import com.kamesuta.mc.signpic.image.meta.RotationData.KeyRotation;

public class ImageMeta {
	protected static final Pattern g = Pattern.compile("\\((?:([^\\)]*?)~)?(.*?)\\)");
	protected static final Pattern p = Pattern.compile("(?:([^\\d-\\+Ee\\.]?)([\\d-\\+Ee\\.]*)?)+?");

	private boolean hasInvalidMeta;

	public final Movie<SizeData, SizeData> sizes;
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

		SizeData basesize;
		OffsetData baseoffset;
		KeyRotation baserotation;
		TextureMapData basemap;
		AnimationData baseanimation;

		this.sizes = new Movie<SizeData, SizeData>(basesize = new ImageSize().get(null));
		this.offsets = new Movie<OffsetData, OffsetData>(baseoffset = new ImageOffset().get(null));
		this.rotations = new Movie<KeyRotation, RotationData>(baserotation = new ImageRotation().get(null));
		this.maps = new Movie<TextureMapData, TextureMapData>(basemap = new ImageTextureMap().get(null));
		this.animations = new Movie<AnimationData, AnimationData>(baseanimation = new ImageAnimation().get(null));

		boolean bb = true;

		for (final Entry<Float, String> entry : timeline.entrySet()) {
			final float time = entry.getKey();
			final String meta = entry.getValue();

			final ImageSize size = new ImageSize();
			final ImageOffset offset = new ImageOffset();
			final ImageRotation rotation = new ImageRotation();
			final ImageTextureMap map = new ImageTextureMap();
			final ImageAnimation animation = new ImageAnimation();

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
						final boolean ia = size.parse(src, key, value);
						final boolean ib = offset.parse(src, key, value);
						final boolean ic = rotation.parse(src, key, value);
						final boolean id = map.parse(src, key, value);
						final boolean ie = animation.parse(src, key, value);
						bb = (ia||ib||ic||id||ie)&&bb;
						a = a||ia;
						b = b||ib;
						c = c||ic;
						d = d||id;
						e = e||ie;
					}
				}
			}

			Easings easing = Easings.easeLinear;
			if (e) {
				final AnimationData anim = baseanimation = animation.get(baseanimation);
				easing = anim.easing;
				this.animations.add(time, anim, easing);
			}
			if (a)
				this.sizes.add(time, basesize = size.get(basesize), easing);
			if (b)
				this.offsets.add(time, baseoffset = offset.get(baseoffset), easing);
			if (c)
				this.rotations.add(time, baserotation = rotation.get(baserotation), easing);
			if (d)
				this.maps.add(time, basemap = map.get(basemap), easing);
		}

		this.hasInvalidMeta = this.hasInvalidMeta||!bb;
	}

	public boolean hasInvalidMeta() {
		return this.hasInvalidMeta;
	}
}
