package com.kamesuta.mc.signpic.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.ILoadCancelable;
import com.kamesuta.mc.signpic.LoadCanceledException;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.attr.prop.SizeData.ImageSizes;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentLocation;
import com.kamesuta.mc.signpic.lib.GifDecoder;
import com.kamesuta.mc.signpic.lib.GifDecoder.GifImage;
import com.kamesuta.mc.signpic.state.Progress;
import com.kamesuta.mc.signpic.state.StateType;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class ImageIOLoader implements ILoadCancelable {
	public static @Nonnull SizeData MAX_SIZE = maxSize(Config.getConfig().imageWidthLimit.get(), Config.getConfig().imageHeightLimit.get());

	public static @Nonnull SizeData maxSize(final int limitWidth, final int limitHeight) {
		return SizeData.create(
				limitWidth>0 ? limitWidth : SizeData.Unknown,
				limitHeight>0 ? limitHeight : SizeData.Unknown);
	}

	public static float DefaultDelay = .05f;

	protected @Nonnull Content content;
	protected @Nonnull InputFactory input;
	private boolean canceled;

	public ImageIOLoader(final @Nonnull Content content, final @Nonnull InputFactory inputFactory) throws IOException {
		this.content = content;
		this.input = inputFactory;
	}

	public @Nonnull RemoteImageTexture load() throws IOException {
		InputStream stream = null;
		try {
			final ImageInputStream imagestream = ImageIO.createImageInputStream(stream = this.input.createInput());
			final Iterator<ImageReader> iter = ImageIO.getImageReaders(imagestream);
			if (!iter.hasNext())
				throw new InvaildImageException();
			final ImageReader reader = iter.next();

			this.content.state.setType(StateType.LOADING);
			this.content.state.setProgress(new Progress());
			RemoteImageTexture textures;
			if (Config.getConfig().imageAnimationGif.get()&&reader.getFormatName()=="gif")
				textures = loadGif();
			else
				textures = loadImage(reader, imagestream);
			this.content.state.setType(StateType.LOADED);
			return textures;
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

	private @Nonnull RemoteImageTexture loadGif() throws IOException {
		InputStream stream = null;
		try {
			final GifImage gifImage = GifDecoder.read(stream = this.input.createInput());
			final int width = gifImage.getWidth();
			final int height = gifImage.getHeight();
			final SizeData newsize = ImageSizes.LIMIT.defineSize(width, height, MAX_SIZE);

			final List<Pair<Float, DynamicImageTexture>> textures = Lists.newArrayList();
			final int frameCount = gifImage.getFrameCount();
			this.content.state.getProgress().overall = frameCount;
			for (int i = 0; i<frameCount; i++) {
				if (this.canceled)
					throw new LoadCanceledException();
				final BufferedImage image = gifImage.getFrame(i);
				final int delay = gifImage.getDelay(i);
				textures.add(Pair.of(delay/100f, DynamicImageTexture.create(image, (int) newsize.getWidth(), (int) newsize.getHeight())));
				this.content.state.getProgress().done = i;
			}
			return new RemoteImageTexture(textures);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

	private @Nonnull RemoteImageTexture loadImage(final @Nonnull ImageReader reader, final @Nonnull ImageInputStream imagestream) throws IOException {
		final ImageReadParam param = reader.getDefaultReadParam();
		reader.setInput(imagestream, true, true);
		BufferedImage canvas;
		try {
			canvas = reader.read(0, param);
		} finally {
			reader.dispose();
			imagestream.close();
		}
		final SizeData newsize = ImageSizes.LIMIT.defineSize(canvas.getWidth(), canvas.getHeight(), MAX_SIZE);
		final List<Pair<Float, DynamicImageTexture>> textures = Lists.newArrayList();
		textures.add(Pair.of(0f, DynamicImageTexture.create(canvas, (int) newsize.getWidth(), (int) newsize.getHeight())));
		return new RemoteImageTexture(textures);
	}

	public static interface InputFactory {
		@Nonnull
		InputStream createInput() throws IOException;

		public static class FileInputFactory implements InputFactory {
			private final @Nonnull File file;

			public FileInputFactory(final @Nonnull File file) {
				this.file = file;
			}

			@Override
			public @Nonnull InputStream createInput() throws FileNotFoundException {
				return new FileInputStream(file);
			}

			public static @Nullable FileInputFactory createFromContent(final @Nonnull Content content) {
				final String id = content.meta.getCacheID();
				if (id!=null)
					return new FileInputFactory(ContentLocation.cacheLocation(id));
				return null;
			}
		}

		public static class ResourceInputFactory implements InputFactory {
			private @Nonnull IResourceManager manager;
			private @Nonnull ResourceLocation location;

			public ResourceInputFactory(final @Nonnull IResourceManager manager, final @Nonnull ResourceLocation location) {
				this.manager = manager;
				this.location = location;
			}

			@Override
			public @Nonnull InputStream createInput() throws IOException {
				return manager.getResource(location).getInputStream();
			}
		}
	}

	@Override
	public void cancel() {
		this.canceled = true;
	}
}
