package com.kamesuta.mc.signpic.image.meta;

import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.signpic.image.meta.RotationData.DiffRotation;
import com.kamesuta.mc.signpic.image.meta.RotationData.KeyRotation;

public abstract class MovieMeta<KeyFrame extends IMotionFrame<InterFrame>, InterFrame, Diffed extends KeyFrame, Builder extends MetaMovie<Diffed, KeyFrame>> {
	private KeyFrame base;
	private Builder builder = builder();
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

	public abstract Builder builder();

	public Movie<KeyFrame, InterFrame> getMovie() {
		return this.movie;
	}

	public static class SizeMovieMeta extends MovieMeta<SizeData, SizeData, SizeData, ImageSize> {
		@Override
		public ImageSize builder() {
			return new ImageSize();
		}
	}

	public static class RotationMovieMeta extends MovieMeta<KeyRotation, RotationData, DiffRotation, ImageRotation> {
		@Override
		public ImageRotation builder() {
			return new ImageRotation();
		}
	}
}
