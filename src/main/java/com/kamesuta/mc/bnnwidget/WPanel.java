package com.kamesuta.mc.bnnwidget;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;

public class WPanel extends WBase implements WContainer {
	protected final ArrayList<WCommon> widgets = new ArrayList<WCommon>();
	protected final Deque<WCommon> removelist = new ArrayDeque<WCommon>();

	public WPanel(final R position) {
		super(position);
	}

	@Override
	public List<WCommon> getContainer() {
		return this.widgets;
	}

	@Override
	public boolean add(final WCommon widget) {
		final boolean b = this.widgets.add(widget);
		widget.onAdded();
		return b;
	}

	@Override
	public boolean remove(final WCommon widget) {
		if (widget.onCloseRequest()) {
			this.widgets.remove(widget);
			return true;
		} else {
			this.removelist.offer(widget);
			return false;
		}
	}

	@Override
	public void onAdded() {
		initWidget();
	}

	protected void initWidget() {
	}

	@Override
	public void onInit(final WEvent ev, final Area pgp, final Point p) {
		final Area gp = getGuiPosition(pgp);
		for (final WCommon widget : this.widgets)
			widget.onInit(ev, gp, p);
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area gp = getGuiPosition(pgp);
		for (final WCommon widget : this.widgets)
			widget.draw(ev, gp, p, frame);
	}

	@Override
	public void update(final WEvent ev, final Area pgp, final Point p) {
		final Area gp = getGuiPosition(pgp);
		for (final WCommon widget : this.widgets)
			widget.update(ev, gp, p);
		for (final Iterator<WCommon> itr = this.removelist.iterator(); itr.hasNext();) {
			final WCommon widget = itr.next();
			if (widget.onClosing(ev, gp, p)) {
				this.widgets.remove(widget);
				itr.remove();
			}
		}
	}

	@Override
	public void keyTyped(final WEvent ev, final Area pgp, final Point p, final char c, final int keycode) {
		final Area gp = getGuiPosition(pgp);
		for (final WCommon widget : this.widgets)
			widget.keyTyped(ev, gp, p, c, keycode);
	}

	@Override
	public void mouseScrolled(final WEvent ev, final Area pgp, final Point p, final int scroll) {
		final Area gp = getGuiPosition(pgp);
		for (final WCommon widget : this.widgets)
			widget.mouseScrolled(ev, gp, p, scroll);
	}

	@Override
	public void mouseMoved(final WEvent ev, final Area pgp, final Point p, final int button) {
		final Area gp = getGuiPosition(pgp);
		for (final WCommon widget : this.widgets)
			widget.mouseMoved(ev, gp, p, button);
	}

	@Override
	public void mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
		final Area gp = getGuiPosition(pgp);
		for (final WCommon widget : this.widgets)
			widget.mouseClicked(ev, gp, p, button);
	}

	@Override
	public void mouseDragged(final WEvent ev, final Area pgp, final Point p, final int button, final long time) {
		final Area gp = getGuiPosition(pgp);
		for (final WCommon widget : this.widgets)
			widget.mouseDragged(ev, gp, p, button, time);
	}

	@Override
	public void mouseReleased(final WEvent ev, final Area pgp, final Point p, final int button) {
		final Area gp = getGuiPosition(pgp);
		for (final WCommon widget : this.widgets)
			widget.mouseReleased(ev, gp, p, button);
	}

	@Override
	public boolean onCloseRequest() {
		boolean closable = true;
		for (final Iterator<WCommon> itr = this.widgets.iterator(); itr.hasNext();) {
			final WCommon widget = itr.next();
			if (widget.onCloseRequest())
				itr.remove();
			else {
				this.removelist.offer(widget);
				closable = false;
			}
		}
		return closable;
	}

	@Override
	public boolean onClosing(final WEvent ev, final Area pgp, final Point p) {
		final Area gp = getGuiPosition(pgp);
		boolean closable = true;
		for (final Iterator<WCommon> itr = this.removelist.iterator(); itr.hasNext();) {
			final WCommon widget = itr.next();
			if (widget.onClosing(ev, gp, p)) {
				this.widgets.remove(widget);
				itr.remove();
			} else
				closable = false;
		}
		return closable;
	}

	@Override
	public WCommon top(final WEvent ev, final Area pgp, final Point point) {
		final Area gp = getGuiPosition(pgp);
		if (gp.pointInside(point)) {
			WCommon topwidget = null;
			for (final WCommon widget : this.widgets) {
				final WCommon top = widget.top(ev, gp, point);
				if (top != null)
					topwidget = top;
			}
			return topwidget;
		}
		return null;
	}
}
