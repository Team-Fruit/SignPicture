package com.kamesuta.mc.signpic.compat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum CompatVersion {
	V7(7),
	V8(8),
	V9(9),
	V10(10),
	V11(11),
	V12(12),
	V13(13),
	;

	public final int value;

	private CompatVersion(final int value) {
		this.value = value;
	}

	public static @Nonnull CompatVersion version() {
		return V9;
	}

	public static boolean isVersion(final CompatVersion version) {
		return version==version();
	}

	public static boolean isVersion(final CompatVersion... versions) {
		for (final CompatVersion version : versions)
			if (version==version())
				return true;
		return false;
	}

	public static boolean range(@Nullable final CompatVersion min, @Nullable final CompatVersion max) {
		if (min!=null&&version().value<min.value)
			return false;
		if (max!=null&&version().value>max.value)
			return false;
		return true;
	}

	public static boolean older(@Nullable final CompatVersion max) {
		if (max!=null&&version().value>max.value)
			return false;
		return true;
	}

	public static boolean newer(@Nullable final CompatVersion min) {
		if (min!=null&&version().value<min.value)
			return false;
		return true;
	}
}