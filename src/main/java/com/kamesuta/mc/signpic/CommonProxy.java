package com.kamesuta.mc.signpic;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	public void preInit(final FMLPreInitializationEvent event) {
		Log.log = event.getModLog();
		Config.instance = new Config(event.getSuggestedConfigurationFile());
	}

	public void init(final FMLInitializationEvent event) {
	}

	public void postInit(final FMLPostInitializationEvent event) {
		Config.instance.save();
	}
}
