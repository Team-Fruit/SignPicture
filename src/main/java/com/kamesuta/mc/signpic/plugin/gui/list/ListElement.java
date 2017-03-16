package com.kamesuta.mc.signpic.plugin.gui.list;

import javax.annotation.Nonnull;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.gui.SignPicLabel;
import com.kamesuta.mc.signpic.plugin.SignData;

public class ListElement extends WPanel {

	protected final @Nonnull SignData data;

	public ListElement(final R position, final SignData t) {
		super(position);
		this.data = t;
	}

	@Override
	protected void initWidget() {
		add(new WPanel(new R(Coord.top(.5f), Coord.bottom(.5f))) {
			@Override
			protected void initWidget() {
				add(new SignPicLabel(new R(Coord.left(0), Coord.width(38.6f)), ContentManager.instance) {
					@Override
					public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
						WRenderer.startShape();
						OpenGL.glColor4f(0f, 0f, 0f, .5f);
						draw(getGuiPosition(pgp));
						super.draw(ev, pgp, p, frame, popacity);
					};
				}.setEntryId(EntryId.from(ListElement.this.data.getSign())));
			}

			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
				WRenderer.startShape();
				OpenGL.glColor4f(.3f, .3f, .3f, .5f);
				draw(getGuiPosition(pgp));
				super.draw(ev, pgp, p, frame, popacity);
			}
		});
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
		if (pgp.areaOverlap(getGuiPosition(pgp)))
			super.draw(ev, pgp, p, frame, popacity);
	}
}
