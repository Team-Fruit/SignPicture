package net.teamfruit.signpic;

import java.io.File;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import net.teamfruit.signpic.compat.CompatProxy;
import net.teamfruit.signpic.compat.CompatProxy.CompatFMLInitializationEvent;
import net.teamfruit.signpic.compat.CompatProxy.CompatFMLPostInitializationEvent;
import net.teamfruit.signpic.compat.CompatProxy.CompatFMLPreInitializationEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY)
public class SignPictureL {
	@Instance(Reference.MODID)
	public static @Nullable SignPictureL instance;

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
