package com.kamesuta.mc.bnnwidget.motion;

public interface ICompoundMotion {

	boolean isFinished();

	float getLast();

	float get();

	IMotion getAnimationLast();

	IMotion getAnimation();

	ICompoundMotion stopNext();

	ICompoundMotion next();

	ICompoundMotion start();

	ICompoundMotion pause();

	ICompoundMotion stopLast();

	ICompoundMotion stop();

	ICompoundMotion stopFirst();

	ICompoundMotion setLoop(final boolean b);

	ICompoundMotion add(final IMotion animation);

	ICompoundMotion restart();

	ICompoundMotion reset();

}
