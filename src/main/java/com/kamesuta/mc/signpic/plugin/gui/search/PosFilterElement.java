package com.kamesuta.mc.signpic.plugin.gui.search;

import com.kamesuta.mc.signpic.plugin.SignData;

public abstract class PosFilterElement implements IFilterElement {

	public final int x;
	public final int y;
	public final int z;

	public PosFilterElement(final int x, final int y, final int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public abstract boolean filter(SignData data, int x, int y, int z);

	public static class EqualPosFilterElement extends PosFilterElement {

		public EqualPosFilterElement(final int x, final int y, final int z) {
			super(x, y, z);
		}

		@Override
		public boolean filter(final SignData data, final int x, final int y, final int z) {
			return this.x==x&&this.y==y&&this.z==z;
		}

	}

	public static abstract class RangePosFilterElement extends PosFilterElement {

		public final int x2;
		public final int y2;
		public final int z2;

		public RangePosFilterElement(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
			super(x1, y1, z1);
			this.x2 = x2;
			this.y2 = y2;
			this.z2 = z2;
		}

		public static class WithinRangePosFilterElement extends RangePosFilterElement {

			public WithinRangePosFilterElement(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
				super(x1, y1, z1, x2, y2, z2);
			}

			@Override
			public boolean filter(final SignData data, final int x, final int y, final int z) {
				return x>=this.x&&x<this.x2&&y>=this.y&&y<this.y2&&z>=this.z&&z<this.z2;
			}
		}

		public static class OutRangePosFilterElement extends RangePosFilterElement {

			public OutRangePosFilterElement(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
				super(x1, y1, z1, x2, y2, z2);
			}

			@Override
			public boolean filter(final SignData data, final int x, final int y, final int z) {
				return x<=this.x&&x>this.x2&&y<=this.y&&y>this.y2&&z<=this.z&&z>this.z2;
			}
		}
	}
}
