package com.kamesuta.mc.signpic.render;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.kamesuta.mc.signpic.Log;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.animation.IClip;

public class CustomItemSignModel implements ICustomModelLoader {
	public static CustomItemSignModel INSTANCE = new CustomItemSignModel();

	private @Nullable ICustomModelLoader loader;

	private CustomItemSignModel() {
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
	public void onResourceManagerReload(@Nullable final IResourceManager resourceManager) {
	}

	@Override
	public @Nullable IModel loadModel(@Nullable final ResourceLocation modelLocation) throws Exception {
		if (this.loader!=null) {
			final IModel model = this.loader.loadModel(modelLocation);
			return new ItemSignModel(model);
		}
		return null;
	}

	@Override
	public boolean accepts(@Nullable final ResourceLocation modelLocation) {
		if (modelLocation!=null&&modelLocation.getResourceDomain().equals("minecraft")&&modelLocation.getResourcePath().equals("models/item/sign"))
			if (this.loader!=null)
				return this.loader.accepts(modelLocation);
		return false;
	}

	@Override
	public String toString() {
		return "CustomItemSignModel.INSTANCE";
	}

	public static class ItemSignModel implements IModel {
		private final IModel delegate;

		public ItemSignModel(final IModel delegate) {
			this.delegate = delegate;
		}

		@Override
		public @Nullable IBakedModel bake(@Nullable final IModelState state, @Nullable final VertexFormat format, @Nullable final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
			final IBakedModel bakedModel = this.delegate.bake(state, format, bakedTextureGetter);
			if (bakedModel!=null) {
				final CustomItemSignRenderer atlas6 = new CustomItemSignRenderer(bakedModel);
				return atlas6;
			} else
				Log.log.error("Failed to register ItemSignPicture");
			return null;
		}

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
