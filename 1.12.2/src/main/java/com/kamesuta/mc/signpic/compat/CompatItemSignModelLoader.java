package com.kamesuta.mc.signpic.compat;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.compat.Compat.CompatBakedModel;
import com.kamesuta.mc.signpic.compat.Compat.CompatModel;
import com.kamesuta.mc.signpic.compat.CompatItemSignRenderer.CompatModelLoaderRegistry;

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
		final CompatModel compatModel = loadModelCompat(modelLocation);
		return compatModel!=null ? compatModel.model : null;
	}

	public abstract @Nullable CompatModel loadModelCompat(@Nullable final ResourceLocation modelLocation) throws Exception;

	@Override
	public boolean accepts(@Nullable final ResourceLocation modelLocation) {
		return acceptsCompat(modelLocation);
	}

	public abstract boolean acceptsCompat(@Nullable final ResourceLocation modelLocation);

	public static class CompatModelLoader {
		public final @Nonnull ICustomModelLoader loader;

		private CompatModelLoader() throws Exception {
			this.loader = getInstance();
		}

		public static @Nullable CompatModelLoader create() {
			try {
				return new CompatModelLoader();
			} catch (final Exception e) {
				Log.log.warn("An error has occurred, ItemSignPicture won't work.", e);
			}
			return null;
		}

		private <T extends Enum<T>> T getInstance() throws Exception {
			@SuppressWarnings("unchecked")
			final Class<T> $class = (Class<T>) Class.forName("net.minecraftforge.client.model.ModelLoader$VanillaLoader");
			return Enum.<T> valueOf($class, "INSTANCE");
		}

		public CompatModel loadModel(final ResourceLocation location) throws Exception {
			return new CompatModel(this.loader.loadModel(location));
		}

		public boolean accepts(final ResourceLocation location) {
			return this.loader.accepts(location);
		}
	}

	public static abstract class CompatItemSignModel implements IModel {
		private final CompatModel delegate;

		public CompatItemSignModel(final CompatModel delegate) {
			this.delegate = delegate;
		}

		@Override
		public @Nullable IBakedModel bake(@Nullable final IModelState state, @Nullable final VertexFormat format, @Nullable final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
			final IBakedModel bakedModel = this.delegate.model.bake(state, format, bakedTextureGetter);
			if (bakedModel!=null) {
				final CompatBakedModel compatBakedModel = injectBakedModel(new CompatBakedModel(bakedModel));
				return compatBakedModel!=null ? compatBakedModel.bakedModel : null;
			}
			return CompatModelLoaderRegistry.getMissingModel().model.bake(state, format, bakedTextureGetter);
		}

		public abstract @Nullable CompatBakedModel injectBakedModel(CompatBakedModel bakedModel);

		@Override
		public Collection<ResourceLocation> getDependencies() {
			return this.delegate.model.getDependencies();
		}

		@Override
		public Collection<ResourceLocation> getTextures() {
			return this.delegate.model.getTextures();
		}

		@Override
		public IModelState getDefaultState() {
			return this.delegate.model.getDefaultState();
		}

		@Override
		public Optional<? extends IClip> getClip(@Nullable final String name) {
			return this.delegate.model.getClip(name);
		}

		@Override
		public IModel process(@Nullable final ImmutableMap<String, String> customData) {
			return this.delegate.model.process(customData);
		}

		@Override
		public IModel smoothLighting(final boolean value) {
			return this.delegate.model.smoothLighting(value);
		}

		@Override
		public IModel gui3d(final boolean value) {
			return this.delegate.model.gui3d(value);
		}

		@Override
		public IModel uvlock(final boolean value) {
			return this.delegate.model.uvlock(value);
		}

		@Override
		public IModel retexture(@Nullable final ImmutableMap<String, String> textures) {
			return this.delegate.model.retexture(textures);
		}

		public CompatModel getModel() {
			return new CompatModel(this);
		}
	}

	public static CompatModel getMissingModel() {
		return new CompatModel(ModelLoaderRegistry.getMissingModel());
	}
}
