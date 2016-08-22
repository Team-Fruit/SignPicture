package com.kamesuta.mc.signpic.placer;

import com.kamesuta.mc.guiwidget.GuiComponent;
import com.kamesuta.mc.guiwidget.GuiPosition;
import com.kamesuta.mc.guiwidget.GuiTools;
import com.kamesuta.mc.guiwidget.position.IPositionAbsolute;
import com.kamesuta.mc.guiwidget.position.IPositionRelative;
import com.kamesuta.mc.guiwidget.position.Point;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;

public class GuiCCTextField extends GuiComponent {
	protected IPositionRelative position;
	private String text;
	private boolean isFocused;
	private boolean isEnabled;
	public int maxStringLength;
	public int cursorCounter;
	public String actionCommand;
	private String allowedCharacters;

	public GuiCCTextField(final IPositionRelative position, final String text) {
		this.position = position;
		this.isFocused = false;
		this.isEnabled = true;
		this.text = text;
	}
	public void setText(final String s) {
		if (s.equals(this.text)) {
			return;
		}
		final String oldText = this.text;
		this.text = s;
		onTextChanged(oldText);
	}

	public void onTextChanged(final String oldText) {
	}

	public final String getText() {
		return this.text;
	}

	public final boolean isEnabled() {
		return this.isEnabled;
	}

	public void setEnabled(final boolean b) {
		this.isEnabled = b;
		if ((!this.isEnabled) && (this.isFocused)) {
			setFocused(false);
		}
	}

	public final boolean isFocused() {
		return this.isFocused;
	}

	@Override
	public void update(final GuiTools tools, final GuiPosition pgp, final Point p) {
		this.cursorCounter += 1;
	}

	@Override
	public void keyTyped(final GuiTools tools, final GuiPosition pgp, final Point p, final char c, final int keycode) {
		if ((!this.isEnabled) || (!this.isFocused)) {
			return;
		}
		if (c == '\026') {
			final String s = GuiScreen.getClipboardString();
			if ((s == null) || (s.equals(""))) {
				return;
			}
			for (int i = 0; i < s.length(); i++) {
				if (this.text.length() == this.maxStringLength) {
					return;
				}
				final char tc = s.charAt(i);
				if (canAddChar(tc)) {
					setText(this.text + tc);
				}
			}
		}
		if (keycode == 28) {
			setFocused(false);
		}
		if ((keycode == 14) && (this.text.length() > 0)) {
			setText(this.text.substring(0, this.text.length() - 1));
		}
		if (((this.text.length() < this.maxStringLength) || (this.maxStringLength == 0)) && (canAddChar(c))) {
			setText(this.text + c);
		}
	}

	public boolean canAddChar(final char c) {
		return this.allowedCharacters.indexOf(c) >= 0 ? true
				: this.allowedCharacters == null ? ChatAllowedCharacters.isAllowedCharacter(c) : false;
	}

	@Override
	public void mouseClicked(final GuiTools tools, final GuiPosition pgp, final Point p, final int button) {
		final IPositionAbsolute gp = pgp.child(this.position).getAbsolute();
		if (gp.pointInside(p)) {
			setFocused(true);
			if (button == 1) {
				setText("");
			}
		} else {
			setFocused(false);
		}
	}

	public void setFocused(final boolean focus) {
		if (focus == this.isFocused) {
			return;
		}
		this.isFocused = focus;
		onFocusChanged();
	}

	public void onFocusChanged() {
		if (this.isFocused) {
			this.cursorCounter = 0;
		}
	}

	@Override
	public void draw(final GuiTools tools, final GuiPosition pgp, final Point p, final float frame) {
		drawBackground();
		drawText();
		tools.drawDebug(pgp);
		drawRect(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
		drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
	}

	public void drawBackground() {
		drawRect(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
		drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
	}

	public String getDrawText() {
		String s = getText();
		if ((this.isEnabled) && (this.isFocused) && (this.cursorCounter / 6 % 2 == 0)) {
			s = s + "_";
		}
		return s;
	}

	public void drawText() {
		drawString(this.fontRenderer, getDrawText(), this.x + 4, this.y + this.height / 2 - 4, getTextColour());
	}

	public int getTextColour() {
		return this.isEnabled ? 14737632 : 7368816;
	}

	public GuiCCTextField setMaxStringLength(final int i) {
		this.maxStringLength = i;
		return this;
	}

	public GuiCCTextField setAllowedCharacters(final String s) {
		this.allowedCharacters = s;
		return this;
	}
}
