package net.teamfruit.emojicord;

import java.io.File;

public class Locations {
	public static final Locations instance = new Locations();

	public File getMinecraftDirectory() {
		final File gameDir =
				#if MC_12_LATER
				net.minecraftforge.fml.loading.FMLPaths.GAMEDIR.get().toFile()
				#else
				net.teamfruit.emojicord.compat.Compat.getMinecraft().mcDataDir
				#endif
				;
		//try {
		//	gameDir = gameDir.getCanonicalFile();
		//} catch (final IOException e) {
		//	Log.log.error("Cannot get game directory: ", e);
		//}
		return gameDir;
	}

	public File getEmojicordDirectory() {
		return new File(getMinecraftDirectory(), Reference.MODID);
	}

	public File getCacheDirectory() {
		return new File(getEmojicordDirectory(), "cache");
	}

	public File getDictionaryDirectory() {
		return new File(getEmojicordDirectory(), "dictionary");
	}
}
