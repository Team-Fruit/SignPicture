package com.kamesuta.mc.signpic.state;

import javax.annotation.Nonnull;

public class Progress {
	public long overall;
	public long done;

	float per() {
		return (float) this.done/(float) this.overall;
	}

	public float getProgress() {
		if (this.overall>0)
			return Math.max(0, Math.min(1, per()));
		return 0;
	}

	public @Nonnull Progress setOverall(final long n) {
		this.overall = n;
		return this;
	}

	public @Nonnull Progress setDone(final long n) {
		this.done = n;
		return this;
	}
}