package com.kamesuta.mc.bnnwidget;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.annotation.Nonnull;

public class ShortestFloatFormatter {
	private static final @Nonnull DecimalFormat signformat;

	static {
		final DecimalFormat format = new DecimalFormat(".##");
		final DecimalFormatSymbols custom = new DecimalFormatSymbols();
		custom.setDecimalSeparator('.');
		format.setDecimalFormatSymbols(custom);
		signformat = format;
	}

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
