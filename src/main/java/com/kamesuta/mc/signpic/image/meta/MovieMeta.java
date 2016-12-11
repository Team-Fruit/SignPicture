package com.kamesuta.mc.signpic.image.meta;

import com.kamesuta.mc.bnnwidget.motion.Easings;

public abstract class MovieMeta<A extends IMotionFrame<B>, B, C extends MetaMovie<E, A>, E extends A> {
	private A base;
	private C builder = builder();
	public final Movie<A, B> movie = new Movie<A, B>(this.base = this.builder.diff(this.base));
	private boolean parsed;

	public MovieMeta() {
		// TODO 自動生成されたコンストラクター・スタブ
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

	public abstract C builder();

	public static class SizeMovieMeta extends MovieMeta<SizeData, SizeData, ImageSize, SizeData> {
		@Override
		public ImageSize builder() {
			return new ImageSize();
		}
	}

}
