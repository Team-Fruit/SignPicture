package com.kamesuta.mc.guiwidget;

import java.util.ArrayList;
import java.util.List;

import com.kamesuta.mc.guiwidget.position.Point;
import com.kamesuta.mc.guiwidget.position.R;
import com.kamesuta.mc.guiwidget.position.Area;

public class WPanel extends WBase implements WContainer {
	protected final ArrayList<WCommon> widgets = new ArrayList<WCommon>();

	public WPanel(final R position) {
		super(position);
	}

	@Override
	public List<WCommon> getContainer() {
		return this.widgets;
	}

	@Override
	public boolean add(final WCommon widget) {
		return this.widgets.add(widget);
	}

	@Override
	public boolean remove(final WCommon widget) {
		return this.widgets.remove(widget);
	}

	@Override
	public void init(final WEvent ev, final Area pgp) {
		for (final WCommon widget : this.widgets)
			widget.init(ev, pgp);
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
		if (gp.isVaild())
			for (final WCommon widget : this.widgets)
				widget.update(ev, gp, p);
	}

	@Override
	public void keyTyped(final WEvent ev, final Area pgp, final Point p, final char c, final int keycode) {
		final Area gp = getGuiPosition(pgp);
		if (gp.isVaild())
			for (final WCommon widget : this.widgets)
				widget.keyTyped(ev, gp, p, c, keycode);
	}

	@Override
	public void mouseScrolled(final WEvent ev, final Area pgp, final Point p, final int scroll) {
		final Area gp = getGuiPosition(pgp);
		if (gp.isVaild())
			for (final WCommon widget : this.widgets)
				widget.mouseScrolled(ev, gp, p, scroll);
	}

	@Override
	public void mouseMoved(final WEvent ev, final Area pgp, final Point p, final int button) {
		final Area gp = getGuiPosition(pgp);
		if (gp.isVaild())
			for (final WCommon widget : this.widgets)
				widget.mouseMoved(ev, gp, p, button);
	}

	@Override
	public void mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
		final Area gp = getGuiPosition(pgp);
		if (gp.isVaild())
			for (final WCommon widget : this.widgets)
				widget.mouseClicked(ev, gp, p, button);
	}

	@Override
	public void mouseDragged(final WEvent ev, final Area pgp, final Point p, final int button, final long time) {
		final Area gp = getGuiPosition(pgp);
		if (gp.isVaild())
			for (final WCommon widget : this.widgets)
				widget.mouseDragged(ev, gp, p, button, time);
	}

	@Override
	public void mouseReleased(final WEvent ev, final Area pgp, final Point p, final int button) {
		final Area gp = getGuiPosition(pgp);
		if (gp.isVaild())
			for (final WCommon widget : this.widgets)
				widget.mouseReleased(ev, gp, p, button);
	}
}
