package com.kamesuta.mc.signpic.image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.ILoadCancelable;
import com.kamesuta.mc.signpic.LoadCanceledException;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentLocation;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.image.meta.SizeData;
import com.kamesuta.mc.signpic.image.meta.SizeData.ImageSizes;
import com.kamesuta.mc.signpic.lib.GifDecoder;
import com.kamesuta.mc.signpic.lib.GifDecoder.GifImage;
import com.kamesuta.mc.signpic.state.Progress;
import com.kamesuta.mc.signpic.state.StateType;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class ImageIOLoader implements ILoadCancelable {
	public static final ImageSize MAX_SIZE = new ImageSize().setSize(
			Config.instance.imageWidthLimit.get()>0 ? Config.instance.imageWidthLimit.get() : ImageSize.unknownSize,
			Config.instance.imageHeightLimit.get()>0 ? Config.instance.imageHeightLimit.get() : ImageSize.unknownSize);

	protected Content content;
	protected InputFactory input;
	private boolean canceled;

	public ImageIOLoader(final Content content, final InputFactory inputFactory) throws IOException {
		this.content = content;
		this.input = inputFactory;
	}

	public RemoteImageTexture load() throws IOException {
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
			if (Config.instance.imageAnimationGif.get()&&reader.getFormatName()=="gif")
				textures = loadGif();
			else
				textures = loadImage(reader, imagestream);
			this.content.state.setType(StateType.LOADED);
			return textures;
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

	private RemoteImageTexture loadGif() throws IOException {
		InputStream stream = null;
		try {
			final GifImage gifImage = GifDecoder.read(stream = this.input.createInput());
			final int width = gifImage.getWidth();
			final int height = gifImage.getHeight();
			final SizeData newsize = ImageSizes.LIMIT.defineSize(width, height, MAX_SIZE.get());

			final ArrayList<DynamicImageTexture> textures = new ArrayList<DynamicImageTexture>();
			final int frameCount = gifImage.getFrameCount();
			this.content.state.getProgress().overall = frameCount;
			for (int i = 0; i<frameCount; i++) {
				if (this.canceled)
					throw new LoadCanceledException();
				final BufferedImage image = gifImage.getFrame(i);
				final int delay = gifImage.getDelay(i);
				final DynamicImageTexture texture = new DynamicImageTexture(createResizedImage(image, newsize), (float) delay/100);
				textures.add(texture);
				this.content.state.getProgress().done = i;
			}
			return new RemoteImageTexture(textures);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

	private RemoteImageTexture loadImage(final ImageReader reader, final ImageInputStream imagestream) throws IOException {
		final ImageReadParam param = reader.getDefaultReadParam();
		reader.setInput(imagestream, true, true);
		BufferedImage canvas;
		try {
			canvas = reader.read(0, param);
		} finally {
			reader.dispose();
			imagestream.close();
		}
		final SizeData newsize = ImageSizes.LIMIT.defineSize(canvas.getWidth(), canvas.getHeight(), MAX_SIZE.get());
		return new RemoteImageTexture(Lists.newArrayList(new DynamicImageTexture(createResizedImage(canvas, newsize))));
	}

	private BufferedImage createResizedImage(final BufferedImage image, final SizeData newsize) {
		final int wid = (int) newsize.width;
		final int hei = (int) newsize.height;
		final BufferedImage thumb = new BufferedImage(wid, hei, image.getType());
		final Graphics g = thumb.getGraphics();
		g.drawImage(image.getScaledInstance(wid, hei, java.awt.Image.SCALE_AREA_AVERAGING), 0, 0, wid, hei, null);
		g.dispose();
		return thumb;
	}

	public static interface InputFactory {
		InputStream createInput() throws IOException;

		public static class FileInputFactory implements InputFactory {
			private File file;

			public FileInputFactory(final File file) {
				this.file = file;
			}

			@Override
			public InputStream createInput() throws FileNotFoundException {
				return new FileInputStream(file);
			}
		}

		public static class ContentInputFactory extends FileInputFactory {
			public ContentInputFactory(final Content content) {
				super(ContentLocation.cacheLocation(content.meta.getCacheID()));
			}
		}

		public static class ResourceInputFactory implements InputFactory {
			private IResourceManager manager;
			private ResourceLocation location;

			public ResourceInputFactory(final IResourceManager manager, final ResourceLocation location) {
				this.manager = manager;
				this.location = location;
			}

			@Override
			public InputStream createInput() throws IOException {
				return manager.getResource(location).getInputStream();
			}
		}
	}

	@Override
	public void cancel() {
		this.canceled = true;
	}
}
