package com.kamesuta.mc.signpic.proxy;

import com.kamesuta.mc.signpic.Reference;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	public void preInit(final FMLPreInitializationEvent event) {
		Reference.logger = event.getModLog();
	}

	public void init(final FMLInitializationEvent event) {
	}

	public void postInit(final FMLPostInitializationEvent event) {
	}
}
