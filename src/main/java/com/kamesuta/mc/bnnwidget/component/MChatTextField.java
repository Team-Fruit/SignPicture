package com.kamesuta.mc.bnnwidget.component;

import static org.lwjgl.opengl.GL11.*;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatAllowedCharacters;

public class MChatTextField extends WBase {
	protected final GuiTextField t;

	protected String watermark;
	protected String allowedCharacters;

	public MChatTextField(final R position) {
		super(position);
		this.t = new GuiTextField(font(), 0, 0, 0, 0);
	}

	public boolean canAddChar(final char c) {
		if (StringUtils.isEmpty(this.allowedCharacters))
			return true;
		else if (!ChatAllowedCharacters.isAllowedCharacter(c))
			return true;
		else
			return this.allowedCharacters.indexOf(c) >= 0;
	}

	public MChatTextField setWatermark(final String watermark) {
		this.watermark = watermark;
		return this;
	}

	public String getWatermark() {
		return this.watermark;
	}

	public MChatTextField setAllowedCharacters(final String s) {
		this.allowedCharacters = s;
		return this;
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity) {
		final Area a = getGuiPosition(pgp);
		final int x = this.t.xPosition;
		final int y = this.t.yPosition;
		final int w = this.t.width;
		final int h = this.t.height;
		this.t.xPosition = 1;
		this.t.yPosition = 1;
		this.t.width = (int)a.w() - 2;
		this.t.height = (int) a.h() - 2;
		glPushMatrix();
		glTranslatef(a.x1(), a.y1(), 0f);

		this.t.drawTextBox();
		if (!StringUtils.isEmpty(this.watermark) && StringUtils.isEmpty(getText()) && !isFocused()) {
			final int l = getEnableBackgroundDrawing() ? this.t.xPosition + 4 : this.t.xPosition;
			final int i1 = getEnableBackgroundDrawing() ? this.t.yPosition + (this.t.height - 8) / 2 : this.t.yPosition;
			font().drawStringWithShadow(this.watermark, l, i1, 0x777777);
		}

		glPopMatrix();
		this.t.xPosition = x;
		this.t.yPosition = y;
		this.t.width = w;
		this.t.height = h;
	}

	@Override
	public void onAdded() {
		updateArea(new Area(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE));
	}

	@Override
	public boolean mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
		final Area a = getGuiPosition(pgp);
		updateArea(a);
		final boolean b = isFocused();
		if (button == 1 && a.pointInside(p))
			setText("");
		this.t.mouseClicked((int) p.x(), (int) p.y(), button);
		if (b!=isFocused()) onFocusChanged();
		return isFocused();
	}

	@Override
	public void update(final WEvent ev, final Area pgp, final Point p) {
		this.t.updateCursorCounter();
	}

	@Override
	public boolean keyTyped(final WEvent ev, final Area pgp, final Point p, final char c, final int keycode) {
		if (canAddChar(c))
			this.t.textboxKeyTyped(c, keycode);
		return isFocused();
	}

	@Override
	public boolean onCloseRequest() {
		setFocused(false);
		return true;
	}

	protected void updateArea(final Area a) {
		final Area b = a.child(1, 1, -1, -1);
		this.t.xPosition = (int) b.x1();
		this.t.yPosition = (int) b.y1();
		this.t.width = (int) b.w();
		this.t.height = (int) b.h();
	}

	public void setText(final String p_146180_1_) {
		final String old = getText();
		this.t.setText(p_146180_1_);
		onTextChanged(old);
	}

	protected void onTextChanged(final String oldText) {
	}

	public String getText() {
		return this.t.getText();
	}

	public String getSelectedText() {
		return this.t.getSelectedText();
	}

	public void writeText(final String p_146191_1_) {
		final String old = getText();
		this.t.writeText(p_146191_1_);
		onTextChanged(old);
	}

	@Override
	public boolean equals(final Object obj) {
		return this.t.equals(obj);
	}

	public void deleteWords(final int p_146177_1_) {
		this.t.deleteWords(p_146177_1_);
	}

	public void deleteFromCursor(final int p_146175_1_) {
		final String old = getText();
		this.t.deleteFromCursor(p_146175_1_);
		onTextChanged(old);
	}

	public int getNthWordFromCursor(final int p_146187_1_) {
		return this.t.getNthWordFromCursor(p_146187_1_);
	}

	public int getNthWordFromPos(final int p_146183_1_, final int p_146183_2_) {
		return this.t.getNthWordFromPos(p_146183_1_, p_146183_2_);
	}

	public int func_146197_a(final int p_146197_1_, final int p_146197_2_, final boolean p_146197_3_) {
		return this.t.func_146197_a(p_146197_1_, p_146197_2_, p_146197_3_);
	}

	public void moveCursorBy(final int p_146182_1_) {
		this.t.moveCursorBy(p_146182_1_);
	}

	public void setCursorPosition(final int p_146190_1_) {
		this.t.setCursorPosition(p_146190_1_);
	}

	public void setCursorPositionZero() {
		this.t.setCursorPositionZero();
	}

	public void setCursorPositionEnd() {
		this.t.setCursorPositionEnd();
	}

	@Override
	public String toString() {
		return this.t.toString();
	}

	public void setMaxStringLength(final int p_146203_1_) {
		final String old = getText();
		this.t.setMaxStringLength(p_146203_1_);
		onTextChanged(old);
	}

	public int getMaxStringLength() {
		return this.t.getMaxStringLength();
	}

	public int getCursorPosition() {
		return this.t.getCursorPosition();
	}

	public boolean getEnableBackgroundDrawing() {
		return this.t.getEnableBackgroundDrawing();
	}

	public void setEnableBackgroundDrawing(final boolean p_146185_1_) {
		this.t.setEnableBackgroundDrawing(p_146185_1_);
	}

	public void setTextColor(final int p_146193_1_) {
		this.t.setTextColor(p_146193_1_);
	}

	public void setDisabledTextColour(final int p_146204_1_) {
		this.t.setDisabledTextColour(p_146204_1_);
	}

	public void setFocused(final boolean p_146195_1_) {
		if (p_146195_1_!=isFocused()) onFocusChanged();
		this.t.setFocused(p_146195_1_);
	}

	protected void onFocusChanged() {
	}

	public boolean isFocused() {
		return this.t.isFocused();
	}

	public void setEnabled(final boolean p_146184_1_) {
		this.t.setEnabled(p_146184_1_);
	}

	public int getSelectionEnd() {
		return this.t.getSelectionEnd();
	}

	public int getWidth() {
		return this.t.getWidth();
	}

	public void setSelectionPos(final int p_146199_1_) {
		this.t.setSelectionPos(p_146199_1_);
	}

	public void setCanLoseFocus(final boolean p_146205_1_) {
		this.t.setCanLoseFocus(p_146205_1_);
	}

	public boolean getVisible() {
		return this.t.getVisible();
	}

	public void setVisible(final boolean p_146189_1_) {
		this.t.setVisible(p_146189_1_);
	}
}
