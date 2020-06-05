package net.teamfruit.emojicord.compat;

import java.io.File;

import javax.annotation.Nonnull;

public interface CompatBaseProxy {
	public static interface CompatFMLPreInitializationEvent {
		File getSuggestedConfigurationFile();
	}

	void preInit(final @Nonnull CompatFMLPreInitializationEvent event);

	public static interface CompatFMLInitializationEvent {
	}

	void init(final @Nonnull CompatFMLInitializationEvent event);

	public static interface CompatFMLPostInitializationEvent {
	}

	void postInit(final @Nonnull CompatFMLPostInitializationEvent event);
}