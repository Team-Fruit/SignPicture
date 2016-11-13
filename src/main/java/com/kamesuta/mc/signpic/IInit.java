package com.kamesuta.mc.signpic;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public interface IInit {
	void preInit(final FMLPreInitializationEvent event);

	void init(final FMLInitializationEvent event);

	void postInit(final FMLPostInitializationEvent event);
}
