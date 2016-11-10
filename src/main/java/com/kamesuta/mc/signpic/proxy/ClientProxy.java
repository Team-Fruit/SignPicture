package com.kamesuta.mc.signpic.proxy;

import java.io.File;
import java.io.IOException;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Locations;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.handler.CoreHandler;
import com.kamesuta.mc.signpic.information.CommandDownloadLatest;
import com.kamesuta.mc.signpic.render.CustomTileEntitySignRenderer;
import com.mojang.util.UUIDTypeAdapter;

import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(final FMLPreInitializationEvent event) {
		super.preInit(event);

		// Setup stencil clip
		//StencilClip.init();

		// Setup image
		Client.renderer = new CustomTileEntitySignRenderer();

		Client.mcversion = MinecraftForge.MC_VERSION;
		Client.forgeversion = ForgeVersion.getVersion();

		// Setup location
		Client.location = new Locations(event, getDataDirectory());

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
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySign.class, Client.renderer);

		// Event Register
		Client.handler.init();
		ClientCommandHandler.instance.registerCommand(new CommandDownloadLatest());
	}

	@Override
	public void postInit(final FMLPostInitializationEvent event) {
		super.postInit(event);
	}
}