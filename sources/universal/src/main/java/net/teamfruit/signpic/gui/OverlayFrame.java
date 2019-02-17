package net.teamfruit.signpic.gui;

import javax.annotation.Nonnull;

import org.lwjgl.util.Timer;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.teamfruit.bnnwidget.WBase;
import net.teamfruit.bnnwidget.WEvent;
import net.teamfruit.bnnwidget.WFrame;
import net.teamfruit.bnnwidget.WPanel;
import net.teamfruit.bnnwidget.compat.OpenGL;
import net.teamfruit.bnnwidget.component.MLabel;
import net.teamfruit.bnnwidget.component.MScaledLabel;
import net.teamfruit.bnnwidget.motion.Easings;
import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.position.Coord;
import net.teamfruit.bnnwidget.position.Point;
import net.teamfruit.bnnwidget.position.R;
import net.teamfruit.bnnwidget.render.RenderOption;
import net.teamfruit.bnnwidget.render.WRenderer;
import net.teamfruit.bnnwidget.var.V;
import net.teamfruit.bnnwidget.var.VCommon;
import net.teamfruit.bnnwidget.var.VMotion;
import net.teamfruit.signpic.Client;
import net.teamfruit.signpic.Config;
import net.teamfruit.signpic.CoreEvent;
import net.teamfruit.signpic.compat.CompatEvents.CompatGuiScreenEvent;
import net.teamfruit.signpic.compat.CompatEvents.CompatRenderGameOverlayEvent;
import net.teamfruit.signpic.compat.CompatEvents.CompatRenderGameOverlayEvent.CompatElementType;
import net.teamfruit.signpic.compat.CompatEvents.CompatTickEvent.CompatClientTickEvent;

public class OverlayFrame extends WFrame {
	public static final @Nonnull OverlayFrame instance = new OverlayFrame();

	protected boolean initialized;
	public @Nonnull GuiOverlay pane = new GuiOverlay(new R());
	private boolean d;

	private OverlayFrame() {
	}

	@Override
	protected void initWidget() {
		add(this.pane);
	}

	@CoreEvent
	public void onDraw(final @Nonnull CompatGuiScreenEvent.CompatDrawScreenEvent.CompatPost event) {
		final GuiScreen gui = event.getGui();
		if (gui==null)
			return;
		if (Config.getConfig().renderGuiOverlay.get())
			if (!isDelegated()) {
				setWidth(gui.width);
				setHeight(gui.height);
				OpenGL.glPushMatrix();
				OpenGL.glTranslatef(0f, 0f, 1000f);
				drawScreen(event.getMouseX(), event.getMouseY(), event.getRenderPartialTicks());
				OpenGL.glPopMatrix();
			}
	}

	@CoreEvent
	public void onDraw(final @Nonnull CompatRenderGameOverlayEvent.CompatPost event) {
		final ScaledResolution resolution = event.getResolution();
		if (resolution==null)
			return;
		if (event.getType()==CompatElementType.CHAT&&Client.mc.currentScreen==null)
			if (!isDelegated()) {
				setWidth(resolution.getScaledWidth());
				setHeight(resolution.getScaledHeight());
				drawScreen(0, 0, event.getPartialTicks());
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
	public void onTick(final @Nonnull CompatClientTickEvent event) {
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
		public final @Nonnull GuiTask task;

		private GuiOverlay(final @Nonnull R position) {
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
				public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
					if (Config.getConfig().renderOverlayPanel.get()||instance.isDelegated())
						super.draw(ev, pgp, p, frame, popacity, opt);
				}
			});
		}

		@Override
		public boolean onCloseRequest() {
			return true;
		}

		@Override
		public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
			return true;
		}

		@Deprecated
		public void addNotice1(final @Nonnull String string, final float showtime) {
			invokeLater(() -> {
				final @Nonnull VMotion o = V.pm(0f).add(Easings.easeOutQuart.move(.25f, 1f)).start();
				add(new WPanel(new R(Coord.ptop(.5f), Coord.left(0), Coord.right(0), Coord.pheight(.1f)).child(Coord.ptop(-.5f))) {
					protected @Nonnull Timer timer = new Timer();

					private boolean removed;

					@Override
					public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
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
							protected @Nonnull VCommon initOpacity() {
								return o;
							}

							@Override
							public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
								final Area a = getGuiPosition(pgp);
								WRenderer.startShape();
								OpenGL.glColor4f(0f, 0f, 0f, getGuiOpacity(popacity)*.5f);
								draw(a);
							}
						});
						add(new WPanel(new R()) {

							@Override
							protected @Nonnull VCommon initOpacity() {
								return o;
							}

							@Override
							protected void initWidget() {
								final MLabel label = new MScaledLabel(new R(Coord.top(5f), Coord.bottom(4f), Coord.pleft(.1f), Coord.pright(.1f))).setText(string).setVerticalAlign(VerticalAlign.MIDDLE);
								add(label);
							}

							@Override
							public boolean onCloseRequest() {
								o.stop().add(Easings.easeOutQuart.move(.25f, 0f)).start();
								return false;
							}

							@Override
							public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
								return o.isFinished();
							}
						});
					}
				});
			});
		}
	}
}
