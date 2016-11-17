package com.kamesuta.mc.bnnwidget.var;

public enum VType {
	Absolute {
		@Override
		public float calc(final float a, final float b, final float c) {
			return c;
		}
	},
	Percent {
		@Override
		public float calc(final float a, final float b, final float c) {
			return a*(1f-c)+b*c;
		}
	},
	;

	public abstract float calc(float a, float b, float f);
}
