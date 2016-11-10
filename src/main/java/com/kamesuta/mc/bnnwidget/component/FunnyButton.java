package com.kamesuta.mc.bnnwidget.component;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.MCoord;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;

import net.minecraft.client.renderer.GlStateManager;

public class FunnyButton extends MButton {
	public FunnyButton(final R position, final String text) {
		super(position, text);
	}

	private boolean isHighlight = true;
	private boolean highlighted;
	MCoord m = new MCoord(0);
	MCoord s = new MCoord(1);

	public boolean isHighlight() {
		return this.isHighlight;
	}

	public void setHighlight(final boolean b) {
		this.isHighlight = b;
	}

	@Override
	public void update(final WEvent ev, final Area pgp, final Point p) {
		if (isHighlight()) {
			if (!this.highlighted) {
				this.highlighted = true;
				this.m.stop().add(Easings.easeOutElastic.move(.5f, 6f)).start();
				this.s.stop().add(Easings.easeOutElastic.move(.5f, 1.1f)).start();
			}
		} else if (this.highlighted) {
			this.highlighted = false;
				this.m.stop().add(Easings.easeOutElastic.move(.5f, 0f)).start();
				this.s.stop().add(Easings.easeOutElastic.move(.5f, 1f)).start();
			}
		}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity) {
		final Area a = getGuiPosition(pgp);
		GlStateManager.pushMatrix();
		GlStateManager.translate(a.x1()+a.w()/2, a.y1()+a.h()/2, 0);
		final float c = this.s.get();
		GlStateManager.scale(c, c, 1f);
		GlStateManager.rotate(this.m.get(), 0, 0, 1);
		GlStateManager.translate(-a.x1()-a.w()/2, -a.y1()-a.h()/2, 0);
		super.draw(ev, pgp, p, frame, opacity);
		GlStateManager.popMatrix();
	}
}