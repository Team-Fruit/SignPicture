package com.kamesuta.mc.bnnwidget.component;

import com.kamesuta.mc.bnnwidget.WCommon;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.render.RenderHelper;

import net.minecraft.client.renderer.GlStateManager;

public class MSelectLabel extends MSelect {

	protected MLabel field;

	public MSelectLabel(final R position, final float buttonwidth) {
		super(position, buttonwidth);
	}

	@Override
	protected WCommon getField() {
		return this.field = new MLabel(new R(Coord.left(this.buttonwidth), Coord.right(this.buttonwidth), Coord.top(0), Coord.bottom(0)), "") {
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
				final Area a = getGuiPosition(pgp);
				RenderHelper.startShape();
				GlStateManager.color(0f, 0f, 0f, .4f);
				drawRect(a);
				super.draw(ev, pgp, p, frame, popacity);
			}

			@Override
			protected void onTextChanged(final String oldText) {
				onChanged(oldText, getText());
			}
		};
	}

	@Override
	public MSelect setText(final String text) {
		this.field.setText(text);
		return this;
	}
}
