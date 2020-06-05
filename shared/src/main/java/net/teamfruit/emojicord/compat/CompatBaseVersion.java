package net.teamfruit.emojicord.compat;

import javax.annotation.Nullable;

public enum CompatBaseVersion {
	V7(7),
	V8(8),
	V9(9),
	V10(10),
	V11(11),
	V12(12),
	V13(13),
	V14(14),
	V15(15),
	;

	public final int value;

	private CompatBaseVersion(final int value) {
		this.value = value;
	}

	// ==
	public boolean is(final CompatBaseVersion version) {
		return version==this;
	}

	// ==
	public boolean is(final CompatBaseVersion... versions) {
		for (final CompatBaseVersion version : versions)
			if (version==this)
				return true;
		return false;
	}

	// min <= this <= max
	public boolean range(@Nullable final CompatBaseVersion min, @Nullable final CompatBaseVersion max) {
		if (min!=null&&this.value<min.value)
			return false;
		if (max!=null&&this.value>max.value)
			return false;
		return true;
	}

	// this <= max
	public boolean older(@Nullable final CompatBaseVersion max) {
		if (max!=null&&this.value>max.value)
			return false;
		return true;
	}

	// min <= this
	public boolean newer(@Nullable final CompatBaseVersion min) {
		if (min!=null&&this.value<min.value)
			return false;
		return true;
	}
}