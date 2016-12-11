package com.kamesuta.mc.signpic.image.meta;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.ImmutableSet;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.signpic.image.meta.AnimationData.RSNeed;

public class ImageAnimation extends MetaMovie<AnimationData, AnimationData> {
	public ImageAnimation() {
		super(ImmutableSet.of("k", "t"));
	}

	public Easings easing = Easings.easeLinear;
	public RSNeed redstone = RSNeed.IGNORE;

	@Override
	public AnimationData diff(final AnimationData base) {
		return new AnimationData(this.easing, this.redstone);
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

	@Override
	public String compose() {
		return diff(null).compose();
	}

	@Deprecated
	@Override
	public String toString() {
		return compose();
	}
}
