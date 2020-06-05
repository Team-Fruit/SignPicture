package net.teamfruit.bnnwidget.component;

import java.awt.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import net.teamfruit.bnnwidget.OverridablePoint;
import net.teamfruit.bnnwidget.WBase;
import net.teamfruit.bnnwidget.WEvent;
import net.teamfruit.bnnwidget.compat.OpenGL;
import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.position.Point;
import net.teamfruit.bnnwidget.position.R;
import net.teamfruit.bnnwidget.render.RenderOption;
import net.teamfruit.bnnwidget.render.WRenderer;

/**
 * ラベルです
 *
 * @author TeamFruit
 */
public class MLabel extends WBase {
	/**
	 * テキストの色
	 */
	protected int textcolor = 14737632;
	/**
	 * テキスト
	 */
	protected @Nonnull String text = "";
	/**
	 * 影を付けるかどうか
	 */
	protected boolean shadow;
	/**
	 * 透かし文字
	 */
	protected @Nullable String watermark;
	/**
	 * 透かし文字色
	 */
	protected int watermarkcolor = 0x777777;
	/**
	 * 横方向のテキスト位置
	 */
	protected @Nonnull Align align = Align.CENTER;
	/**
	 * 縦方向のテキスト位置
	 */
	protected @Nonnull VerticalAlign valign = VerticalAlign.MIDDLE;

	public MLabel(final @Nonnull R position) {
		super(position);
	}

	/**
	 * 横方向のテキスト位置を設定
	 * @param align 横方向のテキスト位置
	 * @return this
	 */
	public @Nonnull MLabel setAlign(final @Nonnull Align align) {
		this.align = align;
		return this;
	}

	/**
	 * 横方向のテキスト位置
	 * @return 横方向のテキスト位置
	 */
	public @Nonnull Align getAlign() {
		return this.align;
	}

	/**
	 * 縦方向のテキスト位置を設定
	 * @param valign 縦方向のテキスト位置
	 * @return this
	 */
	public @Nonnull MLabel setVerticalAlign(final @Nonnull VerticalAlign valign) {
		this.valign = valign;
		return this;
	}

	/**
	 * 縦方向のテキスト位置
	 * @return 縦方向のテキスト位置
	 */
	public @Nonnull VerticalAlign getVerticalAlign() {
		return this.valign;
	}

	/**
	 * 透かし文字を設定します
	 * @param watermark 透かし文字
	 * @return this
	 */
	public @Nonnull MLabel setWatermark(final @Nullable String watermark) {
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
	public @Nonnull MLabel setWatermarkColor(final int watermark) {
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
	 * 影を付けるかどうかを設定します
	 * @param b 影を付ける場合true
	 * @return this
	 */
	public @Nonnull MLabel setShadow(final boolean b) {
		this.shadow = b;
		return this;
	}

	/**
	 * 影を付けるかどうか
	 * @return 影を付ける場合true
	 */
	public boolean isShadow() {
		return this.shadow;
	}

	/**
	 * テキストの色を設定します
	 * @param color テキストの色
	 * @return this
	 */
	public @Nonnull MLabel setColor(final int color) {
		this.textcolor = color;
		return this;
	}

	/**
	 * テキストの色
	 * @return テキストの色
	 */
	public int getColor() {
		return this.textcolor;
	}

	/**
	 * テキストを設定します
	 * @param p_146180_1_
	 */
	public @Nonnull MLabel setText(final @Nonnull String s) {
		if (StringUtils.equals(s, getText()))
			return this;
		final String oldText = getText();
		this.text = s;
		onTextChanged(oldText);
		return this;
	}

	/**
	 * テキスト
	 * @return テキスト
	 */
	public @Nonnull String getText() {
		return this.text;
	}

	/**
	 * テキストが変更された場合に呼び出されます。
	 * @param oldText 変更前のテキスト
	 */
	@OverridablePoint
	protected void onTextChanged(final @Nonnull String oldText) {
	}

	@Override
	public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
		drawText(ev, getGuiPosition(pgp), p, frame, getGuiOpacity(popacity));
	}

	protected float wscale = 1f;

	/**
	 * 幅の倍率を設定します
	 * @param f 幅の倍率
	 * @return this
	 */
	public @Nonnull MLabel setScaleWidth(final float f) {
		this.wscale = f;
		return this;
	}

	/**
	 * 幅の倍率
	 * @return f 幅の倍率
	 */
	@OverridablePoint
	public float getScaleWidth(final @Nonnull Area a) {
		return this.wscale;
	}

	protected float hscale = 1f;

	/**
	 * 高さの倍率を設定します
	 * @param f 高さの倍率
	 * @return this
	 */
	public @Nonnull MLabel setScaleHeight(final float f) {
		this.hscale = f;
		return this;
	}

	/**
	 * 高さの倍率
	 * @return f 高さの倍率
	 */
	@OverridablePoint
	public float getScaleHeight(final @Nonnull Area a) {
		return this.hscale;
	}

	/**
	 * テキストを描画します
	 * @param a 絶対座標
	 * @param opacity 絶対透明度
	 */
	protected void drawText(final @Nonnull WEvent ev, final @Nonnull Area a, final @Nonnull Point p, final float frame, final float opacity) {
		OpenGL.glPushMatrix();
		OpenGL.glTranslated(a.x1()+a.w()/2, a.y1()+a.h()/2, 0);
		OpenGL.glScaled(getScaleWidth(a), getScaleHeight(a), 1);
		OpenGL.glTranslated(-(a.x1()+a.w()/2), -(a.y1()+a.h()/2), 0);
		WRenderer.startTexture();
		final Color c = new Color(getColor());
		OpenGL.glColor4i(c.getRed(), c.getGreen(), c.getBlue(), (int) Math.max(4, opacity*c.getAlpha()));
		drawString(getText(), a, getAlign(), getVerticalAlign(), isShadow());
		final String watermark = getWatermark();
		if (watermark!=null&&!StringUtils.isEmpty(watermark)&&StringUtils.isEmpty(getText())) {
			final Color w = new Color(getWatermarkColor());
			OpenGL.glColor4i(w.getRed(), w.getGreen(), w.getBlue(), (int) Math.max(4, opacity*c.getAlpha()));
			drawString(watermark, a, getAlign(), getVerticalAlign(), isShadow());
		}
		OpenGL.glPopMatrix();
	}
}