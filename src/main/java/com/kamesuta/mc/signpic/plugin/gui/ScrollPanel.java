package com.kamesuta.mc.signpic.plugin.gui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.Motion;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.RenderOption;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;

public abstract class ScrollPanel extends WPanel implements Scrollable {
	protected final @Nonnull VMotion top = V.am(0);

	public ScrollPanel(final R position) {
		super(position);
	}

	protected @Nullable Area area;

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity, final RenderOption opt) {
		this.area = getGuiPosition(pgp);
		super.draw(ev, pgp, p, frame, popacity, opt);
	}

	private float heightCache;

	@Override
	public void update(final WEvent ev, final Area pgp, final Point p) {
		final Area a = getGuiPosition(pgp);
		if (this.heightCache!=a.h()&&getScrollableHeight()<getNowHeight())
			this.top.stop().add(Easings.easeLinear.move(.2f, -getScrollableHeight())).start();
		this.heightCache = a.h();
		super.update(ev, pgp, p);
	}

	@Override
	public boolean mouseScrolled(final WEvent ev, final Area pgp, final Point p, final int scroll) {
		scroll(scroll, (GuiManager) ev.owner, getGuiPosition(pgp));
		return super.mouseScrolled(ev, pgp, p, scroll);
	}

	@Override
	public void scroll(final float scroll, final @Nullable GuiManager manager, final @Nullable Area position) {
		final float now = this.top.get();
		float to = now+scroll/2f;
		if (position!=null)
			if (to>0||-to>(getScrollableHeight()))
				to = now+scroll/4f;
		scrollTo(to, manager, position);
	}

	@Override
	public void scrollTo(final float to, final @Nullable GuiManager manager, final @Nullable Area position) {
		if (manager!=null&&position!=null) {
			final float buttom = getScrollableHeight();
			if (this.top.get()<=0&&-this.top.get()<=buttom) {
				final VMotion motion = this.top.stop().add(Easings.easeLinear.move(.2f, to));
				if (to>0)
					motion.add(Easings.easeInOutCubic.move(.5f, 0));
				else if (-to>buttom)
					motion.add(Easings.easeInOutCubic.move(.5f, -buttom));
				motion.start();
			}
		} else
			this.top.stop().add(Motion.move(to)).start();
	}

	@Override
	public float getNowHeight() {
		return -this.top.get();
	};

	@Override
	public float getScrollableHeight() {
		float height = getAllHeight();
		if (this.area!=null)
			height -= this.area.h();
		return height;
	}

	@Override
	public abstract float getAllHeight();
}
