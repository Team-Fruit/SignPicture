package com.kamesuta.mc.signpic.image.meta;

import javax.annotation.Nullable;

public interface DiffBuilder<E, B> {
	E diff(@Nullable B base);
}
