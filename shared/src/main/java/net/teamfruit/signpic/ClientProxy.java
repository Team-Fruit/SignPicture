package net.teamfruit.signpic;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

import com.mojang.util.UUIDTypeAdapter;

import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.Session;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.teamfruit.signpic.compat.Compat.CompatItemSignRendererRegistrar;
import net.teamfruit.signpic.compat.CompatBaseProxy;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(final @Nonnull CompatBaseProxy.CompatFMLPreInitializationEvent event) {
		super.preInit(event);

		Config.spec.registerConfigDefine( #if MC_12_LATER Dist #else Side #endif .CLIENT);
		Config.spec.registerConfigHandler( #if MC_12_LATER Dist #else Side #endif .CLIENT, event.getSuggestedConfigurationFile());

		// Setup stencil clip
		// StencilClip.init();

		// Setup location
		Client.initLocation(new Locations(getDataDirectory()));

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

		// Event Register
		Client.handler.init();

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

		ClientCommandHandler.instance.registerCommand(Client.rootCommand);
	}

	@Override
	public void postInit(final @Nonnull CompatFMLPostInitializationEvent event) {
		super.postInit(event);

		Config.spec.save();
	}
}