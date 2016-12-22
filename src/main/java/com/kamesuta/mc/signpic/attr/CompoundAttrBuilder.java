package com.kamesuta.mc.signpic.attr;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.kamesuta.mc.signpic.attr.prop.AnimationData.AnimationBuilder;
import com.kamesuta.mc.signpic.attr.prop.OffsetData.OffsetBuilder;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationBuilder;
import com.kamesuta.mc.signpic.attr.prop.SizeData.SizeBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureMapData.DataType;
import com.kamesuta.mc.signpic.attr.prop.TextureMapData.DataTypeBoolean;
import com.kamesuta.mc.signpic.attr.prop.TextureMapData.TextureMapBooleanBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureMapData.TextureMapBuilder;

public class CompoundAttrBuilder {
	public final SizeBuilder size = new SizeBuilder();
	public final OffsetBuilder xoffset = new OffsetBuilder("L", "R");
	public final OffsetBuilder yoffset = new OffsetBuilder("D", "U");
	public final OffsetBuilder zoffset = new OffsetBuilder("B", "F");
	public final RotationBuilder rotation = new RotationBuilder();
	public final TextureMapBuilder u = new TextureMapBuilder(DataType.U);
	public final TextureMapBuilder v = new TextureMapBuilder(DataType.V);
	public final TextureMapBuilder w = new TextureMapBuilder(DataType.W);
	public final TextureMapBuilder h = new TextureMapBuilder(DataType.H);
	public final TextureMapBuilder c = new TextureMapBuilder(DataType.C);
	public final TextureMapBuilder s = new TextureMapBuilder(DataType.S);
	public final TextureMapBuilder o = new TextureMapBuilder(DataType.O);
	public final TextureMapBooleanBuilder r = new TextureMapBooleanBuilder(DataTypeBoolean.R);
	public final TextureMapBooleanBuilder m = new TextureMapBooleanBuilder(DataTypeBoolean.M);
	public final AnimationBuilder animation = new AnimationBuilder();

	public CompoundAttrBuilder() {

	}

	public CompoundAttrBuilder(final String src) {
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
					this.animation.parse(src, key, value);
					this.size.parse(src, key, value);
					this.xoffset.parse(src, key, value);
					this.yoffset.parse(src, key, value);
					this.zoffset.parse(src, key, value);
					this.rotation.parse(src, key, value);
					this.u.parse(src, key, value);
					this.v.parse(src, key, value);
					this.w.parse(src, key, value);
					this.h.parse(src, key, value);
					this.c.parse(src, key, value);
					this.s.parse(src, key, value);
					this.o.parse(src, key, value);
					this.r.parse(src, key, value);
					this.m.parse(src, key, value);
				}
			}
		}
	}

	public String compose() {
		return "{"+
				this.size.compose()+
				this.xoffset.compose()+
				this.yoffset.compose()+
				this.zoffset.compose()+
				this.rotation.compose()+
				this.u.compose()+
				this.v.compose()+
				this.w.compose()+
				this.h.compose()+
				this.c.compose()+
				this.s.compose()+
				this.o.compose()+
				this.r.compose()+
				this.m.compose()+
				this.animation.compose()+
				"}";
	}
}
