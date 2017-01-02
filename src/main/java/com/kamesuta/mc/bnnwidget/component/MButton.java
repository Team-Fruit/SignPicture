package com.kamesuta.mc.bnnwidget.component;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WRenderer;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.render.OpenGL;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class MButton extends WBase {
	public static final @Nonnull ResourceLocation button = new ResourceLocation("signpic", "textures/gui/buttons.png");

	public @Nullable String text = null;
	public @Nullable String actionCommand;
	private boolean isEnabled = true;

	public MButton(final @Nonnull R position) {
		super(position);
	}

	public MButton setText(final @Nullable String s) {
		this.text = s;
		return this;
	}

	public boolean isEnabled() {
		return this.isEnabled;
	}

	public @Nonnull MButton setEnabled(final boolean b) {
		this.isEnabled = b;
		return this;
	}

	@Override
	public boolean mouseClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
		final Area abs = getGuiPosition(pgp);
		if (abs.pointInside(p)) {
			if (isEnabled())
				if (onClicked(ev, pgp, p, button)) {
					if (this.actionCommand!=null)
						ev.eventDispatch(this.actionCommand, Integer.valueOf(button));
					playPressButtonSound();
				}
			return true;
		}
		return false;
	}

	public static void playPressButtonSound() {
		mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

	protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
		return true;
	}

	@Override
	public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity) {
		final Area a = getGuiPosition(pgp);
		final float opacity = getGuiOpacity(popacity);

		if (Config.getConfig().informationTryNew.get()) {
			WRenderer.startShape();
			if (isEnabled()) {
				OpenGL.glColor4f(.2f, .2f, .2f, opacity*.2f);
				draw(a);
			}
			OpenGL.glColor4f(1f, 1f, 1f, opacity*this.o.get()*.3f);
			draw(a);
			OpenGL.glLineWidth(1f);
			if (isEnabled())
				OpenGL.glColor4f(0f, 0f, 0f, opacity);
			else
				OpenGL.glColor4f(.5f, .5f, .5f, opacity);
			draw(a, GL_LINE_LOOP);
		} else {
			WRenderer.startTexture();
			OpenGL.glColor4f(1.0F, 1.0F, 1.0F, opacity);
			texture().bindTexture(button);
			final int state = !isEnabled() ? 0 : a.pointInside(p) ? 2 : 1;

			drawTextureModalSize(a.x1(), a.y1(), a.w()/2, a.h()/2, 0, state*80, a.w()/2, a.h()/2);
			drawTextureModalSize(a.x1()+a.w()/2, a.y1(), a.w()/2, a.h()/2, 256-a.w()/2, state*80, a.w()/2, a.h()/2);
			drawTextureModalSize(a.x1(), a.y1()+a.h()/2, a.w()/2, a.h()/2, 0, state*80+80-a.h()/2, a.w()/2, a.h()/2);
			drawTextureModalSize(a.x1()+a.w()/2, a.y1()+a.h()/2, a.w()/2, a.h()/2, 256-a.w()/2, state*80+80-a.h()/2, a.w()/2, a.h()/2);
		}
		drawText(ev, pgp, p, frame, opacity);
	}

	protected VMotion o = V.pm(0).start();
	protected boolean ob = false;;

	@Override
	public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
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

	public void drawText(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float opacity) {
		final String text = this.text;
		if (text!=null) {
			final Area a = getGuiPosition(pgp);
			WRenderer.startTexture();
			final Color c = new Color(getTextColour(ev, pgp, p, frame));
			fontColor(c.getRed(), c.getGreen(), c.getBlue(), (int) (c.getAlpha()*opacity));
			drawString(text, a, Align.CENTER, VerticalAlign.MIDDLE, true);
		}
	}

	public int getTextColour(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame) {
		final Area abs = getGuiPosition(pgp);
		return abs.pointInside(p) ? -96 : !isEnabled() ? -6250336 : -2039584;
	}

	public MButton setActionCommand(final @Nullable String string) {
		this.actionCommand = string;
		return this;
	}
}
