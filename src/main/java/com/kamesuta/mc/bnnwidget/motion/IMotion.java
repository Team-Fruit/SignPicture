package com.kamesuta.mc.bnnwidget.motion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IMotion {

	@Nonnull
	IMotion restart();

	@Nonnull
	IMotion finish();

	@Nonnull
	IMotion pause();

	@Nonnull
	IMotion resume();

	@Nonnull
	IMotion setTime(float time);

	boolean isFinished();

	@Nonnull
	IMotion setAfter(@Nullable Runnable r);

	float getDuration();

	float getEnd(float start);

	@Nullable
	Runnable getAfter();

	void onFinished();

	float get(float start);

}