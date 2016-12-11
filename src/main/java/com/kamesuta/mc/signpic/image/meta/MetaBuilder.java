package com.kamesuta.mc.signpic.image.meta;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class MetaBuilder {
	public final ImageSize size = new ImageSize();
	public final ImageOffset offset = new ImageOffset();
	public final ImageRotation rotation = new ImageRotation();
	public final ImageTextureMap map = new ImageTextureMap();
	public final ImageAnimation animation = new ImageAnimation();

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
					this.size.parse(src, key, value);
					this.offset.parse(src, key, value);
					this.rotation.parse(src, key, value);
					this.map.parse(src, key, value);
					this.animation.parse(src, key, value);
				}
			}
		}
	}

	public String compose() {
		return "{"+this.size+this.offset+this.rotation+this.map+this.animation+"}";
	}
}
