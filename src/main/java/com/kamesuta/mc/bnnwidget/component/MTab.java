package com.kamesuta.mc.bnnwidget.component;

import com.kamesuta.mc.bnnwidget.WBox;
import com.kamesuta.mc.bnnwidget.WCommon;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Coord.CoordSide;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.var.VCommon;

public class MTab extends WPanel {
	protected final CoordSide side;
	protected final float widthadd;
	protected final float height;
	protected final R tabarea;
	protected final R boxarea;
	protected Tab tab;
	protected WBox box;
	protected float verticalWidth = 0f;

	public MTab(final R position, final CoordSide side, final float widthadd, final float height) {
		super(position);
		this.side = side;
		this.height = height;
		this.widthadd = widthadd;
		final VCommon width = new VCommon() {
			@Override
			public float getAbsCoord(final float a, final float b) {
				return get();
			}

			@Override
			public float get() {
				return MTab.this.verticalWidth;
			}
		};
		switch (side) {
			case Left:
				this.tabarea = new R(Coord.left(0), Coord.width(width));
				this.boxarea = new R(Coord.left(width), Coord.right(0));
				break;
			default:
			case Top:
				this.tabarea = new R(Coord.top(0), Coord.height(height));
				this.boxarea = new R(Coord.top(height), Coord.bottom(0));
				break;
			case Right:
				this.tabarea = new R(Coord.right(0), Coord.width(width));
				this.boxarea = new R(Coord.right(width), Coord.left(0));
				break;
			case Bottom:
				this.tabarea = new R(Coord.bottom(0), Coord.height(height));
				this.boxarea = new R(Coord.bottom(height), Coord.top(0));
				break;
		}
		this.tab = new Tab(this.tabarea);
		this.box = new WBox(this.boxarea);
	}

	@Override
	protected void initWidget() {
		add(this.tab);
		add(this.box);
	}

	public class Tab extends WPanel {
		protected float ileft;

		public Tab(final R position) {
			super(position);
		}

		public void addTab(final String name, final WCommon widget) {
			Coord cleft;
			Coord cwidth;
			Coord ctop;
			final Coord cheight = Coord.height(MTab.this.height);
			final float w = font().getStringWidth(name)+MTab.this.widthadd;
			switch (MTab.this.side) {
				case Left:
				case Right:
					cleft = Coord.left(0);
					MTab.this.verticalWidth = Math.max(MTab.this.verticalWidth, w);
					cwidth = Coord.pwidth(1f);
					ctop = Coord.top(MTab.this.height*getContainer().size());
					break;
				default:
				case Top:
				case Bottom:
					cleft = Coord.left((this.ileft += w)/2f);
					cwidth = Coord.width(w);
					ctop = Coord.top(0);
					break;
			}
			add(new TabButton(new R(cleft, cwidth, ctop, cheight), widget).setText(name));
		}
	}

	public class TabButton extends MButton {
		public final WCommon widget;

		public TabButton(final R position, final WCommon widget) {
			super(position);
			this.widget = widget;
		}

		@Override
		protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
			MTab.this.box.add(this.widget);
			return true;
		}
	}

	public void addTab(final String name, final WCommon widget) {
		if (this.tab.getContainer().isEmpty())
			this.box.add(widget);
		this.tab.addTab(name, widget);
	}
}
