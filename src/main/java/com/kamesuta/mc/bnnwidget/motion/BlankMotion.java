package com.kamesuta.mc.bnnwidget.motion;

import org.lwjgl.util.Timer;

public class BlankMotion implements IMotion {
	protected final Timer timer;
	protected final float duration;
	protected Runnable after;

	public BlankMotion(final float duration) {
		this.timer = new Timer();
		this.timer.pause();
		this.duration = duration;
	}

	@Override
	public IMotion reset() {
		this.timer.reset();
		return this;
	}

	@Override
	public IMotion finish() {
		this.timer.set(this.duration);
		return this;
	}

	@Override
	public IMotion pause() {
		this.timer.pause();
		return this;
	}

	@Override
	public IMotion resume() {
		this.timer.resume();
		return this;
	}

	@Override
	public boolean isFinished() {
		return this.timer.getTime() >= this.duration;
	}

	@Override
	public void after(final Runnable r) {
		this.after = r;
	}

	@Override
	public Timer getTimer() {
		return this.timer;
	}

	@Override
	public float getDuration() {
		return this.duration;
	}

	@Override
	public float getEnd(final float start) {
		return start;
	}

	@Override
	public Runnable getAfter() {
		return this.after;
	}

	@Override
	public void onFinished() {
		if (this.after != null)
			this.after.run();
	}

	@Override
	public double get(final double start) {
		return start;
	}

	@Override
	public String toString() {
		return String.format("Space[(%2$ss)]", this.duration);
	}
}
