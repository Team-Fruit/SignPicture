package com.kamesuta.mc.signpic.plugin.search;

import com.kamesuta.mc.signpic.attr.AttrReaders;

public abstract class SizeDataAttrFilterElement extends AbstractAttrFilterElement<Float, SizeDataAttrFilterProperty> {

	public SizeDataAttrFilterElement(final SizeDataAttrFilterProperty property, final float f) {
		super(property, f);
	}

	public static class LessSizeDataAttrFilterElement extends SizeDataAttrFilterElement {

		public LessSizeDataAttrFilterElement(final SizeDataAttrFilterProperty property, final float f) {
			super(property, f);
		}

		@Override
		public boolean filter(final AttrReaders attr) {
			return this.data<this.property.get(attr).max();
		}
	}

	public static class GreaterSizeDataAttrFilterElement extends SizeDataAttrFilterElement {

		public GreaterSizeDataAttrFilterElement(final SizeDataAttrFilterProperty property, final float f) {
			super(property, f);
		}

		@Override
		public boolean filter(final AttrReaders attr) {
			return this.data>this.property.get(attr).max();
		}
	}

	public static class EqualSizeDataAttrFilterElement extends SizeDataAttrFilterElement {

		public EqualSizeDataAttrFilterElement(final SizeDataAttrFilterProperty property, final float f) {
			super(property, f);
		}

		@Override
		public boolean filter(final AttrReaders attr) {
			return this.data==this.property.get(attr).max();
		}
	}
}
