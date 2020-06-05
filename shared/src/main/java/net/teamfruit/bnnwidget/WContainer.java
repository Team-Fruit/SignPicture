package net.teamfruit.bnnwidget;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * 軽量コンポーネントを含むことができるコンテナーインターフェイスです。
 * @param <W> 含まれているコンポーネント型
 * @author TeamFruit
 */
public interface WContainer<W extends WCommon> {

	/**
	 * コンポーネントを消去します。
	 * @param widget コンポーネント
	 * @return 消去できた場合はtrue
	 */
	boolean remove(final @Nonnull W widget);

	/**
	 * コンポーネントを追加します。
	 * @param widget コンポーネント
	 * @return 追加できた場合はtrue
	 */
	boolean add(final @Nonnull W widget);

	/**
	 * 含まれているコンポーネント
	 * <p>
	 * 通常、深いコンポーネントから浅いコンポーネントへの順番になります。
	 * @return 含まれているコンポーネントリスト
	 */
	@Nonnull
	List<W> getContainer();

}
