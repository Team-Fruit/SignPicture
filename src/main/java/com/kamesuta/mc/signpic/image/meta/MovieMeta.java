package com.kamesuta.mc.signpic.image.meta;

import com.kamesuta.mc.bnnwidget.motion.Easings;

public abstract class MovieMeta<KeyFrame extends IMotionFrame<InterFrame>, InterFrame, Diffed extends KeyFrame> {
	private KeyFrame base;
	private MetaMovie<Diffed, KeyFrame> builder = builder();
	private final Movie<KeyFrame, InterFrame> movie = new Movie<KeyFrame, InterFrame>(this.base = this.builder.diff(this.base));
	private boolean parsed;

	public MovieMeta() {
	}

	public boolean parse(final String src, final String key, final String value) {
		return this.parsed = this.builder.parse(src, key, value)||this.parsed;
	}

	public void next(final float time, final Easings easing) {
		if (this.parsed)
			this.movie.add(time, this.base = this.builder.diff(this.base), easing);
		this.parsed = false;
		this.builder = builder();
	}

	public abstract MetaMovie<Diffed, KeyFrame> builder();

	public Movie<KeyFrame, InterFrame> getMovie() {
		return this.movie;
	}
}
