package net.teamfruit.bnnwidget;

import javax.annotation.Nonnull;

import net.teamfruit.bnnwidget.position.R;

/**
 * デフォルトのパネル実装です。パネルの中にウィジェットを配置することができます。
 *
 * @author TeamFruit
 */
public class WPanel extends WTypedPanel<WCommon> {
	public WPanel(final @Nonnull R position) {
		super(position);
	}
}
