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
import com.kamesuta.mc.signpic.attr.prop.AnimationData.AnimationBuilder;
import com.kamesuta.mc.signpic.attr.prop.OffsetData;
import com.kamesuta.mc.signpic.attr.prop.OffsetData.OffsetBuilder;
import com.kamesuta.mc.signpic.attr.prop.RotationData;
import com.kamesuta.mc.signpic.attr.prop.RotationData.DiffRotation;
import com.kamesuta.mc.signpic.attr.prop.RotationData.KeyRotation;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationBuilder;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.attr.prop.SizeData.SizeBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBoolean;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBoolean.TextureBooleanType;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBoolean.TextureBooleanBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureFloat;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureFloat.TextureFloatBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureFloat.TextureFloatType;

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

	public final PropAnimator<OffsetData, OffsetData, OffsetData> offsets = add(new PropAnimator<OffsetData, OffsetData, OffsetData>(new IPropBuilderBuilder<OffsetData, OffsetData>() {
		@Override
		public OffsetBuilder builder() {
			return new OffsetBuilder();
		}
	}));
	public final PropAnimator<KeyRotation, RotationData, DiffRotation> rotations = add(new PropAnimator<KeyRotation, RotationData, DiffRotation>(new IPropBuilderBuilder<DiffRotation, KeyRotation>() {
		@Override
		public RotationBuilder builder() {
			return new RotationBuilder();
		}
	}));

	private static class TexDataBuilder implements IPropBuilderBuilder<TextureFloat, TextureFloat> {
		private TextureFloatType type;

		public TexDataBuilder(final TextureFloatType type) {
			this.type = type;
		}

		@Override
		public IPropBuilder<TextureFloat, TextureFloat> builder() {
			return new TextureFloatBuilder(this.type);
		}
	}

	public final PropAnimator<TextureFloat, TextureFloat, TextureFloat> u = add(new PropAnimator<TextureFloat, TextureFloat, TextureFloat>(new TexDataBuilder(TextureFloatType.U)));
	public final PropAnimator<TextureFloat, TextureFloat, TextureFloat> v = add(new PropAnimator<TextureFloat, TextureFloat, TextureFloat>(new TexDataBuilder(TextureFloatType.V)));
	public final PropAnimator<TextureFloat, TextureFloat, TextureFloat> w = add(new PropAnimator<TextureFloat, TextureFloat, TextureFloat>(new TexDataBuilder(TextureFloatType.W)));
	public final PropAnimator<TextureFloat, TextureFloat, TextureFloat> h = add(new PropAnimator<TextureFloat, TextureFloat, TextureFloat>(new TexDataBuilder(TextureFloatType.H)));
	public final PropAnimator<TextureFloat, TextureFloat, TextureFloat> c = add(new PropAnimator<TextureFloat, TextureFloat, TextureFloat>(new TexDataBuilder(TextureFloatType.C)));
	public final PropAnimator<TextureFloat, TextureFloat, TextureFloat> s = add(new PropAnimator<TextureFloat, TextureFloat, TextureFloat>(new TexDataBuilder(TextureFloatType.S)));
	public final PropAnimator<TextureFloat, TextureFloat, TextureFloat> o = add(new PropAnimator<TextureFloat, TextureFloat, TextureFloat>(new TexDataBuilder(TextureFloatType.O)));

	private static class TexDataBooleanBuilder implements IPropBuilderBuilder<TextureBoolean, TextureBoolean> {
		private TextureBooleanType type;

		public TexDataBooleanBuilder(final TextureBooleanType type) {
			this.type = type;
		}

		@Override
		public TextureBooleanBuilder builder() {
			return new TextureBooleanBuilder(this.type);
		}
	}

	public final PropAnimator<TextureBoolean, TextureBoolean, TextureBoolean> r = add(new PropAnimator<TextureBoolean, TextureBoolean, TextureBoolean>(new TexDataBooleanBuilder(TextureBooleanType.R)));
	public final PropAnimator<TextureBoolean, TextureBoolean, TextureBoolean> m = add(new PropAnimator<TextureBoolean, TextureBoolean, TextureBoolean>(new TexDataBooleanBuilder(TextureBooleanType.M)));

	public CompoundAttr(final String src) {
		Validate.notNull(src);

		final TreeMap<Float, String> timeline = Maps.newTreeMap();

		final Matcher mgb = g.matcher(src);
		final String s = mgb.replaceAll("");
		timeline.put(0f, s);

		float current = 0;
		float lastinterval = defaultInterval;
		final Matcher mg = g.matcher(src);
		while (mg.find()) {
			final int gcount = mg.groupCount();
			if (2<=gcount) {
				final float time = NumberUtils.toFloat(mg.group(1), lastinterval);
				lastinterval = time;
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
