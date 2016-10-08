package com.kamesuta.mc.signpic.image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
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
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentLocation;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.image.meta.ImageSize.ImageSizes;
import com.kamesuta.mc.signpic.lib.GifDecoder;
import com.kamesuta.mc.signpic.lib.GifDecoder.GifImage;
import com.kamesuta.mc.signpic.state.ContentStateType;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class ImageIOLoader {
	public static final ImageSize MAX_SIZE = new ImageSize().setSize(
			((Config.instance.imageWidthLimit > 0) ? Config.instance.imageWidthLimit : ImageSize.unknownSize),
			((Config.instance.imageHeightLimit > 0) ? Config.instance.imageHeightLimit : ImageSize.unknownSize));

	protected Content content;
	protected InputStream input;

	public ImageIOLoader(final Content content, final InputStream in) throws IOException {
		this.content = content;
		this.input = in;
	}

	public ImageIOLoader(final Content content, final File file) throws IOException {
		this(content, new FileInputStream(file));
	}

	public ImageIOLoader(final Content content, final IResourceManager manager, final ResourceLocation location) throws IOException {
		this(content, manager.getResource(location).getInputStream());
	}

	public ImageIOLoader(final Content content, final ContentLocation location) throws IOException {
		this(content, location.localLocation(content.id));
	}

	public ImageTextures load() throws IOException {
		final byte[] data = IOUtils.toByteArray(this.input);

		final ImageInputStream imagestream = ImageIO.createImageInputStream(new ByteArrayInputStream(data));
		final Iterator<ImageReader> iter = ImageIO.getImageReaders(imagestream);
		if (!iter.hasNext()) throw new InvaildImageException();
		final ImageReader reader = iter.next();

		this.content.state.setType(ContentStateType.LOADING);
		ImageTextures textures;
		if (Config.instance.imageAnimationGif && reader.getFormatName()=="gif") {
			textures = loadGif(data);
		} else {
			textures = loadImage(reader, imagestream);
		}
		this.content.state.setType(ContentStateType.LOADED);
		return textures;
	}

	private ImageTextures loadGif(final byte[] data) throws IOException {
		final GifImage gifImage = GifDecoder.read(data);
		final int width = gifImage.getWidth();
		final int height = gifImage.getHeight();
		final ImageSize newsize = new ImageSize().setSize(ImageSizes.LIMIT, width, height, MAX_SIZE);

		final ArrayList<ImageTexture> textures = new ArrayList<ImageTexture>();
		final int frameCount = gifImage.getFrameCount();
		this.content.state.getProgress().overall = frameCount;
		for (int i = 0; i < frameCount; i++) {
			final BufferedImage image = gifImage.getFrame(i);
			final int delay = gifImage.getDelay(i);
			final ImageTexture texture = new ImageTexture(createResizedImage(image, newsize), (float)delay / 100);
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
}
