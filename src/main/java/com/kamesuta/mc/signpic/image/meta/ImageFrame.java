package com.kamesuta.mc.signpic.image.meta;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.signpic.image.meta.AnimationData.RSNeed;

public class ImageFrame extends ImageMeta.MetaParser {
	public Easings easing = Easings.easeLinear;
	public RSNeed redstone = RSNeed.RS_OFF;

	public AnimationData get() {
		return new AnimationData(this.easing, this.redstone);
	}

	@Override
	public ImageFrame reset() {
		this.easing = Easings.easeLinear;
		this.redstone = RSNeed.RS_OFF;
		return this;
	}

	@Override
	public boolean parse(final String src, final String key, final String value) {
		if (StringUtils.equals(key, "k"))
			this.easing = Easings.fromId(NumberUtils.toInt(value));
		else if (StringUtils.equals(key, "t"))
			this.redstone = RSNeed.fromId(NumberUtils.toInt(value));
		else
			return false;
		return true;
	}

	@Deprecated
	@Override
	public String compose() {
		return get().compose();
	}

	@Deprecated
	@Override
	public String toString() {
		return compose();
	}
}
