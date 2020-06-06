package net.teamfruit.signpic.asm;

#if !MC_12_LATER

import javax.annotation.Nullable;
import java.util.Map;

public class SignPictureCorePlugin implements #if MC_7_LATER net.minecraftforge.fml.relauncher.IFMLLoadingPlugin #else cpw.mods.fml.relauncher.IFMLLoadingPlugin #endif {
	@Override
	public @Nullable String[] getASMTransformerClass() {
		return new String[] {
				SignPictureTransformer.class.getName()
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