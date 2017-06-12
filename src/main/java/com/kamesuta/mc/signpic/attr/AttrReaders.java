package com.kamesuta.mc.signpic.attr;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.attr.prop.AnimationData;
import com.kamesuta.mc.signpic.attr.prop.OffsetData;
import com.kamesuta.mc.signpic.attr.prop.RotationData;
import com.kamesuta.mc.signpic.attr.prop.RotationData.DiffRotation;
import com.kamesuta.mc.signpic.attr.prop.RotationData.KeyRotation;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBlend;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBoolean;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureFloat;

public class AttrReaders {
	public static final @Nonnull AttrReaders Blank = new AttrReaders();

	private boolean hasInvalidMeta;

	private @Nonnull Set<PropReader<?, ?, ?>> metas = Sets.newHashSet();

	private @Nonnull <E extends PropReader<?, ?, ?>> E add(final @Nonnull E e) {
		this.metas.add(e);
		return e;
	}

	public final @Nonnull PropReader<AnimationData, AnimationData, AnimationData> animations = add(new PropReader<AnimationData, AnimationData, AnimationData>(Attrs.ANIMATION.getReader()));
	public final @Nonnull PropReader<SizeData, SizeData, SizeData> sizes = add(new PropReader<SizeData, SizeData, SizeData>(Attrs.SIZE.getReader()));
	public final @Nonnull PropReader<OffsetData, OffsetData, OffsetData> offsets = add(new PropReader<OffsetData, OffsetData, OffsetData>(Attrs.OFFSET.getReader()));
	public final @Nonnull PropReader<OffsetData, OffsetData, OffsetData> centeroffsets = add(new PropReader<OffsetData, OffsetData, OffsetData>(Attrs.OFFSET_CENTER.getReader()));
	public final @Nonnull PropReader<KeyRotation, RotationData, DiffRotation> rotations = add(new PropReader<KeyRotation, RotationData, DiffRotation>(Attrs.ROTATION.getReader()));
	public final @Nonnull PropReader<TextureFloat, TextureFloat, TextureFloat> u = add(new PropReader<TextureFloat, TextureFloat, TextureFloat>(Attrs.TEXTURE_X.getReader()));
	public final @Nonnull PropReader<TextureFloat, TextureFloat, TextureFloat> v = add(new PropReader<TextureFloat, TextureFloat, TextureFloat>(Attrs.TEXTURE_Y.getReader()));
	public final @Nonnull PropReader<TextureFloat, TextureFloat, TextureFloat> w = add(new PropReader<TextureFloat, TextureFloat, TextureFloat>(Attrs.TEXTURE_W.getReader()));
	public final @Nonnull PropReader<TextureFloat, TextureFloat, TextureFloat> h = add(new PropReader<TextureFloat, TextureFloat, TextureFloat>(Attrs.TEXTURE_H.getReader()));
	public final @Nonnull PropReader<TextureFloat, TextureFloat, TextureFloat> c = add(new PropReader<TextureFloat, TextureFloat, TextureFloat>(Attrs.TEXTURE_SPLIT_W.getReader()));
	public final @Nonnull PropReader<TextureFloat, TextureFloat, TextureFloat> s = add(new PropReader<TextureFloat, TextureFloat, TextureFloat>(Attrs.TEXTURE_SPLIT_H.getReader()));
	public final @Nonnull PropReader<TextureFloat, TextureFloat, TextureFloat> o = add(new PropReader<TextureFloat, TextureFloat, TextureFloat>(Attrs.TEXTURE_OPACITY.getReader()));
	public final @Nonnull PropReader<TextureFloat, TextureFloat, TextureFloat> f = add(new PropReader<TextureFloat, TextureFloat, TextureFloat>(Attrs.TEXTURE_LIGHT_X.getReader()));
	public final @Nonnull PropReader<TextureFloat, TextureFloat, TextureFloat> g = add(new PropReader<TextureFloat, TextureFloat, TextureFloat>(Attrs.TEXTURE_LIGHT_Y.getReader()));
	public final @Nonnull PropReader<TextureBoolean, TextureBoolean, TextureBoolean> r = add(new PropReader<TextureBoolean, TextureBoolean, TextureBoolean>(Attrs.TEXTURE_REPEAT.getReader()));
	public final @Nonnull PropReader<TextureBoolean, TextureBoolean, TextureBoolean> m = add(new PropReader<TextureBoolean, TextureBoolean, TextureBoolean>(Attrs.TEXTURE_MIPMAP.getReader()));
	public final @Nonnull PropReader<TextureBoolean, TextureBoolean, TextureBoolean> l = add(new PropReader<TextureBoolean, TextureBoolean, TextureBoolean>(Attrs.TEXTURE_LIGHTING.getReader()));
	public final @Nonnull PropReader<TextureBlend, TextureBlend, TextureBlend> b = add(new PropReader<TextureBlend, TextureBlend, TextureBlend>(Attrs.TEXTURE_BLEND_SRC.getReader()));
	public final @Nonnull PropReader<TextureBlend, TextureBlend, TextureBlend> d = add(new PropReader<TextureBlend, TextureBlend, TextureBlend>(Attrs.TEXTURE_BLEND_DST.getReader()));

	private AttrReaders() {
	}

	public AttrReaders(final @Nonnull String src) {
		Validate.notNull(src);

		final TreeMap<Float, String> timeline = Maps.newTreeMap();

		final Matcher mgb = Attrs.pg.matcher(src);
		final String s = mgb.replaceAll("");
		timeline.put(0f, s);

		float current = 0;
		final Matcher mg = Attrs.pg.matcher(src);
		while (mg.find()) {
			final int gcount = mg.groupCount();
			if (2<=gcount) {
				final float time = NumberUtils.toFloat(mg.group(1), Attrs.defaultInterval);
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

			final Matcher mp = Attrs.pp.matcher(meta);
			while (mp.find()) {
				final int gcount = mp.groupCount();
				if (1<=gcount) {
					final String key = mp.group(1);
					final String value = 2<=gcount ? mp.group(2) : "";
					if (!StringUtils.isEmpty(key)||!StringUtils.isEmpty(value)) {
						boolean b = false;
						for (final PropReader<?, ?, ?> m : this.metas)
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

			for (final PropReader<?, ?, ?> m : this.metas)
				m.next(time, easing);
		}

		this.hasInvalidMeta = !bb;

		Log.dev.info("signmeta={"+src+"}, unsupported="+!bb);
	}

	public boolean hasInvalidMeta() {
		return this.hasInvalidMeta;
	}

	@Override
	public String toString() {
		return String.format("AttrReaders [hasInvalidMeta=%s, metas=%s, animations=%s, sizes=%s, offsets=%s, centeroffsets=%s, rotations=%s, u=%s, v=%s, w=%s, h=%s, c=%s, s=%s, o=%s, f=%s, g=%s, r=%s, m=%s, l=%s, b=%s, d=%s]", this.hasInvalidMeta, this.metas, this.animations, this.sizes, this.offsets, this.centeroffsets, this.rotations, this.u, this.v, this.w, this.h, this.c, this.s, this.o, this.f, this.g, this.r, this.m, this.l, this.b, this.d);
	}
}
