package com.kamesuta.mc.signpic.plugin.search;

import com.kamesuta.mc.signpic.attr.AttrReaders;
import com.kamesuta.mc.signpic.attr.prop.AnimationData;

public class AnimationDataAttrFilterProperty implements AttrFilterProperty<AnimationData> {

	public static final AnimationDataAttrFilterProperty ANIMATION = new AnimationDataAttrFilterProperty();

	private AnimationDataAttrFilterProperty() {
	}

	@Override
	public AnimationData get(final AttrReaders attr) {
		return attr.animations.getMovie().get();
	}

}
