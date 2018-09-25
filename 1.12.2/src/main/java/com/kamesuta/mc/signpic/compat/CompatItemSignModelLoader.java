package com.kamesuta.mc.signpic.compat;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.animation.IClip;

public abstract class CompatItemSignModelLoader implements ICustomModelLoader {
	@Override
	public void onResourceManagerReload(@Nullable final IResourceManager resourceManager) {
		onResourceManagerReloadCompat(resourceManager);
	}

	public abstract void onResourceManagerReloadCompat(@Nullable final IResourceManager resourceManager);

	@Override
	public @Nullable IModel loadModel(@Nullable final ResourceLocation modelLocation) throws Exception {
		return loadModelCompat(modelLocation);
	}

	public abstract @Nullable IModel loadModelCompat(@Nullable final ResourceLocation modelLocation) throws Exception;

	@Override
	public boolean accepts(@Nullable final ResourceLocation modelLocation) {
		return acceptsCompat(modelLocation);
	}

	public abstract boolean acceptsCompat(@Nullable final ResourceLocation modelLocation);

	public static abstract class CompatItemSignModel implements IModel {
		private final IModel delegate;

		public CompatItemSignModel(final IModel delegate) {
			this.delegate = delegate;
		}

		@Override
		public @Nullable IBakedModel bake(@Nullable final IModelState state, @Nullable final VertexFormat format, @Nullable final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
			final IBakedModel bakedModel = this.delegate.bake(state, format, bakedTextureGetter);
			if (bakedModel!=null)
				return injectBakedModel(bakedModel);
			return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
		}

		public abstract IBakedModel injectBakedModel(IBakedModel bakedModel);

		@Override
		public Collection<ResourceLocation> getDependencies() {
			return this.delegate.getDependencies();
		}

		@Override
		public Collection<ResourceLocation> getTextures() {
			return this.delegate.getTextures();
		}

		@Override
		public IModelState getDefaultState() {
			return this.delegate.getDefaultState();
		}

		@Override
		public Optional<? extends IClip> getClip(@Nullable final String name) {
			return this.delegate.getClip(name);
		}

		@Override
		public IModel process(@Nullable final ImmutableMap<String, String> customData) {
			return this.delegate.process(customData);
		}

		@Override
		public IModel smoothLighting(final boolean value) {
			return this.delegate.smoothLighting(value);
		}

		@Override
		public IModel gui3d(final boolean value) {
			return this.delegate.gui3d(value);
		}

		@Override
		public IModel uvlock(final boolean value) {
			return this.delegate.uvlock(value);
		}

		@Override
		public IModel retexture(@Nullable final ImmutableMap<String, String> textures) {
			return this.delegate.retexture(textures);
		}
	}
}
