package com.kamesuta.mc.signpic.gui;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nonnull;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MScaledLabel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.Motion;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.RenderOption;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.bnnwidget.render.WRenderer.WVertex;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VCommon;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.attr.AttrReaders;
import com.kamesuta.mc.signpic.attr.prop.OffsetData;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationGL;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId.PreviewEntryId;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.information.Informations;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.render.StateRender;
import com.kamesuta.mc.signpic.state.StateType;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class GuiImage extends WFrame {
	protected @Nonnull Entry entry;

	public static final @Nonnull ResourceLocation resError = new ResourceLocation("signpic", "textures/state/error.png");

	public GuiImage(final @Nonnull Entry entry) {
		this.entry = entry;
		setWorldAndResolution(Client.mc, 0, 0);
	}

	@Override
	protected void initWidget() {
		add(new WPanel(new R()) {
			@Override
			protected void initWidget() {
				add(new WBase(new R()) {
					@Override
					public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, @Nonnull final RenderOption opt) {
						final Area a = getGuiPosition(pgp);
						float opacity = getGuiOpacity(popacity);
						final Content content = GuiImage.this.entry.getContent();
						final AttrReaders meta = GuiImage.this.entry.getMeta();

						OpenGL.glPushMatrix();
						if (GuiImage.this.entry.id instanceof PreviewEntryId||GuiImage.this.entry.isNotSupported()||GuiImage.this.entry.isOutdated())
							opacity *= .5f;
						OpenGL.glPushMatrix();
						if (CurrentMode.instance.isState(CurrentMode.State.SEE)) {
							OpenGL.glColor4f(.5f, .5f, .5f, opacity*.5f);
							OpenGL.glLineWidth(1f);
							WRenderer.startShape();
							draw(Area.abs(0, 0, a.w(), a.h()), GL_LINE_LOOP);
							final float h = a.h()-.5f;
							final WVertex v = begin(GL_LINES);
							v.pos(a.w()/2f, h, 0f);
							v.pos(0f, 0f, 0f);
							v.pos(a.w()/2f, h, 0f);
							v.pos(a.w(), 0f, 0f);
							v.pos(a.w()/2f, h, 0f);
							v.pos(a.w(), a.h(), 0f);
							v.pos(a.w()/2f, h, 0f);
							v.pos(0f, a.h(), 0f);
							v.draw();
						}
						OpenGL.glScalef(a.w(), a.h(), 1f);

						if (content!=null&&content.state.getType()==StateType.AVAILABLE&&!meta.hasInvalidMeta()) {
							final float o = meta.o.getMovie().get().data*0.1f;
							OpenGL.glColor4f(1.0F, 1.0F, 1.0F, opacity*o);
							content.image.draw(meta, opt.get("vertex", Area.class), opt.get("trim", Area.class));
						} else {
							WRenderer.startShape();
							OpenGL.glLineWidth(1f);
							OpenGL.glColor4f(1.0F, 0.0F, 0.0F, opacity*1.0F);
							draw(Area.abs(0, 0, 1, 1), GL_LINE_LOOP);
						}
						OpenGL.glPopMatrix();

						if (a.w()<1.5f||a.h()<1.5) {
							OpenGL.glScalef(.5f, .5f, .5f);
							OpenGL.glTranslatef(a.w()/2, a.h()/4, 0);
						}
						OpenGL.glTranslatef(a.w()/2, a.h()/2, 0);
						OpenGL.glScalef(.5f, .5f, 1f);
						if (content!=null&&content.state.getType()!=StateType.AVAILABLE) {
							if (content.state.getType()==StateType.ERROR) {
								OpenGL.glPushMatrix();
								OpenGL.glTranslatef(-.5f, -.5f, 0f);
								WRenderer.startTexture();
								texture().bindTexture(resError);
								drawTexture(opt.get("vertex", Area.class), opt.get("trim", Area.class), null);
								OpenGL.glPopMatrix();
							}
							StateRender.drawLoading(content.state.getProgress(), content.state.getType().circle, content.state.getType().speed);
							StateRender.drawMessage(content, font());
						}
						OpenGL.glEnable(GL_LIGHTING);
						OpenGL.glPopMatrix();
					}
				});
				if (GuiImage.this.entry.isOutdated())
					add(new OutdatedPanel(new R()));
				else if (GuiImage.this.entry.isNotSupported())
					add(new NotSupportedPanel(new R()));
			}
		});
	}

	public void renderSignPicture(final float opacity, final float scale, @Nonnull final RenderOption opt) {
		// Load Image
		final Content content = this.entry.getContent();

		final AttrReaders attr = this.entry.getMeta();

		// Size
		final SizeData size01 = content!=null ? content.image.getSize() : SizeData.DefaultSize;
		final SizeData size = attr.sizes.getMovie().get().aspectSize(size01);

		OpenGL.glPushMatrix();

		final OffsetData offset = attr.offsets.getMovie().get();
		final OffsetData centeroffset = attr.centeroffsets.getMovie().get();
		final Quat4f rotate = attr.rotations.getMovie().get().getRotate();
		final boolean see = CurrentMode.instance.isState(CurrentMode.State.SEE);
		if (see) {
			OpenGL.glColor4f(.5f, .5f, .5f, opacity*.5f);
			OpenGL.glLineWidth(1f);
			WRenderer.startShape();
			final WVertex v1 = WRenderer.begin(GL_LINE_STRIP);
			final Matrix4f m = new Matrix4f();
			final Vector3f ov = new Vector3f(offset.x.offset, offset.y.offset, offset.z.offset);
			final Vector3f cv = new Vector3f(centeroffset.x.offset, centeroffset.y.offset, centeroffset.z.offset);

			final Point3f p = new Point3f();
			v1.pos(p.x, p.y, p.z);

			m.set(ov);
			m.transform(p);

			m.set(cv);
			m.transform(p);

			v1.pos(p.x, p.y, p.z);
			p.set(0f, 0f, 0f);

			cv.negate();
			m.set(cv);
			m.transform(p);

			m.set(rotate);
			m.transform(p);

			cv.negate();
			m.set(cv);
			m.transform(p);

			m.set(ov);
			m.transform(p);

			v1.pos(p.x, p.y, p.z);
			v1.draw();
		}
		OpenGL.glTranslatef(offset.x.offset, offset.y.offset, offset.z.offset);
		OpenGL.glTranslatef(centeroffset.x.offset, centeroffset.y.offset, centeroffset.z.offset);
		RotationGL.glRotate(rotate);
		OpenGL.glTranslatef(-centeroffset.x.offset, -centeroffset.y.offset, -centeroffset.z.offset);

		OpenGL.glTranslatef(-size.getWidth()/2, size.getHeight()+(size.getHeight()>=0 ? 0 : -size.getHeight())-.5f, 0f);
		OpenGL.glScalef(size.getWidth()<0 ? -1f : 1f, size.getHeight()<0 ? 1f : -1f, 1f);

		OpenGL.glScalef(scale, scale, 1f);
		drawScreen(0, 0, 0, opacity, size.getWidth()/scale, size.getHeight()/scale, opt);

		OpenGL.glPopMatrix();
	}

	public void applyLight(final float x, final float y, final float z, final @Nonnull Quat4f signrotate) {
		final AttrReaders attr = this.entry.getMeta();
		int lightx = (int) attr.f.getMovie().get().data;
		int lighty = (int) attr.g.getMovie().get().data;
		if (lightx!=-1||lighty!=-1) {
			if (lightx<0||lighty<0) {
				int lsign = 0;
				int lcenter = 0;
				int lpicture = 0;

				if (lightx!=-2&&lightx!=-3||lighty!=-2&&lighty!=-3)
					lsign = Client.mc.theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_float(x), MathHelper.floor_float(y), MathHelper.floor_float(z), 0);

				if (lightx==-2||lighty==-2||lightx==-3||lighty==-3) {
					final OffsetData offset = attr.offsets.getMovie().get();
					final OffsetData centeroffset = attr.centeroffsets.getMovie().get();
					final Matrix4f m = new Matrix4f();
					final Point3f p = new Point3f();
					final Quat4f rotate = attr.rotations.getMovie().get().getRotate();
					final Vector3f tv = new Vector3f(x, y, z);
					final Vector3f ov = new Vector3f(offset.x.offset, offset.y.offset, offset.z.offset);
					final Vector3f cv = new Vector3f(centeroffset.x.offset, centeroffset.y.offset, centeroffset.z.offset);

					m.set(ov);
					m.transform(p);

					m.set(cv);
					m.transform(p);

					m.set(signrotate);
					m.transform(p);

					m.set(tv);
					m.transform(p);

					if (lightx==-2||lighty==-2)
						lcenter = Client.mc.theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_float(p.x), MathHelper.floor_float(p.y), MathHelper.floor_float(p.z), 0);

					if (lightx==-3||lighty==-3) {
						final Point3f p2 = new Point3f();

						m.set(cv);
						m.transform(p2);

						m.set(signrotate);
						m.transform(p2);

						m.set(rotate);
						m.transform(p2);

						p.sub(p2);

						lpicture = Client.mc.theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_float(p.x), MathHelper.floor_float(p.y), MathHelper.floor_float(p.z), 0);
					}
				}
				if (lightx<0)
					if (lightx==-2)
						lightx = lcenter%65536>>4;
					else if (lightx==-3)
						lightx = lpicture%65536>>4;
					else
						lightx = lsign%65536>>4;
				if (lighty<0)
					if (lighty==-2)
						lighty = lcenter/65536>>4;
					else if (lighty==-3)
						lighty = lpicture/65536>>4;
					else
						lighty = lsign/65536>>4;
			}
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightx<<4, lighty<<4);
		}
	}

	protected class NotSupportedPanel extends WPanel {
		public NotSupportedPanel(final R position) {
			super(position);
		}

		@Override
		protected void initWidget() {
			final VCommon var = V.a(.8f);
			add(new UpdateLogo(new R(Coord.width(var), Coord.height(var), Coord.pleft(.5f), Coord.ptop(.5f)).child(Coord.pleft(-.5f), Coord.ptop(-.5f))));
			add(new MScaledLabel(new R(Coord.pleft(.5f), Coord.top(0), Coord.pheight(.4f), Coord.width(2)).child(Coord.pleft(-.5f))).setText(I18n.format("signpic.advmsg.format.unsupported")).setColor(0xff9900).setShadow(true));
			if (Informations.instance.isUpdateRequired())
				add(new MScaledLabel(new R(Coord.pleft(.5f), Coord.bottom(0), Coord.pheight(.4f), Coord.width(2)).child(Coord.pleft(-.5f))).setText(I18n.format("signpic.advmsg.format.needupdate")).setColor(0xff9900).setShadow(true));
		}

		@Override
		public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final RenderOption opt) {
			WRenderer.startShape();
			OpenGL.glLineWidth(1f);
			OpenGL.glColor4f(1f, 1f, 1f, 1f);
			OpenGL.glPushMatrix();
			OpenGL.glTranslatef(0f, 0f, .002f);
			super.draw(ev, pgp, p, frame, popacity, opt);
			OpenGL.glPopMatrix();
		}
	}

	protected class OutdatedPanel extends WPanel {
		public OutdatedPanel(final R position) {
			super(position);
		}

		@Override
		protected void initWidget() {
			final VCommon var = V.a(.8f);
			add(new UpdateLogo(new R(Coord.width(var), Coord.height(var), Coord.pleft(.5f), Coord.ptop(.5f)).child(Coord.pleft(-.5f), Coord.ptop(-.5f))));
			add(new MScaledLabel(new R(Coord.pleft(.5f), Coord.top(0), Coord.pheight(.4f), Coord.width(2)).child(Coord.pleft(-.5f))).setText(I18n.format("signpic.advmsg.format.outdated")).setColor(0xff9900).setShadow(true));
			add(new MScaledLabel(new R(Coord.pleft(.5f), Coord.bottom(0), Coord.pheight(.4f), Coord.width(2)).child(Coord.pleft(-.5f))).setText(I18n.format("signpic.advmsg.format.needreplace")).setColor(0xff9900).setShadow(true));
		}

		@Override
		public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final RenderOption opt) {
			WRenderer.startShape();
			OpenGL.glLineWidth(1f);
			OpenGL.glColor4f(1f, 1f, 1f, 1f);
			OpenGL.glPushMatrix();
			OpenGL.glTranslatef(0f, 0f, .002f);
			super.draw(ev, pgp, p, frame, popacity, opt);
			OpenGL.glPopMatrix();
		}
	}

	protected class UpdateLogo extends WBase {
		protected @Nonnull VMotion rot = V.pm(0).add(Motion.of(0, Easings.easeInOutSine.move(2.87f, 1f), Motion.blank(0.58f)).setLoop(true)).setLoop(true).start();

		public UpdateLogo(final @Nonnull R position) {
			super(position);
		}

		@Override
		public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final RenderOption opt) {
			final Area a = getGuiPosition(pgp);
			texture().bindTexture(GuiSettings.update);
			OpenGL.glColor4f(144f/256f, 191f/256f, 48f/256f, 1f);
			WRenderer.startTexture();
			OpenGL.glPushMatrix();
			OpenGL.glTranslatef(a.x1()+a.w()/2, a.y1()+a.h()/2, 0f);
			OpenGL.glRotatef(this.rot.get()*360, 0, 0, 1);
			OpenGL.glTranslatef(-a.x1()-a.w()/2, -a.y1()-a.h()/2, -.001f);
			drawTexture(a, null, null);
			OpenGL.glTranslatef(0f, 0f, -.002f);
			drawTexture(a, null, null);
			OpenGL.glPopMatrix();
		}
	};

	public void drawScreen(final int mousex, final int mousey, final float f, final float opacity, final float width, final float height, @Nonnull final RenderOption opt) {
		setWidth(width).setHeight(height);
		super.drawScreen(mousex, mousey, f, opacity, opt);
	}
}
