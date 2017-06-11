package com.kamesuta.mc.signpic.gui;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.FontLabel;
import com.kamesuta.mc.bnnwidget.font.WFont;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.RenderOption;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.util.Sign.SendPacketTask;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;

public class GuiPAAS extends WFrame {
	private final @Nonnull SendPacketTask task;
	private boolean preview;

	public GuiPAAS(final @Nonnull SendPacketTask task) {
		this.task = task;
		setGuiPauseGame(false);
	}

	@Override
	protected void initWidget() {
		this.preview = CurrentMode.instance.isState(CurrentMode.State.PREVIEW);
		CurrentMode.instance.setState(CurrentMode.State.PREVIEW, false);

		add(new WPanel(new R()) {
			private final int max = StringUtils.length(GuiPAAS.this.task.id.id());
			private int cursor;
			private boolean close = true;
			private int c;

			@Override
			protected void initWidget() {
				add(new WBase(new R()) {
					VMotion c = V.pm(0f).add(Easings.easeLinear.move(.25f, .2f)).start();

					@Override
					public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
						WRenderer.startShape();
						OpenGL.glColor4f(0f, 0f, 0f, this.c.get());
						draw(getGuiPosition(pgp));
					}
				});

				final float f1 = 93.75F;
				add(new WBase(new R(Coord.right(15), Coord.top(15), Coord.width(f1), Coord.height(f1))) {
					@Override
					public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
						final Area a = getGuiPosition(pgp);

						WRenderer.startTexture();
						OpenGL.glColor4f(1f, 1f, 1f, 1f);
						OpenGL.glPushMatrix();
						OpenGL.glTranslatef(a.x1()+a.w()/2, a.y1(), 0f);
						drawSign(GuiPAAS.this.task.entity);
						OpenGL.glPopMatrix();
					}

					private void drawSign(final @Nonnull TileEntitySign sign) {
						OpenGL.glPushMatrix();
						OpenGL.glTranslatef(0.0F, 0.0F, 50.0F);
						final float f1 = 93.75F;
						OpenGL.glScalef(-f1, -f1, -f1);
						OpenGL.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
						final Block block = sign.getBlockType();

						if (block==Blocks.standing_sign) {
							final float f2 = sign.getBlockMetadata()*360/16.0F;
							OpenGL.glRotatef(f2, 0.0F, 1.0F, 0.0F);
							OpenGL.glTranslatef(0F, -.33590F, 0.0F);
						} else {
							final int k = sign.getBlockMetadata();
							float f3 = 0.0F;
							if (k==2)
								f3 = 180.0F;
							if (k==4)
								f3 = 90.0F;
							if (k==5)
								f3 = -90.0F;
							OpenGL.glRotatef(f3, 0.0F, 1.0F, 0.0F);
							OpenGL.glTranslatef(0.0F, -.02348F, 0.0F);
						}

						TileEntityRendererDispatcher.instance.renderTileEntityAt(sign, -0.5D, -0.75D, -0.5D, 0.0F);
						OpenGL.glPopMatrix();
					}
				});

				final int f = font().FONT_HEIGHT/2;

				add(new FontLabel(new R(Coord.left(15), Coord.right(15), Coord.top(-f), Coord.bottom(+f)), WFont.fontRenderer) {
					@Override
					public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
						setText(I18n.format("signpic.gui.paas.count", String.format("%d", c), String.format("%d", max)));
					}
				});

				add(new FontLabel(new R(Coord.left(15), Coord.right(15), Coord.top(+f), Coord.bottom(-f)), WFont.fontRenderer) {
					@Override
					public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
						setText(I18n.format("signpic.gui.paas.time", String.format("%.1f", GuiPAAS.this.task.timer.getTime()), String.format("%.1f", GuiPAAS.this.task.limit/1000f)));
					}
				});
			}

			@Override
			public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
				if (this.close)
					if (!GuiPAAS.this.task.tick()) {
						this.c = (int) (GuiPAAS.this.task.timer.getTime()*1000/GuiPAAS.this.task.limit*this.max);
						if (this.cursor!=this.c) {
							final EntryId id = EntryId.from(StringUtils.substring(GuiPAAS.this.task.id.id(), 0, this.c));
							final int last = id.getLastLine();
							id.toEntity(GuiPAAS.this.task.entity);
							GuiPAAS.this.task.entity.lineBeingEdited = last;
							final TileEntity e1 = Client.mc.theWorld.getTileEntity(GuiPAAS.this.task.entity.xCoord, GuiPAAS.this.task.entity.yCoord, GuiPAAS.this.task.entity.zCoord);
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
				super.update(ev, pgp, p);
			}
		});
	}

	@Override
	public void onGuiClosed() {
		CurrentMode.instance.setState(CurrentMode.State.PREVIEW, this.preview);
	}
}
