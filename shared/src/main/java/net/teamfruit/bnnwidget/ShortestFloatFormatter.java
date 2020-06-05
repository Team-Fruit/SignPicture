package net.teamfruit.bnnwidget;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.annotation.Nonnull;

/**
 * 小数点をできるだけ短い文字列で表現します。
 *
 * @author TeamFruit
 */
public class ShortestFloatFormatter {
	private static final @Nonnull DecimalFormat signformat;

	static {
		final DecimalFormat format = new DecimalFormat(".##");
		// どの環境でも同じ構文になるように調整します。
		final DecimalFormatSymbols custom = new DecimalFormatSymbols();
		custom.setDecimalSeparator('.');
		format.setDecimalFormatSymbols(custom);
		signformat = format;
	}

	/**
	 * 小数点を文字列に変換します。
	 * @param f 小数点
	 * @return 変換後の文字列
	 */
	public static @Nonnull String format(final float f) {
		if (f==0)
			return "0";

		final String str = signformat.format(f);

		final String cut = ".0";

		int end = str.length();
		int last = cut.length();

		while (end!=0&&last!=0)
			if (cut.charAt(last-1)==str.charAt(end-1))
				end--;
			else
				last--;
		return str.substring(0, end);
	}
}
