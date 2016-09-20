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
import com.kamesuta.mc.signpic.entry.content.ContentStateType;
import com.kamesuta.mc.signpic.image.exception.InvaildImageException;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.image.meta.ImageSize.ImageSizes;
import com.kamesuta.mc.signpic.lib.GifDecoder;
import com.kamesuta.mc.signpic.lib.GifDecoder.GifImage;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class ImageIOLoader {
	public static final ImageSize MAX_SIZE = new ImageSize().setSize(512, 512);

	protected RemoteImage image;
	protected InputStream input;

	public ImageIOLoader(final RemoteImage image, final InputStream in) throws IOException {
		this.image = image;
		this.input = in;
	}

	public ImageIOLoader(final RemoteImage image, final File file) throws IOException {
		this(image, new FileInputStream(file));
	}

	public ImageIOLoader(final RemoteImage image, final IResourceManager manager, final ResourceLocation location) throws IOException {
		this(image, manager.getResource(location).getInputStream());
	}

	public void load() throws IOException {
		final byte[] data = IOUtils.toByteArray(this.input);

		final ImageInputStream imagestream = ImageIO.createImageInputStream(new ByteArrayInputStream(data));
		final Iterator<ImageReader> iter = ImageIO.getImageReaders(imagestream);
		if (!iter.hasNext()) throw new InvaildImageException();
		final ImageReader reader = iter.next();

		if (reader.getFormatName()=="gif") {
			loadGif(data);
		} else {
			loadImage(reader, imagestream);
		}
		this.image.isTextureLoaded = true;
	}

	protected static final String[] imageatt = new String[] {
			"imageLeftPosition",
			"imageTopPosition",
			"imageWidth",
			"imageHeight",
	};

	protected void loadGif(final byte[] data) throws IOException {
		final GifImage gifImage = GifDecoder.read(data);
		final int width = gifImage.getWidth();
		final int height = gifImage.getHeight();
		final ImageSize newsize = new ImageSize().setSize(ImageSizes.LIMIT, width, height, MAX_SIZE);

		final ArrayList<ImageTexture> textures = new ArrayList<ImageTexture>();
		final int frameCount = gifImage.getFrameCount();
		this.image.state.setType(ContentStateType.LOADING);
		this.image.state.progress.overall = frameCount;
		for (int i = 0; i < frameCount; i++) {
			final BufferedImage image = gifImage.getFrame(i);
			final int delay = gifImage.getDelay(i);
			final ImageTexture texture = new ImageTexture(createResizedImage(image, newsize), (float)delay / 100);
			textures.add(texture);
			this.image.state.progress.done = i;
		}
		this.image.texture = new ImageTextures(textures);
		this.image.state.setType(ContentStateType.LOADED);
	}

	protected void loadImage(final ImageReader reader, final ImageInputStream imagestream) throws IOException {
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
		this.image.texture = new ImageTextures(Lists.newArrayList(new ImageTexture(createResizedImage(canvas, newsize))));
	}

	protected BufferedImage createResizedImage(final BufferedImage image, final ImageSize newsize) {
		final int wid = (int) newsize.width;
		final int hei = (int) newsize.height;
		final BufferedImage thumb = new BufferedImage(wid, hei, image.getType());
		final Graphics g = thumb.getGraphics();
		g.drawImage(image.getScaledInstance(wid, hei, java.awt.Image.SCALE_AREA_AVERAGING), 0, 0, wid, hei, null);
		g.dispose();
		return thumb;
	}
}
