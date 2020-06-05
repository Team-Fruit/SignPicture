package net.teamfruit.bnnwidget.render;

import static org.lwjgl.opengl.GL11.*;

import java.nio.IntBuffer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GLAllocation;
import net.teamfruit.bnnwidget.compat.Compat;
import net.teamfruit.bnnwidget.compat.OpenGL;
import net.teamfruit.bnnwidget.position.Area;

/**
 * GUI関連の描画を担当します
 * <p>
 * 各ウィジェットはこのクラスを継承します
 *
 * @author TeamFruit
 */
public class WGui extends WRenderer {
	/**
	 * テクスチャ倍率
	 * <p>
	 * GUIを描画する際に使用します。
	 */
	public static final float textureScale = 1f/256f;

	public static final @Nonnull Area defaultTextureArea = Area.abs(0f, 0f, 1f, 1f);

	private static final @Nullable org.lwjgl.input.Cursor cur;

	static {
		org.lwjgl.input.Cursor cursor = null;
		try {
			final IntBuffer buf = GLAllocation.createDirectIntBuffer(1);
			buf.put(0);
			buf.flip();
			cursor = new org.lwjgl.input.Cursor(1, 1, 0, 0, 1, buf, null);
		} catch (final LWJGLException e) {
		}
		cur = cursor;
	}

	/**
	 * カーソルの表示を切り替えます。
	 * <p>
	 * 状況によっては1pxの黒ドットになる場合があります。
	 * @param b カーソルを表示する場合true
	 */
	public static void setCursorVisible(final boolean b) {
		if (cur!=null)
			try {
				Mouse.setNativeCursor(b ? null : cur);
			} catch (final LWJGLException e) {
			}
	}

	public static void showCursor() {
		setCursorVisible(true);
	}

	public static void hideCursor() {
		setCursorVisible(false);
	}

	/**
	 * 4つの絶対座標からテクスチャを描画します
	 * <p>
	 * テクスチャ座標(倍率)は(0, 0)⇒(1, 1)にすることでテクスチャを1枚表示できます
	 * @param vx1 1つ目のX絶対座標
	 * @param vy1 1つ目のY絶対座標
	 * @param vx2 2つ目のX絶対座標
	 * @param vy2 2つ目のY絶対座標
	 * @param tx1 1つ目のXテクスチャ座標(倍率)
	 * @param ty1 1つ目のYテクスチャ座標(倍率)
	 * @param tx2 2つ目のXテクスチャ座標(倍率)
	 * @param ty2 2つ目のYテクスチャ座標(倍率)
	 */
	private static void drawTextureAbs(final float vx1, final float vy1, final float vx2, final float vy2, final float tx1, final float ty1, final float tx2, final float ty2) {
		beginTextureQuads()
				.pos(vx1, vy2, 0).tex(tx1, ty2)
				.pos(vx2, vy2, 0).tex(tx2, ty2)
				.pos(vx2, vy1, 0).tex(tx2, ty1)
				.pos(vx1, vy1, 0).tex(tx1, ty1)
				.draw();
	}

	/**
	 * 4つの絶対座標からテクスチャを描画します
	 * <p>
	 * リピートされる無限サイズのテクスチャをトリミング絶対座標でくり抜きます。
	 * @param vx1 1つ目のX画像絶対座標
	 * @param vy1 1つ目のY画像絶対座標
	 * @param vx2 2つ目のX画像絶対座標
	 * @param vy2 2つ目のY画像絶対座標
	 * @param rx1 1つ目のXトリミング絶対座標
	 * @param ry1 1つ目のYトリミング絶対座標
	 * @param rx2 2つ目のXトリミング絶対座標
	 * @param ry2 2つ目のYトリミング絶対座標
	 * @param tx1 1つ目のXテクスチャ座標(倍率)
	 * @param ty1 1つ目のYテクスチャ座標(倍率)
	 * @param tx2 2つ目のXテクスチャ座標(倍率)
	 * @param ty2 2つ目のYテクスチャ座標(倍率)
	 */
	private static void drawTextureAbsTrim(final float vx1, final float vy1, final float vx2, final float vy2, final float rx1, final float ry1, final float rx2, final float ry2, final float tx1, final float ty1, final float tx2, final float ty2) {
		final float ox1 = tx2-tx1;
		final float oy1 = ty2-ty1;
		final float ox2 = vx2-vx1;
		final float oy2 = vy2-vy1;
		final float ox3 = ox2/ox1;
		final float oy3 = oy2/oy1;
		final float ox4 = (rx1-vx1)/ox3;
		final float oy4 = (ry1-vy1)/oy3;
		final float ox5 = (rx2-vx1)/ox3;
		final float oy5 = (ry2-vy1)/oy3;
		final float ox6 = (rx2-rx1)/ox3;
		final float oy6 = (ry2-ry1)/oy3;
		final float ox7 = ox2/ox1*ox6;
		final float oy7 = oy2/oy1*oy6;
		beginTextureQuads()
				.pos(rx1, ry1+oy7, 0).tex(ox4+tx1, oy5+ty1)
				.pos(rx1+ox7, ry1+oy7, 0).tex(ox5+tx1, oy5+ty1)
				.pos(rx1+ox7, ry1, 0).tex(ox5+tx1, oy4+ty1)
				.pos(rx1, ry1, 0).tex(ox4+tx1, oy4+ty1)
				.draw();
	}

