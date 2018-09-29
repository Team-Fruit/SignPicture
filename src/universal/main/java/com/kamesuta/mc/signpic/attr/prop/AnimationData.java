package com.kamesuta.mc.signpic.attr.prop;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.signpic.attr.IPropBuilder;
import com.kamesuta.mc.signpic.attr.IPropComposable;
import com.kamesuta.mc.signpic.attr.IPropInterpolatable;

public class AnimationData implements IPropInterpolatable<AnimationData>, IPropComposable {
	public final @Nonnull Easings easing;
	public final @Nonnull RSNeed redstone;

	public AnimationData(final @Nonnull Easings easing, final @Nonnull RSNeed redstone) {
		this.easing = easing;
		this.redstone = redstone;
	}

	@Override
	public @Nonnull AnimationData per() {
		return this;
	}

	@Override
	public @Nonnull AnimationData per(final float per, final @Nullable AnimationData before) {
		return this;
	}

	@Override
	public @Nonnull String compose() {
		final StringBuilder stb = new StringBuilder();
		if (this.easing!=Easings.easeLinear)
			stb.append(PropSyntax.ANIMATION_EASING.id).append(this.easing.id);
		if (this.redstone!=RSNeed.IGNORE)
			stb.append(PropSyntax.ANIMATION_REDSTONE.id).append(this.redstone.id);
		return stb.toString();
	}

	@Override
	public @Nonnull String toString() {
		return "AnimationData [easing="+this.easing+", redstone="+this.redstone+"]";
	}

	public static enum RSNeed {
		IGNORE(0), RS_ON(1), RS_OFF(2),
		;

		public final int id;

		private RSNeed(final int id) {
			this.id = id;
		}

		private static final @Nonnull ImmutableMap<Integer, RSNeed> rsIds;

		public static @Nonnull RSNeed fromId(final int id) {
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
		public @Nonnull Easings easing = Easings.easeLinear;
		public @Nonnull RSNeed redstone = RSNeed.IGNORE;

		@Override
		public @Nonnull AnimationData diff(final @Nullable AnimationData base) {
			return new AnimationData(this.easing, this.redstone);
		}

		@Override
		public boolean parse(final @Nonnull String src, final @Nonnull String key, final @Nonnull String value) {
			if (StringUtils.equals(key, PropSyntax.ANIMATION_EASING.id))
				this.easing = Easings.fromId(NumberUtils.toInt(value));
			else if (StringUtils.equals(key, PropSyntax.ANIMATION_REDSTONE.id))
				this.redstone = RSNeed.fromId(NumberUtils.toInt(value));
			else
				return false;
			return true;
		}

		@Override
		public @Nonnull String compose() {
			return diff(null).compose();
		}

		@Deprecated
		@Override
		public @Nonnull String toString() {
			return compose();
		}
	}
}
