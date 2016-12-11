package com.kamesuta.mc.signpic.image.meta;

public interface IMotionFrame<InterFrame> {
	public InterFrame per();

	public InterFrame per(final float per, final InterFrame before);
}
