package com.kamesuta.mc.guiwidget.animation;

import com.kamesuta.mc.guiwidget.MathHelper;

public class Animation {
	public final double duration;
	protected double elapsed = 0;

	protected transient long timeCache = -1;

	public Animation(final double duration) {
		this.duration = duration;
	}

	public Animation setElapsed(final double elapsed) {
		this.elapsed = MathHelper.clip(elapsed, 0, this.duration);
		return this;
	}

	public double getElapsed() {
		return this.elapsed;
	}

	public Animation addElapsed(final double addelapsed) {
		return setElapsed(this.elapsed + addelapsed);
	}

	public Animation setProgress(final double percentage) {
		return setElapsed(this.duration * percentage);
	}

	public double getProgress() {
		if (this.duration > 0)
			return this.elapsed / this.duration;
		else
			return 0;
	}

	public Animation addProgress(final double addpercentage) {
		return addElapsed(this.duration * addpercentage);
	}

	public Animation addElapsedByTime() {
		final long oldtime = this.timeCache;
		final long newtime = this.timeCache = System.currentTimeMillis();
		if (oldtime >= 0) {
			final double elapsed = (newtime - oldtime) / 1000d;
			addElapsed(elapsed);
		}
		return this;
	}

	public Animation reset() {
		this.elapsed = 0;
		return this;
	}

	public Animation finish() {
		this.elapsed = this.duration;
		return this;
	}

	public boolean hasNotYetBegun() {
		return this.elapsed <= 0;
	}

	public boolean hasFinished() {
		return this.elapsed >= this.duration;
	}

	public double easingBetween(final Easing easing, final double initialValue, final double destinationValue) {
		final double amountOfChange = destinationValue - initialValue;
		return easing.easing(this.elapsed, initialValue, amountOfChange, this.duration);
	}

	public double easingChange(final Easing easing, final double initialValue, final double amountOfChange) {
		return easing.easing(this.elapsed, initialValue, amountOfChange, this.duration);
	}

	public double easingProgress(final Easing easing) {
		return easing.easing(this.elapsed, 0, 1, this.duration);
	}
}
