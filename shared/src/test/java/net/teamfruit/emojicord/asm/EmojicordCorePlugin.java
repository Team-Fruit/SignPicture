package net.teamfruit.emojicord.asm;

#if !MC_12_LATER
import java.util.Map;

import javax.annotation.Nullable;

import net.teamfruit.emojicord.Reference;

public class EmojicordCorePlugin implements #if MC_7_LATER net.minecraftforge.fml.relauncher.IFMLLoadingPlugin #else cpw.mods.fml.relauncher.IFMLLoadingPlugin #endif {
	@Override
	public @Nullable String[] getASMTransformerClass() {
		return new String[] {
				EmojicordTransformer.class.getName()
		};
	}

	@Override
	public @Nullable String getModContainerClass() {
		return null;
	}

	@Override
	public @Nullable String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(final @Nullable Map<String, Object> data) {
	}

	@Override
	public @Nullable String getAccessTransformerClass() {
		return null;
	}
}
#endif