	/**
	 * 4つの絶対座標からテクスチャを描画します
	 * <p>
	 * 画像絶対座標サイズのテクスチャをトリミング絶対座標でくり抜きます。
	 * @param vx1 1つ目のX画像絶対座標
	 * @param vy1 1つ目のY画像絶対座標
	 * @param vx2 2つ目のX画像絶対座標
	 * @param vy2 2つ目のY画像絶対座標
	 * @param rx1 1つ目のXトリミング絶対座標
	 * @param ry1 1つ目のYトリミング絶対座標
	 * @param rx2 2つ目のXトリミング絶対座標
	 * @param ry2 2つ目のYトリミング絶対座標
	 * @param tx1 1つ目のXテクスチャ座標(倍率)
	 * @param ty1 1つ目のYテクスチャ座標(倍率)
	 * @param tx2 2つ目のXテクスチャ座標(倍率)
	 * @param ty2 2つ目のYテクスチャ座標(倍率)
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private static void drawTextureAbsTrimOne(final float vx1, final float vy1, final float vx2, final float vy2, final float rx1, final float ry1, final float rx2, final float ry2, final float tx1, final float ty1, final float tx2, final float ty2) {
		drawTextureAbsTrim(vx1, vy1, vx2, vy2, Math.max(vx1, rx1), Math.max(vy1, ry1), Math.min(vx2, rx2), Math.min(vy2, ry2), tx1, ty1, tx2, ty2);
	}

	/**
	 * 絶対範囲からテクスチャを描画します
	 * <p>
	 * テクスチャ範囲は(0, 0)⇒(1, 1)にすることでテクスチャを1枚表示でき、nullが指定された場合と同様です
	 * @param vertex 絶対範囲 デフォルト:(0, 0)⇒(1, 1)
	 * @param trim トリミング範囲 デフォルト:(-∞, -∞)⇒(∞, ∞)
	 * @param texture テクスチャ範囲 デフォルト:(0, 0)⇒(1, 1)
	 */
	public static void drawTexture(@Nullable final Area vertex, @Nullable Area trim, @Nullable final Area texture) {
		final Area v = vertex!=null ? vertex : defaultTextureArea;
		final Area t = texture!=null ? texture : defaultTextureArea;
		if (trim!=null) {
			trim = v.trimArea(trim);
			if (trim!=null)
				drawTextureAbsTrim(v.x1(), v.y1(), v.x2(), v.y2(), trim.x1(), trim.y1(), trim.x2(), trim.y2(), t.x1(), t.y1(), t.x2(), t.y2());
		} else
			drawTextureAbs(v.x1(), v.y1(), v.x2(), v.y2(), t.x1(), t.y1(), t.x2(), t.y2());
	}

	/**
	 * テクスチャ倍率(1/256)をかけ、{@link #drawTexture(Area Area Area)}と同様に描画します。
	 * <p>
	 * GUIを描画する場合主にこちらを使用します。
	 * @param vertex 絶対範囲 デフォルト:(0, 0)⇒(1, 1)
	 * @param trim トリミング範囲 デフォルト:(-∞, -∞)⇒(∞, ∞)
	 * @param texture テクスチャ範囲 デフォルト:(0, 0)⇒((1/256), (1/256))
	 */
	public static void drawTextureModal(@Nullable final Area vertex, final @Nullable Area trim, @Nullable final Area texture) {
		drawTexture(vertex, trim, (texture!=null ? texture : defaultTextureArea).scale(textureScale));
	}

	/**
	 * 4つの絶対座標とGL描画モードを使用して描画します。
	 * @param x1 1つ目のX絶対座標
	 * @param y1 1つ目のY絶対座標
	 * @param x2 2つ目のX絶対座標
	 * @param y2 2つ目のY絶対座標
	 * @param mode GL描画モード
	 */
	private static void drawAbs(final float x1, final float y1, final float x2, final float y2, final int mode) {
		begin(mode)
				.pos(x1, y2, 0)
				.pos(x2, y2, 0)
				.pos(x2, y1, 0)
				.pos(x1, y1, 0)
				.draw();
	}

