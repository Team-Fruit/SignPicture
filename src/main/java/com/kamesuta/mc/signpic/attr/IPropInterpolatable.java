package com.kamesuta.mc.signpic.attr;

import javax.annotation.Nullable;

public interface IPropInterpolatable<InterFrame> {
	public InterFrame per();

	public InterFrame per(final float per, final @Nullable InterFrame before);
}
