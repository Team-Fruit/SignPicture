package com.kamesuta.mc.signpic;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	public void preInit(final FMLPreInitializationEvent event) {
		Reference.logger = event.getModLog();
		Config.instance = new Config(event.getSuggestedConfigurationFile());
	}

	public void init(final FMLInitializationEvent event) {
	}

	public void postInit(final FMLPostInitializationEvent event) {
		Config.instance.save();
	}
}
