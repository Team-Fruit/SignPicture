package com.kamesuta.mc.signpic.attr;

public interface IPropInterpolatable<InterFrame> {
	public InterFrame per();

	public InterFrame per(final float per, final InterFrame before);
}
