package net.teamfruit.bnnwidget.component;

import javax.annotation.Nonnull;

import net.teamfruit.bnnwidget.OverridablePoint;
import net.teamfruit.bnnwidget.WEvent;
import net.teamfruit.bnnwidget.compat.Compat;
import net.teamfruit.bnnwidget.compat.OpenGL;
import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.position.Point;
import net.teamfruit.bnnwidget.position.R;
import net.teamfruit.bnnwidget.render.RenderOption;
import net.teamfruit.bnnwidget.render.WRenderer;

/**
 * チェックボックス
 *
 * @author TeamFruit
 */
public class MCheckBox extends MLabel {
	/**
	 * チェック状態
	 */
	protected boolean checked = true;

	public MCheckBox(final @Nonnull R position) {
		super(position);
	}

	/**
	 * チェック状態を設定します
	 * @param check チェックされている場合true
	 */
	public void check(final boolean check) {
		this.checked = check;
		onCheckChanged(!check);
	}

	/**
	 * チェック状態
	 * @return チェックされている場合true
	 */
	public final boolean isCheck() {
		return this.checked;
	}

	/**
	 * チェック状態が変更された際に呼ばれます
	 * @param oldCheck
	 */
	@OverridablePoint
	protected void onCheckChanged(final boolean oldCheck) {
	}

	@Override
	public boolean mouseClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
		final Area a = getGuiPosition(pgp);
		if (a.pointInside(p)) {
			check(!this.checked);
			MButton.playPressButtonSound();
			return true;
		}
		return false;
	}

	@Override
	public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
		final Area o = getGuiPosition(pgp);
		final Area a = Area.abs(o.x1(), o.y1(), o.x1()+o.h(), o.y2());
		drawCheckBox(a);
		final Area b = o.child(o.x1()+o.h(), 0, o.x1()+o.h(), 0);
		drawText(ev, b, p, frame, getGuiOpacity(popacity));
	}

	/**
	 * チェックボックスを描画します
	 * @param out 絶対座標
	 */
	protected void drawCheckBox(final @Nonnull Area out) {
		final Area in = out.child(1, 1, -1, -1);
		WRenderer.startShape();
		OpenGL.glColor4f(0.627451f, 0.627451f, 0.627451f, 1f);
		draw(out);
		OpenGL.glColor4f(0f, 0f, 0f, 1f);
		draw(in);
		WRenderer.startTexture();
		if (this.checked) {
			final String strcheck = "\u2713";
			OpenGL.glPushMatrix();
			OpenGL.glTranslatef(in.x1()+(in.w()-Compat.getFontRenderer().getStringWidth(strcheck))/2, in.y1()+(in.h()-Compat.getFontRenderer().getFontRendererObj().FONT_HEIGHT)/2, 0f);
			//glScalef(2f, 2f, 1f);
			Compat.getFontRenderer().drawStringWithShadow(strcheck, 0, 0, 0xffffff);
			OpenGL.glPopMatrix();
		}
	}
}
