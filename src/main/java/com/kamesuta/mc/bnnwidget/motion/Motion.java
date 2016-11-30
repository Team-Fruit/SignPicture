package com.kamesuta.mc.bnnwidget.motion;

import org.lwjgl.util.Timer;

public abstract class Motion implements IMotion {

	protected final Timer timer;
	protected final float duration;
	protected Runnable after;

	public Motion(final float duration) {
		this.timer = new Timer();
		this.timer.pause();
		restart();
		this.duration = duration;
	}

	@Override
	public IMotion restart() {
		setTime(0);
		return this;
	}

	@Override
	public IMotion finish() {
		setTime(this.duration);
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
	public IMotion setTime(final float time) {
		this.timer.set(time);
		return this;
	}

	@Override
	public boolean isFinished() {
		return this.timer.getTime()>=this.duration;
	}

	@Override
	public IMotion setAfter(final Runnable r) {
		this.after = r;
		return this;
	}

	@Override
	public float getDuration() {
		return this.duration;
	}

	@Override
	public Runnable getAfter() {
		return this.after;
	}

	@Override
	public void onFinished() {
		if (getAfter()!=null)
			getAfter().run();
	}

	public static IMotion easing(final float duration, final Easing easing, final float end) {
		return new EasingMotion(duration, easing, end);
	}

	public static IMotion blank(final float duration) {
		return new BlankMotion(duration);
	}

	public static IMotion move(final float end) {
		return new MoveMotion(end);
	}

	public static CompoundMotion of(final IMotion... motions) {
		return CompoundMotion.of(motions);
	}

	public static CompoundMotion of(final float coord, final IMotion... motions) {
		return CompoundMotion.of(coord, motions);
	}

	static class EasingMotion extends Motion {
		protected final Easing easing;
		protected final float end;

		public EasingMotion(final float duration, final Easing easing, final float end) {
			super(duration);
			this.easing = easing;
			this.end = end;
		}

		@Override
		public float getEnd(final float start) {
			return this.end;
		}

		@Override
		public float get(final float start) {
			return (float) this.easing.easing(this.timer.getTime(), start, this.end-start, this.duration);
		}

		@Override
		public String toString() {
			return String.format("Motion[%2$s->%3$s(%1$ss)]", this.duration, this.easing, this.end);
		}
	}

	static class BlankMotion extends Motion {
		public BlankMotion(final float duration) {
			super(duration);
		}

		@Override
		public float getEnd(final float start) {
			return start;
		}

		@Override
		public float get(final float start) {
			return start;
		}

		@Override
		public String toString() {
			return String.format("Blank[(%ss)]", this.duration);
		}
	}

	static class MoveMotion extends Motion {
		protected final float end;

		public MoveMotion(final float end) {
			super(0.5f);
			this.end = end;
		}

		@Override
		public float getEnd(final float start) {
			return this.end;
		}

		@Override
		public float get(final float start) {
			return this.end;
		}

		@Override
		public String toString() {
			return String.format("Move[->%2$s(%1$ss)]", this.duration, this.end);
		}
	}

}