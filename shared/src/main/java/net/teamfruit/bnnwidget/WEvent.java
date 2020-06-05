package net.teamfruit.bnnwidget;

import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.lwjgl.util.Timer;

import com.google.common.eventbus.EventBus;

import net.teamfruit.bnnwidget.render.WRenderer;

/**
 * いくつかのデータをGUI全体で共有するのに役立ちます。
 * <p>
 * イベントバスを持ち、独自のイベントを発生させることができます。
 * <p>
 * これはGUIで管理される単一のインスタンスです。イベントはGUI単位で管理されます。
 * @see EventBus
 * @author TeamFruit
 */
public class WEvent {
	/**
	 * デフォルトのダブルクリック間隔です。
	 */
	public static final int DefaultMultiClickInterval = 500;

	/**
	 * ユーザー設定のダブルクリック間隔を取得します。
	 * @return ユーザー設定のダブルクリック間隔
	 */
	public static int getUserMultiClickInterval() {
		final Object o = Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
		if (o instanceof Integer)
			return (Integer) o;
		return DefaultMultiClickInterval;
	}

	/**
	 * このイベントインスタンスを管理しているGUIです。
	 */
	public final @Nonnull WFrame owner;
	/**
	 * データを保持するのに役立ちます。
	 */
	public final @Nonnull Map<String, Object> data;
	/**
	 * GUIで管理されるイベントバスです。
	 */
	public final @Nonnull EventBus bus;
	/**
	 * ユーザー設定のダブルクリック間隔を取得します。
	 */
	public final int multiClickInterval;
	private Timer lastClickedTime = new Timer();
	private boolean isDoubleClicked;

	public WEvent(final @Nonnull WFrame owner) {
		this.owner = owner;
		this.data = new HashMap<String, Object>();
		this.bus = new EventBus();
		this.multiClickInterval = getUserMultiClickInterval();
	}

	/**
	 * 現在の画面がこのGUIであるかを確認します。
	 * @return
	 */
	public boolean isCurrent() {
		return WRenderer.mc.currentScreen==this.owner.getScreen();
	}

	/**
	 * このクリックがダブルクリックかどうか
	 * @return このクリックがダブルクリックの場合true
	 */
	public boolean isDoubleClick() {
		return this.isDoubleClicked;
	}

	protected void updateDoubleClick() {
		if (this.lastClickedTime.getTime()*1000<this.multiClickInterval)
			this.isDoubleClicked = true;
		else {
			this.lastClickedTime.reset();
			this.isDoubleClicked = false;
		}
	}
}
