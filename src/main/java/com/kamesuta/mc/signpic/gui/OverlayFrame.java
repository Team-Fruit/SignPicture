package com.kamesuta.mc.signpic.gui;

import org.lwjgl.util.Timer;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MLabel;
import com.kamesuta.mc.bnnwidget.component.MScaledLabel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.CoreEvent;
import com.kamesuta.mc.signpic.render.OpenGL;
import com.kamesuta.mc.signpic.render.RenderHelper;

import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class OverlayFrame extends WFrame {
	public static final OverlayFrame instance = new OverlayFrame();

	protected boolean initialized;
	public GuiOverlay pane = new GuiOverlay(new R());
	private boolean d;

	private OverlayFrame() {
		this.mc = Client.mc;
	}

	@Override
	protected void initWidget() {
		add(this.pane);
	}

	@CoreEvent
	public void onDraw(final GuiScreenEvent.DrawScreenEvent.Post event) {
		if (Config.instance.renderGuiOverlay.get())
			if (!isDelegated()) {
				setWidth(event.gui.width);
				setHeight(event.gui.height);
				OpenGL.glPushMatrix();
				OpenGL.glTranslatef(0f, 0f, 1000f);
				drawScreen(event.mouseX, event.mouseY, event.renderPartialTicks);
				OpenGL.glPopMatrix();
			}
	}

	@CoreEvent
	public void onDraw(final RenderGameOverlayEvent.Post event) {
		if (event.type==ElementType.CHAT&&Client.mc.currentScreen==null)
			if (!isDelegated()) {
				setWidth(event.resolution.getScaledWidth());
				setHeight(event.resolution.getScaledHeight());
				drawScreen(event.mouseX, event.mouseY, event.partialTicks);
			}
	}

	@Override
	public void drawScreen(final int mousex, final int mousey, final float f) {
		if (!this.initialized) {
			setWorldAndResolution(Client.mc, (int) width(), (int) height());
			this.initialized = true;
		}
		super.drawScreen(mousex, mousey, f);
	}

	@CoreEvent
	public void onTick(final ClientTickEvent event) {
		updateScreen();
	}

	public void delegate() {
		this.d = true;
	}

	public void release() {
		this.d = false;
	}

	public boolean isDelegated() {
		return this.d;
	}

	public static class GuiOverlay extends WPanel {
		public final GuiTask task;

		private GuiOverlay(final R position) {
			super(position);
			this.task = new GuiTask(new R(Coord.width(100), Coord.right(0), Coord.top(20), Coord.bottom(20)));
		}

		@Override
		protected void initWidget() {
			add(new WPanel(new R()) {
				@Override
				protected void initWidget() {
					add(GuiOverlay.this.task);
				}

				@Override
				public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
					if (Config.instance.renderOverlayPanel.get()||instance.isDelegated())
						super.draw(ev, pgp, p, frame, popacity);
				}
			});
		}

		@Override
		public boolean onCloseRequest() {
			return true;
		}

		@Override
		public boolean onClosing(final WEvent ev, final Area pgp, final Point p) {
			return true;
		}

		@Deprecated
		public void addNotice1(final String string, final float showtime) {
			invokeLater(new Runnable() {
				@Override
				public void run() {
					final VMotion o = V.pm(0f).add(Easings.easeOutQuart.move(.25f, 1f)).start();
					add(new WPanel(new R(Coord.ptop(.5f), Coord.left(0), Coord.right(0), Coord.pheight(.1f)).child(Coord.ptop(-.5f))) {
						protected Timer timer = new Timer();

						private boolean removed;

						@Override
						public void update(final WEvent ev, final Area pgp, final Point p) {
							if (this.timer.getTime()>0f)
								if (!this.removed) {
									GuiOverlay.this.remove(this);
									this.removed = true;
								}
						}

						@Override
						protected void initWidget() {
							this.timer.set(-showtime);
							add(new WBase(new R(Coord.top(V.pm(.5f).add(Easings.easeOutElastic.move(1f, 0f)).start()), Coord.bottom(V.pm(.5f).add(Easings.easeOutElastic.move(1f, 0f)).start()))) {
								@Override
								protected void initOpacity() {
									super.setOpacity(o);
								}

								@Override
								public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
									final Area a = getGuiPosition(pgp);
									RenderHelper.startShape();
									OpenGL.glColor4f(0f, 0f, 0f, getGuiOpacity(popacity)*.5f);
									draw(a);
								}
							});
							add(new WPanel(new R()) {

								@Override
								protected void initOpacity() {
									super.setOpacity(o);
								}

								@Override
								protected void initWidget() {
									final MLabel label = new MScaledLabel(new R(Coord.ptop(.2f), Coord.pbottom(.2f), Coord.pleft(.2f), Coord.pright(.2f))).setText(string);
									add(label);
								}

								@Override
								public boolean onCloseRequest() {
									o.stop().add(Easings.easeOutQuart.move(.25f, 0f)).start();
									return false;
								}

								@Override
								public boolean onClosing(final WEvent ev, final Area pgp, final Point p) {
									return o.isFinished();
								}
							});
						}
					});
				}
			});
		}
	}
}
