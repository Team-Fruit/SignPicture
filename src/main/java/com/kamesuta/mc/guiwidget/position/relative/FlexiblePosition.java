package com.kamesuta.mc.guiwidget.position.relative;

//import java.util.EnumSet;
//
//import com.kamesuta.mc.guiwidget.MathHelper;
//import com.kamesuta.mc.guiwidget.position.IPositionAbsolute;
//import com.kamesuta.mc.guiwidget.position.PositionAbsolute;
//
//@Deprecated
//public class FlexiblePosition extends PercentageSizedPosition {
//	private final EnumSet<EnumAbsolute> absolute_value;
//
//	public FlexiblePosition(final EnumSet<EnumAbsolute> absolute_value, final float per_x, final float per_y, final float per_w, final float per_h) {
//		super(per_x, per_y, per_w, per_h);
//		this.absolute_value = absolute_value;
//	}
//
//	public FlexiblePosition(final float per_x, final float per_y, final float per_w, final float per_h) {
//		this(EnumSet.noneOf(EnumAbsolute.class), per_x, per_y, per_w, per_h);
//	}
//
//	@Override
//	public IPositionAbsolute getAbsolute(final IPositionAbsolute parent) {
//		final int pw = parent.x2();
//		final int ph = parent.y2();
//		final int px = parent.x1();
//		final int py = parent.y1();
//		final int w = getFlexibleW(pw);
//		final int h = getFlexibleH(ph);
//		final int x = getFlexibleX(pw, w, px);
//		final int y = getFlexibleY(ph, h, py);
//		return new PositionAbsolute(x, y, w, h);
//	}
//
//	protected int getFlexibleW(final int pw) {
//		if (this.absolute_value.contains(EnumAbsolute.ABSOLUTE_W)) {
//			return (int) this.per_w;
//		} else {
//			final double clipped_per_w = MathHelper.clip(this.per_w, 0, 1);
//			return (int) (pw * clipped_per_w);
//		}
//	}
//
//	protected int getFlexibleH(final int ph) {
//		if (this.absolute_value.contains(EnumAbsolute.ABSOLUTE_H)) {
//			return (int) this.per_h;
//		} else {
//			final double clipped_per_h = MathHelper.clip(this.per_h, 0, 1);
//			return (int) (ph * clipped_per_h);
//		}
//	}
//
//	protected int getFlexibleX(final int pw, final int w, final int px) {
//		if (this.absolute_value.contains(EnumAbsolute.ABSOLUTE_X)) {
//			return (int) this.per_x;
//		} else {
//			final int max_x = pw - w;
//			return px + (int) (max_x * MathHelper.clip(this.per_x, 0, 1));
//		}
//	}
//
//	protected int getFlexibleY(final int ph, final int h, final int py) {
//		if (this.absolute_value.contains(EnumAbsolute.ABSOLUTE_Y)) {
//			return (int) this.per_x;
//		} else {
//			final int max_y = ph - h;
//			return py + (int) (max_y * MathHelper.clip(this.per_y, 0, 1));
//		}
//	}
//
//	public static enum EnumAbsolute {
//		ABSOLUTE_X,
//		ABSOLUTE_Y,
//		ABSOLUTE_W,
//		ABSOLUTE_H;
//	}
//}
