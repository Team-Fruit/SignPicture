package com.kamesuta.mc.signpic.asm;

import java.util.Map;

import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.UniversalVersioner;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class SignPictureCorePlugin implements IFMLLoadingPlugin {
	public static class UniversalVersionerInjector {
		static {
			try {
				Class.forName("com.kamesuta.mc.signpic.compat.Compat");
			} catch (final ClassNotFoundException e) {
				UniversalVersioner.loadVersionFromCoreMod(SignPictureCorePlugin.class);
			}
		}

		public static void inject() {
		}
	}

	@Override
	public @Nullable String[] getASMTransformerClass() {
		UniversalVersionerInjector.inject();
		return new String[] {
				Reference.TRANSFORMER
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