package com.kamesuta.mc.bnnwidget.motion;

import org.lwjgl.util.Timer;

public interface IMotion {

	IMotion reset();

	IMotion finish();

	IMotion pause();

	IMotion resume();

	boolean isFinished();

	void after(Runnable r);

	Timer getTimer();

	float getDuration();

	float getEnd(float start);

	Runnable getAfter();

	void onFinished();

	double get(double start);

}