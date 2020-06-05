package net.teamfruit.emojicord.compat;

import javax.annotation.Nonnull;

public class CompatVersion {
	public static @Nonnull CompatBaseVersion version() {
		return
				#if MC_7
				CompatBaseVersion.V7
				#elif MC_10
				CompatBaseVersion.V10
				#elif MC_14
				CompatBaseVersion.V14
				#elif MC_15
				CompatBaseVersion.V15
				#else
				CompatBaseVersion.V12
				#endif
		;
	}
}