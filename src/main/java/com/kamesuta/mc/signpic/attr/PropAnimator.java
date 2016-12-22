package com.kamesuta.mc.signpic.attr;

import com.kamesuta.mc.bnnwidget.motion.Easings;

public class PropAnimator<KeyFrame extends IPropInterpolatable<InterFrame>, InterFrame, Diffed extends KeyFrame> {
	private KeyFrame base;
	private final IPropBuilderBuilder<Diffed, KeyFrame> metabuilder;
	private IPropBuilder<Diffed, KeyFrame> builder;
	private final PropAnimation<KeyFrame, InterFrame> movie;
	private boolean parsed;

	public PropAnimator(final IPropBuilderBuilder<Diffed, KeyFrame> metabuilder) {
		this.metabuilder = metabuilder;
		this.builder = metabuilder.builder();
		this.movie = new PropAnimation<KeyFrame, InterFrame>(this.base = this.builder.diff(this.base));
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

	public PropAnimation<KeyFrame, InterFrame> getMovie() {
		return this.movie;
	}

	public static interface IPropBuilderBuilder<Diffed, KeyFrame> {
		IPropBuilder<Diffed, KeyFrame> builder();
	}
}
