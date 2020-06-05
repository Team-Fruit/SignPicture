package net.teamfruit.bnnwidget.component;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

import net.minecraft.client.gui.GuiTextField;
import net.teamfruit.bnnwidget.OverridablePoint;
import net.teamfruit.bnnwidget.WBase;
import net.teamfruit.bnnwidget.WEvent;
import net.teamfruit.bnnwidget.compat.Compat;
import net.teamfruit.bnnwidget.compat.OpenGL;
import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.position.Point;
import net.teamfruit.bnnwidget.position.R;
import net.teamfruit.bnnwidget.render.RenderOption;

/**
 * Minecraftの{@link GuiTextField}のウィジェットラッパーです。
 *
 * @author TeamFruit
 */
public class MChatTextField extends WBase {
	/**
	 * テキストフィールド
	 */
	protected final @Nonnull MGuiTextField t;

	/**
	 * 透かし文字
	 */
	protected @Nullable String watermark;
	/**
	 * 透かし文字色
	 */
	protected int watermarkcolor = 0x777777;
	/**
	 * 入力可能文字
	 */
	protected @Nonnull CharacterFilter filter = CharacterFilter.VanillaChatFilter.create();

	public MChatTextField(final @Nonnull R position) {
		super(position);
		this.t = new MGuiTextField();
	}

	/**
	 * 入力可能かどうか
	 * @param c 文字
	 * @return 入力可能の場合true
	 */
	public boolean canAddChar(final char c) {
		return getFilter().checkCharacter(c);
	}

	/**
	 * 透かし文字を設定します
	 * @param watermark 透かし文字
	 * @return this
	 */
	public @Nonnull MChatTextField setWatermark(final @Nullable String watermark) {
		this.watermark = watermark;
		return this;
	}

	/**
	 * 透かし文字
	 * @return 透かし文字
	 */
	public @Nullable String getWatermark() {
		return this.watermark;
	}

	/**
	 * 透かし文字の色を設定します
	 * @param watermark 透かし文字の色
	 * @return this
	 */
	public @Nonnull MChatTextField setWatermarkColor(final int watermark) {
		this.watermarkcolor = watermark;
		return this;
	}

	/**
	 * 透かし文字の色
	 * @return 透かし文字の色
	 */
	public int getWatermarkColor() {
		return this.watermarkcolor;
	}

	/**
	 * 入力可能文字を設定します
	 * @param filter 入力可能文字
	 * @return this
	 */
	public @Nonnull MChatTextField setFilter(final @Nonnull CharacterFilter filter) {
		this.filter = filter;
		return this;
	}

	/**
	 * 入力可能文字
	 * @return 入力可能文字
	 */
	public @Nonnull CharacterFilter getFilter() {
		return this.filter;
	}

