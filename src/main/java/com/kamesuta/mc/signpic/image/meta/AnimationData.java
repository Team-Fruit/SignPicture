package com.kamesuta.mc.signpic.image.meta;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.kamesuta.mc.bnnwidget.motion.Easings;

public class AnimationData implements IMotionFrame<AnimationData>, IComposable {
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
}
