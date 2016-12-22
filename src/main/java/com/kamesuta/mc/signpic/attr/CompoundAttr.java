package com.kamesuta.mc.signpic.attr;

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
import com.kamesuta.mc.signpic.attr.PropAnimator.IPropBuilderBuilder;
import com.kamesuta.mc.signpic.attr.prop.AnimationData;
import com.kamesuta.mc.signpic.attr.prop.OffsetData;
import com.kamesuta.mc.signpic.attr.prop.RotationData;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.attr.prop.TextureMapData;
import com.kamesuta.mc.signpic.attr.prop.AnimationData.AnimationBuilder;
import com.kamesuta.mc.signpic.attr.prop.OffsetData.OffsetBuilder;
import com.kamesuta.mc.signpic.attr.prop.RotationData.DiffRotation;
import com.kamesuta.mc.signpic.attr.prop.RotationData.KeyRotation;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationBuilder;
import com.kamesuta.mc.signpic.attr.prop.SizeData.SizeBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureMapData.DataType;
import com.kamesuta.mc.signpic.attr.prop.TextureMapData.DataTypeBoolean;
import com.kamesuta.mc.signpic.attr.prop.TextureMapData.TextureMapBooleanBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureMapData.TextureMapBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureMapData.TextureMapDataBoolean;

public class CompoundAttr {
	protected static final Pattern g = Pattern.compile("\\((?:([^\\)]*?)~)?(.*?)\\)");
	protected static final Pattern p = Pattern.compile("(?:([^\\d-\\+Ee\\.]?)([\\d-\\+Ee\\.]*)?)+?");

	public static final float defaultInterval = 1f;

	private boolean hasInvalidMeta;

	private Set<PropAnimator<?, ?, ?>> metas = Sets.newHashSet();

	private <E extends PropAnimator<?, ?, ?>> E add(final E e) {
		this.metas.add(e);
		return e;
	}

	public final PropAnimator<AnimationData, AnimationData, AnimationData> animations = add(new PropAnimator<AnimationData, AnimationData, AnimationData>(new IPropBuilderBuilder<AnimationData, AnimationData>() {
		@Override
		public IPropBuilder<AnimationData, AnimationData> builder() {
			return new AnimationBuilder();
		}
	}));
	public final PropAnimator<SizeData, SizeData, SizeData> sizes = add(new PropAnimator<SizeData, SizeData, SizeData>(new IPropBuilderBuilder<SizeData, SizeData>() {
		@Override
		public SizeBuilder builder() {
			return new SizeBuilder();
		}
	}));

	private static class OffsetDataBuilder implements IPropBuilderBuilder<OffsetData, OffsetData> {
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

	public final PropAnimator<OffsetData, OffsetData, OffsetData> xoffsets = add(new PropAnimator<OffsetData, OffsetData, OffsetData>(new OffsetDataBuilder("L", "R")));
	public final PropAnimator<OffsetData, OffsetData, OffsetData> yoffsets = add(new PropAnimator<OffsetData, OffsetData, OffsetData>(new OffsetDataBuilder("D", "U")));
	public final PropAnimator<OffsetData, OffsetData, OffsetData> zoffsets = add(new PropAnimator<OffsetData, OffsetData, OffsetData>(new OffsetDataBuilder("B", "F")));
	public final PropAnimator<KeyRotation, RotationData, DiffRotation> rotations = add(new PropAnimator<KeyRotation, RotationData, DiffRotation>(new IPropBuilderBuilder<DiffRotation, KeyRotation>() {
		@Override
		public RotationBuilder builder() {
			return new RotationBuilder();
		}
	}));

	private static class TexDataBuilder implements IPropBuilderBuilder<TextureMapData, TextureMapData> {
		private DataType type;

		public TexDataBuilder(final DataType type) {
			this.type = type;
		}

		@Override
		public IPropBuilder<TextureMapData, TextureMapData> builder() {
			return new TextureMapBuilder(this.type);
		}
	}

	public final PropAnimator<TextureMapData, TextureMapData, TextureMapData> u = add(new PropAnimator<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.U)));
	public final PropAnimator<TextureMapData, TextureMapData, TextureMapData> v = add(new PropAnimator<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.V)));
	public final PropAnimator<TextureMapData, TextureMapData, TextureMapData> w = add(new PropAnimator<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.W)));
	public final PropAnimator<TextureMapData, TextureMapData, TextureMapData> h = add(new PropAnimator<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.H)));
	public final PropAnimator<TextureMapData, TextureMapData, TextureMapData> c = add(new PropAnimator<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.C)));
	public final PropAnimator<TextureMapData, TextureMapData, TextureMapData> s = add(new PropAnimator<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.S)));
	public final PropAnimator<TextureMapData, TextureMapData, TextureMapData> o = add(new PropAnimator<TextureMapData, TextureMapData, TextureMapData>(new TexDataBuilder(DataType.O)));

	private static class TexDataBooleanBuilder implements IPropBuilderBuilder<TextureMapDataBoolean, TextureMapDataBoolean> {
		private DataTypeBoolean type;

		public TexDataBooleanBuilder(final DataTypeBoolean type) {
			this.type = type;
		}

		@Override
		public TextureMapBooleanBuilder builder() {
			return new TextureMapBooleanBuilder(this.type);
		}
	}

	public final PropAnimator<TextureMapDataBoolean, TextureMapDataBoolean, TextureMapDataBoolean> r = add(new PropAnimator<TextureMapData.TextureMapDataBoolean, TextureMapData.TextureMapDataBoolean, TextureMapData.TextureMapDataBoolean>(new TexDataBooleanBuilder(DataTypeBoolean.R)));
	public final PropAnimator<TextureMapDataBoolean, TextureMapDataBoolean, TextureMapDataBoolean> m = add(new PropAnimator<TextureMapData.TextureMapDataBoolean, TextureMapData.TextureMapDataBoolean, TextureMapData.TextureMapDataBoolean>(new TexDataBooleanBuilder(DataTypeBoolean.M)));

	public CompoundAttr(final String src) {
		Validate.notNull(src);

		final TreeMap<Float, String> timeline = Maps.newTreeMap();

		final Matcher mgb = g.matcher(src);
		final String s = mgb.replaceAll("");
		timeline.put(0f, s);

		float current = 0;
		final Matcher mg = g.matcher(src);
		while (mg.find()) {
			final int gcount = mg.groupCount();
			if (2<=gcount) {
				final float time = NumberUtils.toFloat(mg.group(1), defaultInterval);
				current += time;
				final String before = timeline.get(current);
				String meta = mg.group(2);
				if (before!=null)
					meta = before+meta;
				timeline.put(current, meta);
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
						for (final PropAnimator<?, ?, ?> m : this.metas)
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

			for (final PropAnimator<?, ?, ?> m : this.metas)
				m.next(time, easing);
		}

		this.hasInvalidMeta = !bb;

		Log.dev.info("signmeta={"+src+"}, unsupported="+!bb);
	}

	public boolean hasInvalidMeta() {
		return this.hasInvalidMeta;
	}
}
