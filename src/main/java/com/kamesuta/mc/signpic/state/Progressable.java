package com.kamesuta.mc.signpic.state;

import javax.annotation.Nonnull;

public interface Progressable {
	@Nonnull
	State getState();
}