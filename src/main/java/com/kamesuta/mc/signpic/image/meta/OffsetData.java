package com.kamesuta.mc.signpic.image.meta;

public class OffsetData implements IMotionFrame<OffsetData> {
	public static final float defaultOffset = 0.5f;

	public final float offset;

	public OffsetData(final float offset) {
		this.offset = offset;
	}

	@Override
	public OffsetData per() {
		return this;
	}

	@Override
	public OffsetData per(final float per, final OffsetData before) {
		return new OffsetData(this.offset*per+before.offset*(1f-per));
	}

	@Override
	public String toString() {
		return "OffsetData [offset="+this.offset+"]";
	}
}
