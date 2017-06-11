package com.kamesuta.mc.signpic.gui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.util.Timer;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WList;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.font.WFont;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.Motion;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.RenderOption;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VCommon;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.ILoadCancelable;
import com.kamesuta.mc.signpic.http.Communicator;
import com.kamesuta.mc.signpic.state.Progress;
import com.kamesuta.mc.signpic.state.Progressable;
import com.kamesuta.mc.signpic.state.State;

import net.minecraft.util.ResourceLocation;

public class GuiTask extends WPanel {
	public static final @Nonnull ResourceLocation panel = new ResourceLocation("signpic", "textures/gui/panel.png");
	public static final @Nonnull String ShowPanel = "gui.showpanel";
	public static final @Nonnull String HighlightPanel = "gui.highlight";

	public GuiTask(final @Nonnull R position) {
		super(position);
	}

	protected @Nonnull Timer showtime = new Timer();

	public void show(final float j) {
		this.showtime.set(-j);
	}

	protected boolean oshow;
	protected @Nonnull VMotion oright = V.am(0f);

	protected boolean show;
	protected @Nonnull VMotion right = V.pm(0f);

	@Override
	protected void initWidget() {
		add(new WPanel(new R()) {
			@Override
			protected void initWidget() {
				add(new WPanel(new R(Coord.right(V.per(V.combine(V.p(-1), GuiTask.this.oright), V.p(0f), GuiTask.this.right)))) {

					@Override
					public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float opacity, final @Nonnull RenderOption opt) {
						final Area a = getGuiPosition(pgp);
						WRenderer.startShape();
						OpenGL.glColor4f(0f, 0f, 0f, .6f);
						draw(a);
						super.draw(ev, pgp, p, frame, opacity, opt);
					}

					@Override
					public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
						final Area a = getGuiPosition(pgp);
						if (a.pointInside(p))
							GuiTask.this.showtime.set(-1f);
						final boolean b = GuiTask.this.showtime.getTime()<0f;
						if (b) {
							if (!GuiTask.this.show) {
								GuiTask.this.right.stop().add(Easings.easeOutQuart.move(.7f, 1f)).start();
								Client.playSound(new ResourceLocation("signpic", "gui.show"), 1.0F);
							}
							GuiTask.this.show = true;
						} else {
							if (GuiTask.this.show)
								GuiTask.this.right.stop().add(Easings.easeOutQuart.move(.7f, 0f)).start();
							GuiTask.this.show = false;
						}
						if (Client.mc.currentScreen!=null&&!Communicator.instance.getTasks().isEmpty()) {
							if (!GuiTask.this.oshow)
								GuiTask.this.oright.stop().add(Easings.easeOutQuart.move(.5f, 2f)).start();
							GuiTask.this.oshow = true;
						} else {
							if (GuiTask.this.oshow)
								GuiTask.this.oright.stop().add(Easings.easeOutQuart.move(.5f, 0f)).start();
							GuiTask.this.oshow = false;
						}
						super.update(ev, pgp, p);
					}

					@Override
					public boolean mouseClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
						final Area a = getGuiPosition(pgp);
						return super.mouseClicked(ev, pgp, p, button)||a.pointInside(p);
					}

					@Override
					protected void initWidget() {
						add(new WBase(new R(Coord.top(1), Coord.left(1), Coord.width(80), Coord.height(16))) {
							@Override
							public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float opacity, final @Nonnull RenderOption opt) {
								final Area a = getGuiPosition(pgp);
								texture().bindTexture(panel);
								OpenGL.glColor4f(1, 1, 1, 1);
								WRenderer.startTexture();
								drawTexture(a, null, null);
							}
						});

						add(new WList<Progressable, TaskElement>(new R(Coord.top(16), Coord.bottom(0)), Communicator.instance.getTasks()) {
							@Override
							protected @Nonnull TaskElement createWidget(final @Nonnull Progressable t, final int i) {
								final Object j = t.getState().getMeta().get(ShowPanel);
								if (j instanceof Number)
									show(((Number) j).floatValue());
								final VMotion top = V.am(i*15);
								return new TaskElement(new R(Coord.top(top), Coord.height(15)), top, t);
							}

							@Override
							protected void onMoved(final @Nonnull Progressable t, final @Nonnull TaskElement w, final int from, final int to) {
								w.top.stop().add(Motion.blank(.75f)).add(Easings.easeInCirc.move(.25f, to*15)).start();
							};
						});
					}
				});
			}
		});
	}

	class TaskElement extends WPanel {
		public final @Nonnull VMotion top;

		protected @Nullable VMotion right;

		protected @Nullable VMotion opacity;

		@Override
		protected @Nonnull R initPosition(final @Nonnull R position) {
			return position.child(Coord.right(this.right = V.pm(-1f).add(Easings.easeOutQuart.move(.5f, 0f)).start()));
		}

		@Override
		protected @Nonnull VCommon initOpacity() {
			return this.opacity = V.pm(1f);
		}

		private @Nonnull final State state;
		private @Nonnull final Progress progress;
		private @Nullable ILoadCancelable cancelable;

		public TaskElement(final @Nonnull R position, final @Nonnull VMotion top, final @Nonnull Progressable progressable) {
			super(position);
			this.top = top;
			this.state = progressable.getState();
			this.progress = progressable.getState().getProgress();
			if (progressable instanceof ILoadCancelable)
				this.cancelable = (ILoadCancelable) progressable;
		}

		@Override
		public boolean onCloseRequest() {
			if (!GuiTask.this.show&&this.state.getMeta().get(HighlightPanel)!=null)
				if (this.right!=null)
					this.right.stop().add(Easings.easeOutQuart.move(2f, 1f)).start();
			if (this.opacity!=null)
				this.opacity.stop().add(Easings.easeLinear.move(1f, 0f)).start();
			return false;
		}

		@Override
		public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
			if (this.opacity!=null)
				return this.opacity.isFinished();
			return true;
		}

		@Override
		public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
			final Area a = getGuiPosition(pgp);
			final Area b = Area.abs(pgp.x1(), a.y1(), pgp.x2(), a.y2());
			if (pgp.areaInside(b))
				super.draw(ev, pgp, p, frame, popacity, opt);
		}

		@Override
		public boolean mouseClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
			final Area a = getGuiPosition(pgp);
			if (a.pointInside(p)) {
				if (this.cancelable!=null)
					this.cancelable.cancel();
				return true;
			}
			return false;
		}

		@Override
		protected void initWidget() {
			add(new WPanel(new R(Coord.top(1f), Coord.left(1f), Coord.bottom(0f), Coord.right(0f))) {
				@Override
				protected void initWidget() {
					add(new WBase(new R(Coord.left(5f), Coord.top(2), Coord.height(font().FONT_HEIGHT/2), Coord.right(2))) {
						@Override
						public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
							final Area a = getGuiPosition(pgp);
							OpenGL.glPushMatrix();
							final String cont = "...";
							final int contwidth = font().getStringWidth(cont);
							final String name = TaskElement.this.state.getName();
							final int namewidth = font().getStringWidth(name);
							String res;
							final float prefwidth = a.w()*2;
							if (namewidth<prefwidth)
								res = name;
							else
								res = font().trimStringToWidth(name, (int) (prefwidth-contwidth))+cont;
							// WRenderer.startShape();
							// OpenGL.glColor4f(1f, 1f, 1f, 1f);
							// draw(a, GL11.GL_LINE_LOOP);
							WRenderer.startTexture();
							final float opacity = getGuiOpacity(popacity);
							OpenGL.glColor4f(4f, 4f, 4f, opacity);
							OpenGL.glColor4f(1f, 1f, 1f, Math.max(.05f, opacity*1f));
							WFont.fontRenderer.drawString(res, a.scaleSize(1f).translate(-1f, -1f), ev.owner.guiScale(), Align.LEFT, false);
							OpenGL.glPopMatrix();
						}
					});

					add(new WPanel(new R(Coord.left(4f), Coord.top(font().FONT_HEIGHT/2+3.8f), Coord.bottom(1.8f), Coord.right(2))) {
						protected @Nonnull VMotion progresscoord = V.pm(0f);

						@Override
						protected void initWidget() {
							add(new WBase(new R(Coord.pleft(0f), Coord.left(this.progresscoord))) {
								@Override
								public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
									final Area a = getGuiPosition(pgp);
									OpenGL.glColor4f(0f, 78f/256f, 192f/256f, getGuiOpacity(popacity)*1f);
									WRenderer.startShape();
									draw(a);

									OpenGL.glPushMatrix();
									final String prog = String.format("%.1f%%", TaskElement.this.progress.getProgress()*100);
									final int progwidth = font().getStringWidth(prog);
									final float maxx = pgp.x2()*2-progwidth;
									OpenGL.glColor4f(1f, 1f, 1f, getGuiOpacity(popacity)*1f);
									WRenderer.startTexture();
									final float opacity = getGuiOpacity(popacity);
									OpenGL.glColor4f(1f, 1f, 1f, opacity);
									WFont.fontRenderer.drawString(prog, Area.size(Math.min(a.x2()+1, maxx/2-1)-4f, a.y1()-.5f, 100f, 3f), ev.owner.guiScale(), Align.LEFT, true);
									OpenGL.glPopMatrix();

									super.draw(ev, pgp, p, frame, popacity, opt);
								}
							});
						}

						@Override
						public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
							this.progresscoord.stop().add(Easings.easeOutQuart.move(.1f, TaskElement.this.progress.getProgress())).start();
						}

						@Override
						public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float opacity, final @Nonnull RenderOption opt) {
							final Area a = getGuiPosition(pgp);
							WRenderer.startShape();
							OpenGL.glColor4f(0f, 0f, 0f, getGuiOpacity(opacity)*0.8f);
							draw(a);
							super.draw(ev, pgp, p, frame, opacity, opt);
						}
					});
				}

				@Override
				public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float opacity, final @Nonnull RenderOption opt) {
					final Area a = getGuiPosition(pgp);
					WRenderer.startShape();
					if (a.pointInside(p))
						OpenGL.glColor4f(.75f, .75f, .75f, getGuiOpacity(opacity)*.125f);
					else
						OpenGL.glColor4f(.5f, .5f, .5f, getGuiOpacity(opacity)*.125f);
					draw(a);
					super.draw(ev, pgp, p, frame, opacity, opt);
				}
			});
		}
	}
}
