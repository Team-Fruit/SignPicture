package com.kamesuta.mc.signpic.plugin.gui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
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

public class GuiScrollBar extends WPanel {

	protected final @Nonnull Scrollable pane;
	protected final @Nonnull VMotion top = V.am(0f);
	protected final @Nonnull VMotion height = V.am(0f);

	public GuiScrollBar(final @Nonnull R position, final @Nonnull Scrollable pane) {
		super(position);
		this.pane = pane;
	}

	@Override
	protected void initWidget() {
		add(new Knob(new R(Coord.top(this.top), Coord.height(this.height))));
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity, final RenderOption opt) {
		final float arrow = getGuiPosition(pgp).h();
		final float gui = this.pane.getAllHeight();
		if (arrow>=gui)
			return;
		final float ratio = arrow/gui;
		float knob = arrow*ratio;
		if (knob<15f)
			knob = 15f;
		final float scroll = this.pane.getNowHeight()*ratio;
		this.top.stop().add(Motion.move(scroll>=0f ? scroll : 0f)).start();
		this.height.stop().add(Motion.move(arrow>scroll+knob ? scroll>=0f ? knob : knob+scroll : arrow-scroll)).start();
		if (pgp.pointInside(p))
			super.draw(ev, pgp, p, frame, popacity, opt);
	}

	public void move(final float f) {
		this.top.stop().add(Easings.easeLinear.move(.25f, this.top.get()+f)).start();
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

		private @Nullable Point lastClick;
		private int lastButton = -1;

		@Override
		public boolean mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
			if (button<2)
				if (getGuiPosition(pgp).pointInside(p)) {
					this.lastClick = p;
					this.lastButton = button;
				}
			return super.mouseClicked(ev, pgp, p, button);
		}

		@Override
		public boolean mouseReleased(final WEvent ev, final Area pgp, final Point p, final int button) {
			if (button<2)
				if (getGuiPosition(pgp).pointInside(p))
					if (this.lastClick!=null&&this.lastButton==button)
						move(this.lastClick.y()-p.y());
			return super.mouseReleased(ev, pgp, p, button);
		}
	}
}
