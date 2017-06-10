package com.kamesuta.mc.signpic.plugin.search;

import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.signpic.attr.AttrReaders;
import com.kamesuta.mc.signpic.attr.prop.AnimationData;
import com.kamesuta.mc.signpic.attr.prop.AnimationData.RSNeed;

public class AnimationDataFilterElement {

	public static class BooleanAnimationDataFilterElement extends AbstractAttrFilterElement<Boolean, AnimationDataAttrFilterProperty> {

		public BooleanAnimationDataFilterElement(final AnimationDataAttrFilterProperty property, final boolean b) {
			super(property, b);
		}

		@Override
		public boolean filter(final AttrReaders attr) {
			final AnimationData data = this.property.get(attr);
			return (data.easing!=Easings.easeLinear||data.redstone!=RSNeed.IGNORE)==this.data;
		}
	}

	public static class EasingAnimationDataFilterElement extends AbstractAttrFilterElement<Easings, AnimationDataAttrFilterProperty> {

		public EasingAnimationDataFilterElement(final AnimationDataAttrFilterProperty property, final Easings easing) {
			super(property, easing);
		}

		@Override
		public boolean filter(final AttrReaders attr) {
			return this.property.get(attr).easing==this.data;
		}
	}

	public static class RSNeedAnimationDataFilterElement extends AbstractAttrFilterElement<RSNeed, AnimationDataAttrFilterProperty> {

		public RSNeedAnimationDataFilterElement(final AnimationDataAttrFilterProperty property, final RSNeed rs) {
			super(property, rs);
		}

		@Override
		public boolean filter(final AttrReaders attr) {
			return this.property.get(attr).redstone==this.data;
		}
	}
}
