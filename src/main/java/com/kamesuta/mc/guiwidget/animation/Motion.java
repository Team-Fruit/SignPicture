package com.kamesuta.mc.guiwidget.animation;

import org.lwjgl.util.Timer;

public class Motion {
	public final Timer timer;
	public final Easing easing;
	public final float duration;
	public final float end;
	public Runnable after;

	public Motion(final Easing easing, final float duration, final float end) {
		this.timer = new Timer();
		this.timer.pause();
		this.easing = easing;
		this.duration = duration;
		this.end = end;
	}

	public Motion reset() {
		this.timer.reset();
		return this;
	}

	public Motion finish() {
		this.timer.set(this.duration);
		return this;
	}

	public Motion pause() {
		this.timer.pause();
		return this;
	}

	public Motion resume() {
		this.timer.resume();
		return this;
	}

	public boolean isFinished() {
		return this.timer.getTime() >= this.duration;
	}

	public void after(final Runnable r) {
		this.after = r;
	}

	public void onFinished() {
		if (this.after != null)
			this.after.run();
	}

	public double easing(final double start) {
		return this.easing.easing(this.timer.getTime(), start, this.end - start, this.duration);
	}

	@Override
	public String toString() {
		return String.format("Motion[%1$s->%3$s(%2$ss)]", this.easing, this.duration, this.end);
	}
}
