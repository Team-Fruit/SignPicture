package com.kamesuta.mc.signpic.gui;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WList;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.MCoord;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.position.RArea;
import com.kamesuta.mc.signpic.http.Communicator;
import com.kamesuta.mc.signpic.render.RenderHelper;
import com.kamesuta.mc.signpic.state.Progress;
import com.kamesuta.mc.signpic.state.Progressable;

public class GuiTask extends WPanel {
	public GuiTask(final R position) {
		super(position);
	}

	@Override
	protected void initWidget() {
		add(new WPanel(new RArea()) {
			protected boolean show;

			protected MCoord right = MCoord.pright(-.95f);
			protected RArea area = new RArea(this.right);

			@Override
			public Area getGuiPosition(final Area pgp) {
				return super.getGuiPosition(pgp).child(this.area);
			}

			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity) {
				final Area a = getGuiPosition(pgp);
				RenderHelper.startShape();
				glColor4f(0f, 0f, 0f, .5f);
				drawRect(a);
				super.draw(ev, pgp, p, frame, opacity);
			}

			@Override
			public void update(final WEvent ev, final Area pgp, final Point p) {
				final Area a = getGuiPosition(pgp);
				if (a.pointInside(p)) {
					if (!this.show)
						this.right.add(Easings.easeOutCirc.move(.5f, 0f)).start();
					this.show = true;
				} else {
					if (this.show)
						this.right.add(Easings.easeOutCirc.move(.5f, -.95f)).start();
					this.show = false;
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
				Communicator.instance.getTasks().add(new Progressable() {
					@Override
					public Progress getProgress() {
						return new Progress().setOverall(100).setDone(50);
					}

					@Override
					public String getName() {
						return "com.kamesuta.mc.signpic.gui.GuiSignPicEditor";
					}
				});

				add(new WList<Progressable, TaskElement>(new RArea(), Communicator.instance.getTasks()) {
					@Override
					protected TaskElement createWidget(final Progressable t, final int i) {
						final MCoord top = MCoord.top(i*15);
						return new TaskElement(new RArea(top, Coord.height(10), Coord.left(0), right), top, t);
					}

					@Override
					protected void onMoved(final Progressable t, final TaskElement w, final int from, final int to) {
						w.top.stop().add(Easings.easeInCirc.move(.25f, to*15)).start();
					};
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

			class TaskElement extends WPanel {
				public final MCoord top;

				protected MCoord right = MCoord.pright(-1f).add(Easings.easeInOutCirc.move(.5f, 0f)).start();
				protected RArea area = new RArea(this.right);

				@Override
				public Area getGuiPosition(final Area pgp) {
					return super.getGuiPosition(pgp).child(this.area);
				}

				Progressable progressable;
				public TaskElement(final R position, final MCoord top, final Progressable progressable) {
					super(position);
					this.top = top;
					this.progressable = progressable;
				}

				@Override
				public boolean onCloseRequest() {
					this.right.stop().add(Easings.easeInCirc.move(.25f, -1f)).start();
					return false;
				}

				@Override
				public boolean onClosing(final WEvent ev, final Area pgp, final Point p) {
					return this.right.isFinished();
				}

				@Override
				protected void initWidget() {
					add(new WBase(new RArea(Coord.left(2f), Coord.top(2), Coord.height(font().FONT_HEIGHT/2), Coord.right(2))) {
						@Override
						public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity) {
							final Area a = getGuiPosition(pgp);
							glPushMatrix();
							glTranslatef(a.x1(), a.y1(), 0f);
							glScalef(.5f, .5f, .5f);
							final int contwidth = font().getStringWidth("...");
							final String name = TaskElement.this.progressable.getName();
							final int trimwidth = (int) (a.w()-contwidth)*2;
							String name2 = font().trimStringToWidth(name, trimwidth);
							if (trimwidth < font().getStringWidth(name))
								name2 += "...";
							drawString(name2, 0f, 0f, 0xffffff);
							glPopMatrix();
						}
					});
				}
			}
		});
	}
}
