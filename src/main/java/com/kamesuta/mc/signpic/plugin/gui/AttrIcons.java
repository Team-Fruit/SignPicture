package com.kamesuta.mc.signpic.plugin.gui;

import javax.annotation.Nonnull;
import javax.vecmath.Quat4f;

import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.signpic.attr.AttrReaders;
import com.kamesuta.mc.signpic.attr.prop.AnimationData;
import com.kamesuta.mc.signpic.attr.prop.AnimationData.RSNeed;
import com.kamesuta.mc.signpic.attr.prop.OffsetData;

import net.minecraft.util.ResourceLocation;

public enum AttrIcons {
	OFFSET(new ResourceLocation("signpic", "textures/plugin/offset.png")) {
		@Override
		public boolean isInclude(final AttrReaders meta) {
			final OffsetData[] datas = { meta.offsets.getMovie().get(), meta.centeroffsets.getMovie().get() };
			boolean b = false;
			for (final OffsetData data : datas) {
				if (b)
					return b;
				b = b||(data.x.offset!=0||data.y.offset!=0||data.z.offset!=0);
			}
			return b;
		}

	},
	ROTATION(new ResourceLocation("signpic", "textures/plugin/rotation.png")) {
		@Override
		public boolean isInclude(final AttrReaders meta) {
			final Quat4f data = meta.rotations.getMovie().get().getRotate();
			return data.x!=0||data.y!=0||data.z!=0||data.w!=0;
		}
	},
	ANIMATION(new ResourceLocation("signpic", "textures/plugin/animation.png")) {
		@Override
		public boolean isInclude(final AttrReaders meta) {
			final AnimationData data = meta.animations.getMovie().get();
			return data.easing!=Easings.easeLinear||data.redstone!=RSNeed.IGNORE;
		}
	};
	private final @Nonnull ResourceLocation icon;

	private AttrIcons(final ResourceLocation icon) {
		this.icon = icon;
	}

	public ResourceLocation getIcon() {
		return this.icon;
	}

	public abstract boolean isInclude(AttrReaders meta);
}
