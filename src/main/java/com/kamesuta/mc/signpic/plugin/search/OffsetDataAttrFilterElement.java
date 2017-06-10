package com.kamesuta.mc.signpic.plugin.search;

import com.kamesuta.mc.signpic.attr.AttrReaders;
import com.kamesuta.mc.signpic.attr.prop.OffsetData;

public abstract class OffsetDataAttrFilterElement extends AbstractAttrFilterElement<Float, OffsetDataAttrFilterProperty> {

	public OffsetDataAttrFilterElement(final OffsetDataAttrFilterProperty property, final float f) {
		super(property, f);
	}

	public static class LessOffsetDataAttrFilterElement extends OffsetDataAttrFilterElement {

		public LessOffsetDataAttrFilterElement(final OffsetDataAttrFilterProperty property, final float f) {
			super(property, f);
		}

		@Override
		public boolean filter(final AttrReaders attr) {
			final OffsetData o = this.property.get(attr);
			final float max = Math.max(o.x.offset, Math.max(o.y.offset, o.z.offset));
			return max<this.data;
		}
	}

	public static class GreaterOffsetDataAttrFilterElement extends OffsetDataAttrFilterElement {

		public GreaterOffsetDataAttrFilterElement(final OffsetDataAttrFilterProperty property, final float f) {
			super(property, f);
		}

		@Override
		public boolean filter(final AttrReaders attr) {
			final OffsetData o = this.property.get(attr);
			final float max = Math.max(o.x.offset, Math.max(o.y.offset, o.z.offset));
			return max>this.data;
		}
	}

	public static class EqualOffsetDataAttrFilterElement extends OffsetDataAttrFilterElement {

		public EqualOffsetDataAttrFilterElement(final OffsetDataAttrFilterProperty property, final float f) {
			super(property, f);
		}

		@Override
		public boolean filter(final AttrReaders attr) {
			final OffsetData o = this.property.get(attr);
			final float max = Math.max(o.x.offset, Math.max(o.y.offset, o.z.offset));
			return max==this.data;
		}
	}
}
