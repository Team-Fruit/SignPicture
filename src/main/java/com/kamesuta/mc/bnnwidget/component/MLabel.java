package com.kamesuta.mc.bnnwidget.component;

import java.awt.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WRenderer;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.render.OpenGL;

public class MLabel extends WBase {
	protected int textcolor = 14737632;
	protected @Nonnull String text = "";
	protected boolean shadow;
	protected @Nullable String watermark;
	protected int watermarkcolor = 0x777777;
	protected @Nonnull Align align = Align.CENTER;
	protected @Nonnull VerticalAlign valign = VerticalAlign.MIDDLE;

	public @Nonnull MLabel(final @Nonnull R position) {
		super(position);
	}

	public @Nonnull MLabel setAlign(final @Nonnull Align align) {
		this.align = align;
		return this;
	}

	public @Nonnull Align getAlign() {
		return this.align;
	}

	public @Nonnull MLabel setVerticalAlign(final @Nonnull VerticalAlign valign) {
		this.valign = valign;
		return this;
	}

	public @Nonnull VerticalAlign getVerticalAlign() {
		return this.valign;
	}

	public @Nonnull MLabel setWatermark(final @Nullable String watermark) {
		this.watermark = watermark;
		return this;
	}

	public @Nullable String getWatermark() {
		return this.watermark;
	}

	public @Nonnull MLabel setWatermarkColor(final int watermark) {
		this.watermarkcolor = watermark;
		return this;
	}

	public int getWatermarkColor() {
		return this.watermarkcolor;
	}

	public @Nonnull MLabel setShadow(final boolean b) {
		this.shadow = b;
		return this;
	}

	public boolean isShadow() {
		return this.shadow;
	}

	public @Nonnull MLabel setColor(final int color) {
		this.textcolor = color;
		return this;
	}

	public int getColor() {
		return this.textcolor;
	}

	public @Nonnull MLabel setText(final @Nonnull String s) {
		if (StringUtils.equals(s, getText()))
			return this;
		final String oldText = getText();
		this.text = s;
		onTextChanged(oldText);
		return this;
	}

	public @Nonnull String getText() {
		return this.text;
	}

	protected void onTextChanged(final @Nonnull String oldText) {
	}

	@Override
	public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity) {
		final Area out = getGuiPosition(pgp);
		drawText(out, getGuiOpacity(popacity));
	}

	protected float wscale = 1f;

	public @Nonnull MLabel setScaleWidth(final float f) {
		this.wscale = f;
		return this;
	}

	public float getScaleWidth(final @Nonnull Area a) {
		return this.wscale;
	}

	protected float hscale = 1f;

	public @Nonnull MLabel setScaleHeight(final float f) {
		this.hscale = f;
		return this;
	}

	public float getScaleHeight(final @Nonnull Area a) {
		return this.hscale;
	}

	protected void drawText(final @Nonnull Area a, final float opacity) {
		OpenGL.glPushMatrix();
		OpenGL.glTranslated(a.x1()+a.w()/2, a.y1()+a.h()/2, 0);
		OpenGL.glScaled(getScaleWidth(a), getScaleHeight(a), 1);
		OpenGL.glTranslated(-(a.x1()+a.w()/2), -(a.y1()+a.h()/2), 0);
		WRenderer.startTexture();
		final Color c = new Color(getColor());
		OpenGL.glColor4f(1f, 1f, 1f, opacity);
		fontColor(c.getRed(), c.getGreen(), c.getBlue(), (int) Math.max(4, opacity*c.getAlpha()));
		drawString(getText(), a, getAlign(), getVerticalAlign(), isShadow());
		final String watermark = getWatermark();
		if (watermark!=null&&!StringUtils.isEmpty(watermark)&&StringUtils.isEmpty(getText())) {
			final Color w = new Color(getWatermarkColor());
			fontColor(w.getRed(), w.getGreen(), w.getBlue(), (int) Math.max(4, opacity*c.getAlpha()));
			drawString(watermark, a, getAlign(), getVerticalAlign(), isShadow());
		}
		OpenGL.glPopMatrix();
	}
}