	/**
	 * 絶対範囲とGL描画モードを使用して描画します。
	 * @param vertex 絶対範囲 デフォルト:(0, 0)⇒(1, 1)
	 * @param mode GL描画モード
	 */
	public static void draw(@Nullable final Area vertex, final int mode) {
		final Area v = vertex!=null ? vertex : defaultTextureArea;
		drawAbs(v.x1(), v.y1(), v.x2(), v.y2(), mode);
	}

	/**
	 * 絶対範囲を使用して描画します。
	 * <p>
	 * GL描画モードは{@link org.lwjgl.opengl.GL11#GL_QUADS GL_QUADS}が使用されます
	 * @param vertex 絶対範囲 デフォルト:(0, 0)⇒(1, 1)
	 */
	public static void draw(@Nullable final Area vertex) {
		draw(vertex, GL_QUADS);
	}

	/**
	 * 文字列を描画します
	 * @param text 文字列
	 * @param x X絶対座標
	 * @param y Y絶対座標
	 * @param w 絶対幅
	 * @param h 絶対高さ
	 * @param align 水平寄せ
	 * @param valign 垂直寄せ
	 * @param shadow 影を付ける場合true
	 */
	public static void drawString(final @Nonnull String text, final float x, final float y, final float w, final float h, final @Nonnull Align align, final @Nonnull VerticalAlign valign, final boolean shadow) {
		OpenGL.glPushMatrix();
		align.translate(text, x, w);
		valign.translate(text, y, h);
		buf.clear();
		GL11.glGetFloat(GL11.GL_CURRENT_COLOR, buf);
		final float r = buf.get(0);
		final float g = buf.get(1);
		final float b = buf.get(2);
		final float a = buf.get(3);
		OpenGL.glColor4f(1f, 1f, 1f, 1f);
		Compat.getFontRenderer().drawString(text, 0, 0, Math.max((int) (a*255+0.5)&0xff, 0x4)<<24|((int) (r*255+0.5)&0xFF)<<16|((int) (g*255+0.5)&0xFF)<<8|((int) (b*255+0.5)&0xFF)<<0, shadow);
		OpenGL.glColor4f(r, g, b, a);
		OpenGL.glPopMatrix();
	}

	/**
	 * 文字列を描画します
	 * @param text 文字列
	 * @param a 絶対範囲
	 * @param align 水平寄せ
	 * @param valign 垂直寄せ
	 * @param shadow 影を付ける場合true
	 */
	public static void drawString(final @Nonnull String text, final @Nonnull Area a, final @Nonnull Align align, final @Nonnull VerticalAlign valign, final boolean shadow) {
		drawString(text, a.x1(), a.y1(), a.w(), a.h(), align, valign, shadow);
	}

	/**
	 * 水平寄せ
	 *
	 * @author TeamFruit
	 */
	public static enum Align {
		/**
		 * 左寄せ
		 */
		LEFT {
			@Override
			protected void translate(final @Nonnull String text, final float x, final float w) {
				OpenGL.glTranslatef(x, 0, 0);
			}
		},
		/**
		 * 中央寄せ
		 */
		CENTER {
			@Override
			protected void translate(final @Nonnull String text, final float x, final float w) {
				OpenGL.glTranslatef(x+(w-getStringWidth(text))/2, 0, 0);
			}
		},
		/**
		 * 右寄せ
		 */
		RIGHT {
			@Override
			protected void translate(final @Nonnull String text, final float x, final float w) {
				OpenGL.glTranslatef(x-getStringWidth(text), 0, 0);
			}
		},
		;
		protected abstract void translate(@Nonnull String text, float x, float w);
	}

	/**
	 * 垂直寄せ
	 *
	 * @author TeamFruit
	 */
	public static enum VerticalAlign {
		/**
		 * 上寄せ
		 */
		TOP {
			@Override
			protected void translate(final @Nonnull String text, final float y, final float h) {
				OpenGL.glTranslatef(0, y, 0);
			}
		},
		/**
		 * 中央寄せ
		 */
		MIDDLE {
			@Override
			protected void translate(final @Nonnull String text, final float y, final float h) {
				OpenGL.glTranslatef(0, y+(h-Compat.getFontRenderer().getFontRendererObj().FONT_HEIGHT)/2, 0);
			}
		},
		/**
		 * 下寄せ
		 */
		BOTTOM {
			@Override
			protected void translate(final @Nonnull String text, final float y, final float h) {
				OpenGL.glTranslatef(0, y+h-Compat.getFontRenderer().getFontRendererObj().FONT_HEIGHT, 0);
			}
		},
		;
		protected abstract void translate(@Nonnull String text, float y, float h);
	}

	/**
	 * 文字列の幅
	 * @param s 文字列
	 * @return 幅
	 */
	public static int getStringWidth(final @Nonnull String s) {
		if (StringUtils.isEmpty(s))
			return 0;
		return Compat.getFontRenderer().getStringWidthWithoutFormattingCodes(s);
	}
}
