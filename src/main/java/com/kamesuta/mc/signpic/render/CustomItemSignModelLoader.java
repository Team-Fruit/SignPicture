package com.kamesuta.mc.signpic.render;

import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.compat.CompatItemSignModelLoader;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class CustomItemSignModelLoader extends CompatItemSignModelLoader {
	public static CustomItemSignModelLoader INSTANCE = new CustomItemSignModelLoader();

	private @Nullable ICustomModelLoader loader;

	private CustomItemSignModelLoader() {
		try {
			this.loader = getInstance();
		} catch (final Exception e) {
			Log.log.warn("An error has occurred, ItemSignPicture won't work.", e);
		}
	}

	private <T extends Enum<T>> T getInstance() throws Exception {
		@SuppressWarnings("unchecked")
		final Class<T> $class = (Class<T>) Class.forName("net.minecraftforge.client.model.ModelLoader$VanillaLoader");
		return Enum.<T> valueOf($class, "INSTANCE");
	}

	@Override
	public void onResourceManagerReloadCompat(@Nullable final IResourceManager resourceManager) {
	}

	@Override
	public @Nullable IModel loadModelCompat(@Nullable final ResourceLocation modelLocation) throws Exception {
		if (this.loader!=null) {
			final IModel model = this.loader.loadModel(modelLocation);
			return new ItemSignModel(model);
		}
		return ModelLoaderRegistry.getMissingModel();
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
		public ItemSignModel(final IModel delegate) {
			super(delegate);
		}

		@Override
		public IBakedModel injectBakedModel(final IBakedModel bakedModel) {
			Client.itemRenderer.setBaseModel(bakedModel);
			return Client.itemRenderer;
		}
	}
}
