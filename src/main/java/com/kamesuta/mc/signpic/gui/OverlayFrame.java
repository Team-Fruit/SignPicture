package com.kamesuta.mc.signpic.gui;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.handler.CoreEvent;

import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class OverlayFrame extends WFrame {
	public static final OverlayFrame instance = new OverlayFrame();

	protected GuiOverlay pane = new GuiOverlay(new R());
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

	public GuiOverlay delegate() {
		this.d = true;
		return this.pane;
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
	}
}
