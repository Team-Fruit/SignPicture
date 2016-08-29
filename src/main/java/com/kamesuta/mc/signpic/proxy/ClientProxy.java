package com.kamesuta.mc.signpic.proxy;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.kamesuta.mc.guiwidget.StencilClip;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.handler.KeyHandler;
import com.kamesuta.mc.signpic.handler.SignHandler;
import com.kamesuta.mc.signpic.image.ImageLocation;
import com.kamesuta.mc.signpic.image.ImageManager;
import com.kamesuta.mc.signpic.render.CustomTileEntitySignRenderer;
import com.kamesuta.mc.signpic.render.RenderOverlay;
import com.kamesuta.mc.signpic.version.CommandDownloadLatest;
import com.kamesuta.mc.signpic.version.VersionChecker;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	public static ImageManager manager;

	@Override
	public void preInit(final FMLPreInitializationEvent event) {
		super.preInit(event);

		// Enable stencil clip
		StencilClip.init();

		// Occupy my cache directory
		final File mcdir = getDataDirectory();
		final File rootdir = new File(mcdir, "signpic");
		if (rootdir.exists() && !rootdir.isDirectory()) {
			File to;
			int i = 2;
			do {
				to = new File(mcdir, "signpic"+i);
				i++;
			} while (to.exists());
			rootdir.renameTo(to);
			Reference.logger.warn("non-directory conflicting file exists. renamed to " + to.getName());
		}
		rootdir.mkdir();

		manager = new ImageManager(new ImageLocation(rootdir));

		KeyHandler.INSTANCE.init();
	}

	public File getDataDirectory() {
		final File file = FMLClientHandler.instance().getClient().mcDataDir;
		try {
			return file.getCanonicalFile();
		} catch (final IOException e) {
			Reference.logger.debug("Could not canonize path!", e);
		}
		return file;
	}

	@Override
	public void init(final FMLInitializationEvent event) {
		super.init(event);

		// Replace Sign Renderer
		final CustomTileEntitySignRenderer renderer = new CustomTileEntitySignRenderer(manager);
		renderer.func_147497_a(TileEntityRendererDispatcher.instance);
		@SuppressWarnings("unchecked")
		final Map<Class<?>, ? super TileEntitySpecialRenderer> renderers = TileEntityRendererDispatcher.instance.mapSpecialRenderers;
		renderers.put(TileEntitySign.class, renderer);

		// Event Register
		FMLCommonHandler.instance().bus().register(manager);
		FMLCommonHandler.instance().bus().register(KeyHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(new SignHandler());
		MinecraftForge.EVENT_BUS.register(new RenderOverlay(manager));

		// Versioning
		ClientCommandHandler.instance.registerCommand(new CommandDownloadLatest());
		new VersionChecker().init();
	}

	@Override
	public void postInit(final FMLPostInitializationEvent event) {
		super.postInit(event);
	}
}