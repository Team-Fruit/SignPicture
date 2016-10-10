package com.kamesuta.mc.signpic.gui;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MList;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.MCoord;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.position.RArea;
import com.kamesuta.mc.signpic.http.Communicator;
import com.kamesuta.mc.signpic.render.RenderHelper;
import com.kamesuta.mc.signpic.state.Progressable;

public class GuiTask extends WPanel {
	protected boolean show;
	protected MCoord right = MCoord.pright(-.95f);

	public GuiTask(final R position) {
		super(position);
	}

	@Override
	protected void initWidget() {
		this.right.start();
		add(new WPanel(new RArea(this.right, Coord.pwidth(1f), Coord.top(0f), Coord.bottom(0f))) {
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
				final Area a = getGuiPosition(pgp);
				RenderHelper.startShape();
				glColor4f(0f, 0f, 0f, .5f);
				drawRect(a);
				super.draw(ev, pgp, p, frame);
			}

			@Override
			public void update(final WEvent ev, final Area pgp, final Point p) {
				final Area a = getGuiPosition(pgp);
				if (a.pointInside(p)) {
					if (!GuiTask.this.show)
						GuiTask.this.right.add(Easings.easeOutCirc.move(.5f, 0f));
					GuiTask.this.show = true;
				} else {
					if (GuiTask.this.show)
						GuiTask.this.right.add(Easings.easeOutCirc.move(.5f, -.95f));
					GuiTask.this.show = false;
				}
				super.update(ev, pgp, p);
			}

			@Override
			public boolean mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				final Area a = getGuiPosition(pgp);
				return a.pointInside(p);
			}

			@Override
			protected void initWidget() {
				add(new MList<Progressable, TaskElement>(new RArea(Coord.left(0), Coord.right(0), Coord.top(0), Coord.bottom(0)), Communicator.instance.getTasks()) {
					@Override
					protected TaskElement createWidget(final Progressable t, final int i) {
						final MCoord top = MCoord.top(i*15);
						return new TaskElement(new RArea(top, Coord.height(10), Coord.left(0), Coord.right(0)), top, t);
					}

					@Override
					protected void onMoved(final Progressable t, final TaskElement w, final int from, final int to) {
						w.top.stop().add(Easings.easeInCirc.move(.5f, to*15)).start();
					};
				});
			}

			class TaskElement extends WPanel {
				public final MCoord top;
				Progressable progressable;
				public TaskElement(final R position, final MCoord top, final Progressable progressable) {
					super(position);
					this.top = top;
					this.progressable = progressable;
				}

				@Override
				public void onAdded() {
					this.top.start();
				}

				@Override
				public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
					final Area a = getGuiPosition(pgp);
					drawStringC(this.progressable.getName(), a.x1()+a.w()/2, a.y1()+a.h()/2, 0xffffff);
				}
			}
		});
	}

	@Override
	public boolean onCloseRequest() {
		this.right.add(Easings.easeOutCirc.move(.5f, -.95f));
		return false;
	}

	@Override
	public boolean onClosing(final WEvent ev, final Area pgp, final Point p) {
		return this.right.isFinished();
	}
}
