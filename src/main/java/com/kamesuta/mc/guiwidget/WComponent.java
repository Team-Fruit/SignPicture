package com.kamesuta.mc.guiwidget;

import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.Point;

public class WComponent extends WGui implements WCommon {
	@Override
	public void init(final WEvent ev, final Area pgp) {}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {}

	@Override
	public void update(final WEvent ev, final Area pgp, final Point p) {}

	@Override
	public void keyTyped(final WEvent ev, final Area pgp, final Point p, final char c, final int keycode) {}

	@Override
	public void mouseScrolled(final WEvent ev, final Area pgp, final Point p, final int scroll) {}

	@Override
	public void mouseMoved(final WEvent ev, final Area pgp, final Point p, final int button) {}

	@Override
	public void mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {}

	@Override
	public void mouseDragged(final WEvent ev, final Area pgp, final Point p, final int button, final long time) {}

	@Override
	public void mouseReleased(final WEvent ev, final Area pgp, final Point mouse, final int button) {}
}
