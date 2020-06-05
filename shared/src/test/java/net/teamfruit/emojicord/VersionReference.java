package net.teamfruit.emojicord;

import javax.annotation.Nonnull;

#if MC_12_LATER
import java.util.Optional;
import java.util.jar.Attributes;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
#endif

public class VersionReference {
	public static @Nonnull #if !MC_12_LATER final #endif String VERSION = "${version}";
	public static @Nonnull #if !MC_12_LATER final #endif String FORGE = "${forgeversion}";
	public static @Nonnull #if !MC_12_LATER final #endif String MINECRAFT = "${mcversion}";

	#if MC_12_LATER
	static {
		ModList.get().getModContainerById(Reference.MODID)
				.map(e -> e.getModInfo())
				.ifPresent(e -> {
					VERSION = e.getVersion().toString();
					if (e instanceof ModInfo) {
						final ModFile modFile = ((ModInfo) e).getOwningFile().getFile();
						modFile.getLocator().findManifest(modFile.getFilePath()).ifPresent(s -> {
							final Attributes attributes = s.getMainAttributes();
							Optional.ofNullable(attributes.getValue("ForgeVersion")).ifPresent(v -> FORGE = v);
							Optional.ofNullable(attributes.getValue("MinecraftVersion")).ifPresent(v -> MINECRAFT = v);
						});
					}
				});
	}
	#endif
}
