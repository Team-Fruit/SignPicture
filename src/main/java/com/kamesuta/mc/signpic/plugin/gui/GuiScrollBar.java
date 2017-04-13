package com.kamesuta.mc.signpic.plugin.gui;

import javax.annotation.Nonnull;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.motion.Motion;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.RenderOption;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Log;

public class GuiScrollBar extends WPanel {

	protected final @Nonnull Scrollable pane;
	protected final @Nonnull VMotion top = V.am(0f);
	protected final @Nonnull VMotion height = V.am(0f);
	protected final @Nonnull Knob knob;

	public GuiScrollBar(final @Nonnull R position, final @Nonnull Scrollable pane) {
		super(position);
		this.pane = pane;
		this.knob = new Knob(new R(Coord.top(this.top), Coord.height(this.height)));
	}

	@Override
	protected void initWidget() {
		add(this.knob);
	}

	protected float ratio;

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity, final RenderOption opt) {
		final float arrow = getGuiPosition(pgp).h();
		final float gui = this.pane.getAllHeight();
		if (arrow>=gui) {
			this.ratio = -1f;
			return;
		}
		this.ratio = arrow/gui;
		float knob = arrow*this.ratio;
		if (knob<15f)
			knob = 15f;
		final float scroll = this.pane.getNowHeight()*this.ratio;
		final float top = scroll>=0f ? scroll : 0f;
		if (this.top.get()!=top)
			this.top.stop().add(Motion.move(top)).start();
		final float height = arrow>scroll+knob ? scroll>=0f ? knob : knob+scroll : arrow-scroll;
		if (this.height.get()!=height)
			this.height.stop().add(Motion.move(height)).start();
		if (pgp.pointInside(p))
			super.draw(ev, pgp, p, frame, popacity, opt);
	}

	public class Knob extends WBase {

		public Knob(final @Nonnull R position) {
			super(position);
		}

		@Override
		public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity, final RenderOption opt) {
			super.draw(ev, pgp, p, frame, popacity, opt);
			final Area a = getGuiPosition(pgp);
			WRenderer.startShape();
			if (a.pointInside(p))
				OpenGL.glColor4i(154, 205, 50, 255);
			else
				OpenGL.glColor4i(255, 255, 255, 255);
			draw(a);
		}

		@Override
		public boolean mouseDragged(final WEvent ev, final Area pgp, final Point p, final int button, final long time) {
			if (button<2)
				if (p.y()>=pgp.y1()&&p.y()<pgp.y2()) {
					final float per = p.y()-pgp.y1();
					Log.log.info(per/GuiScrollBar.this.ratio);
					GuiScrollBar.this.pane.scrollTo(-(per/GuiScrollBar.this.ratio), (GuiManager) ev.owner, null);
				}
			return super.mouseDragged(ev, pgp, p, button, time);
		}
	}
}
