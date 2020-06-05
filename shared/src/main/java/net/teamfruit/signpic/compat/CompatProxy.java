package net.teamfruit.signpic.compat;

import java.io.File;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.Logger;

public interface CompatProxy {
	public static interface CompatFMLPreInitializationEvent {
		Logger getModLog();

		File getSuggestedConfigurationFile();

		File getSourceFile();
	}

	void preInit(final @Nonnull CompatFMLPreInitializationEvent event);

	public static interface CompatFMLInitializationEvent {
	}

	void init(final @Nonnull CompatFMLInitializationEvent event);

	public static interface CompatFMLPostInitializationEvent {
	}

	void postInit(final @Nonnull CompatFMLPostInitializationEvent event);
}