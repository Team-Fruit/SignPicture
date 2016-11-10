package com.kamesuta.mc.bnnwidget.component;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.render.RenderHelper;

public class MLabel extends WBase {
	protected int textcolor = 14737632;
	protected String text;
	protected boolean shadow;

	public MLabel(final R position, final String text) {
		super(position);
		this.text = text;
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
		glPushMatrix();
		glTranslated(a.x1()+a.w()/2, a.y1()+a.h()/2, 0);
		glScaled(getScaleWidth(a), getScaleHeight(a), 1);
		RenderHelper.startTexture();
		final Color c = new Color(getColor());
		final Color color2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) Math.max(4, opacity*c.getAlpha()));
		drawStringC(getText(), 0, 0, 0, 0, color2.getRGB(), isShadow());
		glPopMatrix();
	}
}