package net.teamfruit.emojicord;

import java.io.File;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

#if MC_12_LATER
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
#elif MC_7_LATER
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;
#else
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
#endif

import net.teamfruit.emojicord.compat.CompatBaseProxy;
import net.teamfruit.emojicord.compat.CompatBaseProxy.CompatFMLInitializationEvent;
import net.teamfruit.emojicord.compat.CompatBaseProxy.CompatFMLPostInitializationEvent;
import net.teamfruit.emojicord.compat.CompatBaseProxy.CompatFMLPreInitializationEvent;

@Mod(
		#if MC_12_LATER
		value = Reference.MODID
		#else
		modid = Reference.MODID,
		name = Reference.NAME,
		version = VersionReference.VERSION,
		guiFactory = Reference.GUI_FACTORY
		#if MC_7_LATER , updateJSON = Reference.UPDATE_JSON #endif
		#endif
)
public class Emojicord {
	#if !MC_12_LATER
	@Instance(Reference.MODID)
	#endif
	public static @Nullable Emojicord instance;

	#if !MC_12_LATER
	@SidedProxy(serverSide = Reference.PROXY_SERVER, clientSide = Reference.PROXY_CLIENT)
	#endif
	public static @Nullable CompatBaseProxy proxy
	#if MC_12_LATER
	= DistExecutor.<CompatBaseProxy> runForDist(() -> () -> {
		try {
			return (CompatBaseProxy) Class.forName(Reference.PROXY_CLIENT).newInstance();
		} catch (InstantiationException|IllegalAccessException|ClassNotFoundException e) {
			throw new RuntimeException("Could not load proxy class: ", e);
		}
	}, () -> () -> {
		try {
			return (CompatBaseProxy) Class.forName(Reference.PROXY_SERVER).newInstance();
		} catch (InstantiationException|IllegalAccessException|ClassNotFoundException e) {
			throw new RuntimeException("Could not load proxy class: ", e);
		}
	})
	#endif
	;

	#if MC_12_LATER
	public Emojicord() {
		instance = this;

		// FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInit);

		if (proxy!=null)
			proxy.preInit(new CompatFMLPreInitializationEventImpl());
	}
	#else
	@NetworkCheckHandler
	public boolean checkModList(final @Nonnull Map<String, String> versions, final @Nonnull Side side) {
		return true;
	}
	#endif

	#if !MC_12_LATER
	@EventHandler
	public void preInit(final @Nonnull FMLPreInitializationEvent event) {
		if (proxy!=null)
			proxy.preInit(new CompatFMLPreInitializationEventImpl(event));
	}
	#endif

	#if MC_12_LATER @SubscribeEvent #else @EventHandler #endif
	public void init(final @Nonnull #if MC_12_LATER FMLClientSetupEvent #else FMLInitializationEvent #endif event) {
		if (proxy!=null)
			proxy.init(new CompatFMLInitializationEventImpl());
	}

	#if MC_12_LATER @SubscribeEvent #else @EventHandler #endif
	public void postInit(final @Nonnull #if MC_12_LATER FMLLoadCompleteEvent #else FMLPostInitializationEvent #endif event) {
		if (proxy!=null)
			proxy.postInit(new CompatFMLPostInitializationEventImpl());
	}

	private static class CompatFMLPreInitializationEventImpl implements CompatFMLPreInitializationEvent {
		#if !MC_12_LATER
		private final @Nonnull FMLPreInitializationEvent event;

		public CompatFMLPreInitializationEventImpl(final FMLPreInitializationEvent event) {
			this.event = event;
		}
		#endif

		@Override
		public File getSuggestedConfigurationFile() {
			return #if MC_12_LATER null #else this.event.getSuggestedConfigurationFile() #endif ;
		}
	}

	private static class CompatFMLInitializationEventImpl implements CompatFMLInitializationEvent {
	}

	private static class CompatFMLPostInitializationEventImpl implements CompatFMLPostInitializationEvent {
	}
}
