package com.kamesuta.mc.signpic;

import java.io.File;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.Logger;

import com.kamesuta.mc.signpic.compat.Compat.CompatProxy;
import com.kamesuta.mc.signpic.compat.Compat.CompatProxy.CompatFMLInitializationEvent;
import com.kamesuta.mc.signpic.compat.Compat.CompatProxy.CompatFMLPostInitializationEvent;
import com.kamesuta.mc.signpic.compat.Compat.CompatProxy.CompatFMLPreInitializationEvent;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY)
public class SignPicture {
	@Instance(Reference.MODID)
	public static @Nullable SignPicture instance;

	@SidedProxy(serverSide = Reference.PROXY_SERVER, clientSide = Reference.PROXY_CLIENT)
	public static @Nullable CompatProxy proxy;

	@NetworkCheckHandler
	public boolean checkModList(final @Nonnull Map<String, String> versions, final @Nonnull Side side) {
		return true;
	}

	@EventHandler
	public void preInit(final @Nonnull FMLPreInitializationEvent event) {
		if (proxy!=null)
			proxy.preInit(new CompatFMLPreInitializationEventImpl(event));
	}

	@EventHandler
	public void init(final @Nonnull FMLInitializationEvent event) {
		if (proxy!=null)
			proxy.init(new CompatFMLInitializationEventImpl(event));
	}

	@EventHandler
	public void postInit(final @Nonnull FMLPostInitializationEvent event) {
		if (proxy!=null)
			proxy.postInit(new CompatFMLPostInitializationEventImpl(event));
	}

	private static class CompatFMLPreInitializationEventImpl implements CompatFMLPreInitializationEvent {
		private final @Nonnull FMLPreInitializationEvent event;

		public CompatFMLPreInitializationEventImpl(final FMLPreInitializationEvent event) {
			this.event = event;
		}

		@Override
		public Logger getModLog() {
			return this.event.getModLog();
		}

		@Override
		public File getSuggestedConfigurationFile() {
			return this.event.getSuggestedConfigurationFile();
		}

		@Override
		public File getSourceFile() {
			return this.event.getSourceFile();
		}
	}

	private static class CompatFMLInitializationEventImpl implements CompatFMLInitializationEvent {
		public CompatFMLInitializationEventImpl(final FMLInitializationEvent event) {
		}
	}

	private static class CompatFMLPostInitializationEventImpl implements CompatFMLPostInitializationEvent {
		public CompatFMLPostInitializationEventImpl(final FMLPostInitializationEvent event) {
		}
	}
}
