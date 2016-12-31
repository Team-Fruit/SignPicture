package com.kamesuta.mc.bnnwidget.motion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ICompoundMotion {

	boolean isFinished();

	float getLast();

	float get();

	@Nullable
	IMotion getAnimationLast();

	@Nullable
	IMotion getAnimation();

	@Nonnull
	ICompoundMotion stopNext();

	@Nonnull
	ICompoundMotion next();

	@Nonnull
	ICompoundMotion start();

	@Nonnull
	ICompoundMotion pause();

	@Nonnull
	ICompoundMotion stopLast();

	@Nonnull
	ICompoundMotion stop();

	@Nonnull
	ICompoundMotion stopFirst();

	@Nonnull
	ICompoundMotion setLoop(final boolean b);

	@Nonnull
	ICompoundMotion add(final @Nonnull IMotion animation);

	@Nonnull
	ICompoundMotion restart();

	@Nonnull
	ICompoundMotion reset();

}
