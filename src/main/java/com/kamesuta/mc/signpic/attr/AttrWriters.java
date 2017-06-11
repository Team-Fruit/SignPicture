package com.kamesuta.mc.signpic.attr;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;
import com.kamesuta.mc.signpic.attr.Attrs.Attr;
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
		private @Nonnull List<IPropBuilder<?, ?>> metawriter = Lists.newArrayList();
		private @Nonnull Map<String, IPropBuilder<?, ?>> str2attr = Maps.newHashMap();
		private @Nonnull Map<IPropBuilder<?, ?>, ImmutableSet<String>> attr2str = Maps.newHashMap();

		public final @Nonnull AnimationBuilder animation = init(Attrs.ANIMATION);
		public final @Nonnull SizeBuilder size = init(Attrs.SIZE);
		public final @Nonnull OffsetBuilder offset = init(Attrs.OFFSET);
		public final @Nonnull OffsetBuilder centeroffset = init(Attrs.OFFSET_CENTER);
		public final @Nonnull RotationBuilder rotation = init(Attrs.ROTATION);
		public final @Nonnull TextureFloatBuilder u = init(Attrs.TEXTURE_X);
		public final @Nonnull TextureFloatBuilder v = init(Attrs.TEXTURE_Y);
		public final @Nonnull TextureFloatBuilder w = init(Attrs.TEXTURE_W);
		public final @Nonnull TextureFloatBuilder h = init(Attrs.TEXTURE_H);
		public final @Nonnull TextureFloatBuilder c = init(Attrs.TEXTURE_SPLIT_W);
		public final @Nonnull TextureFloatBuilder s = init(Attrs.TEXTURE_SPLIT_H);
		public final @Nonnull TextureFloatBuilder o = init(Attrs.TEXTURE_OPACITY);
		public final @Nonnull TextureFloatBuilder f = init(Attrs.TEXTURE_LIGHT_X);
		public final @Nonnull TextureFloatBuilder g = init(Attrs.TEXTURE_LIGHT_Y);
		public final @Nonnull TextureBooleanBuilder r = init(Attrs.TEXTURE_REPEAT);
		public final @Nonnull TextureBooleanBuilder m = init(Attrs.TEXTURE_MIPMAP);
		public final @Nonnull TextureBooleanBuilder l = init(Attrs.TEXTURE_LIGHTING);
		public final @Nonnull TextureBlendBuilder b = init(Attrs.TEXTURE_BLEND_SRC);
		public final @Nonnull TextureBlendBuilder d = init(Attrs.TEXTURE_BLEND_DST);

		private @Nonnull <T extends IPropBuilder<?, ?>, E extends Attr<?, ?, T>> T init(final @Nonnull E e) {
			final T writer = e.getWriter();
			final ImmutableSet<String> ids = e.getId();
			for (final String id : ids)
				this.str2attr.put(id, writer);
			this.attr2str.put(writer, ids);
			this.metareader.add(writer);
			return writer;
		}

		public @Nonnull IPropBuilder<?, ?> getAttr(final @Nonnull String id) {
			return this.str2attr.get(id);
		}

		public @Nonnull ImmutableSet<String> getIds(final @Nonnull IPropBuilder<?, ?> prop) {
			return this.attr2str.get(prop);
		}

		public @Nonnull List<IPropBuilder<?, ?>> getWriters() {
			return this.metawriter;
		}

		public @Nonnull AttrWriter add(final @Nonnull IPropBuilder<?, ?> e) {
			if (!this.metawriter.contains(e))
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
					stb.append(ShortestFloatFormatter.format(interval)).append("~");
				stb.append(attr.compose()).append(")");
				lasttime = time;
			}
		}

		return stb.append("}").toString();
	}
}
