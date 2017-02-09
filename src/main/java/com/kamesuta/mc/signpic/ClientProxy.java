package com.kamesuta.mc.signpic;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

import com.kamesuta.mc.signpic.render.CustomItemSignRenderer;
import com.mojang.util.UUIDTypeAdapter;

import net.minecraft.init.Items;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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

		ModelLoader.setCustomModelResourceLocation(Items.sign, 0, CustomItemSignRenderer.modelResourceLocation);
	}

	private @Nonnull File getDataDirectory() {
		final File file = Client.mc.mcDataDir;
		try {
			return file.getCanonicalFile();
		} catch (final IOException e) {
			Log.dev.error("Could not canonize path!", e);
		}
		return file;
	}

	@Override
	public void init(final @Nonnull FMLInitializationEvent event) {
		super.init(event);

		// Replace Sign Renderer
		//TODO ItemSignPictureを何とかする
		//		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySign.class, Client.renderer);

		// Event Register
		Client.handler.init();
		ClientCommandHandler.instance.registerCommand(Client.rootCommand);
	}

	@Override
	public void postInit(final @Nonnull FMLPostInitializationEvent event) {
		super.postInit(event);
	}
}