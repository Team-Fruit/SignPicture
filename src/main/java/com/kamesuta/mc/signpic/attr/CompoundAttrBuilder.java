package com.kamesuta.mc.signpic.attr;

import java.util.Set;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.google.common.collect.Sets;
import com.kamesuta.mc.signpic.attr.prop.AnimationData.AnimationBuilder;
import com.kamesuta.mc.signpic.attr.prop.OffsetData.OffsetBuilder;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationBuilder;
import com.kamesuta.mc.signpic.attr.prop.SizeData.SizeBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBoolean.TextureBooleanType;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBoolean.TextureMapBooleanBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureFloat.TextureFloatBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureFloat.TextureFloatType;

public class CompoundAttrBuilder {
	private Set<IPropBuilder<?, ?>> metas = Sets.newHashSet();

	public final SizeBuilder size = new SizeBuilder();
	public final OffsetBuilder offset = add(new OffsetBuilder());
	public final RotationBuilder rotation = add(new RotationBuilder());
	public final TextureFloatBuilder u = add(new TextureFloatBuilder(TextureFloatType.U));
	public final TextureFloatBuilder v = add(new TextureFloatBuilder(TextureFloatType.V));
	public final TextureFloatBuilder w = add(new TextureFloatBuilder(TextureFloatType.W));
	public final TextureFloatBuilder h = add(new TextureFloatBuilder(TextureFloatType.H));
	public final TextureFloatBuilder c = add(new TextureFloatBuilder(TextureFloatType.C));
	public final TextureFloatBuilder s = add(new TextureFloatBuilder(TextureFloatType.S));
	public final TextureFloatBuilder o = add(new TextureFloatBuilder(TextureFloatType.O));
	public final TextureMapBooleanBuilder r = add(new TextureMapBooleanBuilder(TextureBooleanType.R));
	public final TextureMapBooleanBuilder m = add(new TextureMapBooleanBuilder(TextureBooleanType.M));
	public final AnimationBuilder animation = add(new AnimationBuilder());

	private <E extends IPropBuilder<?, ?>> E add(final E e) {
		this.metas.add(e);
		return e;
	}

	public CompoundAttrBuilder() {

	}

	public CompoundAttrBuilder parse(final String src) {
		Validate.notNull(src);

		final Matcher mgb = CompoundAttr.g.matcher(src);
		final String meta = mgb.replaceAll("");

		final Matcher mp = CompoundAttr.p.matcher(meta);
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

	public String compose() {
		final StringBuilder stb = new StringBuilder("{");
		stb.append(this.size.compose());
		for (final IPropBuilder<?, ?> m : this.metas)
			stb.append(m.compose());
		return stb.append("}").toString();
	}
}
