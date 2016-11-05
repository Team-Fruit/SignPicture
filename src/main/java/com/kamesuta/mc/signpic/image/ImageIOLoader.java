package com.kamesuta.mc.signpic.image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.image.meta.ImageSize.ImageSizes;
import com.kamesuta.mc.signpic.lib.GifDecoder;
import com.kamesuta.mc.signpic.lib.GifDecoder.GifImage;
import com.kamesuta.mc.signpic.state.Progress;
import com.kamesuta.mc.signpic.state.StateType;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class ImageIOLoader {
	public static final ImageSize MAX_SIZE = new ImageSize().setSize(
			Config.instance.imageWidthLimit>0 ? Config.instance.imageWidthLimit : ImageSize.unknownSize,
			Config.instance.imageHeightLimit>0 ? Config.instance.imageHeightLimit : ImageSize.unknownSize);

	protected Content content;
	protected InputFactory input;

	public ImageIOLoader(final Content content, final InputFactory inputFactory) throws IOException {
		this.content = content;
		this.input = inputFactory;
	}

	public ImageTextures load() throws IOException {
		final ImageInputStream imagestream = ImageIO.createImageInputStream(this.input.createInput());
		final Iterator<ImageReader> iter = ImageIO.getImageReaders(imagestream);
		if (!iter.hasNext())
			throw new InvaildImageException();
		final ImageReader reader = iter.next();

		this.content.state.setType(StateType.LOADING);
		this.content.state.setProgress(new Progress());
		ImageTextures textures;
		if (Config.instance.imageAnimationGif&&reader.getFormatName()=="gif")
			textures = loadGif();
		else
			textures = loadImage(reader, imagestream);
		this.content.state.setType(StateType.LOADED);
		this.input.close();
		return textures;
	}

	private ImageTextures loadGif() throws IOException {
		final GifImage gifImage = GifDecoder.read(this.input.createInput());
		final int width = gifImage.getWidth();
		final int height = gifImage.getHeight();
		final ImageSize newsize = new ImageSize().setSize(ImageSizes.LIMIT, width, height, MAX_SIZE);

		final ArrayList<ImageTexture> textures = new ArrayList<ImageTexture>();
		final int frameCount = gifImage.getFrameCount();
		this.content.state.getProgress().overall = frameCount;
		for (int i = 0; i<frameCount; i++) {
			final BufferedImage image = gifImage.getFrame(i);
			final int delay = gifImage.getDelay(i);
			final ImageTexture texture = new ImageTexture(createResizedImage(image, newsize), (float) delay/100);
			textures.add(texture);
			this.content.state.getProgress().done = i;
		}
		return new ImageTextures(textures);
	}

	private ImageTextures loadImage(final ImageReader reader, final ImageInputStream imagestream) throws IOException {
		final ImageReadParam param = reader.getDefaultReadParam();
		reader.setInput(imagestream, true, true);
		BufferedImage canvas;
		try {
			canvas = reader.read(0, param);
		} finally {
			reader.dispose();
			imagestream.close();
		}
		final ImageSize newsize = new ImageSize().setSize(ImageSizes.LIMIT, canvas.getWidth(), canvas.getHeight(), MAX_SIZE);
		return new ImageTextures(Lists.newArrayList(new ImageTexture(createResizedImage(canvas, newsize))));
	}

	private BufferedImage createResizedImage(final BufferedImage image, final ImageSize newsize) {
		final int wid = (int) newsize.width;
		final int hei = (int) newsize.height;
		final BufferedImage thumb = new BufferedImage(wid, hei, image.getType());
		final Graphics g = thumb.getGraphics();
		g.drawImage(image.getScaledInstance(wid, hei, java.awt.Image.SCALE_AREA_AVERAGING), 0, 0, wid, hei, null);
		g.dispose();
		return thumb;
	}

	public static interface InputFactory extends Closeable {
		InputStream createInput() throws IOException;

		@Override
		void close();

		public static abstract class AbstractInputFactory implements InputFactory {
			protected Set<Closeable> closables = Sets.newHashSet();

			@Override
			public void close() {
				for (final Closeable closable : closables)
					IOUtils.closeQuietly(closable);
			}
		}

		public static class FileInputFactory extends AbstractInputFactory {
			private File file;

			public FileInputFactory(final File file) {
				this.file = file;
			}

			@Override
			public InputStream createInput() throws FileNotFoundException {
				final InputStream stream = new FileInputStream(file);
				closables.add(stream);
				return stream;
			}
		}

		public static class ContentInputFactory extends FileInputFactory {
			public ContentInputFactory(final Content content) {
				super(content.location.cacheLocation());
			}
		}

		public static class ResourceInputFactory extends AbstractInputFactory {
			private IResourceManager manager;
			private ResourceLocation location;

			public ResourceInputFactory(final IResourceManager manager, final ResourceLocation location) {
				this.manager = manager;
				this.location = location;
			}

			@Override
			public InputStream createInput() throws IOException {
				final InputStream stream = manager.getResource(location).getInputStream();
				closables.add(stream);
				return stream;
			}
		}
	}
}
