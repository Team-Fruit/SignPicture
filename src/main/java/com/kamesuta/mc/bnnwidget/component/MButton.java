package com.kamesuta.mc.bnnwidget.component;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.render.RenderHelper;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class MButton extends WBase {
	public static final ResourceLocation button = new ResourceLocation("signpic", "textures/gui/buttons.png");

	public String text;
	public String actionCommand;
	private boolean isEnabled = true;

	public MButton(final R position, final String text) {
		super(position);
		this.text = text;
	}

	public void setText(final String s) {
		this.text = s;
	}

	public boolean isEnabled() {
		return this.isEnabled;
	}

	public void setEnabled(final boolean b) {
		this.isEnabled = b;
	}

	@Override
	public boolean mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
		final Area abs = getGuiPosition(pgp);
		if (abs.pointInside(p)) {
			if (isEnabled())
				if (onClicked(ev, pgp, p, button)) {
					if (this.actionCommand!=null)
						ev.eventDispatch(this.actionCommand, Integer.valueOf(button));
					mc.getSoundHandler()
							.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
				}
			return true;
		}
		return false;
	}

	protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
		return true;
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
		final Area a = getGuiPosition(pgp);
		final float opacity = getGuiOpacity(popacity);

		if (Config.instance.informationTryNew) {
			RenderHelper.startShape();
			if (isEnabled()) {
				GlStateManager.color(.2f, .2f, .2f, opacity*.2f);
				draw(a);
			}
			GlStateManager.color(1f, 1f, 1f, opacity*this.o.get()*.3f);
			draw(a);
			glLineWidth(1f);
			if (isEnabled())
				GlStateManager.color(0f, 0f, 0f, opacity);
			else
				GlStateManager.color(.5f, .5f, .5f, opacity);
			draw(a, GL_LINE_LOOP);
		} else {
			RenderHelper.startTexture();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			texture().bindTexture(button);
			final int state = !isEnabled() ? 0 : a.pointInside(p) ? 2 : 1;

			drawTextureModalSize(a.x1(), a.y1(), a.w()/2, a.h()/2, 0, state*80, a.w()/2, a.h()/2);
			drawTextureModalSize(a.x1()+a.w()/2, a.y1(), a.w()/2, a.h()/2, 256-a.w()/2, state*80, a.w()/2, a.h()/2);
			drawTextureModalSize(a.x1(), a.y1()+a.h()/2, a.w()/2, a.h()/2, 0, state*80+80-a.h()/2, a.w()/2, a.h()/2);
			drawTextureModalSize(a.x1()+a.w()/2, a.y1()+a.h()/2, a.w()/2, a.h()/2, 256-a.w()/2, state*80+80-a.h()/2, a.w()/2, a.h()/2);
		}
		if (this.text!=null)
			drawText(ev, pgp, p, frame);
	}

	protected VMotion o = V.pm(0).start();
	protected boolean ob = false;;

	@Override
	public void update(final WEvent ev, final Area pgp, final Point p) {
		final Area a = getGuiPosition(pgp);
		if (a.pointInside(p)) {
			if (!this.ob)
				this.o.stop().add(Easings.easeLinear.move(.1f, 1f)).start();
			this.ob = true;
		} else {
			if (this.ob)
				this.o.stop().add(Easings.easeLinear.move(.1f, 0f)).start();
			this.ob = false;
		}
	}

	public void drawText(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area a = getGuiPosition(pgp);
		RenderHelper.startTexture();
		fontColor(getTextColour(ev, pgp, p, frame));
		drawString(this.text, a, Align.CENTER, VerticalAlign.MIDDLE, true);
	}

	public int getTextColour(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area abs = getGuiPosition(pgp);
		return abs.pointInside(p) ? -96 : !isEnabled() ? -6250336 : -2039584;
	}

	public MButton setActionCommand(final String string) {
		this.actionCommand = string;
		return this;
	}
}
