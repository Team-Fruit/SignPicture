package net.teamfruit.bnnwidget.font;

import javax.annotation.Nonnull;

/**
 * フォント描画をします。
 * @see FontManager#getFont(FontShape)
 * @author TeamFruit
 */
public interface WFont {
	/**
	 * フォントを描画します。
	 * @param p 文字描画設定
	 */
	void drawString(@Nonnull FontPosition p);

	/**
	 * 文字列の幅を取得します。
	 * @param p 文字描画設定
	 * @return 幅
	 */
	float getWidth(final @Nonnull FontPosition p);

	/**
	 * 文字列の高さを取得します。
	 * @param p 文字描画設定
	 * @return 高さ
	 */
	float getHeight(final @Nonnull FontPosition p);
}
