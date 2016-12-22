package com.kamesuta.mc.signpic.image.meta;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.kamesuta.mc.signpic.image.meta.AnimationData.AnimationBuilder;
import com.kamesuta.mc.signpic.image.meta.OffsetData.OffsetBuilder;
import com.kamesuta.mc.signpic.image.meta.RotationData.RotationBuilder;
import com.kamesuta.mc.signpic.image.meta.SizeData.SizeBuilder;
import com.kamesuta.mc.signpic.image.meta.TextureMapData.DataType;
import com.kamesuta.mc.signpic.image.meta.TextureMapData.DataTypeBoolean;
import com.kamesuta.mc.signpic.image.meta.TextureMapData.TextureMapBuilder;
import com.kamesuta.mc.signpic.image.meta.TextureMapData.TextureMapBooleanBuilder;

public class MetaBuilder {
	public final SizeBuilder size = new SizeBuilder();
	public final OffsetBuilder xoffset = new OffsetBuilder("L", "R");
	public final OffsetBuilder yoffset = new OffsetBuilder("D", "U");
	public final OffsetBuilder zoffset = new OffsetBuilder("B", "F");
	public final RotationBuilder rotation = new RotationBuilder();
	public final TextureMapBuilder u = new TextureMapBuilder(DataType.U);
	public final TextureMapBuilder v = new TextureMapBuilder(DataType.U);
	public final TextureMapBuilder w = new TextureMapBuilder(DataType.U);
	public final TextureMapBuilder h = new TextureMapBuilder(DataType.U);
	public final TextureMapBuilder c = new TextureMapBuilder(DataType.U);
	public final TextureMapBuilder s = new TextureMapBuilder(DataType.U);
	public final TextureMapBuilder o = new TextureMapBuilder(DataType.U);
	public final TextureMapBooleanBuilder r = new TextureMapBooleanBuilder(DataTypeBoolean.R);
	public final TextureMapBooleanBuilder m = new TextureMapBooleanBuilder(DataTypeBoolean.M);
	public final AnimationBuilder animation = new AnimationBuilder();

	public MetaBuilder() {

	}

	public MetaBuilder(final String src) {
		Validate.notNull(src);

		final Matcher mgb = ImageMeta.g.matcher(src);
		final String meta = mgb.replaceAll("");

		final Matcher mp = ImageMeta.p.matcher(meta);
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
