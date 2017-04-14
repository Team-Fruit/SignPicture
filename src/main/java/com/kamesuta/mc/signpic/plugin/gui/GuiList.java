package com.kamesuta.mc.signpic.plugin.gui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WList;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.Motion;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.RenderOption;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.bnnwidget.util.NotifyCollections.IModCount;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.gui.SignPicLabel;
import com.kamesuta.mc.signpic.plugin.SignData;

import net.minecraft.util.ResourceLocation;

public class GuiList extends WPanel implements Scrollable {
	protected static @Nonnull ResourceLocation mouseoverSound = new ResourceLocation("signpic", "gui.mouseover");

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

	protected @Nullable Area box;
	protected @Nullable Area list;

	@Override
	protected void initWidget() {
		add(this.scrollPane);
		add(new GuiScrollBar(new R(Coord.right(3), Coord.width(8)), this));
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity, @Nonnull final RenderOption opt) {
		this.box = pgp;
		this.list = getGuiPosition(pgp);
		super.draw(ev, pgp, p, frame, popacity, opt);
	}

	private float heightCache;

	@Override
	public void update(final WEvent ev, final Area pgp, final Point p) {
		final Area a = getGuiPosition(pgp);
		if (this.heightCache!=a.h()&&getAllHeight()-a.h()<getNowHeight())
			this.top.stop().add(Easings.easeLinear.move(.2f, -(getAllHeight()-a.h()))).start();
		this.heightCache = a.h();
		super.update(ev, pgp, p);
	}

	@Override
	public boolean mouseScrolled(final WEvent ev, final Area pgp, final Point p, final int scroll) {
		final Area box = GuiList.this.box;
		if (box!=null)
			if (box.pointInside(p))
				scroll(scroll, (GuiManager) ev.owner, getGuiPosition(pgp));
		return super.mouseScrolled(ev, pgp, p, scroll);
	}

	@Override
	public void scroll(final float scroll, final @Nullable GuiManager manager, final @Nullable Area position) {
		final float now = this.top.get();
		float to = now+scroll/2f;
		if (position!=null)
			if (to>0||-to>(getAllHeight()-position.h()))
				to = now+scroll/4f;
		scrollTo(to, manager, position);
	}

	@Override
	public void scrollTo(final float to, final @Nullable GuiManager manager, final @Nullable Area position) {
		if (manager!=null&&position!=null) {
			final float buttom = getAllHeight()-position.h();
			if (this.top.get()<=0&&-this.top.get()<=buttom) {
				final VMotion motion = this.top.stop().add(Easings.easeLinear.move(.2f, to));
				if (to>0)
					motion.add(Easings.easeInOutCubic.move(.5f, 0));
				else if (-to>buttom)
					motion.add(Easings.easeInOutCubic.move(.5f, -buttom));
				motion.start();
			}
			if (-to>buttom) {
				final int size = this.data.size();
				manager.get(size, size+100);
			}
		} else
			this.top.stop().add(Motion.move(to)).start();
	}

	@Override
	public float getNowHeight() {
		return -this.top.get();
	};

	@Override
	public float getAllHeight() {
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
						public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
							final Area a = getGuiPosition(pgp);
							final Area list = GuiList.this.list;
							if (list!=null) {
								final Area t = list.trimArea(a);
								WRenderer.startShape();
								OpenGL.glColor4f(0f, 0f, 0f, .5f);
								draw(t);
								if (t!=null)
									opt.put("trim", Area.abs(0f, 1f/a.h()*(t.y1()-a.y1()), 1f, 1f/a.h()*(a.h()-(a.y2()-t.y2()))));
								super.draw(ev, pgp, p, frame, popacity, opt);
							}
						};
					}.setEntryId(EntryId.from(ListElement.this.data.getSign())));
				}

				@Override
				public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
					final Area list = GuiList.this.list;
					if (list!=null)
						if (list.areaOverlap(getGuiPosition(pgp))) {
							WRenderer.startShape();
							OpenGL.glColor4f(.3f, .3f, .3f, .5f);
							draw(list.trimArea(getGuiPosition(pgp)));
							super.draw(ev, pgp, p, frame, popacity, opt);
						}
				}

				boolean playsound = false;

				@Override
				public void update(final WEvent ev, final Area pgp, final Point p) {
					final Area list = GuiList.this.list;
					if (list!=null)
						if (list.areaOverlap(getGuiPosition(pgp))) {
							final boolean mouseover = getGuiPosition(pgp).pointInside(p);
							if (!mouseover)
								this.playsound = false;
							if (!this.playsound&&mouseover) {
								Client.playSound(mouseoverSound, 1f);
								this.playsound = true;
							}
							super.update(ev, pgp, p);
						}
				}
			});
		}
	}
}
