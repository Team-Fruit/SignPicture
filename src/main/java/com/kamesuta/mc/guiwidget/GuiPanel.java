package com.kamesuta.mc.guiwidget;

import java.util.ArrayList;
import java.util.List;

import com.kamesuta.mc.guiwidget.position.Point;
import com.kamesuta.mc.guiwidget.position.relative.IPositionRelative;

public class GuiPanel extends GuiBase implements GuiContainer {
	protected final ArrayList<GuiCommon> widgets = new ArrayList<GuiCommon>();

	public GuiPanel(final IPositionRelative position) {
		super(position);
	}

	@Override
	public List<GuiCommon> getContainer() {
		return this.widgets;
	}

	@Override
	public boolean add(final GuiCommon widget) {
		return this.widgets.add(widget);
	}

	@Override
	public boolean remove(final GuiCommon widget) {
		return this.widgets.remove(widget);
	}

	@Override
	public void init(final GuiEvent ev, final GuiPosition pgp) {
		for (final GuiCommon widget : this.widgets)
			widget.init(ev, pgp);
	}

	@Override
	public void draw(final GuiEvent ev, final GuiPosition pgp, final Point p, final float frame) {
		final GuiPosition gp = getGuiPosition(pgp);
		for (final GuiCommon widget : this.widgets)
			widget.draw(ev, gp, p, frame);
	}

	@Override
	public void update(final GuiEvent ev, final GuiPosition pgp, final Point p) {
		final GuiPosition gp = getGuiPosition(pgp);
		if (gp.position.isVaild())
			for (final GuiCommon widget : this.widgets)
				widget.update(ev, gp, p);
	}

	@Override
	public void keyTyped(final GuiEvent ev, final GuiPosition pgp, final Point p, final char c, final int keycode) {
		final GuiPosition gp = getGuiPosition(pgp);
		if (gp.position.isVaild())
			for (final GuiCommon widget : this.widgets)
				widget.keyTyped(ev, gp, p, c, keycode);
	}

	@Override
	public void mouseScrolled(final GuiEvent ev, final GuiPosition pgp, final Point p, final int scroll) {
		final GuiPosition gp = getGuiPosition(pgp);
		if (gp.position.isVaild())
			for (final GuiCommon widget : this.widgets)
				widget.mouseScrolled(ev, gp, p, scroll);
	}

	@Override
	public void mouseMoved(final GuiEvent ev, final GuiPosition pgp, final Point p, final int button) {
		final GuiPosition gp = getGuiPosition(pgp);
		if (gp.position.isVaild())
			for (final GuiCommon widget : this.widgets)
				widget.mouseMoved(ev, gp, p, button);
	}

	@Override
	public void mouseClicked(final GuiEvent ev, final GuiPosition pgp, final Point p, final int button) {
		final GuiPosition gp = getGuiPosition(pgp);
		if (gp.position.isVaild())
			for (final GuiCommon widget : this.widgets)
				widget.mouseClicked(ev, gp, p, button);
	}

	@Override
	public void mouseDragged(final GuiEvent ev, final GuiPosition pgp, final Point p, final int button, final long time) {
		final GuiPosition gp = getGuiPosition(pgp);
		if (gp.position.isVaild())
			for (final GuiCommon widget : this.widgets)
				widget.mouseDragged(ev, gp, p, button, time);
	}

	@Override
	public void mouseReleased(final GuiEvent ev, final GuiPosition pgp, final Point p, final int button) {
		final GuiPosition gp = getGuiPosition(pgp);
		if (gp.position.isVaild())
			for (final GuiCommon widget : this.widgets)
				widget.mouseReleased(ev, gp, p, button);
	}
}
