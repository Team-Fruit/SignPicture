package com.kamesuta.mc.bnnwidget;

import javax.annotation.Nonnull;

public interface WActionListener {
	public void actionPerformed(@Nonnull String command, @Nonnull Object... params);
}