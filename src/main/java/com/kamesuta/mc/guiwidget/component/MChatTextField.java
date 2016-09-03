package com.kamesuta.mc.guiwidget.component;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.guiwidget.WBase;
import com.kamesuta.mc.guiwidget.WEvent;
import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.Point;
import com.kamesuta.mc.guiwidget.position.R;

import net.minecraft.client.gui.GuiTextField;

public class MChatTextField extends WBase {
	protected GuiTextField t;

	public MChatTextField(final R position) {
		super(position);
		this.t = new GuiTextField(font, 0, 0, 0, 0);
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area a = getGuiPosition(pgp);
		this.t.width = (int)a.w();
		this.t.height = (int) a.h();
		glPushMatrix();
		glTranslatef(a.x1(), a.y1(), 0f);
		this.t.drawTextBox();
		glPopMatrix();
	}

	@Override
	public void mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
		final Area a = getGuiPosition(pgp);
		updateArea(a);
		this.t.mouseClicked((int) p.x(), (int) p.y(), button);
	}

	@Override
	public void update(final WEvent ev, final Area pgp, final Point p) {
		this.t.updateCursorCounter();
	}

	@Override
	public void keyTyped(final WEvent ev, final Area pgp, final Point p, final char c, final int keycode) {
		this.t.textboxKeyTyped(c, keycode);
	}

	protected void updateArea(final Area a) {
		this.t.xPosition = (int) a.x1();
		this.t.yPosition = (int) a.y1();
		this.t.width = (int) a.w();
		this.t.height = (int) a.h();
	}

	public void setText(final String p_146180_1_) {
		this.t.setText(p_146180_1_);
	}

	public String getText() {
		return this.t.getText();
	}

	public String getSelectedText() {
		return this.t.getSelectedText();
	}

	public void writeText(final String p_146191_1_) {
		this.t.writeText(p_146191_1_);
	}

	@Override
	public boolean equals(final Object obj) {
		return this.t.equals(obj);
	}

	public void deleteWords(final int p_146177_1_) {
		this.t.deleteWords(p_146177_1_);
	}

	public void deleteFromCursor(final int p_146175_1_) {
		this.t.deleteFromCursor(p_146175_1_);
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
		this.t.setMaxStringLength(p_146203_1_);
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
		this.t.setFocused(p_146195_1_);
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
