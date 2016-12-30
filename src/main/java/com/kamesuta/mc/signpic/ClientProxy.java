package com.kamesuta.mc.signpic;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

import com.kamesuta.mc.signpic.render.CustomItemSignRenderer;
import com.mojang.util.UUIDTypeAdapter;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(final @Nonnull FMLPreInitializationEvent event) {
		super.preInit(event);

		// Setup stencil clip
		// StencilClip.init();

		// Setup location
		Client.initLocation(new Locations(event.getSourceFile(), getDataDirectory()));

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
	}

	private File getDataDirectory() {
		final File file = Client.mc.mcDataDir;
		try {
			return file.getCanonicalFile();
		} catch (final IOException e) {
			Log.dev.error("Could not canonize path!", e);
		}
		return file;
	}

	@Override
	public void init(final FMLInitializationEvent event) {
		super.init(event);

		// Replace Sign Renderer
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySign.class, Client.renderer);
		MinecraftForgeClient.registerItemRenderer(Items.sign, new CustomItemSignRenderer());

		// Event Register
		Client.handler.init();
		ClientCommandHandler.instance.registerCommand(Client.rootCommand);
	}

	@Override
	public void postInit(final FMLPostInitializationEvent event) {
		super.postInit(event);
	}
}