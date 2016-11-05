package com.kamesuta.mc.signpic.gui;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.Timer;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MLabel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.MCoord;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.handler.CoreEvent;
import com.kamesuta.mc.signpic.render.RenderHelper;

import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class OverlayFrame extends WFrame {
	public static final OverlayFrame instance = new OverlayFrame();

	public GuiOverlay pane = new GuiOverlay(new R());
	private boolean d;

	private OverlayFrame() {
		final ScaledResolution scaledresolution = new ScaledResolution(Client.mc, Client.mc.displayWidth, Client.mc.displayHeight);
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
		if (event.type==ElementType.CHAT)
			if (Client.mc.currentScreen==null)
				if (!isDelegated())
					drawScreen(event.mouseX, event.mouseY, event.partialTicks);
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
			add(new GuiTask(new R(Coord.width(100), Coord.right(0), Coord.top(20), Coord.bottom(20))));
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
					add(new WPanel(new R(Coord.ptop(.5f), Coord.left(0), Coord.right(0), Coord.pheight(.1f)).child(Coord.ptop(-.5f))) {
						protected Timer timer;

						{
							this.timer = new Timer();
							this.timer.set(-showtime);
						}

						protected MCoord opacity;

						@Override
						protected void initOpacity() {
							super.setOpacity(this.opacity = new MCoord(0f).add(Easings.easeOutQuart.move(.25f, 1f)).start());
						}

						@Override
						protected void initWidget() {
							final MLabel label = new MLabel(new R(Coord.ptop(.2f), Coord.pbottom(.2f), Coord.pleft(.2f), Coord.pright(.2f)), string) {
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
							};
							add(label);
						}

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
						public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
							final Area a = getGuiPosition(pgp);
							RenderHelper.startShape();
							glColor4f(0f, 0f, 0f, getGuiOpacity(popacity)*.5f);
							drawRect(a);
							super.draw(ev, pgp, p, frame, popacity);
						}

						@Override
						public boolean onCloseRequest() {
							this.opacity.stop().add(Easings.easeOutQuart.move(.25f, 0f)).start();
							return false;
						}

						@Override
						public boolean onClosing(final WEvent ev, final Area pgp, final Point p) {
							return this.opacity.isFinished();
						}
					});
				}
			});
		}
	}
}
