package com.kamesuta.mc.signpic.render;

import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.compat.Compat.CompatBakedModel;
import com.kamesuta.mc.signpic.compat.Compat.CompatModel;
import com.kamesuta.mc.signpic.compat.CompatItemSignModelLoader;
import com.kamesuta.mc.signpic.compat.CompatItemSignRenderer.CompatModelLoaderRegistry;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class CustomItemSignModelLoader extends CompatItemSignModelLoader {
	public static CustomItemSignModelLoader INSTANCE = new CustomItemSignModelLoader();

	private final CompatModelLoader loader = CompatModelLoader.create();

	@Override
	public void onResourceManagerReloadCompat(@Nullable final IResourceManager resourceManager) {
	}

	@Override
	public @Nullable CompatModel loadModelCompat(@Nullable final ResourceLocation modelLocation) throws Exception {
		if (this.loader!=null) {
			final CompatModel model = this.loader.loadModel(modelLocation);
			return new ItemSignModel(model).getModel();
		}
		return CompatModelLoaderRegistry.getMissingModel();
	}

	@Override
	public boolean acceptsCompat(@Nullable final ResourceLocation modelLocation) {
		if (modelLocation!=null&&modelLocation.getResourceDomain().equals("minecraft")&&modelLocation.getResourcePath().equals("models/item/sign"))
			if (this.loader!=null)
				return this.loader.accepts(modelLocation);
		return false;
	}

	@Override
	public String toString() {
		return "CustomItemSignModel.INSTANCE";
	}

	public static class ItemSignModel extends CompatItemSignModel {
		public ItemSignModel(final CompatModel delegate) {
			super(delegate);
		}

		@Override
		public CompatBakedModel injectBakedModel(final CompatBakedModel bakedModel) {
			return Client.itemRenderer.injectBakedModel(bakedModel);
		}
	}
}
