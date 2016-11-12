package com.kamesuta.mc.bnnwidget.component;

import java.awt.Color;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.render.RenderHelper;

import net.minecraft.client.renderer.GlStateManager;

public class MLabel extends WBase {
	protected int textcolor = 14737632;
	protected String text;
	protected boolean shadow;
	protected String watermark;
	protected int watermarkcolor = 0x777777;

	public MLabel(final R position, final String text) {
		super(position);
		this.text = text;
	}

	public MLabel setWatermark(final String watermark) {
		this.watermark = watermark;
		return this;
	}

	public String getWatermark() {
		return this.watermark;
	}

	public MLabel setWatermarkColor(final int watermark) {
		this.watermarkcolor = watermark;
		return this;
	}

	public int getWatermarkColor() {
		return this.watermarkcolor;
	}

	public void setShadow(final boolean b) {
		this.shadow = b;
	}

	public boolean isShadow() {
		return this.shadow;
	}

	public void setColor(final int color) {
		this.textcolor = color;
	}

	public int getColor() {
		return this.textcolor;
	}

	public void setText(final String s) {
		if (StringUtils.equals(s, getText()))
			return;
		final String oldText = getText();
		this.text = s;
		onTextChanged(oldText);
	}

	public String getText() {
		return this.text;
	}

	protected void onTextChanged(final String oldText) {
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
		final Area out = getGuiPosition(pgp);
		drawText(out, getGuiOpacity(popacity));
	}

	protected float wscale = 1f;

	public void setScaleWidth(final float f) {
		this.wscale = f;
	}

	public float getScaleWidth(final Area a) {
		return this.wscale;
	}

	protected float hscale = 1f;

	public void setScaleHeight(final float f) {
		this.hscale = f;
	}

	public float getScaleHeight(final Area a) {
		return this.hscale;
	}

	protected void drawText(final Area a, final float opacity) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(a.x1()+a.w()/2, a.y1()+a.h()/2, 0);
		GlStateManager.scale(getScaleWidth(a), getScaleHeight(a), 1);
		RenderHelper.startTexture();
		final Color c = new Color(getColor());
		final Color c_ = new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) Math.max(4, opacity*c.getAlpha()));
		drawStringC(getText(), 0, 0, 0, 0, c_.getRGB(), isShadow());
		if (!StringUtils.isEmpty(getWatermark())&&StringUtils.isEmpty(getText())) {
			final Color w = new Color(getWatermarkColor());
			final Color w_ = new Color(w.getRed(), w.getGreen(), w.getBlue(), (int) Math.max(4, opacity*c.getAlpha()));
			drawStringC(getWatermark(), 0, 0, 0, 0, w_.getRGB());
		}
		GlStateManager.popMatrix();
	}
}