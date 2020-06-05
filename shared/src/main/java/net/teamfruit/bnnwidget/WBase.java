package net.teamfruit.bnnwidget;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.position.Point;
import net.teamfruit.bnnwidget.position.R;
import net.teamfruit.bnnwidget.var.V;
import net.teamfruit.bnnwidget.var.VCommon;

/**
 * 位置・透明度を管理する実装が入った、コンポーネントの基盤です。
 *
 * @author TeamFruit
 */
public abstract class WBase extends WComponent {
	/**
	 * コンポーネントの相対範囲を保持します。
	 */
	protected @Nonnull R position;
	/**
	 * コンポーネントの相対透明度を保持します。
	 */
	protected @Nonnull VCommon opacity;

	public WBase(final @Nonnull R position) {
		this.position = initPosition(position);
		this.opacity = initOpacity();
	}

	/**
	 * このメソッドをオーバーライドすることで、相対範囲の初期化を変更することができます。
	 * <p>
	 * このメソッドはコンストラクタ内で呼ばれます。サブクラスのフィールドはまだ初期化されていない可能性があります。
	 * @param position コンストラクタで受け取った相対範囲
	 * @return コンポーネントの相対範囲
	 */
	@OverridablePoint
	protected @Nonnull R initPosition(final @Nonnull R position) {
		return position;
	}

	/**
	 * このメソッドをオーバーライドすることで、相対透明度の初期化を変更することができます。
	 * <p>
	 * このメソッドはコンストラクタ内で呼ばれます。サブクラスのフィールドはまだ初期化されていない可能性があります。
	 * @return コンポーネントの相対透明度
	 */
	@OverridablePoint
	protected @Nonnull VCommon initOpacity() {
		return V.p(1f);
	}

	/**
	 * コンポーネントの相対範囲
	 * @return コンポーネントの相対範囲
	 */
	public @Nonnull R getGuiPosition() {
		return this.position;
	}

	/**
	 * コンポーネントの相対透明度
	 * @return コンポーネントの相対透明度
	 */
	public @Nonnull VCommon getGuiOpacity() {
		return this.opacity;
	}

	/**
	 * コンポーネントの絶対範囲
	 * <p>
	 * 相対範囲は、このメソッドを使用して絶対範囲に変換される必要があります。
	 * @param pgp 親コンポーネントの絶対範囲
	 * @return コンポーネントの絶対範囲
	 */
	public @Nonnull Area getGuiPosition(final @Nonnull Area pgp) {
		return pgp.child(getGuiPosition());
	}

	/**
	 * コンポーネントの絶対透明度
	 * <p>
	 * 相対透明度は、このメソッドを使用して絶対透明度に変換される必要があります。
	 * @param popacity 親コンポーネントの絶対透明度
	 * @return コンポーネントの絶対透明度
	 */
	public float getGuiOpacity(final float popacity) {
		return getGuiOpacity().getAbsCoord(0, popacity);
	}

	@Override
	public @Nullable WCommon top(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point point) {
		final Area a = getGuiPosition(pgp);
		if (a.pointInside(point))
			return this;
		return null;
	}
}
