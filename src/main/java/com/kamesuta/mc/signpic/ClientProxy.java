package com.kamesuta.mc.signpic;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

import com.kamesuta.mc.signpic.compat.Compat.CompatItemSignRendererRegistrar;
import com.mojang.util.UUIDTypeAdapter;

import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.Session;
import net.minecraftforge.client.ClientCommandHandler;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(final @Nonnull CompatFMLPreInitializationEvent event) {
		super.preInit(event);

		Log.log = event.getModLog();
		Config.init(event.getSuggestedConfigurationFile());

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
				final Session s = Client.mc.getSession();
				Client.name = s.getUsername();
				Client.token = s.getToken();
			}
		} catch (final IllegalArgumentException e) {
		}

		CompatItemSignRendererRegistrar.registerPreInit(Client.itemRenderer);
	}

	private @Nonnull File getDataDirectory() {
		final File file = Client.mc.mcDataDir;
		try {
			return file.getCanonicalFile();
		} catch (final IOException e) {
			Log.log.debug("Could not canonize path!", e);
		}
		return file;
	}

	@Override
	public void init(final @Nonnull CompatFMLInitializationEvent event) {
		super.init(event);

		// Replace Sign Renderer
		CompatItemSignRendererRegistrar.bindTileEntitySpecialRenderer(TileEntitySign.class, Client.rendererTile);
		CompatItemSignRendererRegistrar.registerInit(Client.itemRenderer);

		// Event Register
		Client.handler.init();
		ClientCommandHandler.instance.registerCommand(Client.rootCommand);
	}

	@Override
	public void postInit(final @Nonnull CompatFMLPostInitializationEvent event) {
		super.postInit(event);

		Config.getConfig().save();
	}
}