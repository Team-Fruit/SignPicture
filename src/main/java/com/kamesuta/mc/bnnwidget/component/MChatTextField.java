package com.kamesuta.mc.bnnwidget.component;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Predicate;
import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class MChatTextField extends WBase {
	protected final GuiTextField t;

	public String watermark;

	public MChatTextField(final R position) {
		super(position);
		this.t = new GuiTextField(0, font(), 0, 0, 0, 0);
	}

	public void setWatermark(final String watermark) {
		this.watermark = watermark;
	}

	public String getWatermark() {
		return this.watermark;
	}

	public int getId() {
		return this.t.getId();
	}

	@Override
	public void drawTexturedModalRect(final int xCoord, final int yCoord, final TextureAtlasSprite textureSprite, final int widthIn,
			final int heightIn) {
		this.t.drawTexturedModalRect(xCoord, yCoord, textureSprite, widthIn, heightIn);
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area a = getGuiPosition(pgp);
		final int x = this.t.xPosition;
		final int y = this.t.yPosition;
		final int w = this.t.width;
		final int h = this.t.height;
		this.t.xPosition = 1;
		this.t.yPosition = 1;
		this.t.width = (int)a.w() - 2;
		this.t.height = (int) a.h() - 2;
		GlStateManager.pushMatrix();
		GlStateManager.translate(a.x1(), a.y1(), 0f);

		this.t.drawTextBox();
		if (!StringUtils.isEmpty(this.watermark) && StringUtils.isEmpty(getText()) && !isFocused()) {
			final int l = getEnableBackgroundDrawing() ? this.t.xPosition + 4 : this.t.xPosition;
			final int i1 = getEnableBackgroundDrawing() ? this.t.yPosition + (this.t.height - 8) / 2 : this.t.yPosition;
			font().drawStringWithShadow(this.watermark, l, i1, 0x777777);
		}

		GlStateManager.popMatrix();
		this.t.xPosition = x;
		this.t.yPosition = y;
		this.t.width = w;
		this.t.height = h;
	}

	@Override
	public void init(final WEvent ev, final Area pgp) {
		final Area a = getGuiPosition(pgp);
		updateArea(a);
	}

	@Override
	public void mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
		final Area a = getGuiPosition(pgp);
		updateArea(a);
		final boolean b = isFocused();
		this.t.mouseClicked((int) p.x(), (int) p.y(), button);
		if (b!=isFocused()) onFocusChanged();
	}

	protected void onFocusChanged() {
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
		final Area b = a.child(1, 1, -1, -1);
		this.t.xPosition = (int) b.x1();
		this.t.yPosition = (int) b.y1();
		this.t.width = (int) b.w();
		this.t.height = (int) b.h();
	}

	protected void onTextChanged(final String old) {
	}

	public void setText(final String p_146180_1_) {
		final String old = getText();
		this.t.setText(p_146180_1_);
		onTextChanged(old);
	}

	public void writeText(final String p_146191_1_) {
		final String old = getText();
		this.t.writeText(p_146191_1_);
		onTextChanged(old);
	}

	public void deleteFromCursor(final int p_146175_1_) {
		final String old = getText();
		this.t.deleteFromCursor(p_146175_1_);
		onTextChanged(old);
	}

	public void setMaxStringLength(final int p_146203_1_) {
		final String old = getText();
		this.t.setMaxStringLength(p_146203_1_);
		onTextChanged(old);
	}

	public void setFocused(final boolean p_146195_1_) {
		if (p_146195_1_!=isFocused()) onFocusChanged();
		this.t.setFocused(p_146195_1_);
	}

	@Override
	public int hashCode() {
		return this.t.hashCode();
	}

	public void setGuiResponder(final GuiResponder guiResponderIn) {
		this.t.setGuiResponder(guiResponderIn);
	}

	public void updateCursorCounter() {
		this.t.updateCursorCounter();
	}

	public String getText() {
		return this.t.getText();
	}

	public String getSelectedText() {
		return this.t.getSelectedText();
	}

	@Override
	public boolean equals(final Object obj) {
		return this.t.equals(obj);
	}

	public void setValidator(final Predicate<String> theValidator) {
		this.t.setValidator(theValidator);
	}

	@Override
	public void drawCenteredString(final FontRenderer fontRendererIn, final String text, final int x, final int y, final int color) {
		this.t.drawCenteredString(fontRendererIn, text, x, y, color);
	}

	public void deleteWords(final int num) {
		this.t.deleteWords(num);
	}

	@Override
	public void drawString(final FontRenderer fontRendererIn, final String text, final int x, final int y, final int color) {
		this.t.drawString(fontRendererIn, text, x, y, color);
	}

	@Override
	public void drawTexturedModalRect(final int x, final int y, final int textureX, final int textureY, final int width, final int height) {
		this.t.drawTexturedModalRect(x, y, textureX, textureY, width, height);
	}

	@Override
	public void drawTexturedModalRect(final float xCoord, final float yCoord, final int minU, final int minV, final int maxU, final int maxV) {
		this.t.drawTexturedModalRect(xCoord, yCoord, minU, minV, maxU, maxV);
	}

	public int getNthWordFromCursor(final int numWords) {
		return this.t.getNthWordFromCursor(numWords);
	}

	public int getNthWordFromPos(final int n, final int pos) {
		return this.t.getNthWordFromPos(n, pos);
	}

	public int getNthWordFromPosWS(final int n, final int pos, final boolean skipWs) {
		return this.t.getNthWordFromPosWS(n, pos, skipWs);
	}

	@Override
	public String toString() {
		return this.t.toString();
	}

	public void moveCursorBy(final int num) {
		this.t.moveCursorBy(num);
	}

	public void setCursorPosition(final int pos) {
		this.t.setCursorPosition(pos);
	}

	public void setCursorPositionZero() {
		this.t.setCursorPositionZero();
	}

	public void setCursorPositionEnd() {
		this.t.setCursorPositionEnd();
	}

	public boolean textboxKeyTyped(final char typedChar, final int keyCode) {
		return this.t.textboxKeyTyped(typedChar, keyCode);
	}

	public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
		this.t.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void drawTextBox() {
		this.t.drawTextBox();
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

	public void setEnableBackgroundDrawing(final boolean enableBackgroundDrawingIn) {
		this.t.setEnableBackgroundDrawing(enableBackgroundDrawingIn);
	}

	public void setTextColor(final int color) {
		this.t.setTextColor(color);
	}

	public void setDisabledTextColour(final int color) {
		this.t.setDisabledTextColour(color);
	}

	public boolean isFocused() {
		return this.t.isFocused();
	}

	public void setEnabled(final boolean enabled) {
		this.t.setEnabled(enabled);
	}

	public int getSelectionEnd() {
		return this.t.getSelectionEnd();
	}

	public int getWidth() {
		return this.t.getWidth();
	}

	public void setSelectionPos(final int position) {
		this.t.setSelectionPos(position);
	}

	public void setCanLoseFocus(final boolean canLoseFocusIn) {
		this.t.setCanLoseFocus(canLoseFocusIn);
	}

	public boolean getVisible() {
		return this.t.getVisible();
	}

	public void setVisible(final boolean isVisible) {
		this.t.setVisible(isVisible);
	}
}
