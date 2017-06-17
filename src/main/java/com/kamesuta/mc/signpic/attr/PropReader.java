package com.kamesuta.mc.signpic.attr;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.motion.Easings;

public class PropReader<KeyFrame extends IPropInterpolatable<InterFrame>, InterFrame, Diffed extends KeyFrame> {
	private @Nullable KeyFrame base;
	private final @Nonnull IPropReader<Diffed, KeyFrame> metabuilder;
	private @Nonnull IPropBuilder<Diffed, KeyFrame> builder;
	private final @Nonnull PropReaderAnimation<KeyFrame, InterFrame> movie;
	private boolean parsed;

	public PropReader(final @Nonnull IPropReader<Diffed, KeyFrame> metabuilder) {
		this.metabuilder = metabuilder;
		this.builder = metabuilder.builder();
		this.movie = new PropReaderAnimation<KeyFrame, InterFrame>(this.builder.diff(this.base));
	}

	public boolean parse(final @Nonnull String src, final @Nonnull String key, final @Nonnull String value) {
		return this.parsed = this.builder.parse(src, key, value)||this.parsed;
	}

	public @Nonnull KeyFrame getDiff() {
		return this.builder.diff(this.base);
	}

	public boolean isParsed() {
		return this.parsed;
	}

	public void next(final float time, final @Nonnull Easings easing) {
		if (this.parsed)
			this.movie.add(time, this.base = getDiff(), easing);
		this.parsed = false;
		this.builder = this.metabuilder.builder();
	}

	public @Nonnull PropReaderAnimation<KeyFrame, InterFrame> getMovie() {
		return this.movie;
	}

	public static interface IPropReader<Diffed, KeyFrame> {
		@Nonnull
		IPropBuilder<Diffed, KeyFrame> builder();
	}
}
