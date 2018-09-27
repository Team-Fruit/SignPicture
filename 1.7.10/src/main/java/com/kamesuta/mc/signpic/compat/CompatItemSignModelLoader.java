package com.kamesuta.mc.signpic.compat;

import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.compat.Compat.CompatBakedModel;
import com.kamesuta.mc.signpic.compat.Compat.CompatModel;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public abstract class CompatItemSignModelLoader {
	public abstract void onResourceManagerReloadCompat(@Nullable final IResourceManager resourceManager);

	public abstract @Nullable CompatModel loadModelCompat(@Nullable final ResourceLocation modelLocation) throws Exception;

	public abstract boolean acceptsCompat(@Nullable final ResourceLocation modelLocation);

	public static class CompatModelLoader {
		public static @Nullable CompatModelLoader create() {
			return null;
		}

		public CompatModel loadModel(final ResourceLocation location) throws Exception {
			return new CompatModel();
		}

		public boolean accepts(final ResourceLocation location) {
			return false;
		}
	}

	public static abstract class CompatItemSignModel {
		public CompatItemSignModel(final CompatModel model) {
		}

		public abstract @Nullable CompatBakedModel injectBakedModel(CompatBakedModel bakedModel);
	}
}
