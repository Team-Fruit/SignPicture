package net.teamfruit.signpic.compat;

import javax.annotation.Nullable;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.teamfruit.signpic.compat.Compat.CompatBakedModel;
import net.teamfruit.signpic.compat.Compat.CompatModel;

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

		public CompatModel getModel() {
			return new CompatModel();
		}
	}
}
