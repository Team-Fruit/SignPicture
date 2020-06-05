package net.teamfruit.bnnwidget;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.position.Point;
import net.teamfruit.bnnwidget.render.RenderOption;

/**
 * 全ての軽量コンポーネントの基盤となるインターフェイスです。
 *
 * @author TeamFruit
 */
public interface WCommon {
	/**
	 * コンポーネントが他のコンポーネントに追加されたときに呼ばれます。
	 * <p>
	 * 主に初期化処理を行います。
	 */
	void onAdded();

	/**
	 * <b>重量コンポーネントが</b>初期化されるとき、一度だけ呼ばれます。
	 * <p>
	 * <b>※後からaddによって追加された場合、このメソッドが呼ばれることはありません。
	 * @param ev イベント
	 * @param pgp 親コンポーネントの絶対座標
	 * @param p カーソル絶対座標
	 */
	void onInit(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point p);

	/**
	 * コンポーネントが描画されるときに呼ばれます。
	 * @param ev イベント
	 * @param pgp 親コンポーネントの絶対座標
	 * @param p カーソル絶対座標
	 * @param frame 描画されるタイミングのpartialTicksです。
	 * @param popacity 親コンポーネントの絶対透明度
	 * @param opt レンダリングオプション
	 */
	void draw(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point p, float frame, float popacity, @Nonnull RenderOption opt);

	/**
	 * コンポーネントが更新されるときに呼ばれます。
	 * @param ev イベント
	 * @param pgp 親コンポーネントの絶対座標
	 * @param p カーソル絶対座標
	 */
	void update(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point p);

	/**
	 * キー入力が発生した際に呼ばれます。
	 * @param ev イベント
	 * @param pgp 親コンポーネントの絶対座標
	 * @param p カーソル絶対座標
	 * @param c 入力文字
	 * @param keycode 入力キーコード
	 * @return イベントを受け取った場合はtrue
	 */
	boolean keyTyped(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point p, char c, int keycode);

	/**
	 * スクロールされた際に呼ばれます。
	 * @param ev イベント
	 * @param pgp 親コンポーネントの絶対座標
	 * @param p カーソル絶対座標
	 * @param scroll スクロール量
	 * @return イベントを受け取った場合はtrue
	 */
	boolean mouseScrolled(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point p, int scroll);

	/**
	 * カーソルが移動された際に呼ばれます。
	 * @param ev イベント
	 * @param pgp 親コンポーネントの絶対座標
	 * @param p カーソル絶対座標
	 * @param button 入力中のボタン
	 * @return イベントを受け取った場合はtrue
	 */
	boolean mouseMoved(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point p, int button);

	/**
	 * クリックされた際に呼ばれます。
	 * @param ev イベント
	 * @param pgp 親コンポーネントの絶対座標
	 * @param p カーソル絶対座標
	 * @param button クリックされたボタン
	 * @return イベントを受け取った場合はtrue
	 */
	boolean mouseClicked(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point p, int button);

	/**
	 * マウスがドラッグされた際に呼ばれます。
	 * @param ev イベント
	 * @param pgp 親コンポーネントの絶対座標
	 * @param p カーソル絶対座標
	 * @param button 入力中のボタン
	 * @param time 入力された時間。
	 * @return イベントを受け取った場合はtrue
	 */
	boolean mouseDragged(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point p, int button, long time);

	/**
	 * リリースされた際に呼ばれます。
	 * @param ev イベント
	 * @param pgp 親コンポーネントの絶対座標
	 * @param p カーソル絶対座標
	 * @param button 離したボタン
	 * @return イベントを受け取った場合はtrue
	 */
	boolean mouseReleased(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point p, int button);

	/**
	 * コンポーネントが閉じられる際に呼ばれます。
	 * <p>
	 * 何か終了処理を行う場合はfalseを返してください。trueを返した場合、コンポーネントはすぐに閉じられます。
	 * @return 終了処理を行う場合はfalse
	 */
	boolean onCloseRequest();

	/**
	 * コンポーネントを閉じている際に呼ばれます。
	 * <p>
	 * コンポーネントはtrueを返すまで終了しません。{@link #onCloseRequest onCloseRequest}でtrueを返した場合、このメソッドが呼ばれることはありません。
	 * @param ev イベント
	 * @param pgp 親コンポーネントの絶対座標
	 * @param p カーソル絶対座標
	 * @return イベントを受け取った場合はtrue
	 */
	boolean onClosing(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point p);

	/**
	 * カーソルが最上面にあるかどうか
	 * <p>
	 * 非コンテナーコンポーネントは通常、範囲内にカーソルがあるかどうかを返します。
	 * @param ev イベント
	 * @param pgp 親コンポーネントの絶対座標
	 * @param p カーソル絶対座標
	 * @return カーソルが最上面にある場合はtrue
	 */
	@Nullable
	WCommon top(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point p);

}
