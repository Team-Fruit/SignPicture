package com.kamesuta.mc.signpic.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import org.lwjgl.util.Timer;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kamesuta.mc.signpic.image.exception.InvaildImageException;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class ImageTexture {

	private Timer texTime;
	protected ArrayList<Integer> glTextureIds = new ArrayList<Integer>();
	protected int currentTextureId = 0;

	public ImageTexture(final ImageInputStream stream) throws IOException {
		if (stream == null) throw new InvaildImageException();
		final Iterator<ImageReader> iter = ImageIO.getImageReaders(stream);
		if (!iter.hasNext()) throw new InvaildImageException();

		final ImageReader reader = iter.next();
		final ImageReadParam param = reader.getDefaultReadParam();
		reader.setInput(stream, true, true);
		if (reader.getFormatName()=="gif") {
			this.texTime = new Timer();
			final String[] imageatt = new String[]{
					"imageLeftPosition",
					"imageTopPosition",
					"imageWidth",
					"imageHeight"
			};

			final int noi = reader.getNumImages(true);
			BufferedImage bi = null;

			for (int i = 0; i < noi; i++) {
				final BufferedImage image = reader.read(i);
				final IIOMetadata metadata = reader.getImageMetadata(i);

				final Node tree = metadata.getAsTree("javax_imageio_gif_image_1.0");
				final NodeList children = tree.getChildNodes();

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
					}
				}
				final int glTextureId = TextureUtil.glGenTextures();
				TextureUtil.allocateTexture(glTextureId, bi.getWidth(), bi.getHeight());
				TextureUtil.uploadTextureImage(glTextureId, bi);
				this.glTextureIds.add(glTextureId);
			}
		} else {
			BufferedImage bi;
			try {
				bi = reader.read(0, param);
			} finally {
				reader.dispose();
				stream.close();
			}
			final int glTextureId = TextureUtil.glGenTextures();
			TextureUtil.allocateTexture(glTextureId, bi.getWidth(), bi.getHeight());
			TextureUtil.uploadTextureImage(glTextureId, bi);
			this.glTextureIds.add(glTextureId);
		}
	}



	@Deprecated
	public ImageTexture(final int width, final int height, final int[] data) {
		TextureUtil.allocateTexture(getGlTextureId(), width, height);
		TextureUtil.uploadTexture(getGlTextureId(), data, width, height);
	}

	public ImageTexture(final File file) throws IOException {
		this(ImageIO.createImageInputStream(file));
	}

	public ImageTexture(final InputStream in) throws IOException {
		this(ImageIO.createImageInputStream(in));
	}

	public ImageTexture(final IResourceManager manager, final ResourceLocation location) throws IOException {
		this(manager.getResource(location).getInputStream());
	}

	public int getGlTextureId() {
		if (this.glTextureIds.size()==1) {
			return this.glTextureIds.get(0);
		} else if (this.glTextureIds.size()>1) {
			if(this.texTime.getTime() > 0.7f){
				this.texTime.set(0);
				if(this.currentTextureId  < this.glTextureIds.size()){
					this.currentTextureId++;
				} else {
					this.currentTextureId = 0;
				}
			}
			return this.glTextureIds.get(this.currentTextureId);
		} else {
			return 0;
		}
	}

	public void deleteGlTexture() {
		final Iterator<Integer> itr = this.glTextureIds.iterator();
		while(itr.hasNext()){
			final Integer i = itr.next();
			itr.remove();
			TextureUtil.deleteTexture(i);
		}
	}
}