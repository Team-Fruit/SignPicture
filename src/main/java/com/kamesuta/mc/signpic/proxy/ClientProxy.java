package com.kamesuta.mc.signpic.proxy;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.image.ImageLocation;
import com.kamesuta.mc.signpic.image.ImageManager;
import com.kamesuta.mc.signpic.render.CustomTileEntitySignRenderer;
import com.kamesuta.mc.signpic.render.RenderOverlay;
import com.kamesuta.mc.signpic.render.RenderTick;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	public ImageManager manager;

	@Override
	public void preInit(final FMLPreInitializationEvent event) {
		super.preInit(event);

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

		this.manager = new ImageManager(new ImageLocation(rootdir));
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

		final CustomTileEntitySignRenderer renderer = new CustomTileEntitySignRenderer(this.manager);
		renderer.func_147497_a(TileEntityRendererDispatcher.instance);
		@SuppressWarnings("unchecked")
		final Map<Class<?>, ? super TileEntitySpecialRenderer> renderers = TileEntityRendererDispatcher.instance.mapSpecialRenderers;
		renderers.put(TileEntitySign.class, renderer);

		FMLCommonHandler.instance().bus().register(new RenderTick());
		MinecraftForge.EVENT_BUS.register(new RenderOverlay(this.manager));
	}

	@Override
	public void postInit(final FMLPostInitializationEvent event) {
		super.postInit(event);
	}
}