package com.kamesuta.mc.signpic;

import javax.annotation.Nonnull;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	public void preInit(final @Nonnull FMLPreInitializationEvent event) {
		Log.log = event.getModLog();
		Config.init(event.getSuggestedConfigurationFile());
	}

	public void init(final @Nonnull FMLInitializationEvent event) {
	}

	public void postInit(final @Nonnull FMLPostInitializationEvent event) {
		Config.getConfig().save();
	}
}
