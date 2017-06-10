package com.kamesuta.mc.signpic.plugin.search;

import javax.vecmath.Quat4f;

import com.kamesuta.mc.signpic.attr.AttrReaders;

public class BooleanRotationDataAttrFilterElement extends AbstractAttrFilterElement<Boolean, RotationDataAttrFilterProperty> {

	public BooleanRotationDataAttrFilterElement(final RotationDataAttrFilterProperty property, final boolean b) {
		super(property, b);
	}

	@Override
	public boolean filter(final AttrReaders attr) {
		final Quat4f data = this.property.get(attr).getRotate();
		return (data.x!=0||data.y!=0||data.z!=0||data.w!=1.0f)==this.data;
	}

}
