package com.kamesuta.mc.bnnwidget;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;

public class WComponent extends WGui implements WCommon {
	@Override
	public void onAdded() {
	}

	@Override
	public void onInit(final WEvent ev, final Area pgp, final Point p) {
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
	}

	@Override
	public void update(final WEvent ev, final Area pgp, final Point p) {
	}

	@Override
	public boolean keyTyped(final WEvent ev, final Area pgp, final Point p, final char c, final int keycode) {
		return false;
	}

	@Override
	public boolean mouseScrolled(final WEvent ev, final Area pgp, final Point p, final int scroll) {
		return false;
	}

	@Override
	public boolean mouseMoved(final WEvent ev, final Area pgp, final Point p, final int button) {
		return false;
	}

	@Override
	public boolean mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
		return false;
	}

	@Override
	public boolean mouseDragged(final WEvent ev, final Area pgp, final Point p, final int button, final long time) {
		return false;
	}

	@Override
	public boolean mouseReleased(final WEvent ev, final Area pgp, final Point mouse, final int button) {
		return false;
	}

	@Override
	public boolean onCloseRequest() {
		return true;
	}

	@Override
	public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
		return true;
	}

	@Override
	public WCommon top(final WEvent ev, final Area pgp, final Point point) {
		return null;
	}
}
