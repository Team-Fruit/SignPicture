package net.teamfruit.signpic;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

import com.mojang.util.UUIDTypeAdapter;

import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.Session;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.teamfruit.signpic.compat.Compat;
import net.teamfruit.signpic.compat.Compat.CompatItemSignRendererRegistrar;
import net.teamfruit.signpic.compat.CompatBaseProxy;
import net.teamfruit.signpic.information.Endpoint;

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

		// Event Register
		Client.handler.init();

		CompatItemSignRendererRegistrar.registerPreInit(Client.itemRenderer);

		Compat.CompatVersionChecker.startVersionCheck(Reference.MODID, VersionReference.VERSION, Reference.UPDATE_JSON);
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

		if (Endpoint.loadGateway()) {
			Analytics.instance.startAnalytics();
		}

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