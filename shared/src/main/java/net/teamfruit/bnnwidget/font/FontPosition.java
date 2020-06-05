package net.teamfruit.bnnwidget.font;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.teamfruit.bnnwidget.render.WGui.Align;
import net.teamfruit.bnnwidget.render.WGui.VerticalAlign;

/**
 * 文字描画時の設定
 *
 * @author TeamFruit
 */
public class FontPosition {
	private float x, y, scale;
	private @Nullable String text;
	private @Nonnull Align align = Align.LEFT;
	private @Nonnull VerticalAlign valign = VerticalAlign.TOP;
	private boolean usecode;
	private boolean shadow;

	public FontPosition() {
		reset();
	}

	public FontPosition(final @Nonnull FontPosition p) {
		set(p);
	}

	public @Nonnull FontPosition reset() {
		this.x = this.y = 0;
		this.text = null;
		this.scale = .5f;
		this.align = Align.LEFT;
		this.usecode = true;
		return this;
	}

	public @Nonnull FontPosition set(@Nonnull final FontPosition p) {
		this.x = p.getX();
		this.y = p.getY();
		this.text = p.getText();
		this.scale = p.getScale();
		this.align = p.getAlign();
		this.usecode = p.isUseCode();
		return this;
	}

	/**
	 * @param x 絶対X座標
	 * @return this
	 */
	public @Nonnull FontPosition setX(final float x) {
		this.x = x;
		return this;
	}

	/**
	 * @return 絶対X座標
	 */
	public float getX() {
		return this.x;
	}

	/**
	 * @param y 絶対Y座標
	 * @return this
	 */
	public @Nonnull FontPosition setY(final float y) {
		this.y = y;
		return this;
	}

	/**
	 * @return 絶対Y座標
	 */
	public float getY() {
		return this.y;
	}

	/**
	 * @param x 絶対X座標
	 * @param y 絶対Y座標
	 * @return this
	 */
	public @Nonnull FontPosition setPosition(final float x, final float y) {
		setX(x);
		setY(y);
		return this;
	}

	/**
	 * @param str 描画文字
	 * @return this
	 */
	public @Nonnull FontPosition setText(final String str) {
		this.text = str;
		return this;
	}

	/**
	 * @return 描画文字
	 */
	public @Nonnull String getText() {
		if (this.text!=null)
			return this.text;
		return this.text = "";
	}

	/**
	 * @param scale 倍率
	 * @return this
	 */
	public @Nonnull FontPosition setScale(final float scale) {
		this.scale = scale;
		return this;
	}

	/**
	 * @return 倍率
	 */
	public float getScale() {
		return this.scale;
	}

	/**
	 * @param align 文字揃え
	 * @return this
	 */
	public @Nonnull FontPosition setAlign(final @Nonnull Align align) {
		this.align = align;
		return this;
	}

	/**
	 * @return 文字揃え
	 */
	public @Nonnull Align getAlign() {
		return this.align;
	}

	/**
	 * @param valign 縦文字揃え
	 * @return this
	 */
	public @Nonnull FontPosition setVAlign(final @Nonnull VerticalAlign valign) {
		this.valign = valign;
		return this;
	}

	/**
	 * @return 縦文字揃え
	 */
	public @Nonnull VerticalAlign getVAlign() {
		return this.valign;
	}

	/**
	 * @param usecode カラーコードを使う場合true
	 * @return this
	 */
	public @Nonnull FontPosition setUseCode(final boolean usecode) {
		this.usecode = usecode;
		return this;
	}

	/**
	 * @return カラーコードを使う場合true
	 */
	public boolean isUseCode() {
		return this.usecode;
	}

	/**
	 * @param shadow 影を付ける場合true
	 * @return this
	 */
	public @Nonnull FontPosition setShadow(final boolean shadow) {
		this.shadow = shadow;
		return this;
	}

	/**
	 * @return 影を付ける場合true
	 */
	public boolean isShadow() {
		return this.shadow;
	}
}
