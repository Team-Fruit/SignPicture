package net.teamfruit.bnnwidget.var;

/**
 * 絶対的な値と相対的な値
 *
 * @author TeamFruit
 */
public enum VType {
	/**
	 * 絶対的な値
	 */
	Absolute {
		@Override
		public float calc(final float a, final float b, final float c) {
			return c;
		}
	},
	/**
	 * 相対的な値
	 */
	Percent {
		@Override
		public float calc(final float a, final float b, final float c) {
			return a*(1f-c)+b*c;
		}
	},
	;

	/**
	 * 絶対的な値は、その値を返します。
	 * <p>
	 * 相対的な値は、始点から終点まで間の値を返します。
	 * @param a 始点
	 * @param b 終点
	 * @param f 値
	 * @return 値
	 */
	public abstract float calc(float a, float b, float f);
}