	@Override
	public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float opacity, final @Nonnull RenderOption opt) {
		final Area a = getGuiPosition(pgp);
		updateArea(a);
		final int x = this.t.getX();
		final int y = this.t.getY();
		final int w = this.t.width;
		final int h = this.t.height;
		this.t.setX(1);
		this.t.setY(1);
		this.t.width = (int) a.w()-2;
		this.t.height = (int) a.h()-2;
		OpenGL.glPushMatrix();
		OpenGL.glTranslatef(a.x1(), a.y1(), 0f);

		this.t.drawTextBox();

		OpenGL.glPopMatrix();
		this.t.setX(x);
		this.t.setY(y);
		this.t.width = w;
		this.t.height = h;
	}

	@Override
	public void onAdded() {
		updateArea(Area.size(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE));
	}

	private boolean drag;
	private int drag_x = -1;

	@Override
	public boolean mouseClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
		final Area a = getGuiPosition(pgp);
		updateArea(a);
		if (button==1&&a.pointInside(p))
			setText("");
		this.drag = true;
		this.drag_x = (int) p.x();
		this.t.mouseClicked(this.drag_x, (int) p.y(), button);
		return a.pointInside(p);
	}

	@Override
	public boolean mouseReleased(final WEvent ev, final Area pgp, final Point p, final int button) {
		this.drag = false;
		return false;
	}

	@Override
	public boolean mouseMoved(final WEvent ev, final Area pgp, final Point p, final int button) {
		final Area a = getGuiPosition(pgp);
		if (this.drag) {
			updateArea(a);
			this.t.mouseClicked(this.drag_x, (int) p.y(), button);
			final int start = this.t.getCursorPosition();
			this.t.mouseClicked((int) p.x(), (int) p.y(), button);
			final int end = this.t.getCursorPosition();
			this.t.setCursorPosition(end);
			this.t.setSelectionPos(start);
		}
		return a.pointInside(p);
	}

	@Override
	public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
		this.t.updateCursorCounter();
	}

	@Override
	public boolean keyTyped(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final char c, final int keycode) {
		this.t.textboxKeyTyped(c, keycode);
		return isFocused();
	}

	@Override
	public boolean onCloseRequest() {
		setFocused(false);
		return true;
	}

	/**
	 * ウィジェットの絶対座標をテキストフィールドに反映します。
	 * @param a 絶対座標
	 */
	protected void updateArea(final @Nonnull Area a) {
		final Area b = a.child(1, 1, -1, -1);
		this.t.setX((int) b.x1());
		this.t.setY((int) b.y1());
		this.t.width = (int) b.w();
		this.t.height = (int) b.h();
	}

	/**
	 * テキストを設定します
	 * @param p_146180_1_
	 */
	public void setText(final @Nonnull String p_146180_1_) {
		this.t.setText(p_146180_1_);
	}

	/**
	 * フィルターを無視してテキストを設定します
	 * @param p_146180_1_
	 */
	public void setTextByPassFilter(final @Nonnull String p_146180_1_) {
		this.t.setTextByPassFilter(p_146180_1_);
	}

	/**
	 * テキストが変更された場合に呼び出されます。
	 * @param oldText 変更前のテキスト
	 */
	@OverridablePoint
	protected void onTextChanged(final @Nonnull String oldText) {
	}

	/**
	 * テキスト
	 * @return テキスト
	 */
	public @Nonnull String getText() {
		return this.t.getText();
	}

	/**
	 * 選択中のテキスト
	 * @return
	 */
	public @Nonnull String getSelectedText() {
		return this.t.getSelectedText();
	}

	public void writeText(final @Nonnull String p_146191_1_) {
		this.t.writeText(p_146191_1_);
	}

	public void writeTextByPassFilter(final @Nonnull String p_146191_1_) {
		this.t.writeTextByPassFilter(p_146191_1_);
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		return this.t.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.t.hashCode();
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
		return this.t.getNthWordFromPosWS(p_146197_1_, p_146197_2_, p_146197_3_);
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
	public @Nullable String toString() {
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

	/**
	 * デフォルトのMinecraftテキストフィールドデザインを描画するかどうか
	 * @return デフォルトのMinecraftテキストフィールドデザインを描画する場合はtrue
	 */
	public boolean getEnableBackgroundDrawing() {
		return this.t.getEnableBackgroundDrawing();
	}

	/**
	 * デフォルトのMinecraftテキストフィールドデザインを描画するかどうかを設定します
	 * @param b デフォルトのMinecraftテキストフィールドデザインを描画する場合はtrue
	 */
	public void setEnableBackgroundDrawing(final boolean b) {
		this.t.setEnableBackgroundDrawing(b);
	}

	/**
	 * テキストの色を設定します
	 * @param color テキストの色
	 */
	public void setTextColor(final int color) {
		this.t.setTextColor(color);
	}

	/**
	 * 無効状態のテキストの色を指定します
	 * @param color 無効状態のテキストの色
	 */
	public void setDisabledTextColour(final int color) {
		this.t.setDisabledTextColour(color);
	}

	/**
	 * このフィールドを選択中かどうかを設定します
	 * @param b このフィールドを選択中の場合はtrue
	 */
	public void setFocused(final boolean b) {
		this.t.setFocused(b);
	}

	/**
	 * フォーカス状態が変更された場合に呼ばれます
	 */
	@OverridablePoint
	protected void onFocusChanged() {
	}

	/**
	 * このフィールドを選択中かどうか
	 * @return このフィールドを選択中の場合はtrue
	 */
	public boolean isFocused() {
		return this.t.isFocused();
	}

	/**
	 * テキストフィールドが有効かどうかを設定します
	 * @param b テキストフィールドが有効な場合true
	 */
	public void setEnabled(final boolean b) {
		this.t.setEnabled(b);
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

	/**
	 * テキストフィールドを表示するかどうか
	 * @return テキストフィールドを表示する場合true
	 */
	public boolean getVisible() {
		return this.t.getVisible();
	}

	/**
	 * テキストフィールドを表示するかどうかを設定します
	 * @param b テキストフィールドを表示する場合true
	 */
	public void setVisible(final boolean b) {
		this.t.setVisible(b);
	}

	/**
	 * GuiTextFieldにフックが加わったクラスです
	 *
	 * @author TeamFruit
	 */
	protected class MGuiTextField extends Compat.CompatGuiTextField {
		@Override
		public void setText(final @Nullable String text) {
			if (text!=null)
				setTextByPassFilter(filerAllowedCharacters(text));
		}

		public void setTextByPassFilter(final @Nonnull String text) {
			final @Nonnull String s = getText();
			super.setText(text);
			onTextChanged(s, getText());
		}

		@Override
		public void deleteFromCursor(final int p_146175_1_) {
			final String s = getText();
			super.deleteFromCursor(p_146175_1_);
			onTextChanged(s, getText());
		}

		@Override
		public void writeText(final @Nullable String text) {
			if (text!=null)
				writeTextByPassFilter(filerAllowedCharacters(text));
		}

		public void writeTextByPassFilter(final @Nonnull String text) {
			final @Nonnull String s = getText();
			writeText0(text);
			onTextChanged(s, getText());
		}

		private void writeText0(final @Nonnull String newtext) {
			String s1 = "";
			final int cpos = getCursorPosition();
			final int send = getSelectionEnd();
			final String text = getText();
			final int textlen = text.length();
			final int i = cpos<send ? cpos : send;
			final int j = cpos<send ? send : cpos;
			final int k = getMaxStringLength()-textlen-(i-send);

			if (textlen>0)
				s1 = s1+text.substring(0, i);

			int l;

			if (k<newtext.length()) {
				s1 = s1+newtext.substring(0, k);
				l = k;
			} else {
				s1 = s1+newtext;
				l = newtext.length();
			}

			if (textlen>0&&j<textlen)
				s1 = s1+text.substring(j);

			super.setText(s1);
			moveCursorBy(i-getSelectionEnd()+l);
		}

		@Override
		public void setMaxStringLength(final int p_146203_1_) {
			final String s = getText();
			super.setMaxStringLength(p_146203_1_);
			onTextChanged(s, getText());
		}

		protected @Nonnull String filerAllowedCharacters(final @Nonnull String text) {
			final StringBuilder stringbuilder = new StringBuilder();
			final int i = text.length();

			for (int j = 0; j<i; ++j) {
				final char c0 = text.charAt(j);

				if (canAddChar(c0))
					stringbuilder.append(c0);
			}

			return stringbuilder.toString();
		}

		protected void onTextChanged(final @Nonnull String oldText, final @Nonnull String newText) {
			if (!StringUtils.equals(oldText, newText))
				MChatTextField.this.onTextChanged(oldText);
		}

		@Override
		public void setFocused(final boolean p_146195_1_) {
			final boolean b = isFocused();
			super.setFocused(p_146195_1_);
			onFocusChanged(b, isFocused());
		}

		protected void onFocusChanged(final boolean oldFocus, final boolean newFocus) {
			if (oldFocus!=newFocus)
				MChatTextField.this.onFocusChanged();
		}

		@Override
		public void drawTextBox() {
			super.drawTextBox();
			if (!StringUtils.isEmpty(getWatermark())&&StringUtils.isEmpty(getText())&&!isFocused()) {
				final int l = getEnableBackgroundDrawing() ? getX()+4 : getX();
				final int i1 = getEnableBackgroundDrawing() ? getY()+(this.height-8)/2 : getY();
				Compat.getFontRenderer().drawStringWithShadow(getWatermark(), l, i1, MChatTextField.this.watermarkcolor);
			}
		}

		@Override
		public @Nonnull String toString() {
			return "TextField [text="+getText()+"]";
		}
	}

	public static abstract class CharacterFilter {
		/** Array of the special characters that are allowed in any text drawing of Minecraft. */
		public static final char[] allowedCharacters = new char[] { '/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };

		/**
		 * Filter string by only keeping those characters for which isAllowedCharacter() returns true.
		 */
		public String filerAllowedCharacters(final String str) {
			final StringBuilder stringbuilder = new StringBuilder();
			final char[] achar = str.toCharArray();
			final int i = achar.length;

			for (int j = 0; j<i; ++j) {
				final char c0 = achar[j];

				if (checkCharacter(c0))
					stringbuilder.append(c0);
			}

			return stringbuilder.toString();
		}

		public abstract boolean checkCharacter(char ch);

		public static abstract class AbstractWhiteListFilter extends CharacterFilter {
			@Override
			public final boolean checkCharacter(final char ch) {
				return isAllowedCharacter(ch);
			}

			public abstract boolean isAllowedCharacter(char ch);
		}

		public static class WhiteListFilter extends AbstractWhiteListFilter {
			private final @Nonnull Set<Character> whitelist;

			public WhiteListFilter(@Nonnull final Set<Character> whitelist) {
				this.whitelist = whitelist;
			}

			public static @Nonnull WhiteListFilter create() {
				return new WhiteListFilter(Sets.<Character> newHashSet());
			}

			public static @Nonnull WhiteListFilter createFromString(final String filter) {
				return new WhiteListFilter(Sets.<Character> newHashSet(ArrayUtils.toObject(filter.toCharArray())));
			}

			public @Nonnull Set<Character> getWhitelist() {
				return this.whitelist;
			}

			@Override
			public boolean isAllowedCharacter(final char ch) {
				for (final char c : this.whitelist)
					if (c==ch)
						return true;
				return false;
			}
		}

		public static abstract class AbstractBlackListFilter extends CharacterFilter {
			@Override
			public final boolean checkCharacter(final char ch) {
				return !isDeniedCharacter(ch);
			}

			public abstract boolean isDeniedCharacter(char ch);
		}

		public static class BlackListFilter extends AbstractBlackListFilter {
			private final @Nonnull Set<Character> blacklist;

			public BlackListFilter(@Nonnull final Set<Character> blacklist) {
				this.blacklist = blacklist;
			}

			public static @Nonnull BlackListFilter create() {
				return new BlackListFilter(Sets.<Character> newHashSet());
			}

			public static @Nonnull BlackListFilter createFromString(final String filter) {
				return new BlackListFilter(Sets.<Character> newHashSet(ArrayUtils.toObject(filter.toCharArray())));
			}

			public @Nonnull Set<Character> getBlacklist() {
				return this.blacklist;
			}

			@Override
			public boolean isDeniedCharacter(final char ch) {
				for (final char c : this.blacklist)
					if (c==ch)
						return true;
				return false;
			}
		}

		public static class VanillaChatFilter extends BlackListFilter {
			public VanillaChatFilter(@Nonnull final Set<Character> blacklist) {
				super(blacklist);
				for (char i = 0; i<32; i++)
					blacklist.add(i);
				blacklist.add((char) 127);
				blacklist.add((char) 167);
			}

			public static @Nonnull VanillaChatFilter create() {
				return new VanillaChatFilter(Sets.<Character> newHashSet());
			}
		}
	}
}
