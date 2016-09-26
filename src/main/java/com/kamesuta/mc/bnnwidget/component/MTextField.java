package com.kamesuta.mc.bnnwidget.component;

import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.render.RenderHelper;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;

@Deprecated
public class MTextField extends WBase {
	protected String text = "";
	public String watermark;
	protected int seek;
	protected boolean isFocused;
	protected boolean isEnabled;
	public int maxStringLength;
	public int cursorCounter;
	public String actionCommand;
	protected String allowedCharacters;

	protected Deque<String> back = new ArrayDeque<String>();
	protected Deque<String> next = new ArrayDeque<String>();


	public void setWatermark(final String watermark) {
		this.watermark = watermark;
	}

	public String getWatermark() {
		return this.watermark;
	}

	protected void log(final String s) {
		if (!StringUtils.equals(s, this.back.peek())) {
			final String t = this.next.poll();
			if (t!=null)
				this.back.push(t);
			this.next.clear();
			this.next.push(s);
		}
		//Reference.logger.info("log" + this.back + ":" + this.next);
	}

	protected void next() {
		if (!this.next.isEmpty()) {
			final String s = getText();
			final String b = this.next.poll();
			this.back.push(s);
			setTextRaw(b);
		}
		//Reference.logger.info("next" + this.back + ":" + this.next);
	}

	protected void back() {
		if (!this.back.isEmpty()) {
			final String s = getText();
			final String b = this.back.poll();
			this.next.push(s);
			setTextRaw(b);
		}
		//Reference.logger.info("back" + this.back + ":" + this.next);
	}

	public MTextField(final R position) {
		super(position);
		this.isFocused = false;
		this.isEnabled = true;
	}

	public MTextField(final R position, final String watermark) {
		this(position);
		this.watermark = watermark;
	}

	public boolean setText(final String s) {
		if (setTextRaw(s)) {
			log(s);
			return true;
		}
		return false;
	}

	public boolean setTextRaw(final String s) {
		if ((s.length() <= this.maxStringLength) || (this.maxStringLength <= 0)) {
			if (StringUtils.equals(s, getText())) {
				return false;
			}
			final String oldText = getText();
			this.text = s;
			onTextChanged(oldText);
			return true;
		}
		return false;
	}

	public final String getText() {
		return this.text;
	}

	protected void onTextChanged(final String oldText) {
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
	public void update(final WEvent ev, final Area pgp, final Point p) {
		this.cursorCounter += 1;
	}

	@Override
	public void keyTyped(final WEvent ev, final Area pgp, final Point p, final char c, final int keycode) {
		if ((!isEnabled()) || (!isFocused())) {
			return;
		} else if (keycode == Keyboard.KEY_LCONTROL || keycode == Keyboard.KEY_RCONTROL) {
			return;
		} else if (c == '\026') {
			final String s = GuiScreen.getClipboardString();
			if (StringUtils.isEmpty(s)) {
				return;
			}
			final StringBuilder stb = new StringBuilder(getText());
			int j = 0;
			for (int i = 0; i < s.length(); i++) {
				if ((getText().length() >= this.maxStringLength) && (this.maxStringLength > 0)) {
					return;
				}
				final char tc = s.charAt(i);
				if (canAddChar(tc)) {
					stb.append(tc);
					j++;
				}
			}
			setText(stb.toString());
			this.seek += j;
		} else if (c == '\031') {
			next();
		} else if (c == '\032') {
			back();
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
		} else if (keycode == Keyboard.KEY_HOME) {
			this.seek = 0;
		} else if (keycode == Keyboard.KEY_END) {
			this.seek = getText().length();
		} else if (keycode == Keyboard.KEY_RETURN) {
			setFocused(false);
		} else if (((getText().length() < this.maxStringLength) || (this.maxStringLength == 0)) && (canAddChar(c))) {
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
		else if (StringUtils.isEmpty(this.allowedCharacters))
			return true;
		else
			return this.allowedCharacters.indexOf(c) >= 0;
	}

	@Override
	public void mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
		final Area gp = pgp.child(this.position);
		if (gp.pointInside(p)) {
			setFocused(true);
			if (button == 1) {
				setText("");
			}
			final Area in = getGuiPosition(pgp).child(1, 1, -1, -1);
			this.seek = font().trimStringToWidth(getText(), (int) (p.x() - (in.x1() + 4))).length();
		} else {
			setFocused(false);
		}
	}

	public void setFocused(final boolean focus) {
		if (focus == isFocused()) {
			return;
		}
		this.isFocused = focus;
		onFocusChanged();
	}

	public void onFocusChanged() {
		if (isFocused()) {
			this.cursorCounter = 0;
		}
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area out = getGuiPosition(pgp);
		final Area in = getGuiPosition(pgp).child(1, 1, -1, -1);
		drawBackground(out, in);
		drawWatermark(in);
		drawText(in);
		drawCursor(in);
	}

	protected void drawBackground(final Area out, final Area in) {
		RenderHelper.startShape();
		GlStateManager.color(0.627451f, 0.627451f, 0.627451f, 1f);
		drawRect(out);
		GlStateManager.color(0f, 0f, 0f, 1f);
		drawRect(in);
	}

	protected void drawWatermark(final Area a) {
		RenderHelper.startTexture();
		if (!StringUtils.isEmpty(this.watermark)&& StringUtils.isEmpty(getText()) && !isFocused())
			drawString(this.watermark, a.x1() + 4, a.y1() + a.h() / 2 - 4, 0x777777);
	}

	protected void drawText(final Area a) {
		RenderHelper.startTexture();
		drawString(getText(), a.x1() + 4, a.y1() + a.h() / 2 - 4, getTextColour());
	}

	protected void drawCursor(final Area a) {
		RenderHelper.startTexture();
		final String s = getText();
		final int seek = getSeek();
		final boolean blink = this.cursorCounter / 6 % 2 == 0;
		if ((isEnabled()) && (isFocused())) {
			if (blink)
				drawCenteredString("\u2503", a.x1() + 4 + font().getStringWidth(s.substring(0, seek)), a.y1() + a.h() / 2 - 4, getTextColour());
		}
	}

	public int getTextColour() {
		return isEnabled() ? 14737632 : 7368816;
	}

	public MTextField setMaxStringLength(final int i) {
		this.maxStringLength = i;
		return this;
	}

	public MTextField setAllowedCharacters(final String s) {
		this.allowedCharacters = s;
		return this;
	}
}
