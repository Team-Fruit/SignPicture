package com.kamesuta.mc.signpic.compat;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public abstract class CompatItemSignModelLoader implements ICustomModelLoader {
	public abstract void onResourceManagerReloadCompat(@Nullable final IResourceManager resourceManager);

	public abstract @Nullable IModel loadModelCompat(@Nullable final ResourceLocation modelLocation) throws Exception;

	public abstract boolean acceptsCompat(@Nullable final ResourceLocation modelLocation);

	public static abstract class CompatItemSignModel {
		public abstract IBakedModel injectBakedModel(IBakedModel bakedModel);
	}
}
