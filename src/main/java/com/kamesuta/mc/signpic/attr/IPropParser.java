package com.kamesuta.mc.signpic.attr;

import javax.annotation.Nonnull;

public interface IPropParser extends IPropComposable {
	boolean parse(@Nonnull String src, @Nonnull String key, @Nonnull String value);
}