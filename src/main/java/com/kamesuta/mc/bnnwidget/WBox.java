package com.kamesuta.mc.bnnwidget;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;

public class WBox extends WPanel {
	protected WCommon addtask;

	public WBox(final R position) {
		super(position);
	}

	@Override
	public boolean add(final WCommon widget) {
		this.addtask = widget;
		removeAll();
		return true;
	}

	protected void removeAll() {
		for (final WCommon c : getContainer())
			remove(c);
	}

	@Override
	public void update(final WEvent ev, final Area pgp, final Point p) {
		if (getContainer().size()<=0)
			if (this.addtask!=null) {
				super.add(this.addtask);
				this.addtask = null;
			}
	}
}
