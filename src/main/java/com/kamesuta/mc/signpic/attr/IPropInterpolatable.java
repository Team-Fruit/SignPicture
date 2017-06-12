package com.kamesuta.mc.signpic.attr;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IPropInterpolatable<InterFrame> {
	public @Nonnull InterFrame per();

	public @Nonnull InterFrame per(final float per, final @Nullable InterFrame before);
}
