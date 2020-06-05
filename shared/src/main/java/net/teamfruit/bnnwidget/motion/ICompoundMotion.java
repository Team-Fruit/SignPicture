package net.teamfruit.bnnwidget.motion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * モーションセット
 *
 * @author TeamFruit
 */
public interface ICompoundMotion {

	/**
	 * モーションが終了しているかどうか
	 * @return モーションが終了している場合はtrue
	 */
	boolean isFinished();

	/**
	 * 最終的な値
	 * @return 最終的な値
	 */
	float getLast();

	/**
	 * 現在の値
	 * @return 現在の値
	 */
	float get();

	/**
	 * 最後のモーション
	 * @return 最後のモーション
	 */
	@Nullable
	IMotion getAnimationLast();

	/**
	 * 現在のモーション
	 * @return 現在のモーション
	 */
	@Nullable
	IMotion getAnimation();

	/**
	 * 現在のモーションを最後までとばし、次のモーションを開始します
	 * @return this
	 */
	@Nonnull
	ICompoundMotion stopNext();

	/**
	 * 次のモーションを開始します
	 * @return this
	 */
	@Nonnull
	ICompoundMotion next();

	/**
	 * モーションを開始します
	 * @return this
	 */
	@Nonnull
	ICompoundMotion start();

	/**
	 * モーションを一時停止します
	 * @return this
	 */
	@Nonnull
	ICompoundMotion pause();

	/**
	 * 最後までとばし、タスクを終了させます
	 * @return this
	 */
	@Nonnull
	ICompoundMotion stopLast();

	/**
	 * 現在の値を保持し、タスクを終了させます
	 * @return this
	 */
	@Nonnull
	ICompoundMotion stop();

	/**
	 * タスクを終了させます
	 * @return this
	 */
	@Nonnull
	ICompoundMotion stopFirst();

	/**
	 * ループ再生を設定します
	 * @param b ループ再生をする場合はtrue
	 * @return this
	 */
	@Nonnull
	ICompoundMotion setLoop(final boolean b);

	/**
	 * モーションを追加します
	 * @param animation モーション
	 * @return this
	 */
	@Nonnull
	ICompoundMotion add(final @Nonnull IMotion animation);

	/**
	 * モーションを再再生します
	 * @return this
	 */
	@Nonnull
	ICompoundMotion restart();

	/**
	 * モーションをすべて消去します
	 * @return this
	 */
	@Nonnull
	ICompoundMotion reset();

}
