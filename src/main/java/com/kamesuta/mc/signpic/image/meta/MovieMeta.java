package com.kamesuta.mc.signpic.image.meta;

import com.kamesuta.mc.bnnwidget.motion.Easings;

public class MovieMeta<KeyFrame extends IMotionFrame<InterFrame>, InterFrame, Diffed extends KeyFrame> {
	private KeyFrame base;
	private final MovieBuilder<Diffed, KeyFrame> metabuilder;
	private MetaMovie<Diffed, KeyFrame> builder;
	private final Movie<KeyFrame, InterFrame> movie;
	private boolean parsed;

	public MovieMeta(final MovieBuilder<Diffed, KeyFrame> metabuilder) {
		this.metabuilder = metabuilder;
		this.builder = metabuilder.builder();
		this.movie = new Movie<KeyFrame, InterFrame>(this.base = this.builder.diff(this.base));
	}

	public boolean parse(final String src, final String key, final String value) {
		return this.parsed = this.builder.parse(src, key, value)||this.parsed;
	}

	public KeyFrame getDiff() {
		return this.builder.diff(this.base);
	}

	public boolean isParsed() {
		return this.parsed;
	}

	public void next(final float time, final Easings easing) {
		if (this.parsed)
			this.movie.add(time, this.base = getDiff(), easing);
		this.parsed = false;
		this.builder = this.metabuilder.builder();
	}

	public Movie<KeyFrame, InterFrame> getMovie() {
		return this.movie;
	}

	public static interface MovieBuilder<Diffed, KeyFrame> {
		MetaMovie<Diffed, KeyFrame> builder();
	}
}
