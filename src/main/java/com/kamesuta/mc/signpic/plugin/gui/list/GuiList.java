package com.kamesuta.mc.signpic.plugin.gui.list;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WList;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.gui.SignPicLabel;
import com.kamesuta.mc.signpic.plugin.SignData;
import com.kamesuta.mc.signpic.plugin.gui.GuiManager;

public class GuiList extends WPanel {

	protected final @Nonnull GuiManager manager;
	protected final @Nonnull WPanel scrollPane;
	protected final @Nonnull VMotion top;

	public GuiList(final R position, final GuiManager manager) {
		super(position);
		this.manager = manager;
		this.scrollPane = new WPanel(new R(Coord.left(0), Coord.right(15), Coord.top(this.top = V.am(0)))) {
			@Override
			protected void initWidget() {
				add(new WList<SignData, ListElement>(new R(), GuiList.this.manager.data) {
					@Override
					protected ListElement createWidget(final SignData t, final int i) {
						return new ListElement(new R(Coord.top(i*30), Coord.height(30)), t);
					}
				});
			}
		};
	}

	@Override
	protected void initWidget() {
		add(this.scrollPane);
	}

	@Override
	public boolean mouseScrolled(final WEvent ev, final Area pgp, final Point p, final int scroll) {
		scroll(scroll);
		return super.mouseScrolled(ev, pgp, p, scroll);
	}

	public void scroll(final int scroll) {
		final float moveto = this.top.get()+(scroll/1.5f);
		this.top.stop().add(Easings.easeLinear.move(.25f, moveto)).start();
	}

	public class ListElement extends WPanel {

		protected final @Nonnull SignData data;

		public ListElement(final R position, final SignData t) {
			super(position);
			this.data = t;
		}

		@Nullable
		Area list;
		@Nullable
		Area element;

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
			this.list = pgp;
			this.element = getGuiPosition(pgp);
			if (pgp.areaOverlap(this.element))
				super.draw(ev, pgp, p, frame, popacity);
		}
	}
}
