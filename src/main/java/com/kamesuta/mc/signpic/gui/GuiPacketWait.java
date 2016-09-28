package com.kamesuta.mc.signpic.gui;

import static org.lwjgl.opengl.GL11.*;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MLabel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.MCoord;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.RArea;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.render.RenderHelper;
import com.kamesuta.mc.signpic.util.Sign.SendPacketTask;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;

public class GuiPacketWait extends WFrame {
	private final SendPacketTask task;
	private boolean preview;

	public GuiPacketWait(final SendPacketTask task) {
		this.task = task;
	}

	@Override
	protected void init() {
		this.preview = CurrentMode.instance.isState(CurrentMode.State.PREVIEW);
		CurrentMode.instance.setState(CurrentMode.State.PREVIEW, false);

		add(new WPanel(RArea.diff(0, 0, 0, 0)) {
			private final int max = StringUtils.length(GuiPacketWait.this.task.id.id());
			private int cursor;
			private boolean close = true;
			private int c;

			@Override
			protected void initWidget() {
				add(new WBase(RArea.diff(0, 0, 0, 0)) {
					MCoord c = new MCoord(0f).add(Easings.easeLinear.move(.25f, .2f)).start();

					@Override
					public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
						RenderHelper.startShape();
						glColor4f(0f, 0f, 0f, this.c.get());
						drawRect(getGuiPosition(pgp));
					}
				});

				final float f1 = 93.75F;
				add(new WBase(new RArea(Coord.right(15), Coord.top(15), Coord.width(f1), Coord.height(f1))) {
					@Override
					public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
						final Area a = getGuiPosition(pgp);

						RenderHelper.startTexture();
						glColor4f(1f, 1f, 1f, 1f);
						glPushMatrix();
						glTranslatef(a.x1()+a.w()/2, a.y1(), 50f);
						glScalef(-f1, -f1, -f1);
						glRotatef(180f, 0f, 1f, 0f);
						Client.renderer.translateBase(GuiPacketWait.this.task.entity, -0.5D, -0.75D, -0.5D, -1f);
						Client.renderer.renderSignPictureBase(GuiPacketWait.this.task.entity, -0.5D, -0.75D, -0.5D, 0.0F, 1f);
						glPopMatrix();
					}
				});

				final int f = font().FONT_HEIGHT/2;

				add(new MLabel(new RArea(Coord.left(15), Coord.right(15), Coord.top(-f), Coord.bottom(+f)), "") {
					@Override
					public String getText() {
						return String.format("Please Wait ( %d / %d letters )", c, max);
					}
				});

				add(new MLabel(new RArea(Coord.left(15), Coord.right(15), Coord.top(+f), Coord.bottom(-f)), "") {
					@Override
					public String getText() {
						return String.format("Esc for Cancel ( %.2f / %.2f seconds )", GuiPacketWait.this.task.timer.getTime(), GuiPacketWait.this.task.limit / 1000f);
					}
				});
			}

			@Override
			public void update(final WEvent ev, final Area pgp, final Point p) {
				if (this.close) {
					if (!GuiPacketWait.this.task.tick()) {
						this.c = (int) (GuiPacketWait.this.task.timer.getTime() * 1000 / GuiPacketWait.this.task.limit * this.max);
						if (this.cursor != this.c) {
							final EntryId id = new EntryId(StringUtils.substring(GuiPacketWait.this.task.id.id(), 0, this.c));
							final int last = id.getLastLine();
							id.toEntity(GuiPacketWait.this.task.entity);
							GuiPacketWait.this.task.entity.lineBeingEdited = last;
							final TileEntity e1 = Client.mc.theWorld.getTileEntity(GuiPacketWait.this.task.entity.xCoord, GuiPacketWait.this.task.entity.yCoord, GuiPacketWait.this.task.entity.zCoord);
							if (e1 instanceof TileEntitySign) {
								final TileEntitySign tileSign = (TileEntitySign) e1;
								id.toEntity(tileSign);
								tileSign.lineBeingEdited = last;
							}
						}
						this.cursor = this.c;
					} else {
						this.close = false;
						requestClose();
					}
				}
				super.update(ev, pgp, p);
			}
		});
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void onGuiClosed() {
		CurrentMode.instance.setState(CurrentMode.State.PREVIEW, this.preview);
	}
}
