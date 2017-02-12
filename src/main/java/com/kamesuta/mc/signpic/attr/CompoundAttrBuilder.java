package com.kamesuta.mc.signpic.attr;

import java.util.Set;
import java.util.regex.Matcher;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.google.common.collect.Sets;
import com.kamesuta.mc.signpic.attr.prop.AnimationData.AnimationBuilder;
import com.kamesuta.mc.signpic.attr.prop.OffsetData.OffsetBuilder;
import com.kamesuta.mc.signpic.attr.prop.OffsetData.OffsetDoublePropBuilder;
import com.kamesuta.mc.signpic.attr.prop.OffsetData.OffsetPropBuilder;
import com.kamesuta.mc.signpic.attr.prop.PropSyntax;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationBuilder;
import com.kamesuta.mc.signpic.attr.prop.SizeData.SizeBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBlend.TextureBlendBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBlend.TextureBlendType;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBoolean.TextureBooleanBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBoolean.TextureBooleanType;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureFloat.TextureFloatBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureFloat.TextureFloatType;

public class CompoundAttrBuilder {
	private @Nonnull Set<IPropBuilder<?, ?>> metas = Sets.newHashSet();

	public final @Nonnull SizeBuilder size = new SizeBuilder();
	public final @Nonnull OffsetBuilder offset = add(new OffsetBuilder(
			new OffsetDoublePropBuilder(PropSyntax.OFFSET_LEFT.id, PropSyntax.OFFSET_RIGHT.id),
			new OffsetDoublePropBuilder(PropSyntax.OFFSET_DOWN.id, PropSyntax.OFFSET_UP.id),
			new OffsetDoublePropBuilder(PropSyntax.OFFSET_BACK.id, PropSyntax.OFFSET_FRONT.id)));
	public final @Nonnull OffsetBuilder centeroffset = add(new OffsetBuilder(
			new OffsetPropBuilder(PropSyntax.OFFSET_CENTER_X.id),
			new OffsetPropBuilder(PropSyntax.OFFSET_CENTER_Y.id),
			new OffsetPropBuilder(PropSyntax.OFFSET_CENTER_Z.id)));
	public final @Nonnull RotationBuilder rotation = add(new RotationBuilder());
	public final @Nonnull TextureFloatBuilder u = add(new TextureFloatBuilder(TextureFloatType.U));
	public final @Nonnull TextureFloatBuilder v = add(new TextureFloatBuilder(TextureFloatType.V));
	public final @Nonnull TextureFloatBuilder w = add(new TextureFloatBuilder(TextureFloatType.W));
	public final @Nonnull TextureFloatBuilder h = add(new TextureFloatBuilder(TextureFloatType.H));
	public final @Nonnull TextureFloatBuilder c = add(new TextureFloatBuilder(TextureFloatType.C));
	public final @Nonnull TextureFloatBuilder s = add(new TextureFloatBuilder(TextureFloatType.S));
	public final @Nonnull TextureFloatBuilder o = add(new TextureFloatBuilder(TextureFloatType.O));
	public final @Nonnull TextureFloatBuilder f = add(new TextureFloatBuilder(TextureFloatType.F));
	public final @Nonnull TextureFloatBuilder g = add(new TextureFloatBuilder(TextureFloatType.G));
	public final @Nonnull TextureBooleanBuilder r = add(new TextureBooleanBuilder(TextureBooleanType.R));
	public final @Nonnull TextureBooleanBuilder m = add(new TextureBooleanBuilder(TextureBooleanType.M));
	public final @Nonnull TextureBooleanBuilder l = add(new TextureBooleanBuilder(TextureBooleanType.L));
	public final @Nonnull TextureBlendBuilder b = add(new TextureBlendBuilder(TextureBlendType.B));
	public final @Nonnull TextureBlendBuilder d = add(new TextureBlendBuilder(TextureBlendType.D));
	public final @Nonnull AnimationBuilder animation = add(new AnimationBuilder());

	private @Nonnull <E extends IPropBuilder<?, ?>> E add(final @Nonnull E e) {
		this.metas.add(e);
		return e;
	}

	public CompoundAttrBuilder() {

	}

	public @Nonnull CompoundAttrBuilder parse(final @Nonnull String src) {
		Validate.notNull(src);

		final Matcher mgb = CompoundAttr.pg.matcher(src);
		final String meta = mgb.replaceAll("");

		final Matcher mp = CompoundAttr.pp.matcher(meta);
		while (mp.find()) {
			final int gcount = mp.groupCount();
			if (1<=gcount) {
				final String key = mp.group(1);
				final String value = 2<=gcount ? mp.group(2) : "";
				if (!StringUtils.isEmpty(key)||!StringUtils.isEmpty(value)) {
					this.size.parse(src, key, value);
					for (final IPropBuilder<?, ?> m : this.metas)
						m.parse(src, key, value);
				}
			}
		}
		return this;
	}

	public @Nonnull String compose() {
		final StringBuilder stb = new StringBuilder("{");
		stb.append(this.size.compose());
		for (final IPropBuilder<?, ?> m : this.metas)
			stb.append(m.compose());
		return stb.append("}").toString();
	}
}
