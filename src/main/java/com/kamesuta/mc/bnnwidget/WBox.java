package com.kamesuta.mc.bnnwidget;

import java.util.Iterator;

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
		for (final Iterator<WCommon> itr = getContainer().iterator(); itr.hasNext();) {
			final WCommon widget = itr.next();
			if (widget.onCloseRequest())
				itr.remove();
			else
				this.removelist.offer(widget);
		}
	}

	@Override
	public void update(final WEvent ev, final Area pgp, final Point p) {
		super.update(ev, pgp, p);
		if (getContainer().size()<=0)
			if (this.addtask!=null) {
				super.add(this.addtask);
				this.addtask = null;
			}
	}
}
