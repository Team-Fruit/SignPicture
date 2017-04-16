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

	public static class EqualPosFilterElement extends PosFilterElement {

		public EqualPosFilterElement(final int x, final int y, final int z) {
			super(x, y, z);
		}

		@Override
		public boolean filter(final SignData data) {
			return this.x==data.getX()&&this.y==data.getY()&&this.z==data.getZ();
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
			public boolean filter(final SignData data) {
				return data.getX()>=this.x&&data.getX()<this.x2&&data.getY()>=this.y&&data.getZ()<this.y2&&data.getZ()>=this.z&&data.getZ()<this.z2;
			}
		}

		public static class OutRangePosFilterElement extends RangePosFilterElement {

			public OutRangePosFilterElement(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
				super(x1, y1, z1, x2, y2, z2);
			}

			@Override
			public boolean filter(final SignData data) {
				return data.getX()<=this.x&&data.getX()>this.x2&&data.getY()<=this.y&&data.getY()>this.y2&&data.getZ()<=this.z&&data.getZ()>this.z2;
			}
		}
	}
}
