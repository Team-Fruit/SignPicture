package com.kamesuta.mc.signpic.attr;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

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
import com.kamesuta.mc.signpic.attr.prop.OffsetData.OffsetDoublePropBuilder;
import com.kamesuta.mc.signpic.attr.prop.OffsetData.OffsetPropBuilder;
import com.kamesuta.mc.signpic.attr.prop.PropSyntax;
import com.kamesuta.mc.signpic.attr.prop.RotationData;
import com.kamesuta.mc.signpic.attr.prop.RotationData.DiffRotation;
import com.kamesuta.mc.signpic.attr.prop.RotationData.KeyRotation;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationBuilder;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.attr.prop.SizeData.SizeBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBlend;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBlend.TextureBlendBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBlend.TextureBlendType;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBoolean;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBoolean.TextureBooleanBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBoolean.TextureBooleanType;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureFloat;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureFloat.TextureFloatBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureFloat.TextureFloatType;

public class CompoundAttr {
	public static final @Nonnull CompoundAttr Blank = new CompoundAttr();

	protected static final @Nonnull Pattern pg = Pattern.compile("\\((?:([^\\)]*?)~)?(.*?)\\)");
	protected static final @Nonnull Pattern pp = Pattern.compile("(?:([^\\d-\\+Ee\\.]?)([\\d-\\+Ee\\.]*)?)+?");

	public static final float defaultInterval = 1f;

	private boolean hasInvalidMeta;

	private @Nonnull Set<PropAnimator<?, ?, ?>> metas = Sets.newHashSet();

	private @Nonnull <E extends PropAnimator<?, ?, ?>> E add(final @Nonnull E e) {
		this.metas.add(e);
		return e;
	}

	public final @Nonnull PropAnimator<AnimationData, AnimationData, AnimationData> animations = add(new PropAnimator<AnimationData, AnimationData, AnimationData>(new IPropBuilderBuilder<AnimationData, AnimationData>() {
		@Override
		public @Nonnull IPropBuilder<AnimationData, AnimationData> builder() {
			return new AnimationBuilder();
		}
	}));
	public final @Nonnull PropAnimator<SizeData, SizeData, SizeData> sizes = add(new PropAnimator<SizeData, SizeData, SizeData>(new IPropBuilderBuilder<SizeData, SizeData>() {
		@Override
		public @Nonnull SizeBuilder builder() {
			return new SizeBuilder();
		}
	}));

	public final @Nonnull PropAnimator<OffsetData, OffsetData, OffsetData> offsets = add(new PropAnimator<OffsetData, OffsetData, OffsetData>(new IPropBuilderBuilder<OffsetData, OffsetData>() {
		@Override
		public @Nonnull OffsetBuilder builder() {
			return new OffsetBuilder(
					new OffsetDoublePropBuilder(PropSyntax.OFFSET_LEFT.id, PropSyntax.OFFSET_RIGHT.id),
					new OffsetDoublePropBuilder(PropSyntax.OFFSET_DOWN.id, PropSyntax.OFFSET_UP.id),
					new OffsetDoublePropBuilder(PropSyntax.OFFSET_BACK.id, PropSyntax.OFFSET_FRONT.id));
		}
	}));
	public final @Nonnull PropAnimator<OffsetData, OffsetData, OffsetData> centeroffsets = add(new PropAnimator<OffsetData, OffsetData, OffsetData>(new IPropBuilderBuilder<OffsetData, OffsetData>() {
		@Override
		public @Nonnull OffsetBuilder builder() {
			return new OffsetBuilder(
					new OffsetPropBuilder(PropSyntax.OFFSET_CENTER_X.id),
					new OffsetPropBuilder(PropSyntax.OFFSET_CENTER_Y.id),
					new OffsetPropBuilder(PropSyntax.OFFSET_CENTER_Z.id));
		}
	}));
	public final @Nonnull PropAnimator<KeyRotation, RotationData, DiffRotation> rotations = add(new PropAnimator<KeyRotation, RotationData, DiffRotation>(new IPropBuilderBuilder<DiffRotation, KeyRotation>() {
		@Override
		public @Nonnull RotationBuilder builder() {
			return new RotationBuilder();
		}
	}));

	private static class TexFloatBuilder implements IPropBuilderBuilder<TextureFloat, TextureFloat> {
		private @Nonnull TextureFloatType type;

		public TexFloatBuilder(final @Nonnull TextureFloatType type) {
			this.type = type;
		}

		@Override
		public @Nonnull IPropBuilder<TextureFloat, TextureFloat> builder() {
			return new TextureFloatBuilder(this.type);
		}
	}

