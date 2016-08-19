package com.kamesuta.mc.signpic.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kamesuta.mc.signpic.image.exception.InvaildImageException;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class ImageLoader implements Runnable {
	public static final int MAX_WIDTH = 512;
	public static final int MAX_HEIGHT = 512;

	protected Image image;
	protected ImageInputStream stream;

	public ImageLoader(final Image image, final ImageInputStream stream) throws IOException {
		this.image = image;
		this.stream = stream;
	}

	public ImageLoader(final Image image, final File file) throws IOException {
		this(image, ImageIO.createImageInputStream(file));
	}

	public ImageLoader(final Image image, final InputStream in) throws IOException {
		this(image, ImageIO.createImageInputStream(in));
	}

	public ImageLoader(final Image image, final IResourceManager manager, final ResourceLocation location) throws IOException {
		this(image, manager.getResource(location).getInputStream());
	}

	@Override
	public void run() {
		this.image.state = ImageState.IOLOADING;
		try {
			if (this.stream == null) throw new InvaildImageException();
			final Iterator<ImageReader> iter = ImageIO.getImageReaders(this.stream);
			if (!iter.hasNext()) throw new InvaildImageException();

			final ImageReader reader = iter.next();
			reader.setInput(this.stream, false);
			try {
				if (reader.getFormatName()=="gif") {
					loadGif(reader);
				} else {
					loadImage(reader);
				}
				this.image.state = ImageState.POSTLOADING;
			} finally {
				reader.dispose();
				this.stream.close();
			}
		} catch (final IOException e) {
			this.image.state = ImageState.FAILED;
			this.image.advmsg = I18n.format("signpic.advmsg.io", e);
		}
	}

	protected void loadGif(final ImageReader reader) throws IOException {
		final String[] imageatt = new String[]{
				"imageLeftPosition",
				"imageTopPosition",
				"imageWidth",
				"imageHeight",
		};

		final int noi = reader.getNumImages(true);
		BufferedImage bi = null;

		for (int i = 0; i < noi; i++) {
			final BufferedImage image = reader.read(i);
			final IIOMetadata metadata = reader.getImageMetadata(i);

			final Node tree = metadata.getAsTree("javax_imageio_gif_image_1.0");
			final NodeList children = tree.getChildNodes();

			final int glTextureId = TextureUtil.glGenTextures();
			for (int j = 0; j < children.getLength(); j++) {
				final Node nodeItem = children.item(j);

				if(nodeItem.getNodeName().equals("ImageDescriptor")){
					final Map<String, Integer> imageAttr = new HashMap<String, Integer>();

					for (int k = 0; k < imageatt.length; k++) {
						final NamedNodeMap attr = nodeItem.getAttributes();
						final Node attnode = attr.getNamedItem(imageatt[k]);
						imageAttr.put(imageatt[k], Integer.valueOf(attnode.getNodeValue()));
					}
					if(i==0){
						bi = new BufferedImage(imageAttr.get("imageWidth"), imageAttr.get("imageHeight"), BufferedImage.TYPE_INT_ARGB);
					}
					bi.getGraphics().drawImage(image, imageAttr.get("imageLeftPosition"), imageAttr.get("imageTopPosition"), null);
				} else if (nodeItem.getNodeName().equals("GraphicControlExtension")) {
					final NamedNodeMap attr = nodeItem.getAttributes();
					final Node nodedelay = attr.getNamedItem("delayTime");
					this.glTextureDelays.put(glTextureId, ((float)Integer.valueOf(nodedelay.getNodeValue())/100f));
				}
			}
			TextureUtil.allocateTexture(glTextureId, bi.getWidth(), bi.getHeight());
			TextureUtil.uploadTextureImage(glTextureId, bi);
			this.glTextureIds.add(glTextureId);
		}
	}

	protected void loadImage(final ImageReader reader) throws IOException {
		final ImageReadParam param = reader.getDefaultReadParam();
		BufferedImage bi;
		bi = reader.read(0, param);

		final int glTextureId = TextureUtil.glGenTextures();
		TextureUtil.allocateTexture(glTextureId, bi.getWidth(), bi.getHeight());
		TextureUtil.uploadTextureImage(glTextureId, bi);
		this.glTextureIds.add(glTextureId);
	}

}
