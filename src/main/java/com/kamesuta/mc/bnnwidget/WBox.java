package com.kamesuta.mc.bnnwidget;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;

public class WBox extends WPanel {
	protected @Nullable WCommon addtask;

	public WBox(final @Nonnull R position) {
		super(position);
	}

	@Override
	public boolean add(final @Nonnull WCommon widget) {
		set(widget);
		return true;
	}

	public void set(final @Nullable WCommon widget) {
		this.addtask = widget;
		removeAll();
	}

	public void reset() {
		set(null);
	}

	private void removeAll() {
		for (final Iterator<WCommon> itr = getContainer().iterator(); itr.hasNext();) {
			final WCommon widget = itr.next();
			if (widget.onCloseRequest())
				itr.remove();
			else
				this.removelist.offer(widget);
		}
	}

	@Override
	public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
		super.update(ev, pgp, p);
		if (getContainer().size()<=0)
			if (this.addtask!=null) {
				super.add(this.addtask);
				this.addtask = null;
			}
	}
}
