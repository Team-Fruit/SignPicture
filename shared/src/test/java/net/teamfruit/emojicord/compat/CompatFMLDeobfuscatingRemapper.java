package net.teamfruit.emojicord.compat;

import javax.annotation.Nonnull;

#if MC_12_LATER
import cpw.mods.modlauncher.Launcher;

#else
import net.minecraft.launchwrapper.Launch;
#if MC_7_LATER
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
#else
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
#endif
#endif

public class CompatFMLDeobfuscatingRemapper {
	public static @Nonnull String mapDesc(@Nonnull final String desc) {
		#if MC_12_LATER
		return desc;
		#else
		return FMLDeobfuscatingRemapper.INSTANCE.mapDesc(desc);
		#endif
	}

	public static @Nonnull String mapMethodDesc(@Nonnull final String desc) {
		#if MC_12_LATER
		return desc;
		#else
		return FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
		#endif
	}

	public static @Nonnull String mapFieldName(@Nonnull final String owner, @Nonnull final String name, @Nonnull final String desc) {
		#if MC_12_LATER
		return name;
		#else
		return FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(owner, name, desc);
		#endif
	}

	public static @Nonnull String mapMethodName(@Nonnull final String owner, @Nonnull final String name, @Nonnull final String desc) {
		#if MC_12_LATER
		return name;
		#else
		return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc);
		#endif
	}

	public static @Nonnull String map(@Nonnull final String typeName) {
		#if MC_12_LATER
		return typeName;
		#else
		return FMLDeobfuscatingRemapper.INSTANCE.map(typeName);
		#endif
	}

	public static @Nonnull String unmap(@Nonnull final String typeName) {
		#if MC_12_LATER
		return typeName;
		#else
		return FMLDeobfuscatingRemapper.INSTANCE.unmap(typeName);
		#endif
	}

	public static boolean useMcpNames() {
		#if MC_12_LATER
		return Launcher.INSTANCE.environment().findNameMapping("srg").isPresent();
		#else
		final Boolean deobfuscated = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
		return deobfuscated!=null&&deobfuscated;
		#endif
	}
}
