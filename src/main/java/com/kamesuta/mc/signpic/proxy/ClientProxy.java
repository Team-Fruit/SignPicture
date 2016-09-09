package com.kamesuta.mc.signpic.proxy;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.kamesuta.mc.bnnwidget.StencilClip;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.handler.CoreHandler;
import com.kamesuta.mc.signpic.image.ImageLocation;
import com.kamesuta.mc.signpic.image.ImageManager;
import com.kamesuta.mc.signpic.information.CommandDownloadLatest;
import com.kamesuta.mc.signpic.render.CustomTileEntitySignRenderer;
import com.mojang.util.UUIDTypeAdapter;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(final FMLPreInitializationEvent event) {
		super.preInit(event);

		// Setup stencil clip
		StencilClip.init();

		// Setup cache directory
		final File mcdir = getDataDirectory();
		final File signpicdir = new File(mcdir, "signpic");
		boolean legacy = signpicdir.isDirectory();
		securementDirectory(signpicdir);
		final File cachedir = new File(signpicdir, "cache");
		legacy = legacy && !cachedir.isDirectory();
		securementDirectory(cachedir);

		// Move legacy file
		if (legacy) {
			Reference.logger.info("moved legacy files");
			for (final File f : signpicdir.listFiles())
				if (f.isFile())
					f.renameTo(new File(cachedir, f.getName()));
		}

		// Setup image
		final ImageManager manager = new ImageManager(new ImageLocation(cachedir));
		Client.manager = manager;
		Client.renderer = new CustomTileEntitySignRenderer(manager);

		Client.mcversion = MinecraftForge.MC_VERSION;
		Client.forgeversion = ForgeVersion.getVersion();

		// Setup location
		Client.mcDir = mcdir;
		Client.signpicDir = signpicdir;
		Client.signpicCacheDir = cachedir;
		Client.configDir = event.getModConfigurationDirectory();
		Client.configFile = event.getSuggestedConfigurationFile();
		Client.modDir = new File(mcdir, "mods");
		Client.modFile = event.getSourceFile();

		// Get Id
		final String id = Client.mc.getSession().getPlayerID();
		try {
			final Object o = UUIDTypeAdapter.fromString(id);
			if (o!=null) {
				Client.id = id;
				Client.name = Client.mc.getSession().getUsername();
			}
		} catch (final IllegalArgumentException e) {}

		// Setup
		Client.handler = new CoreHandler();
	}

	private boolean securementDirectory(final File cachedir) {
		if (cachedir.exists() && !cachedir.isDirectory()) {
			File to;
			int i = 2;
			do {
				to = new File(cachedir.getParent(), cachedir.getName()+i);
				i++;
			} while (to.exists());
			cachedir.renameTo(to);
			Reference.logger.warn("non-directory conflicting file exists. renamed to " + to.getName());
			return true;
		}
		cachedir.mkdir();
		return false;
	}

	public File getDataDirectory() {
		final File file = Client.mc.mcDataDir;
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
		Client.renderer.func_147497_a(TileEntityRendererDispatcher.instance);
		@SuppressWarnings("unchecked")
		final Map<Class<?>, ? super TileEntitySpecialRenderer> renderers = TileEntityRendererDispatcher.instance.mapSpecialRenderers;
		renderers.put(TileEntitySign.class, Client.renderer);

		// Event Register
		Client.handler.init();
		ClientCommandHandler.instance.registerCommand(new CommandDownloadLatest());
	}

	@Override
	public void postInit(final FMLPostInitializationEvent event) {
		super.postInit(event);
	}
}