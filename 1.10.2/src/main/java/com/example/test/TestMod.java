package com.example.test;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "testsushi", name = "TestSUSHI", version = "0.1.0")
public class TestMod {
	@EventHandler
	public void preInit(final FMLPreInitializationEvent event) {

	}

	@EventHandler
	public void init(final FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new Evnt());
	}

	@EventHandler
	public void postInit(final FMLPostInitializationEvent event) {
	}
}
