package com.kamesuta.mc.signpic;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy implements IInit {
	@Override
	public void preInit(final FMLPreInitializationEvent event) {
		Reference.logger = event.getModLog();
		Config.instance = new Config(event.getSuggestedConfigurationFile());
	}

	@Override
	public void init(final FMLInitializationEvent event) {
	}

	@Override
	public void postInit(final FMLPostInitializationEvent event) {
		Config.instance.save();
	}
}
