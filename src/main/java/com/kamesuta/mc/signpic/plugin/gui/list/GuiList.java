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
import com.kamesuta.mc.bnnwidget.util.NotifyCollections.IModCount;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.gui.SignPicLabel;
import com.kamesuta.mc.signpic.plugin.SignData;

public class GuiList extends WPanel {

	protected final @Nonnull IModCount<SignData> data;
	protected final @Nonnull WPanel scrollPane;
	protected final @Nonnull VMotion top;

	public GuiList(final R position, final IModCount<SignData> data) {
		super(position);
		this.data = data;
		this.scrollPane = new WPanel(new R(Coord.left(0), Coord.right(15), Coord.top(this.top = V.am(0)))) {
			@Override
			protected void initWidget() {
				add(new WList<SignData, ListElement>(new R(), data) {
					@Override
					protected ListElement createWidget(final SignData t, final int i) {
						return new ListElement(new R(Coord.top(i*30), Coord.height(30)), t);
					}
				});
			}
		};
	}

	@Nullable
	Area box;
	@Nullable
	Area list;

	@Override
	protected void initWidget() {
		add(this.scrollPane);
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
		this.box = pgp;
		this.list = getGuiPosition(pgp);
		super.draw(ev, pgp, p, frame, popacity);
	}

	@Override
	public boolean mouseScrolled(final WEvent ev, final Area pgp, final Point p, final int scroll) {
		scroll(scroll, getGuiPosition(pgp));
		return super.mouseScrolled(ev, pgp, p, scroll);
	}

	public void scroll(final int scroll, final Area position) {
		final float now = this.top.get();
		float to = now+scroll/2f;
		if (to>0||-to>(getElemetsHeight()-position.h()))
			to = now+scroll/4f;
		scrollTo(to, position);
	}

	public void scrollTo(final float to, final Area position) {
		final float buttom = getElemetsHeight()-position.h();
		if (this.top.get()<=0&&-this.top.get()<=buttom) {
			final VMotion motion = this.top.stop().add(Easings.easeLinear.move(.2f, to));
			if (to>0)
				motion.add(Easings.easeInOutCubic.move(.5f, 0));
			else if (-to>buttom)
				motion.add(Easings.easeInOutCubic.move(.5f, -buttom));
			motion.start();
		}
	}

	public float getElemetsHeight() {
		return this.data.size()*30;
	}

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
		public void update(final WEvent ev, final Area pgp, final Point p) {
			final Area box = GuiList.this.box;
			if (box!=null)
				if (box.areaOverlap(getGuiPosition(pgp)))
					super.update(ev, pgp, p);
		}

		@Override
		public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
			final Area box = GuiList.this.box;
			if (box!=null)
				if (box.areaOverlap(getGuiPosition(pgp)))
					super.draw(ev, pgp, p, frame, popacity);
		}
	}
}
