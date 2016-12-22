package com.kamesuta.mc.signpic.image.meta;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.image.meta.AnimationData.AnimationBuilder;
import com.kamesuta.mc.signpic.image.meta.MovieMeta.MovieBuilder;
import com.kamesuta.mc.signpic.image.meta.OffsetData.OffsetBuilder;
import com.kamesuta.mc.signpic.image.meta.RotationData.DiffRotation;
import com.kamesuta.mc.signpic.image.meta.RotationData.KeyRotation;
import com.kamesuta.mc.signpic.image.meta.RotationData.RotationBuilder;
import com.kamesuta.mc.signpic.image.meta.SizeData.SizeBuilder;
import com.kamesuta.mc.signpic.image.meta.TextureMapData.DataType;
import com.kamesuta.mc.signpic.image.meta.TextureMapData.DataTypeBoolean;
import com.kamesuta.mc.signpic.image.meta.TextureMapData.TextureMapBuilder;
import com.kamesuta.mc.signpic.image.meta.TextureMapData.TextureMapBooleanBuilder;
import com.kamesuta.mc.signpic.image.meta.TextureMapData.TextureMapDataBoolean;

public class ImageMeta {
	protected static final Pattern g = Pattern.compile("\\((?:([^\\)]*?)~)?(.*?)\\)");
	protected static final Pattern p = Pattern.compile("(?:([^\\d-\\+Ee\\.]?)([\\d-\\+Ee\\.]*)?)+?");

	private boolean hasInvalidMeta;

	private Set<MovieMeta<?, ?, ?>> metas = Sets.newHashSet();

	private <E extends MovieMeta<?, ?, ?>> E add(final E e) {
		this.metas.add(e);
		return e;
	}

	public final MovieMeta<AnimationData, AnimationData, AnimationData> animations = add(new MovieMeta<AnimationData, AnimationData, AnimationData>(new MovieBuilder<AnimationData, AnimationData>() {
		@Override
		public MetaMovie<AnimationData, AnimationData> builder() {
			return new AnimationBuilder();
		}
	}));
	public final MovieMeta<SizeData, SizeData, SizeData> sizes = add(new MovieMeta<SizeData, SizeData, SizeData>(new MovieBuilder<SizeData, SizeData>() {
		@Override
		public SizeBuilder builder() {
			return new SizeBuilder();
		}
	}));

	private static class OffsetDataBuilder implements MovieBuilder<OffsetData, OffsetData> {
		private String neg;
		private String pos;

		public OffsetDataBuilder(final String neg, final String pos) {
			this.neg = neg;
			this.pos = pos;
		}

		@Override
		public OffsetBuilder builder() {
			return new OffsetBuilder(this.neg, this.pos);
		}
	}

	public final MovieMeta<OffsetData, OffsetData, OffsetData> xoffsets = add(new MovieMeta<OffsetData, OffsetData, OffsetData>(new OffsetDataBuilder("L", "R")));
	public final MovieMeta<OffsetData, OffsetData, OffsetData> yoffsets = add(new MovieMeta<OffsetData, OffsetData, OffsetData>(new OffsetDataBuilder("D", "U")));
	public final MovieMeta<OffsetData, OffsetData, OffsetData> zoffsets = add(new MovieMeta<OffsetData, OffsetData, OffsetData>(new OffsetDataBuilder("B", "F")));
	public final MovieMeta<KeyRotation, RotationData, DiffRotation> rotations = add(new MovieMeta<KeyRotation, RotationData, DiffRotation>(new MovieBuilder<DiffRotation, KeyRotation>() {
		@Override
		public RotationBuilder builder() {
			return new RotationBuilder();
		}
	}));

	private static class TexDataBuilder implements MovieBuilder<TextureMapData, TextureMapData> {
		private DataType type;

		public TexDataBuilder(final DataType type) {
			this.type = type;
		}

		@Override
		public MetaMovie<TextureMapData, TextureMapData> builder() {
			return new TextureMapBuilder(this.type);
		}
	}

	public final MovieMeta<TextureMapData, TextureMapData, TextureMapData> u = add(new MovieMeta<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.U)));
	public final MovieMeta<TextureMapData, TextureMapData, TextureMapData> v = add(new MovieMeta<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.V)));
	public final MovieMeta<TextureMapData, TextureMapData, TextureMapData> w = add(new MovieMeta<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.W)));
	public final MovieMeta<TextureMapData, TextureMapData, TextureMapData> h = add(new MovieMeta<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.H)));
	public final MovieMeta<TextureMapData, TextureMapData, TextureMapData> c = add(new MovieMeta<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.C)));
	public final MovieMeta<TextureMapData, TextureMapData, TextureMapData> s = add(new MovieMeta<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.S)));
	public final MovieMeta<TextureMapData, TextureMapData, TextureMapData> o = add(new MovieMeta<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.O)));

	private static class TexDataBooleanBuilder implements MovieBuilder<TextureMapDataBoolean, TextureMapDataBoolean> {
		private DataTypeBoolean type;

		public TexDataBooleanBuilder(final DataTypeBoolean type) {
			this.type = type;
		}

		@Override
		public TextureMapBooleanBuilder builder() {
			return new TextureMapBooleanBuilder(this.type);
		}
	}

	public final MovieMeta<TextureMapDataBoolean, TextureMapDataBoolean, TextureMapDataBoolean> r = add(new MovieMeta<TextureMapData.TextureMapDataBoolean, TextureMapData.TextureMapDataBoolean, TextureMapData.TextureMapDataBoolean>(new TexDataBooleanBuilder(DataTypeBoolean.R)));
	public final MovieMeta<TextureMapDataBoolean, TextureMapDataBoolean, TextureMapDataBoolean> m = add(new MovieMeta<TextureMapData.TextureMapDataBoolean, TextureMapData.TextureMapDataBoolean, TextureMapData.TextureMapDataBoolean>(new TexDataBooleanBuilder(DataTypeBoolean.M)));

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
						boolean b = false;
						for (final MovieMeta<?, ?, ?> m : this.metas)
							b = m.parse(src, key, value)||b;
						bb = b&&bb;
					}
				}
			}

			Easings easing = Easings.easeLinear;
			if (this.animations.isParsed()) {
				final AnimationData anim = this.animations.getDiff();
				easing = anim.easing;
			}

			for (final MovieMeta<?, ?, ?> m : this.metas)
				m.next(time, easing);
		}

		this.hasInvalidMeta = !bb;

		Log.dev.info("signmeta={"+src+"}, unsupported="+!bb);
	}

	public boolean hasInvalidMeta() {
		return this.hasInvalidMeta;
	}
}
