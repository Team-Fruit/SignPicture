package com.kamesuta.mc.guiwidget.component;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

import com.kamesuta.mc.guiwidget.GuiBase;
import com.kamesuta.mc.guiwidget.GuiEvent;
import com.kamesuta.mc.guiwidget.GuiPosition;
import com.kamesuta.mc.guiwidget.position.IPositionAbsolute;
import com.kamesuta.mc.guiwidget.position.Point;
import com.kamesuta.mc.guiwidget.position.relative.IPositionRelative;
import com.kamesuta.mc.guiwidget.position.relative.RelativePosition;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;

public class GuiTextField extends GuiBase {
	protected final Minecraft mc = FMLClientHandler.instance().getClient();

	protected String text;
	protected int seek;
	protected boolean isFocused;
	protected boolean isEnabled;
	public int maxStringLength;
	public int cursorCounter;
	public String actionCommand;
	protected String allowedCharacters;

	public GuiTextField(final IPositionRelative position, final String text) {
		super(position);
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
	public void update(final GuiEvent ev, final GuiPosition pgp, final Point p) {
		this.cursorCounter += 1;
	}

	@Override
	public void keyTyped(final GuiEvent ev, final GuiPosition pgp, final Point p, final char c, final int keycode) {
		if ((!this.isEnabled) || (!this.isFocused)) {
			return;
		} else if (keycode == Keyboard.KEY_BACK) {
			final String s = getText();
			final int seek = getSeek();
			if (seek > 0) {
				setText(s.substring(0, seek-1) + s.substring(seek, s.length()));
				this.seek--;
			}
		} else if (keycode == Keyboard.KEY_DELETE) {
			final String s = getText();
			final int seek = getSeek();
			if (seek < s.length())
				setText(s.substring(0, seek) + s.substring(seek+1, s.length()));
		} else if (keycode == Keyboard.KEY_LEFT) {
			this.seek--;
		} else if (keycode == Keyboard.KEY_RIGHT) {
			this.seek++;
		} else if (c == '\026') {
			final String s = GuiScreen.getClipboardString();
			if ((s == null) || (s.equals(""))) {
				return;
			}
			for (int i = 0; i < s.length(); i++) {
				if ((this.text.length() >= this.maxStringLength) && (this.maxStringLength != 0)) {
					return;
				}
				final char tc = s.charAt(i);
				if (canAddChar(tc)) {
					setText(this.text + tc);
				}
			}
		} else if (keycode == Keyboard.KEY_RETURN) {
			setFocused(false);
		} else if (((this.text.length() < this.maxStringLength) || (this.maxStringLength == 0)) && (canAddChar(c))) {
			final String s = getText();
			final int seek = getSeek();
			setText(s.substring(0, seek) + c + s.substring(seek, s.length()));
			this.seek++;
		}
	}

	protected int getSeek() {
		return this.seek = Math.min(getText().length(), Math.max(0, this.seek));
	}

	public boolean canAddChar(final char c) {
		if (this.allowedCharacters == null)
			return ChatAllowedCharacters.isAllowedCharacter(c);
		else if (this.allowedCharacters.isEmpty())
			return true;
		else
			return this.allowedCharacters.indexOf(c) >= 0;
	}

	@Override
	public void mouseClicked(final GuiEvent ev, final GuiPosition pgp, final Point p, final int button) {
		final IPositionAbsolute gp = pgp.child(this.position).getAbsolute();
		if (gp.pointInside(p)) {
			setFocused(true);
			if (button == 1) {
				setText("");
			}
			final IPositionAbsolute in = getGuiPosition(pgp).child(new RelativePosition(1, 1, -2, -2, true)).getAbsolute();
			this.seek = this.mc.fontRenderer.trimStringToWidth(getText(), p.x - (in.x1() + 4)).length();
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
	public void draw(final GuiEvent ev, final GuiPosition pgp, final Point p, final float frame) {
		final IPositionAbsolute out = getGuiPosition(pgp).getAbsolute();
		final IPositionAbsolute in = getGuiPosition(pgp).child(new RelativePosition(1, 1, -2, -2, true)).getAbsolute();

		glDisable(GL_TEXTURE_2D);
		glColor4f(0.627451f, 0.627451f, 0.627451f, 1f);
		draw(out, GL_QUADS);
		glColor4f(0f, 0f, 0f, 1f);
		draw(in, GL_QUADS);
		glEnable(GL_TEXTURE_2D);

		drawString(this.mc.fontRenderer, getDrawText(), in.x1() + 4, in.y1() + (in.y2()-in.y1()) / 2 - 4, getTextColour());
	}

	public String getDrawText() {
		String s = getText();
		final int seek = getSeek();

		final boolean blink = this.cursorCounter / 6 % 2 == 0;
		if ((this.isEnabled) && (this.isFocused)) {
			if (seek < s.length()) {
				s = s.substring(0, seek) + (blink ? "_" : " ") + s.substring(seek, s.length());
			} else {
				if (blink)
					s = s + "_";
			}
		}
		return s;
	}

	public int getTextColour() {
		return this.isEnabled ? 14737632 : 7368816;
	}

	public GuiTextField setMaxStringLength(final int i) {
		this.maxStringLength = i;
		return this;
	}

	public GuiTextField setAllowedCharacters(final String s) {
		this.allowedCharacters = s;
		return this;
	}
}
