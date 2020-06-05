package net.teamfruit.bnnwidget.motion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * モーション
 *
 * @author TeamFruit
 */
public interface IMotion {

	/**
	 * リスタート
	 * @return this
	 */
	@Nonnull
	IMotion restart();

	/**
	 * モーションを完了させます
	 * @return this
	 */
	@Nonnull
	IMotion finish();

	/**
	 * モーションを一時停止します
	 * @return this
	 */
	@Nonnull
	IMotion pause();

	/**
	 * モーションを再開します
	 * @return this
	 */
	@Nonnull
	IMotion resume();

	/**
	 * 進捗時間を設定します
	 * @param time 進捗時間
	 * @return this
	 */
	@Nonnull
	IMotion setTime(float time);

	/**
	 * モーションが完了しているかどうか
	 * @return モーションが完了している場合はtrue
	 */
	boolean isFinished();

	/**
	 * モーション完了後のタスク
	 * @param r タスク
	 * @return this
	 */
	@Nonnull
	IMotion setAfter(@Nullable Runnable r);

	/**
	 * モーションの時間
	 * @return モーションの時間
	 */
	float getDuration();

	/**
	 * モーション後の値
	 * @param start モーション前の値
	 * @return モーション後の値
	 */
	float getEnd(float start);

	/**
	 * モーション完了後のタスク
	 * @return タスク
	 */
	@Nullable
	Runnable getAfter();

	/**
	 * モーションが完了したときに呼ばれます
	 */
	void onFinished();

	/**
	 * 現在の値
	 * @param start モーション前の値
	 * @return 現在の値
	 */
	float get(float start);

}