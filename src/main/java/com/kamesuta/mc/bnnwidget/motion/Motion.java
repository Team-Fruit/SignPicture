package com.kamesuta.mc.bnnwidget.motion;

import org.lwjgl.util.Timer;

public class Motion implements IMotion {
	protected final Timer timer;
	protected final Easing easing;
	protected final float duration;
	protected final float end;
	protected Runnable after;

	public Motion(final Easing easing, final float duration, final float end) {
		this.timer = new Timer();
		this.timer.pause();
		this.easing = easing;
		this.duration = duration;
		this.end = end;
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

	public Easing getEasing() {
		return this.easing;
	}

	@Override
	public float getDuration() {
		return this.duration;
	}

	@Override
	public float getEnd(final float start) {
		return this.end;
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
		return this.easing.easing(this.timer.getTime(), start, this.end - start, this.duration);
	}

	@Override
	public String toString() {
		return String.format("Motion[%1$s->%3$s(%2$ss)]", this.easing, this.duration, this.end);
	}
}
