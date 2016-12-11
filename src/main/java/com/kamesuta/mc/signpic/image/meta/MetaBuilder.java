package com.kamesuta.mc.signpic.image.meta;

import javax.annotation.Nullable;

public interface MetaBuilder<E, B> {
	E get(@Nullable B base);
}
