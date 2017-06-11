package com.kamesuta.mc.signpic.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.font.FontStyle;
import com.kamesuta.mc.bnnwidget.font.TrueTypeFont;
import com.kamesuta.mc.bnnwidget.font.WFontRenderer;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.Motion;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.RenderOption;
import com.kamesuta.mc.bnnwidget.render.WGui;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.http.upload.UploadCallback;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.util.FileUtilitiy;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;

public class GuiIngameScreenShot extends WFrame {

	public GuiIngameScreenShot(final @Nullable GuiScreen parent) {
		super(parent);
	}

	public GuiIngameScreenShot() {
	}

	{
		setGuiPauseGame(false);
	}

	@Override
	public void onGuiClosed() {
		WGui.showCursor();
		super.onGuiClosed();
	}

	private static WFontRenderer font = new WFontRenderer(new TrueTypeFont(new FontStyle.Builder().build()));
	private boolean takescreenshot;

	@Override
	protected void initWidget() {
		WGui.hideCursor();
		add(new WPanel(new R()) {
			private @Nullable Point point;
			private boolean takingscreenshot;
			private boolean disable;
			private boolean flushing;
			private @Nonnull final VMotion opacity = V.pm(0).start();

			@Override
			public boolean mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				if (this.point==null)
					this.point = p;
				return super.mouseClicked(ev, pgp, p, button);
			}

			@Override
			public boolean mouseReleased(final WEvent ev, final Area pgp, final Point p, final int button) {
				if (this.point!=null&&!this.point.equals(p))
					GuiIngameScreenShot.this.takescreenshot = true;
				return super.mouseReleased(ev, pgp, p, button);
			}

			@Override
			public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point point2, final float frame, final float popacity, final @Nonnull RenderOption opt) {
				final boolean continuemode = CurrentMode.instance.isState(CurrentMode.State.CONTINUE);
				final Area a = getGuiPosition(pgp);
				WRenderer.startShape(BlendType.ONE_MINUS_DST_COLOR, BlendType.ZERO);
				OpenGL.glColor4f(1f, 1f, 1f, 1f);
				drawCursor(point2);
				if (this.flushing) {
					final Point point = this.point;
					final Area rect;
					if (point!=null)
						rect = Area.abs(point2.x(), point2.y(), point.x(), point.y());
					else
						rect = a;
					WRenderer.startShape();
					if (continuemode) {
						OpenGL.glColor(GuiWindowScreenShot.bgcolor);
						draw(a);
					}
					OpenGL.glColor4f(1f, 1f, 1f, this.opacity.get());
					draw(rect);
					if (this.opacity.isFinished()) {
						if (!continuemode)
							requestClose();
						this.flushing = false;
						this.point = null;
					}
				} else if (this.takingscreenshot) {
					this.takingscreenshot = false;
					final Point point1 = this.point;
					final BufferedImage image;
					if (point1!=null) {
						final Area rect = Area.abs(point2.x()*scaleX(), point2.y()*scaleY(), point1.x()*scaleX(), point1.y()*scaleY());
						image = takeScreenshotRect((int) rect.x1(), (int) rect.y1(), (int) rect.w()-1, (int) rect.h()-1, mc.getFramebuffer());
					} else
						image = takeScreenshot(mc.getFramebuffer());
					if (image!=null)
						try {
							FileUtilitiy.uploadImage(image, UploadCallback.copyOnDone);
						} catch (final IOException e) {
							Log.notice(I18n.format("signpic.gui.notice.screenshot.ingame.capture.error", e));
						}
					Client.playSound(new ResourceLocation("signpic", "gui.screenshot"), 1.0F);
					this.opacity.stop().add(Motion.move(.25f)).add(Easings.easeLinear.move(.5f, 0f));
					this.flushing = true;
					if (!continuemode)
						this.disable = true;
				} else if (GuiIngameScreenShot.this.takescreenshot) {
					GuiIngameScreenShot.this.takescreenshot = false;
					this.takingscreenshot = true;
				} else if (!this.disable&&!GuiIngameScreenShot.this.closeRequest) {
					final Point point = this.point;
					if (point!=null) {
						final Area rect = Area.abs(point2.x(), point2.y(), point.x(), point.y());
						WRenderer.startShape();
						OpenGL.glColor(GuiWindowScreenShot.bgcolor);
						drawAround(a, rect);
						WRenderer.startTexture();
						OpenGL.glColor(GuiWindowScreenShot.textcolor);
						font.drawString(String.valueOf((int) rect.w()), Area.abs(rect.x2()-5, rect.y2()-12, 10, 3), guiScaleX(), Align.RIGHT, true);
						font.drawString(String.valueOf((int) rect.h()), Area.abs(rect.x2()-5, rect.y2()-8, 10, 3), guiScaleY(), Align.RIGHT, true);
					} else {
						WRenderer.startShape();
						OpenGL.glColor4f(0f, 0f, 0f, .25f);
						draw(a);
					}
				}
				WRenderer.startShape();
				super.draw(ev, pgp, point2, frame, popacity, opt);
			}

			private void drawCursor(final Point point) {
				final float s = .5f;
				draw(Area.abs(point.x()-s, point.y()-s, point.x()+s, point.y()+s));
				draw(Area.abs(point.x()-4.5f, point.y()-s, point.x(), point.y()+s));
				draw(Area.abs(point.x(), point.y()-s, point.x()+4.5f, point.y()+s));
				draw(Area.abs(point.x()-s, point.y()-4.5f, point.x()+s, point.y()));
				draw(Area.abs(point.x()-s, point.y(), point.x()+s, point.y()+4.5f));
			}

			private void drawAround(final @Nonnull Area out, final @Nonnull Area in) {
				draw(Area.abs(out.x1(), out.y1(), out.x2(), in.y1()));
				draw(Area.abs(out.x1(), in.y1(), in.x1(), in.y2()));
				draw(Area.abs(in.x2(), in.y1(), out.x2(), in.y2()));
				draw(Area.abs(out.x1(), in.y2(), out.x2(), out.y2()));
			}
		});
	}

	public void takeFullScreenshot() {
		this.takescreenshot = true;
	}

	private static @Nullable IntBuffer pixelBuffer;
	private static @Nullable int[] pixelValues;

	public static @Nullable BufferedImage takeScreenshotRect(final int x, final int y, final int w, final int h, final Framebuffer framebuffer) {
		if (w<=0||h<=0)
			return null;
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
				final int l = displayHeight-framebuffer.framebufferHeight;
				for (int iy = 0; iy<h; ++iy)
					for (int ix = 0; ix<w; ++ix)
						bufferedimage.setRGB(ix, iy, pixel[(iy+l+y)*displayWidth+ix+x]);
			} else {
				bufferedimage = new BufferedImage(w, h, 1);
				bufferedimage.setRGB(0, 0, w, h, pixel, 0, w);
			}

			return bufferedimage;
		} catch (final Exception e) {
			Log.notice(I18n.format("signpic.gui.notice.screenshot.ingame.error", e));
		}
		return null;
	}

	public static @Nullable BufferedImage takeScreenshot(final Framebuffer framebuffer) {
		return takeScreenshotRect(0, 0, Client.mc.displayWidth, Client.mc.displayHeight, framebuffer);
	}
}
