package com.kamesuta.mc.signpic.gui;

import org.lwjgl.util.Timer;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MLabel;
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
import com.kamesuta.mc.signpic.render.RenderHelper;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class OverlayFrame extends WFrame {
	public static final OverlayFrame instance = new OverlayFrame();

	public GuiOverlay pane = new GuiOverlay(new R());
	private boolean d;

	private OverlayFrame() {
		final ScaledResolution scaledresolution = new ScaledResolution(Client.mc);
		final int i = scaledresolution.getScaledWidth();
		final int j = scaledresolution.getScaledHeight();
		setWorldAndResolution(Client.mc, i, j);
	}

	@Override
	protected void initWidget() {
		add(this.pane);
	}

	@CoreEvent
	public void onDraw(final GuiScreenEvent.DrawScreenEvent.Post event) {
		if (!isDelegated())
			drawScreen(event.mouseX, event.mouseY, event.renderPartialTicks);
	}

	@CoreEvent
	public void onDraw(final RenderGameOverlayEvent.Post event) {
		final ScaledResolution scaledresolution = new ScaledResolution(Client.mc);
		setWidth(scaledresolution.getScaledWidth());
		setHeight(scaledresolution.getScaledHeight());

		if (event.type==ElementType.CHAT)
			if (Client.mc.currentScreen==null)
				if (!isDelegated())
					drawScreen(0, 0, event.partialTicks);
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
		private GuiOverlay(final R position) {
			super(position);
		}

		@Override
		protected void initWidget() {
			add(new WPanel(new R()) {
				@Override
				protected void initWidget() {
					add(new GuiTask(new R(Coord.width(100), Coord.right(0), Coord.top(20), Coord.bottom(20))));
				}

				@Override
				public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
					if (Config.instance.renderOverlayPanel||instance.isDelegated())
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

		public void addNotice1(final String string, final float showtime) {
			invokeLater(new Runnable() {
				@Override
				public void run() {
					final VMotion o = V.pm(0f).add(Easings.easeOutQuart.move(.25f, 1f)).start();
					add(new WPanel(new R(Coord.ptop(.5f), Coord.left(0), Coord.right(0), Coord.pheight(.1f)).child(Coord.ptop(-.5f))) {
						protected Timer timer = new Timer();

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
									GlStateManager.color(0f, 0f, 0f, getGuiOpacity(popacity)*.5f);
									draw(a);
									super.draw(ev, pgp, p, frame, popacity);
								}
							});
							add(new WPanel(new R()) {

								@Override
								protected void initOpacity() {
									super.setOpacity(o);
								}

								@Override
								protected void initWidget() {
									final MLabel label = new MLabel(new R(Coord.ptop(.2f), Coord.pbottom(.2f), Coord.pleft(.2f), Coord.pright(.2f))) {
										@Override
										public float getScaleWidth(final Area a) {
											final float f1 = a.w()/font().getStringWidth(string);
											final float f2 = a.h()/font().FONT_HEIGHT;
											return Math.min(f1, f2);
										}

										@Override
										public float getScaleHeight(final Area a) {
											final float f1 = a.w()/font().getStringWidth(string);
											final float f2 = a.h()/font().FONT_HEIGHT;
											return Math.min(f1, f2);
										}
									}.setText(string);
									add(label);
								}

								private boolean removed;

								@Override
								public void update(final WEvent ev, final Area pgp, final Point p) {
									if (timer.getTime()>0f)
										if (!this.removed) {
											GuiOverlay.this.remove(this);
											this.removed = true;
										}
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
