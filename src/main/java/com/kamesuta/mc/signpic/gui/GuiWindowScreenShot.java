package com.kamesuta.mc.signpic.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.Motion;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.http.upload.UploadCallback;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.util.FileUtilitiy;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiWindowScreenShot extends WFrame {
	public static final Color windowcolor = new Color(0f, 0f, 0f, 0f);
	public static final Color bgcolor = new Color(0f, 0f, 0f, .25f);
	public static final Color textcolor = new Color(1f, 1f, 1f, .6f);
	public static final Color textshadowcolor = new Color(0f, 0f, 0f, .6f);

	public GuiWindowScreenShot(final @Nullable GuiScreen parent) {
		super(parent);
	}

	public GuiWindowScreenShot() {
	}

	{
		setGuiPauseGame(false);
	}

	@Override
	public void onGuiClosed() {
		destroy();
		super.onGuiClosed();
	}

	private @Nullable Window window;
	private @Nullable Point point1;
	private @Nullable Point point2;
	private boolean takescreenshot;

	public void destroy() {
		final Window frame = this.window;
		if (frame!=null) {
			this.window = null;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					frame.dispose();
				}
			});
		}
		requestClose();
	}

	public @Nullable Rectangle getSelectedRect() {
		final Point point1 = GuiWindowScreenShot.this.point1;
		final Point point2 = GuiWindowScreenShot.this.point2;
		if (point1!=null&&point2!=null) {
			final int x1 = Math.min(point1.x, point2.x);
			final int y1 = Math.min(point1.y, point2.y);
			final int x2 = Math.max(point1.x, point2.x);
			final int y2 = Math.max(point1.y, point2.y);
			return new Rectangle(x1, y1, x2-x1, y2-y1);
		}
		return null;
	}

	@Override
	public void drawScreen(final int mousex, final int mousey, final float f) {
		if (this.window!=null)
			this.window.repaint();
		super.drawScreen(mousex, mousey, f);
	}

	@Override
	protected void initWidget() {
		if (this.window==null)
			try {
				final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				final GraphicsDevice[] screens = ge.getScreenDevices();
				Rectangle rect0 = null;
				GraphicsDevice screen0 = null;
				GraphicsConfiguration config0 = null;
				for (final GraphicsDevice screen : screens) {
					final GraphicsConfiguration config = screen.getDefaultConfiguration();
					if (screen0==null)
						screen0 = screen;
					if (config0==null)
						config0 = config;
					if (rect0==null)
						rect0 = config.getBounds();
					else
						rect0.add(config.getBounds());
				}
				final Rectangle rect = rect0;
				if (screen0!=null&&rect!=null) {
					final GraphicsDevice screen = screen0;
					final JDialog window = new JDialog();
					this.window = window;
					window.setTitle("SignPicture");
					window.setUndecorated(true);
					window.setBackground(windowcolor);
					window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
					window.setAlwaysOnTop(true);
					window.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
					window.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(@Nullable final WindowEvent e) {
							destroy();
						}
					});
					window.addKeyListener(new KeyAdapter() {
						@Override
						public void keyPressed(@Nullable final KeyEvent e) {
							if (e!=null&&e.getKeyCode()==KeyEvent.VK_ESCAPE)
								destroy();
						}
					});
					window.setBounds(rect0);

					final JPanel panel = new JPanel() {
						private boolean takingscreenshot;
						private boolean disable;
						private boolean flushing;
						private @Nonnull VMotion opacity = V.pm(0).start();

						@Override
						protected void paintComponent(@Nullable final Graphics g) {
							final boolean continuemode = CurrentMode.instance.isState(CurrentMode.State.CONTINUE);
							if (this.flushing) {
								if (g!=null&&g instanceof Graphics2D) {
									final Graphics2D g2 = (Graphics2D) g;
									if (continuemode) {
										g2.setColor(bgcolor);
										g2.fill(rect);
									}
									g.setColor(new Color(1f, 1f, 1f, this.opacity.get()));
									final Rectangle in = getSelectedRect();
									g2.fill(in);
								}
								if (this.opacity.isFinished()) {
									if (!continuemode)
										destroy();
									this.flushing = false;
									GuiWindowScreenShot.this.point1 = null;
									GuiWindowScreenShot.this.point2 = null;
								}
							} else if (this.takingscreenshot) {
								this.takingscreenshot = false;
								final Rectangle in = getSelectedRect();
								if (in!=null) {
									final BufferedImage image = takeScreenshotRect(screen, in);
									if (image!=null)
										try {
											FileUtilitiy.uploadImage(image, UploadCallback.copyOnDone);
										} catch (final IOException ex) {
											Log.notice(I18n.format("signpic.gui.notice.screenshot.window.capture.error", ex));
										}
									Client.playSound(new ResourceLocation("signpic", "gui.screenshot"), 1.0F);
									this.opacity.stop().add(Motion.move(.25f)).add(Easings.easeLinear.move(.5f, 0f));
									this.flushing = true;
									if (!continuemode)
										this.disable = true;
								}
							} else if (GuiWindowScreenShot.this.takescreenshot) {
								GuiWindowScreenShot.this.takescreenshot = false;
								this.takingscreenshot = true;
							} else if (!this.disable&&g!=null&&g instanceof Graphics2D) {
								final Graphics2D g2 = (Graphics2D) g;
								final Area area = new Area(rect);
								final Rectangle in = getSelectedRect();
								if (in!=null) {
									area.subtract(new Area(in));
									final int x = in.x+in.width;
									final int y = in.y+in.height;
									g2.setColor(textshadowcolor);
									g2.drawString(String.valueOf(in.width), x-30+2, y-20+2);
									g2.drawString(String.valueOf(in.height), x-30+2, y-10+2);
									g2.setColor(textcolor);
									g2.drawString(String.valueOf(in.width), x-30, y-20);
									g2.drawString(String.valueOf(in.height), x-30, y-10);
								}
								g2.setColor(bgcolor);
								g2.fill(area);
							}
						}
					};
					final MouseAdapter mouse = new MouseAdapter() {
						@Override
						public void mousePressed(@Nullable final MouseEvent e) {
							if (e!=null)
								if (GuiWindowScreenShot.this.point1==null)
									GuiWindowScreenShot.this.point1 = e.getPoint();
								else
									checkAndTake();
							super.mousePressed(e);
						}

						@Override
						public void mouseReleased(@Nullable final MouseEvent e) {
							if (e!=null) {
								GuiWindowScreenShot.this.point2 = e.getPoint();
								checkAndTake();
							}
							super.mouseReleased(e);
						}

						@Override
						public void mouseMoved(@Nullable final MouseEvent e) {
							if (e!=null)
								mouseUpdate(e.getPoint());
							super.mouseMoved(e);
						}

						@Override
						public void mouseDragged(@Nullable final MouseEvent e) {
							if (e!=null)
								mouseUpdate(e.getPoint());
							super.mouseDragged(e);
						}

						private void mouseUpdate(@Nullable final Point p) {
							GuiWindowScreenShot.this.point2 = p;
						}

						private void checkAndTake() {
							final Point p1 = GuiWindowScreenShot.this.point1;
							final Point p2 = GuiWindowScreenShot.this.point2;
							if (p1!=null&&p2!=null&&p1.x!=p2.x&&p1.y!=p2.y)
								GuiWindowScreenShot.this.takescreenshot = true;
						}
					};
					panel.addMouseListener(mouse);
					panel.addMouseMotionListener(mouse);

					window.setContentPane(panel);
					window.setVisible(true);
				}
			} catch (final Exception e) {
				Log.notice(I18n.format("signpic.gui.notice.screenshot.window.error", e));
				destroy();
			}
	}

	public static @Nullable BufferedImage takeScreenshotRect(final GraphicsDevice device, final Rectangle rect) {
		if (rect.isEmpty())
			return null;
		try {
			final Robot robot = new Robot(device);
			return robot.createScreenCapture(rect);
		} catch (final Exception e) {
			Log.notice(I18n.format("signpic.gui.notice.screenshot.window.error", e));
		}
		return null;
	}

}
