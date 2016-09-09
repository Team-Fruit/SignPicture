package com.kamesuta.mc.signpic.proxy;

import com.kamesuta.mc.signpic.Reference;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	public void preInit(final FMLPreInitializationEvent event) {
		Reference.logger = event.getModLog();
	}

	public void init(final FMLInitializationEvent event) {
	}

	public void postInit(final FMLPostInitializationEvent event) {
	}
}
