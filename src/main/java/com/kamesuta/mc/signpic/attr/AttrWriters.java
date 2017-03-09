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
import com.kamesuta.mc.signpic.attr.prop.AnimationData.AnimationBuilder;
import com.kamesuta.mc.signpic.attr.prop.OffsetData.OffsetBuilder;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationBuilder;
import com.kamesuta.mc.signpic.attr.prop.SizeData.SizeBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBlend.TextureBlendBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBoolean.TextureBooleanBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureFloat.TextureFloatBuilder;

public class AttrWriters {
	private final @Nonnull TreeMap<Float, AttrWriter> frames = Maps.newTreeMap();

	public static class AttrWriter {
		private @Nonnull Set<IPropBuilder<?, ?>> metareader = Sets.newHashSet();
		private @Nonnull Set<IPropBuilder<?, ?>> metawriter = Sets.newHashSet();

		public final @Nonnull AnimationBuilder animation = addReader(Attrs.ANIMATION.getWriter());
		public final @Nonnull SizeBuilder size = addReader(Attrs.SIZE.getWriter());
		public final @Nonnull OffsetBuilder offset = addReader(Attrs.OFFSET.getWriter());
		public final @Nonnull OffsetBuilder centeroffset = addReader(Attrs.OFFSET_CENTER.getWriter());
		public final @Nonnull RotationBuilder rotation = addReader(Attrs.ROTATION.getWriter());
		public final @Nonnull TextureFloatBuilder u = addReader(Attrs.TEXTURE_X.getWriter());
		public final @Nonnull TextureFloatBuilder v = addReader(Attrs.TEXTURE_Y.getWriter());
		public final @Nonnull TextureFloatBuilder w = addReader(Attrs.TEXTURE_W.getWriter());
		public final @Nonnull TextureFloatBuilder h = addReader(Attrs.TEXTURE_H.getWriter());
		public final @Nonnull TextureFloatBuilder c = addReader(Attrs.TEXTURE_SPLIT_W.getWriter());
		public final @Nonnull TextureFloatBuilder s = addReader(Attrs.TEXTURE_SPLIT_H.getWriter());
		public final @Nonnull TextureFloatBuilder o = addReader(Attrs.TEXTURE_OPACITY.getWriter());
		public final @Nonnull TextureFloatBuilder f = addReader(Attrs.TEXTURE_LIGHT_X.getWriter());
		public final @Nonnull TextureFloatBuilder g = addReader(Attrs.TEXTURE_LIGHT_Y.getWriter());
		public final @Nonnull TextureBooleanBuilder r = addReader(Attrs.TEXTURE_REPEAT.getWriter());
		public final @Nonnull TextureBooleanBuilder m = addReader(Attrs.TEXTURE_MIPMAP.getWriter());
		public final @Nonnull TextureBooleanBuilder l = addReader(Attrs.TEXTURE_LIGHTING.getWriter());
		public final @Nonnull TextureBlendBuilder b = addReader(Attrs.TEXTURE_BLEND_SRC.getWriter());
		public final @Nonnull TextureBlendBuilder d = addReader(Attrs.TEXTURE_BLEND_DST.getWriter());

		private @Nonnull <E extends IPropBuilder<?, ?>> E addReader(final @Nonnull E e) {
			this.metareader.add(e);
			return e;
		}

		public @Nonnull AttrWriter add(final @Nonnull IPropBuilder<?, ?> e) {
			this.metawriter.add(e);
			return this;
		}

		public @Nonnull AttrWriter remove(final @Nonnull IPropBuilder<?, ?> e) {
			this.metawriter.remove(e);
			return this;
		}

		public boolean contains(final @Nonnull IPropBuilder<?, ?> e) {
			return this.metawriter.contains(e);
		}

		public boolean isEmpty() {
			return this.metawriter.isEmpty();
		}

		@Override
		public String toString() {
			return compose();
		}

		public @Nonnull AttrWriter parse(final @Nonnull String src) {
			Validate.notNull(src);

			final Matcher mgb = Attrs.pg.matcher(src);
			final String meta = mgb.replaceAll("");

			final Matcher mp = Attrs.pp.matcher(meta);
			while (mp.find()) {
				final int gcount = mp.groupCount();
				if (1<=gcount) {
					final String key = mp.group(1);
					final String value = 2<=gcount ? mp.group(2) : "";
					if (!StringUtils.isEmpty(key)||!StringUtils.isEmpty(value)) {
						this.size.parse(src, key, value);
						for (final IPropBuilder<?, ?> m : this.metareader)
							if (m.parse(src, key, value))
								add(m);
					}
				}
			}
			return this;
		}

		public @Nonnull String compose() {
			final StringBuilder stb = new StringBuilder();
			if (this.metawriter.contains(this.size))
				stb.append(this.size.compose());
			for (final IPropBuilder<?, ?> m : this.metawriter)
				if (!m.equals(this.size))
					stb.append(m.compose());
			return stb.toString();
		}
	}

	public @Nonnull AttrWriter getFrame(final float time) {
		AttrWriter attr = this.frames.get(time);
		if (attr==null) {
			attr = new AttrWriter();
			this.frames.put(time, attr);
		}
		return attr;
	}

	public @Nonnull AttrWriters parse(final @Nonnull String src) {
		Validate.notNull(src);

		final TreeMap<Float, String> timeline = Maps.newTreeMap();

		final Matcher mgb = Attrs.pg.matcher(src);
		final String s = mgb.replaceAll("");
		getFrame(0f).parse(s);

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

		for (final Entry<Float, String> entry : timeline.entrySet()) {
			final float time = entry.getKey();
			final String meta = entry.getValue();

			getFrame(time).parse(meta);
		}

		return this;
	}

	public @Nonnull String compose() {
		final StringBuilder stb = new StringBuilder("{");
		final AttrWriter first = getFrame(0f);
		if (!first.isEmpty())
			stb.append(first.compose());

		float lasttime = 0f;
		for (final Entry<Float, AttrWriter> entry : this.frames.entrySet()) {
			final float time = entry.getKey();
			if (time==0f)
				continue;
			final AttrWriter attr = entry.getValue();
			if (!attr.isEmpty()) {
				final float interval = time-lasttime;
				stb.append("(");
				if (interval!=Attrs.defaultInterval)
					stb.append(interval).append("~");
				stb.append(attr.compose()).append(")");
				lasttime = time;
			}
		}

		return stb.append("}").toString();
	}
}
