package net.teamfruit.bnnwidget.var;

/**
 * 書き換えることができる値の基本インターフェイスです。
 * <p>
 * オーバーライドして独自の値を定義することができます
 *
 * @author TeamFruit
 */
public interface VCommon {

	/**
	 * 値を取得します
	 * @return 値
	 */
	float get();

	/**
	 * 2つの値をもとに値を取得します
	 * @param a 始点
	 * @param b 終点
	 * @return 値
	 */
	float getAbsCoord(float a, float b);

}