	public final @Nonnull PropAnimator<TextureFloat, TextureFloat, TextureFloat> u = add(new PropAnimator<TextureFloat, TextureFloat, TextureFloat>(new TexFloatBuilder(TextureFloatType.U)));
	public final @Nonnull PropAnimator<TextureFloat, TextureFloat, TextureFloat> v = add(new PropAnimator<TextureFloat, TextureFloat, TextureFloat>(new TexFloatBuilder(TextureFloatType.V)));
	public final @Nonnull PropAnimator<TextureFloat, TextureFloat, TextureFloat> w = add(new PropAnimator<TextureFloat, TextureFloat, TextureFloat>(new TexFloatBuilder(TextureFloatType.W)));
	public final @Nonnull PropAnimator<TextureFloat, TextureFloat, TextureFloat> h = add(new PropAnimator<TextureFloat, TextureFloat, TextureFloat>(new TexFloatBuilder(TextureFloatType.H)));
	public final @Nonnull PropAnimator<TextureFloat, TextureFloat, TextureFloat> c = add(new PropAnimator<TextureFloat, TextureFloat, TextureFloat>(new TexFloatBuilder(TextureFloatType.C)));
	public final @Nonnull PropAnimator<TextureFloat, TextureFloat, TextureFloat> s = add(new PropAnimator<TextureFloat, TextureFloat, TextureFloat>(new TexFloatBuilder(TextureFloatType.S)));
	public final @Nonnull PropAnimator<TextureFloat, TextureFloat, TextureFloat> o = add(new PropAnimator<TextureFloat, TextureFloat, TextureFloat>(new TexFloatBuilder(TextureFloatType.O)));
	public final @Nonnull PropAnimator<TextureFloat, TextureFloat, TextureFloat> f = add(new PropAnimator<TextureFloat, TextureFloat, TextureFloat>(new TexFloatBuilder(TextureFloatType.F)));
	public final @Nonnull PropAnimator<TextureFloat, TextureFloat, TextureFloat> g = add(new PropAnimator<TextureFloat, TextureFloat, TextureFloat>(new TexFloatBuilder(TextureFloatType.G)));

	private static class TexBooleanBuilder implements IPropBuilderBuilder<TextureBoolean, TextureBoolean> {
		private @Nonnull TextureBooleanType type;

		public TexBooleanBuilder(final @Nonnull TextureBooleanType type) {
			this.type = type;
		}

		@Override
		public @Nonnull TextureBooleanBuilder builder() {
			return new TextureBooleanBuilder(this.type);
		}
	}

	public final @Nonnull PropAnimator<TextureBoolean, TextureBoolean, TextureBoolean> r = add(new PropAnimator<TextureBoolean, TextureBoolean, TextureBoolean>(new TexBooleanBuilder(TextureBooleanType.R)));
	public final @Nonnull PropAnimator<TextureBoolean, TextureBoolean, TextureBoolean> m = add(new PropAnimator<TextureBoolean, TextureBoolean, TextureBoolean>(new TexBooleanBuilder(TextureBooleanType.M)));
	public final @Nonnull PropAnimator<TextureBoolean, TextureBoolean, TextureBoolean> l = add(new PropAnimator<TextureBoolean, TextureBoolean, TextureBoolean>(new TexBooleanBuilder(TextureBooleanType.L)));

	private static class TexBlendBuilder implements IPropBuilderBuilder<TextureBlend, TextureBlend> {
		private @Nonnull TextureBlendType type;

		public TexBlendBuilder(final @Nonnull TextureBlendType type) {
			this.type = type;
		}

		@Override
		public @Nonnull TextureBlendBuilder builder() {
			return new TextureBlendBuilder(this.type);
		}
	}

	public final @Nonnull PropAnimator<TextureBlend, TextureBlend, TextureBlend> b = add(new PropAnimator<TextureBlend, TextureBlend, TextureBlend>(new TexBlendBuilder(TextureBlendType.B)));
	public final @Nonnull PropAnimator<TextureBlend, TextureBlend, TextureBlend> d = add(new PropAnimator<TextureBlend, TextureBlend, TextureBlend>(new TexBlendBuilder(TextureBlendType.D)));

	private CompoundAttr() {
	}

	public CompoundAttr(final @Nonnull String src) {
		Validate.notNull(src);

		final TreeMap<Float, String> timeline = Maps.newTreeMap();

		final Matcher mgb = pg.matcher(src);
		final String s = mgb.replaceAll("");
		timeline.put(0f, s);

		float current = 0;
		float lastinterval = defaultInterval;
		final Matcher mg = pg.matcher(src);
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

			final Matcher mp = pp.matcher(meta);
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
