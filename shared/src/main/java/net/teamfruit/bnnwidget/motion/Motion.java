package net.teamfruit.bnnwidget.motion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.util.Timer;

/**
 * モーション
 *
 * @author TeamFruit
 */
public abstract class Motion implements IMotion {

	/**
	 * タイマー
	 */
	protected final @Nonnull Timer timer;
	/**
	 * モーションの長さ
	 */
	protected final float duration;
	/**
	 * モーション後タスク
	 */
	protected @Nullable Runnable after;

	public Motion(final float duration) {
		this.timer = new Timer();
		this.timer.pause();
		restart();
		this.duration = duration;
	}

	@Override
	public @Nonnull IMotion restart() {
		setTime(0);
		return this;
	}

	@Override
	public @Nonnull IMotion finish() {
		setTime(this.duration);
		return this;
	}

	@Override
	public @Nonnull IMotion pause() {
		this.timer.pause();
		return this;
	}

	@Override
	public @Nonnull IMotion resume() {
		this.timer.resume();
		return this;
	}

	@Override
	public @Nonnull IMotion setTime(final float time) {
		this.timer.set(time);
		return this;
	}

	@Override
	public boolean isFinished() {
		return this.timer.getTime()>=this.duration;
	}

	@Override
	public IMotion setAfter(final @Nullable Runnable r) {
		this.after = r;
		return this;
	}

	@Override
	public float getDuration() {
		return this.duration;
	}

	@Override
	public @Nullable Runnable getAfter() {
		return this.after;
	}

	@Override
	public void onFinished() {
		final Runnable r = getAfter();
		if (r!=null)
			r.run();
	}

	@Override
	public String toString() {
		return String.format("Motion[(%1$ss)]", this.duration);
	}

	/**
	 * Easingモーションを作成します
	 * @param duration モーションの長さ
	 * @param easing Easing
	 * @param end モーション後値
	 * @return モーション
	 */
	public static @Nonnull IMotion easing(final float duration, final @Nonnull Easing easing, final float end) {
		return new EasingMotion(duration, easing, end);
	}

	/**
	 * 空のモーションを作成します
	 * <p>
	 * 主に間を置くために使用されます
	 * @param duration モーションの長さ
	 * @return モーション
	 */
	public static @Nonnull IMotion blank(final float duration) {
		return new BlankMotion(duration);
	}

	/**
	 * 移動モーションを作成します。
	 * <p>
	 * 補完がなく、モーションの最初から最後までモーション後値となります
	 * @param end モーション後値
	 * @return モーション
	 */
	public static @Nonnull IMotion move(final float end) {
		return new MoveMotion(end);
	}

	/**
	 * モーションからモーションセットを作成します
	 * @param motions モーション
	 * @return モーションセット
	 */
	public static @Nonnull CompoundMotion of(final @Nonnull IMotion... motions) {
		return CompoundMotion.of(motions);
	}

	/**
	 * 初期値とモーションからモーションセットを作成します
	 * @param coord 初期値
	 * @param motions モーション
	 * @return モーションセット
	 */
	public static @Nonnull CompoundMotion of(final float coord, final @Nonnull IMotion... motions) {
		return CompoundMotion.of(coord, motions);
	}

	/**
	 * Easingモーション
	 *
	 * @author TeamFruit
	 */
	static class EasingMotion extends Motion {
		/**
		 * Easing
		 */
		protected final @Nonnull Easing easing;
		/**
		 * モーション後値
		 */
		protected final float end;

		public EasingMotion(final float duration, final @Nonnull Easing easing, final float end) {
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
		/**
		 * モーション後値
		 */
		protected final float end;

		public MoveMotion(final float end) {
			super(0f);
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
		public @Nonnull String toString() {
			return String.format("Move[->%2$s(%1$ss)]", this.duration, this.end);
		}
	}

}