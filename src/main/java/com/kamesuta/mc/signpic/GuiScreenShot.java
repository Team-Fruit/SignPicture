package com.kamesuta.mc.signpic;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.annotation.Nullable;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.util.FileUtilitiy;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.shader.Framebuffer;

public class GuiScreenShot extends WFrame {

	public GuiScreenShot(final @Nullable GuiScreen parent) {
		super(parent);
	}

	public GuiScreenShot() {
	}

	{
		setGuiPauseGame(false);
		try {
			Mouse.setNativeCursor(null);
		} catch (final LWJGLException e) {
			Log.dev.warn("failed to change cursor", e);
		}
	}

	@Override
	protected void initWidget() {
		add(new WPanel(new R()) {
			private @Nullable Point point;

			@Override
			public boolean mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				this.point = p;
				return super.mouseClicked(ev, pgp, p, button);
			}

			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
				final Point point = this.point;
				if (point!=null) {
					final Area rect = new Area(p.x(), p.y(), point.x(), point.y());
					WRenderer.startShape();
					OpenGL.glLineWidth(.25f);
					OpenGL.glColor4f(1f, 1f, 1f, 1f);
					draw(rect, GL11.GL_LINE_LOOP);
				}
				super.draw(ev, pgp, p, frame, popacity);
			}

			@Override
			public boolean mouseReleased(final WEvent ev, final Area pgp, final Point p, final int button) {
				final Point point = this.point;
				if (point!=null) {
					final Area rect = new Area(p.x()*scaleX(), p.y()*scaleY(), point.x()*scaleX(), point.y()*scaleY());
					//final BufferedImage image = takeScreenshot(mc.displayWidth, mc.displayHeight, mc.getFramebuffer());
					//final BufferedImage image = takeScreenshotRect(0, 0, mc.displayWidth, mc.displayHeight, mc.getFramebuffer());
					final BufferedImage image = takeScreenshotRect((int) rect.minX(), (int) rect.minY(), (int) rect.w(), (int) rect.h(), mc.getFramebuffer());
					if (image!=null)
						try {
							FileUtilitiy.uploadImage(image);
						} catch (final IOException e) {
							Log.notice(I18n.format("signpic.gui.notice.capture.error", e));
						}
					this.point = null;
				}
				return super.mouseReleased(ev, pgp, p, button);
			}
		});
	}

	private static @Nullable IntBuffer pixelBuffer;
	private static @Nullable int[] pixelValues;

	public static @Nullable BufferedImage takeScreenshotRect(final int x, final int y, final int w, final int h, final Framebuffer framebuffer) {
		try {
			final boolean fboEnabled = OpenGlHelper.isFramebufferEnabled();
			//final boolean fboEnabled = false;
			int displayWidth;
			int displayHeight;
			if (fboEnabled) {
				displayWidth = framebuffer.framebufferTextureWidth;
				displayHeight = framebuffer.framebufferTextureHeight;
			} else {
				displayWidth = Client.mc.displayWidth;
				displayHeight = Client.mc.displayHeight;
			}

			final int k = displayWidth*displayHeight;

			IntBuffer buffer = pixelBuffer;
			int[] pixel = pixelValues;
			if (buffer==null||pixel==null||buffer.capacity()<k||pixel.length<k) {
				pixelBuffer = buffer = BufferUtils.createIntBuffer(k);
				pixelValues = pixel = new int[k];
			}

			GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			buffer.clear();

			if (fboEnabled) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.framebufferTexture);
				GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
			} else
				GL11.glReadPixels(x, displayHeight-(h+y+1), w, h, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);

			buffer.get(pixel);
			if (fboEnabled)
				TextureUtil.func_147953_a(pixel, displayWidth, displayHeight);
			else
				TextureUtil.func_147953_a(pixel, w, h);
			BufferedImage bufferedimage = null;

			if (fboEnabled) {
				bufferedimage = new BufferedImage(w, h, 1);
				for (int iy = 0; iy<h; ++iy)
					for (int ix = 0; ix<w; ++ix)
						bufferedimage.setRGB(ix, iy, pixel[(iy+y+1)*displayWidth+ix+x]);
			} else {
				bufferedimage = new BufferedImage(w, h, 1);
				bufferedimage.setRGB(0, 0, w, h, pixel, 0, w);
			}

			return bufferedimage;
		} catch (final Exception e) {
			Log.log.warn("Couldn\'t take screenshot", e);
		}
		return null;
	}

	public static @Nullable BufferedImage takeScreenshot(int displayWidth, int displayHeight, final Framebuffer framebuffer) {
		try {
			if (OpenGlHelper.isFramebufferEnabled()) {
				displayWidth = framebuffer.framebufferTextureWidth;
				displayHeight = framebuffer.framebufferTextureHeight;
			}

			final int k = displayWidth*displayHeight;

			IntBuffer buffer = pixelBuffer;
			int[] pixel = pixelValues;
			if (buffer==null||pixel==null||buffer.capacity()<k) {
				pixelBuffer = buffer = BufferUtils.createIntBuffer(k);
				pixelValues = pixel = new int[k];
			}

			GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			buffer.clear();

			if (OpenGlHelper.isFramebufferEnabled()) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.framebufferTexture);
				GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
			} else
				GL11.glReadPixels(0, 0, displayWidth, displayHeight, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);

			buffer.get(pixel);
			TextureUtil.func_147953_a(pixel, displayWidth, displayHeight);
			BufferedImage bufferedimage = null;

			if (OpenGlHelper.isFramebufferEnabled()) {
				bufferedimage = new BufferedImage(framebuffer.framebufferWidth, framebuffer.framebufferHeight, 1);
				final int l = framebuffer.framebufferTextureHeight-framebuffer.framebufferHeight;

				for (int i1 = l; i1<framebuffer.framebufferTextureHeight; ++i1)
					for (int j1 = 0; j1<framebuffer.framebufferWidth; ++j1)
						bufferedimage.setRGB(j1, i1-l, pixel[i1*framebuffer.framebufferTextureWidth+j1]);
			} else {
				bufferedimage = new BufferedImage(displayWidth, displayHeight, 1);
				bufferedimage.setRGB(0, 0, displayWidth, displayHeight, pixel, 0, displayWidth);
			}

			return bufferedimage;
		} catch (final Exception e) {
			Log.log.warn("Couldn\'t take screenshot", e);
		}
		return null;
	}

	public static @Nullable BufferedImage takeScreenshotRect2(final int x, final int y, final int w, final int h, final Framebuffer framebuffer) {
		if (w<=0||h<=0)
			return null;
		try {
			int width, height;
			if (OpenGlHelper.isFramebufferEnabled()) {
				width = framebuffer.framebufferTextureWidth;
				height = framebuffer.framebufferTextureHeight;
			} else {
				width = w;//Client.mc.displayWidth;
				height = h;//Client.mc.displayHeight;
			}
			final int k = width*height;

			IntBuffer buffer = pixelBuffer;
			int[] pixel = pixelValues;
			if (buffer==null||pixel==null||buffer.capacity()<k) {
				pixelBuffer = buffer = BufferUtils.createIntBuffer(k);
				pixelValues = pixel = new int[k];
			}

			GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			buffer.clear();

			if (OpenGlHelper.isFramebufferEnabled()) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.framebufferTexture);
				GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
			} else
				GL11.glReadPixels(x, y, w, h, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);

			buffer.get(pixel);
			TextureUtil.func_147953_a(pixel, w, h);
			BufferedImage bufferedimage = null;

			if (OpenGlHelper.isFramebufferEnabled()) {
				bufferedimage = new BufferedImage(w, h, 1);

				for (int i1 = 0; i1<h; i1++)
					for (int j1 = 0; j1<w; j1++)
						bufferedimage.setRGB(j1, i1, pixel[(height-(i1+y))*width+j1+x]);
			} else {
				bufferedimage = new BufferedImage(w, h, 1);
				bufferedimage.setRGB(x, y, w, h, pixel, 0, w);
			}

			return bufferedimage;
		} catch (final Exception e) {
			Log.log.warn("Couldn\'t take screenshot", e);
		}
		return null;
	}
}
