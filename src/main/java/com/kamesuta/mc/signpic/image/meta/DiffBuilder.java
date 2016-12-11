package com.kamesuta.mc.signpic.image.meta;

import javax.annotation.Nullable;

public interface DiffBuilder<Diffed, Base> {
	Diffed diff(@Nullable Base base);
}
