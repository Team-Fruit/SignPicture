package com.kamesuta.mc.bnnwidget.motion;

import org.lwjgl.util.Timer;

public interface IMotion {

	IMotion reset();

	IMotion finish();

	IMotion pause();

	IMotion resume();

	IMotion setProgress(float percent);

	boolean isFinished();

	IMotion setAfter(Runnable r);

	Timer getTimer();

	float getDuration();

	float getEnd(float start);

	Runnable getAfter();

	void onFinished();

	float get(float start);

}