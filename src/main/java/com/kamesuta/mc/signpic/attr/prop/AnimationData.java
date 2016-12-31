package com.kamesuta.mc.signpic.attr.prop;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.signpic.attr.IPropComposable;
import com.kamesuta.mc.signpic.attr.IPropInterpolatable;
import com.kamesuta.mc.signpic.attr.IPropBuilder;

public class AnimationData implements IPropInterpolatable<AnimationData>, IPropComposable {
	public final Easings easing;
	public final RSNeed redstone;

	public AnimationData(final Easings easing2, final RSNeed redstone) {
		this.easing = easing2;
		this.redstone = redstone;
	}

	@Override
	public AnimationData per() {
		return this;
	}

	@Override
	public AnimationData per(final float per, final AnimationData before) {
		return this;
	}

	@Override
	public String compose() {
		final StringBuilder stb = new StringBuilder();
		if (this.easing!=Easings.easeLinear)
			stb.append("k").append(this.easing.id);
		if (this.redstone!=RSNeed.IGNORE)
			stb.append("t").append(this.redstone.id);
		return stb.toString();
	}

	@Override
	public String toString() {
		return "AnimationData [easing="+this.easing+", redstone="+this.redstone+"]";
	}

	public static enum RSNeed {
		IGNORE(0), RS_ON(1), RS_OFF(2),
		;

		public final int id;

		private RSNeed(final int id) {
			this.id = id;
		}

		private static final ImmutableMap<Integer, RSNeed> rsIds;

		public static RSNeed fromId(final int id) {
			RSNeed rs = rsIds.get(id);
			if (rs==null)
				rs = IGNORE;
			return rs;
		}

		static {
			final Builder<Integer, RSNeed> builder = ImmutableMap.builder();
			for (final RSNeed easing : RSNeed.values())
				builder.put(easing.id, easing);
			rsIds = builder.build();
		}
	}

	public static class AnimationBuilder implements IPropBuilder<AnimationData, AnimationData> {
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
}
