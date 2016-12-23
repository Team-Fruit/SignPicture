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
import com.kamesuta.mc.signpic.attr.prop.TextureMapData.DataType;
import com.kamesuta.mc.signpic.attr.prop.TextureMapData.DataTypeBoolean;
import com.kamesuta.mc.signpic.attr.prop.TextureMapData.TextureMapBooleanBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureMapData.TextureMapBuilder;

public class CompoundAttrBuilder {
	private Set<IPropBuilder<?, ?>> metas = Sets.newHashSet();

	public final SizeBuilder size = new SizeBuilder();
	public final OffsetBuilder offset = add(new OffsetBuilder());
	public final RotationBuilder rotation = add(new RotationBuilder());
	public final TextureMapBuilder u = add(new TextureMapBuilder(DataType.U));
	public final TextureMapBuilder v = add(new TextureMapBuilder(DataType.V));
	public final TextureMapBuilder w = add(new TextureMapBuilder(DataType.W));
	public final TextureMapBuilder h = add(new TextureMapBuilder(DataType.H));
	public final TextureMapBuilder c = add(new TextureMapBuilder(DataType.C));
	public final TextureMapBuilder s = add(new TextureMapBuilder(DataType.S));
	public final TextureMapBuilder o = add(new TextureMapBuilder(DataType.O));
	public final TextureMapBooleanBuilder r = add(new TextureMapBooleanBuilder(DataTypeBoolean.R));
	public final TextureMapBooleanBuilder m = add(new TextureMapBooleanBuilder(DataTypeBoolean.M));
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
