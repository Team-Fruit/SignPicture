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
import com.kamesuta.mc.signpic.image.meta.ImageTextureMap.ImageTextureMapBoolean;
import com.kamesuta.mc.signpic.image.meta.MovieMeta.MovieBuilder;
import com.kamesuta.mc.signpic.image.meta.RotationData.DiffRotation;
import com.kamesuta.mc.signpic.image.meta.RotationData.KeyRotation;
import com.kamesuta.mc.signpic.image.meta.TextureMapData.DataType;
import com.kamesuta.mc.signpic.image.meta.TextureMapData.DataTypeBoolean;
import com.kamesuta.mc.signpic.image.meta.TextureMapData.TextureMapDataBoolean;

public class ImageMeta {
	protected static final Pattern g = Pattern.compile("\\((?:([^\\)]*?)~)?(.*?)\\)");
	protected static final Pattern p = Pattern.compile("(?:([^\\d-\\+Ee\\.]?)([\\d-\\+Ee\\.]*)?)+?");

	private boolean hasInvalidMeta;

	public final MovieMeta<AnimationData, AnimationData, AnimationData> animations = new MovieMeta<AnimationData, AnimationData, AnimationData>(new MovieBuilder<AnimationData, AnimationData>() {
		@Override
		public MetaMovie<AnimationData, AnimationData> builder() {
			return new ImageAnimation();
		}
	});
	public final MovieMeta<SizeData, SizeData, SizeData> sizes = new MovieMeta<SizeData, SizeData, SizeData>(new MovieBuilder<SizeData, SizeData>() {
		@Override
		public ImageSize builder() {
			return new ImageSize();
		}
	});

	private static class OffsetDataBuilder implements MovieBuilder<OffsetData, OffsetData> {
		private String neg;
		private String pos;

		public OffsetDataBuilder(final String neg, final String pos) {
			this.neg = neg;
			this.pos = pos;
		}

		@Override
		public ImageOffset builder() {
			return new ImageOffset(this.neg, this.pos);
		}
	}

	public final MovieMeta<OffsetData, OffsetData, OffsetData> xoffsets = new MovieMeta<OffsetData, OffsetData, OffsetData>(new OffsetDataBuilder("L", "R"));
	public final MovieMeta<OffsetData, OffsetData, OffsetData> yoffsets = new MovieMeta<OffsetData, OffsetData, OffsetData>(new OffsetDataBuilder("D", "U"));
	public final MovieMeta<OffsetData, OffsetData, OffsetData> zoffsets = new MovieMeta<OffsetData, OffsetData, OffsetData>(new OffsetDataBuilder("B", "F"));
	public final MovieMeta<KeyRotation, RotationData, DiffRotation> rotations = new MovieMeta<KeyRotation, RotationData, DiffRotation>(new MovieBuilder<DiffRotation, KeyRotation>() {
		@Override
		public ImageRotation builder() {
			return new ImageRotation();
		}
	});

	private static class TexDataBuilder implements MovieBuilder<TextureMapData, TextureMapData> {
		private DataType type;

		public TexDataBuilder(final DataType type) {
			this.type = type;
		}

		@Override
		public MetaMovie<TextureMapData, TextureMapData> builder() {
			return new ImageTextureMap(this.type);
		}
	}

	public final MovieMeta<TextureMapData, TextureMapData, TextureMapData> u = new MovieMeta<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.U));
	public final MovieMeta<TextureMapData, TextureMapData, TextureMapData> v = new MovieMeta<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.V));
	public final MovieMeta<TextureMapData, TextureMapData, TextureMapData> w = new MovieMeta<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.W));
	public final MovieMeta<TextureMapData, TextureMapData, TextureMapData> h = new MovieMeta<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.H));
	public final MovieMeta<TextureMapData, TextureMapData, TextureMapData> c = new MovieMeta<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.C));
	public final MovieMeta<TextureMapData, TextureMapData, TextureMapData> s = new MovieMeta<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.S));
	public final MovieMeta<TextureMapData, TextureMapData, TextureMapData> o = new MovieMeta<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.O));

	private static class TexDataBooleanBuilder implements MovieBuilder<TextureMapDataBoolean, TextureMapDataBoolean> {
		private DataTypeBoolean type;

		public TexDataBooleanBuilder(final DataTypeBoolean type) {
			this.type = type;
		}

		@Override
		public ImageTextureMapBoolean builder() {
			return new ImageTextureMapBoolean(this.type);
		}
	}

	public final MovieMeta<TextureMapDataBoolean, TextureMapDataBoolean, TextureMapDataBoolean> r = new MovieMeta<TextureMapData.TextureMapDataBoolean, TextureMapData.TextureMapDataBoolean, TextureMapData.TextureMapDataBoolean>(new TexDataBooleanBuilder(DataTypeBoolean.R));
	public final MovieMeta<TextureMapDataBoolean, TextureMapDataBoolean, TextureMapDataBoolean> m = new MovieMeta<TextureMapData.TextureMapDataBoolean, TextureMapData.TextureMapDataBoolean, TextureMapData.TextureMapDataBoolean>(new TexDataBooleanBuilder(DataTypeBoolean.M));

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
						final boolean ic1 = this.xoffsets.parse(src, key, value);
						final boolean ic2 = this.yoffsets.parse(src, key, value);
						final boolean ic3 = this.zoffsets.parse(src, key, value);
						final boolean id = this.rotations.parse(src, key, value);
						final boolean ie1 = this.u.parse(src, key, value);
						final boolean ie2 = this.v.parse(src, key, value);
						final boolean ie3 = this.w.parse(src, key, value);
						final boolean ie4 = this.h.parse(src, key, value);
						final boolean ie5 = this.c.parse(src, key, value);
						final boolean ie6 = this.s.parse(src, key, value);
						final boolean ie7 = this.o.parse(src, key, value);
						final boolean ie8 = this.r.parse(src, key, value);
						final boolean ie9 = this.m.parse(src, key, value);
						bb = (ia||ib||ic1||ic2||ic3||id||ie1||ie2||ie3||ie4||ie5||ie6||ie7||ie8||ie9)&&bb;
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
			this.xoffsets.next(time, easing);
			this.yoffsets.next(time, easing);
			this.zoffsets.next(time, easing);
			this.rotations.next(time, easing);
			this.u.next(time, easing);
			this.v.next(time, easing);
			this.w.next(time, easing);
			this.h.next(time, easing);
			this.c.next(time, easing);
			this.s.next(time, easing);
			this.o.next(time, easing);
			this.r.next(time, easing);
			this.m.next(time, easing);
		}

		this.hasInvalidMeta = this.hasInvalidMeta||!bb;
	}

	public boolean hasInvalidMeta() {
		return this.hasInvalidMeta;
	}
}
