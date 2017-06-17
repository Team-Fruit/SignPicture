package com.kamesuta.mc.signpic.attr;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IPropDiff<Diffed, Base> {
	@Nonnull
	Diffed diff(@Nullable Base base);
}
