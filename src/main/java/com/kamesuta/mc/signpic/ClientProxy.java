package com.kamesuta.mc.signpic;

import java.io.File;
import java.io.IOException;

import com.kamesuta.mc.bnnwidget.StencilClip;
import com.kamesuta.mc.signpic.command.CommandVersion;
import com.kamesuta.mc.signpic.command.RootCommand;
import com.kamesuta.mc.signpic.render.CustomTileEntitySignRenderer;
import com.mojang.util.UUIDTypeAdapter;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
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
		} catch (final IllegalArgumentException e) {
		}

		// Setup
		Client.handler = new CoreHandler();
		Client.rootCommand = new RootCommand();

		Client.rootCommand.addChildCommand(new CommandVersion());
	}

	private File getDataDirectory() {
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
		ClientCommandHandler.instance.registerCommand(Client.rootCommand);
	}

	@Override
	public void postInit(final FMLPostInitializationEvent event) {
		super.postInit(event);
	}
}