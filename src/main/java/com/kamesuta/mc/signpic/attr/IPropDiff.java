package com.kamesuta.mc.signpic.attr;

import javax.annotation.Nullable;

public interface IPropDiff<Diffed, Base> {
	Diffed diff(@Nullable Base base);
}
