package net.teamfruit.bnnwidget;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.teamfruit.bnnwidget.compat.OpenGL;
import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.position.Point;
import net.teamfruit.bnnwidget.position.R;
import net.teamfruit.bnnwidget.render.RenderOption;
import net.teamfruit.bnnwidget.render.WRenderer;

/**
 * MinecraftのGUIとウィジェットをつなぐ重量コンポーネントです。
 * <p>
 * 全てのウィジェットはこの重量コンポーネントの上で動作し、描画されます。
 * <p>
 * {@link net.minecraft.client.Minecraft#displayGuiScreen(GuiScreen) displayGuiScreen(GuiScreen)}メソッドなどでGUIを開く際はこのクラスのインスタンスを渡す必要があります。
 *
 * @author TeamFruit
 */
public class WFrame implements WCommon, WContainer<WCommon> {
	/**
	 * GUIの背後に表示するGUIです
	 * <p>
	 * ウィジェットを他のGUIの上で動作させる場合に役立ちます
	 */
	protected @Nullable GuiScreen parent;
	/**
	 * ルート・パネルです。すべてのウィジェットはこのパネルの中に配置されます。
	 */
	private @Nonnull WPanel contentPane = new WPanel(new R());
	/**
	 * これはGUIで管理されるイベントです。
	 */
	protected final @Nonnull WEvent event = new WEvent(this);
	/**
	 * ウィジェットが初期化されたかどうかを保持します。
	 * <p>
	 * これは{@link #initWidget()}を一度だけ呼び出すのに役立ちます。
	 */
	protected boolean initialized;
	/**
	 * 画面サイズによるサイズ変更の影響を受けなくする場合はtrue
	 */
	protected boolean fixGuiScale;
	/**
	 * シングルプレイ時にゲームを一時停止させる場合はtrue
	 * @see GuiScreen#doesGuiPauseGame()
	 */
	protected boolean doesPauseGui = true;
	/**
	 * このフラグをtrueにするとこのGUIの一切の処理が行われなくなります。
	 */
	protected boolean closed = false;
	private float width;
	private float height;

	/**
	 * ウィジェットを終了する際のフラグです。
	 */
	protected boolean closeRequest;

	protected @Nonnull WScreenImpl screen = new WScreenImpl(this);

	/**
	 * ウィジェットからGUIを取得します
	 * @return GUI
	 */
	public @Nonnull GuiScreen getScreen() {
		return this.screen;
	}

	/**
	 * ウィジェットからGUIを取得します
	 * @return GUI
	 */
	public static @Nullable GuiScreen getScreen(@Nullable final WFrame frame) {
		return frame==null ? null : frame.getScreen();
	}

	/**
	 * GUIを描画します
	 */
	public static void displayGuiScreen(final GuiScreen screen) {
		WRenderer.mc.displayGuiScreen(screen);
	}

	/**
	 * GUIを描画します
	 */
	public static void displayFrame(final WFrame frame) {
		displayGuiScreen(frame.getScreen());
	}

	/**
	 * float精度で幅を設定します
	 * @param width 幅
	 * @return this
	 */
	public @Nonnull WFrame setWidth(final float width) {
		this.width = width;
		this.screen.width = (int) width;
		return this;
	}

	/**
	 * float精度で高さを設定します
	 * @param height 高さ
	 * @return this
	 */
	public @Nonnull WFrame setHeight(final float height) {
		this.height = height;
		this.screen.height = (int) height;
		return this;
	}

	/**
	 * float精度の幅
	 * @return float精度の幅
	 */
	public float guiWidth() {
		if (this.screen.width!=(int) this.width)
			this.width = this.screen.width;
		return this.width;
	}

	/**
	 * float精度の高さ
	 * @return float精度の高さ
	 */
	public float guiHeight() {
		if (this.screen.height!=(int) this.height)
			this.height = this.screen.height;
		return this.height;
	}

	/**
	 * float精度の幅 (GUIスケールが固定されている場合はMinecraftの画面の幅)
	 * @return float精度の幅
	 */
	public float width() {
		if (this.fixGuiScale)
			return getDisplayWidth();
		return guiWidth();
	}

	/**
	 * float精度の高さ (GUIスケールが固定されている場合はMinecraftの画面の高さ)
	 * @return float精度の高さ
	 */
	public float height() {
		if (this.fixGuiScale)
			return getDisplayHeight();
		return guiHeight();
	}

	/**
	 * Minecraftの画面の幅
	 * @return Minecraftの画面の幅
	 */
	protected static float getDisplayWidth() {
		return WRenderer.mc.displayWidth;
	}

	/**
	 * Minecraftの画面の高さ
	 * @return Minecraftの画面の高さ
	 */
	protected static float getDisplayHeight() {
		return WRenderer.mc.displayHeight;
	}

	/**
	 * このGUIの幅がMinecraftの画面の幅よりも何倍であるか
	 * <p>
	 * この時、GUIは1/n倍されています。
	 * @return このGUIの幅がMinecraftの画面の幅よりも何倍であるか
	 */
	public float guiScaleX() {
		return guiWidth()/getDisplayWidth();
	}

	/**
	 * このGUIの高さがMinecraftの画面の高さよりも何倍であるか
	 * <p>
	 * この時、GUIは1/n倍されています。
	 * @return このGUIの高さがMinecraftの画面の高さよりも何倍であるか
	 */
	public float guiScaleY() {
		return guiHeight()/getDisplayHeight();
	}

	/**
	 * このGUIの大きさがMinecraftの画面の大きさよりも何倍であるか
	 * <p>
	 * この時、GUIは1/n倍されています。
	 * <br>
	 * 縦と横の比率が違うことは考え難いですが、違う場合は小さい方の値が返されます。
	 * @return GUIのコンテンツが、Minecraftの画面の何分の1で描画されるか
	 */
	public float guiScale() {
		return Math.min(guiScaleX(), guiScaleY());
	}

	/**
	 * GUIのコンテンツが、Minecraftの画面の何分の1で描画されるか
	 * @return GUIのコンテンツが、Minecraftの画面の何分の1で描画されるか
	 */
	public float scaleX() {
		return 1f/guiScaleX();
	}

	/**
	 * GUIのコンテンツが、Minecraftの画面の何分の1で描画されるか
	 * @return GUIのコンテンツが、Minecraftの画面の何分の1で描画されるか
	 */
	public float scaleY() {
		return 1f/guiScaleY();
	}

	/**
	 * GUIのコンテンツが、Minecraftの画面の何分の1で描画されるか
	 * <p>
	 * 縦と横の比率が違うことは考え難いですが、違う場合は大きい方の値が返されます。
	 * @return GUIのコンテンツが、Minecraftの画面の何分の1で描画されるか
	 */
	public float scale() {
		return 1f/guiScale();
	}

	/**
	 * 親GUIを指定してGUIを作成します
	 * @param parent 親GUI
	 */
	public WFrame(final @Nullable GuiScreen parent) {
		this.parent = parent;
		this.screen.mc = WRenderer.mc;
	}

	public WFrame() {
	}

	/**
	 * GUIの絶対座標
	 * @return GUIの絶対座標
	 */
	public @Nonnull Area getAbsolute() {
		return Area.size(0, 0, width(), height());
	}

	/**
	 * カーソルの絶対座標
	 * @return カーソルの絶対座標
	 */
	public @Nonnull Point getMouseAbsolute() {
		return new Point(Mouse.getX()*width()/getDisplayWidth(),
				height()-Mouse.getY()*height()/getDisplayHeight()-1);
	}

	@Override
	public @Nonnull List<WCommon> getContainer() {
		return getContentPane().getContainer();
	}

	@Override
	public boolean add(final @Nonnull WCommon widget) {
		return getContentPane().add(widget);
	}

	@Override
	public boolean remove(final @Nonnull WCommon widget) {
		return getContentPane().remove(widget);
	}

	/**
	 * ルート・パネルです。すべてのウィジェットはこのパネルの中に配置されます。
	 * @return ルート・パネル
	 */
	public @Nonnull WPanel getContentPane() {
		return this.contentPane;
	}

	/**
	 * ルート・パネルを設定します。すべてのウィジェットはこのパネルの中に配置されます。
	 * @return ルート・パネル
	 */
	public void setContentPane(final @Nonnull WPanel panel) {
		this.contentPane = panel;
	}

	public void initGui() {
		sInitGui();
		if (!this.initialized) {
			initPane();
			initWidget();
			this.initialized = true;
		}
	}

	protected void sInitGui() {
		checkParentAndClose();
		if (this.parent!=null)
			this.parent.initGui();
		this.screen.sInitGui();
	}

	protected void initPane() {
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		dispatchOnInit(gp, p);
	}

	/**
	 * ウィジェットを初期化します。
	 * <p>
	 * このメソッドはGUIの初期化時に一度だけ呼び出されます。
	 * <p>
	 * オーバーライドしてGUIの構築を行いましょう。
	 */
	@OverridablePoint
	protected void initWidget() {
	}

	public void setWorldAndResolution(final @Nullable Minecraft mc, final int i, final int j) {
		sSetWorldAndResolution(WRenderer.mc, i, j);
	}

	protected void sSetWorldAndResolution(final @Nonnull Minecraft mc, final int i, final int j) {
		checkParentAndClose();
		if (this.parent!=null)
			this.parent.setWorldAndResolution(mc, i, j);
		this.screen.sSetWorldAndResolution(mc, i, j);
	}

	public void drawScreen(final int mousex, final int mousey, final float f, final float opacity, final @Nullable RenderOption opt) {
		sDrawScreen(mousex, mousey, f);
		OpenGL.glPushMatrix();
		if (this.fixGuiScale)
			OpenGL.glScalef(guiScaleX(), guiScaleY(), 1f);
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		dispatchDraw(gp, p, f, opacity, opt!=null ? opt : new RenderOption());
		OpenGL.glPopMatrix();
	}

	protected void sDrawScreen(final int mousex, final int mousey, final float f) {
		checkParentAndClose();
		final GuiScreen parent = this.parent;
		if (parent!=null) {
			OpenGL.glPushMatrix();
			OpenGL.glTranslatef(0, 0, -200f);
			parent.drawScreen(mousex, mousey, f);
			OpenGL.glPopMatrix();
		}
		this.screen.sDrawScreen(mousex, mousey, f);
	}

	public void drawScreen(final int mousex, final int mousey, final float f) {
		drawScreen(mousex, mousey, f, getOpacity(), null);
	}

	/**
	 * GUIの絶対透明度
	 * @return 絶対透明度
	 */
	@OverridablePoint
	protected float getOpacity() {
		return 1f;
	}

	protected void mouseClicked(final int x, final int y, final int button) {
		this.mousebutton.addButton(button);
		this.event.updateDoubleClick();
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		dispatchMouseClicked(gp, p, button);
		sMouseClicked(x, y, button);
	}

	protected void sMouseClicked(final int x, final int y, final int button) {
		this.screen.sMouseClicked(x, y, button);
	}

	protected void mouseClickMove(final int x, final int y, final int button, final long time) {
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		dispatchMouseDragged(gp, p, button, time);
		this.event.updateDoubleClick();
		sMouseClickMove(x, y, button, time);
	}

	protected void sMouseClickMove(final int x, final int y, final int button, final long time) {
		this.screen.sMouseClickMove(x, y, button, time);
	}

	/**
	 * マウスボタン
	 */
	public static class MouseButton {
		private final @Nonnull Set<Integer> pressed = Sets.newHashSet();

		private final @Nonnull MouseState state = new MouseState();

		public @Nonnull MouseState checkButton() {
			this.state.reset();
			for (final Iterator<Integer> itr = this.pressed.iterator(); itr.hasNext();) {
				final Integer button = itr.next();
				if (!Mouse.isButtonDown(button)) {
					this.state.removed.add(button);
					this.state.lastRemoved = button;
					itr.remove();
				} else {
					this.state.pressed.add(button);
					this.state.lastPressed = button;
				}
			}
			return this.state;
		}

		public void addButton(final int button) {
			this.pressed.add(button);
		}

		public static class MouseState {
			public final @Nonnull Set<Integer> removed = Sets.newHashSet();
			public @Nullable Integer lastRemoved;
			public final @Nonnull Set<Integer> pressed = Sets.newHashSet();
			public @Nullable Integer lastPressed;

			public void reset() {
				this.removed.clear();
				this.lastRemoved = null;
				this.pressed.clear();
				this.lastPressed = null;
			}
		}
	}

	/**
	 * 最後に入力されたカーソル位置を保持します
	 */
	protected @Nullable Point mouselast;

	/**
	 * 最後に入力されたマウスボタンを保持します
	 */
	protected final @Nonnull MouseButton mousebutton = new MouseButton();

	public void updateScreen() {
		sUpdateScreen();
		final Point p = getMouseAbsolute();
		final Area gp = getAbsolute();
		dispatchUpdate(gp, p);
		final MouseButton.MouseState button = this.mousebutton.checkButton();
		final Integer lastRemoved = button.lastRemoved;
		if (lastRemoved!=null)
			dispatchMouseReleased(gp, p, lastRemoved);
		if (!p.equals(this.mouselast)) {
			this.mouselast = p;
			dispatchMouseMoved(gp, p, button.lastPressed!=null ? button.lastPressed : -1);
		}
		if (this.closeRequest)
			if (dispatchOnClosing(gp, p))
				close();
	}

	protected void sUpdateScreen() {
		checkParentAndClose();
		if (this.parent!=null)
			this.parent.updateScreen();
		this.screen.sUpdateScreen();
	}

	protected void keyTyped(final char c, final int keycode) {
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		dispatchKeyTyped(gp, p, c, keycode);
		sKeyTyped(c, keycode);
	}

	@OverridablePoint
	protected void sKeyTyped(final char c, final int keycode) {
		if (keycode==Keyboard.KEY_ESCAPE)
			requestClose();
	}

	/**
	 * GUIが終了される最終フェーズで呼び出されます。
	 */
	protected void close() {
		if (WRenderer.mc.currentScreen==this.screen)
			WRenderer.mc.displayGuiScreen(this.parent);
		else
			this.closed = true;
	}

	/**
	 * GUIを終了します。終了処理がある場合は終了処理を行った後、終了されます。
	 */
	public void requestClose() {
		dispatchOnCloseRequest();
		this.closeRequest = true;
	}

	/**
	 * 終了処理をキャンセルします。
	 * <p>
	 * この機能は正しく機能しません。
	 */
	@Deprecated
	protected void cancelCloseRequest() {
		this.closeRequest = false;
	}

	public void onGuiClosed() {
		onClosed();
		sOnGuiClosed();
	}

	public void sOnGuiClosed() {
		this.screen.sOnGuiClosed();
	}

	@OverridablePoint
	protected void onClosed() {
	}

	public void handleMouseInput() {
		final int i = Mouse.getEventDWheel();
		if (i!=0) {
			final Area gp = getAbsolute();
			final Point p = getMouseAbsolute();
			dispatchMouseScrolled(gp, p, i);
		}
		sHandleMouseInput();
	}

	protected void sHandleMouseInput() {
		this.screen.sHandleMouseInput();
	}

	public void handleKeyboardInput() {
		sHandleKeyboardInput();
	}

	protected void sHandleKeyboardInput() {
		this.screen.sHandleKeyboardInput();
	}

	public boolean doesGuiPauseGame() {
		return sDoesGuiPauseGame();
	}

	protected boolean sDoesGuiPauseGame() {
		return this.doesPauseGui||parentDoesGuiPauseGame();
	}

	protected boolean parentDoesGuiPauseGame() {
		checkParentAndClose();
		return this.parent!=null&&this.parent.doesGuiPauseGame();
	}

	/**
	 * 画面サイズによるサイズ変更の影響を受けなくします
	 * @param doesFixScale 画面サイズによるサイズ変更の影響を受けなくする場合はtrue
	 * @return this
	 */
	public @Nonnull WFrame setFixGuiScale(final boolean doesFixScale) {
		this.fixGuiScale = doesFixScale;
		return this;
	}

	/**
	 * シングルプレイ時にゲームを一時停止させるかどうかを設定します。
	 * @param doesPause シングルプレイ時にゲームを一時停止させる場合はtrue
	 * @return this
	 */
	public @Nonnull WFrame setGuiPauseGame(final boolean doesPause) {
		this.doesPauseGui = doesPause;
		return this;
	}

	/**
	 * 親GUIが閉じられているのを確認します
	 */
	public boolean isParentClosed() {
		if (this.parent instanceof WScreen)
			return ((WScreen) this.parent).getWidget().closed;
		return false;
	}

	/**
	 * 親GUIを閉じます
	 */
	public void closeParent() {
		this.parent = getParentOrNull(this.parent);
	}

	/**
	 * 親GUIが閉じられているのを確認し、親GUIを閉じます
	 */
	public void checkParentAndClose() {
		if (isParentClosed())
			closeParent();
	}

	/**
	 * 親GUIを設定します
	 * @param parent 親
	 */
	public void setParent(@Nullable final GuiScreen parent) {
		this.parent = parent;
	}

	/**
	 * 親GUIを返します
	 * @return 親
	 */
	public @Nullable GuiScreen getParent() {
		return this.parent;
	}

	/**
	 * 現在のGUIを返します
	 * @param screen GUI
	 * @return 親
	 */
	public static @Nullable GuiScreen getCurrent() {
		return WRenderer.mc.currentScreen;
	}

	/**
	 * 親GUIを返します
	 * <p>
	 * screenがWFrameでない場合はnullを返します
	 * @param screen GUI
	 * @return 親
	 */
	public static @Nullable GuiScreen getParentOrNull(@Nullable final GuiScreen screen) {
		if (screen instanceof WScreen)
			return ((WScreen) screen).getWidget().parent;
		return null;
	}

	/**
	 * 親GUIを返します
	 * <p>
	 * screenがWFrameでない場合はscreenを返します
	 * @param screen GUI
	 * @return 親
	 */
	public static @Nullable GuiScreen getParentOrThis(@Nullable final GuiScreen screen) {
		if (screen instanceof WScreen)
			return ((WScreen) screen).getWidget().parent;
		return screen;
	}

	/**
	 * 現在のGUIの親GUIを返します
	 * <p>
	 * 現在のGUIがWFrameでない場合はnullを返します
	 * @return 親
	 */
	public static @Nullable GuiScreen getParentOrNull() {
		return getParentOrNull(getCurrent());
	}

	/**
	 * 現在のGUIの親GUIを返します
	 * <p>
	 * 現在のGUIがWFrameでない場合は現在のGUIを返します
	 * @return 親
	 */
	public static @Nullable GuiScreen getParentOrThis() {
		return getParentOrThis(getCurrent());
	}

	/**
	 * GUIのウィジェットを返します
	 * @param screen GUI
	 * @return ウィジェット
	 */
	public static @Nullable WFrame getWidget(@Nullable final GuiScreen screen) {
		if (screen instanceof WScreen)
			return ((WScreen) screen).getWidget();
		return null;
	}

	/**
	 * 現在のGUIのウィジェットを返します
	 * @return ウィジェット
	 */
	public static @Nullable WFrame getCurrentWidget() {
		return getWidget(getCurrent());
	}

	/**
	 * GUIがウィジェットのインスタンスかどうかを判定します
	 * @param screen GUI
	 * @param widgettype 判定用ウィジェット
	 * @return ウィジェットのインスタンスかどうか
	 */
	public static boolean isInstanceOf(@Nullable final GuiScreen screen, @Nonnull final Class<?> widgettype) {
		return widgettype.isInstance(getWidget(screen));
	}

	/**
	 * GUIがウィジェットのインスタンスかどうかを判定します
	 * @param screen GUI
	 * @param widgettype 判定用ウィジェット
	 * @return ウィジェットのインスタンスかどうか
	 */
	public static boolean isInstanceOf(@Nullable final GuiScreen screen, @Nonnull final WFrame widgettype) {
		return isInstanceOf(screen, widgettype.getClass());
	}

	/**
	 * 現在のGUIがウィジェットのインスタンスかどうかを判定します
	 * @param widgettype 判定用ウィジェット
	 * @return ウィジェットのインスタンスかどうか
	 */
	public static boolean isCurrentInstanceOf(@Nonnull final Class<?> widgettype) {
		return isInstanceOf(getCurrent(), widgettype);
	}

	/**
	 * 現在のGUIがウィジェットのインスタンスかどうかを判定します
	 * @param widgettype 判定用ウィジェット
	 * @return ウィジェットのインスタンスかどうか
	 */
	public static boolean isCurrentInstanceOf(@Nonnull final WFrame widgettype) {
		return isInstanceOf(getCurrent(), widgettype);
	}

	/**
	 * 現在のGUIがウィジェットのインスタンスかどうかを判定し、GUIのウィジェットかnullを返します
	 * @param screen GUI
	 * @param widgettype 判定用ウィジェット
	 * @return ウィジェット
	 */
	public static @Nullable <T extends WFrame> T getWidgetWithCheck(@Nullable final GuiScreen screen, @Nonnull final Class<T> widgettype) {
		if (isInstanceOf(screen, widgettype)) {
			@SuppressWarnings("unchecked")
			final T typedwidget = (T) getWidget(screen);
			return typedwidget;
		}
		return null;
	}

	/**
	 * 現在のGUIがウィジェットのインスタンスかどうかを判定し、GUIのウィジェットかnullを返します
	 * @param screen GUI
	 * @param widgettype 判定用ウィジェット
	 * @return ウィジェット
	 */
	public static @Nullable <T extends WFrame> T getWidgetWithCheck(@Nullable final GuiScreen screen, @Nonnull final T widgettype) {
		if (isInstanceOf(screen, widgettype)) {
			@SuppressWarnings("unchecked")
			final T typedwidget = (T) getWidget(screen);
			return typedwidget;
		}
		return null;
	}

	/**
	 * 現在のGUIがウィジェットのインスタンスかどうかを判定し、GUIのウィジェットかnullを返します
	 * @param widgettype 判定用ウィジェット
	 * @return ウィジェット
	 */
	public static @Nullable <T extends WFrame> T getCurrentWidgetWithCheck(@Nonnull final Class<T> widgettype) {
		return getWidgetWithCheck(getCurrent(), widgettype);
	}

	/**
	 * 現在のGUIがウィジェットのインスタンスかどうかを判定し、GUIのウィジェットかnullを返します
	 * @param widgettype 判定用ウィジェット
	 * @return ウィジェット
	 */
	public static @Nullable <T extends WFrame> T getCurrentWidgetWithCheck(@Nonnull final T widgettype) {
		return getWidgetWithCheck(getCurrent(), widgettype);
	}

	@Override
	@OverridablePoint
	public void onAdded() {
	}

	protected void dispatchOnAdded() {
		onAdded();
		getContentPane().onAdded();
	}

	@Override
	@OverridablePoint
	public void onInit(@Nonnull final WEvent ev, @Nonnull final Area pgp, @Nonnull final Point p) {
	}

	protected void dispatchOnInit(final @Nonnull Area pgp, final @Nonnull Point p) {
		onInit(this.event, pgp, p);
		getContentPane().onInit(this.event, pgp, p);
	}

	@Deprecated
	@OverridablePoint
	public void draw(@Nonnull final WEvent ev, @Nonnull final Area pgp, @Nonnull final Point p, final float frame, final float popacity) {
	}

	@Override
	@OverridablePoint
	public void draw(@Nonnull final WEvent ev, @Nonnull final Area pgp, @Nonnull final Point p, final float frame, final float popacity, @Nonnull final RenderOption opt) {
		draw(ev, pgp, p, frame, popacity);
	}

	protected void dispatchDraw(final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, @Nonnull final RenderOption opt) {
		draw(this.event, pgp, p, frame, popacity, opt);
		getContentPane().draw(this.event, pgp, p, frame, popacity, opt);
	}

	@Override
	@OverridablePoint
	public void update(@Nonnull final WEvent ev, @Nonnull final Area pgp, @Nonnull final Point p) {
	}

	protected void dispatchUpdate(final @Nonnull Area pgp, final @Nonnull Point p) {
		update(this.event, pgp, p);
		getContentPane().update(this.event, pgp, p);
	}

	@Override
	@OverridablePoint
	public boolean keyTyped(@Nonnull final WEvent ev, @Nonnull final Area pgp, @Nonnull final Point p, final char c, final int keycode) {
		return false;
	}

	protected boolean dispatchKeyTyped(final @Nonnull Area pgp, final @Nonnull Point p, final char c, final int keycode) {
		return getContentPane().keyTyped(this.event, pgp, p, c, keycode)||keyTyped(this.event, pgp, p, c, keycode);
	}

	@Override
	@OverridablePoint
	public boolean mouseScrolled(@Nonnull final WEvent ev, @Nonnull final Area pgp, @Nonnull final Point p, final int scroll) {
		return false;
	}

	protected boolean dispatchMouseScrolled(final @Nonnull Area pgp, final @Nonnull Point p, final int scroll) {
		return getContentPane().mouseScrolled(this.event, pgp, p, scroll)||mouseScrolled(this.event, pgp, p, scroll);
	}

	@Override
	@OverridablePoint
	public boolean mouseMoved(@Nonnull final WEvent ev, @Nonnull final Area pgp, @Nonnull final Point p, final int button) {
		return false;
	}

	protected boolean dispatchMouseMoved(final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
		return getContentPane().mouseMoved(this.event, pgp, p, button)||mouseMoved(this.event, pgp, p, button);
	}

	@Override
	@OverridablePoint
	public boolean mouseClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
		return false;
	}

	protected boolean dispatchMouseClicked(final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
		return getContentPane().mouseClicked(this.event, pgp, p, button)||mouseClicked(this.event, pgp, p, button);
	}

	@Override
	@OverridablePoint
	public boolean mouseDragged(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button, final long time) {
		return false;
	}

	protected boolean dispatchMouseDragged(final @Nonnull Area pgp, final @Nonnull Point p, final int button, final long time) {
		return getContentPane().mouseDragged(this.event, pgp, p, button, time)||mouseDragged(this.event, pgp, p, button, time);
	}

	@Override
	@OverridablePoint
	public boolean mouseReleased(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
		return false;
	}

	protected boolean dispatchMouseReleased(final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
		return getContentPane().mouseReleased(this.event, pgp, p, button)||mouseReleased(this.event, pgp, p, button);
	}

	@Override
	@OverridablePoint
	public boolean onCloseRequest() {
		return true;
	}

	private boolean isDispatchClosable = true;

	protected boolean dispatchOnCloseRequest() {
		final boolean a = getContentPane().onCloseRequest();
		final boolean b = this.isDispatchClosable = onCloseRequest();
		return a&&b;
	}

	@Override
	@OverridablePoint
	public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
		return true;
	}

	protected boolean dispatchOnClosing(final @Nonnull Area pgp, final @Nonnull Point p) {
		final boolean a = getContentPane().onClosing(this.event, pgp, p);
		final boolean b = this.isDispatchClosable = this.isDispatchClosable||onClosing(this.event, pgp, p);
		return a&&b;
	}

	@Override
	@OverridablePoint
	public @Nullable WCommon top(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
		return null;
	}

	protected @Nullable WCommon dispatchTop(final @Nonnull Area pgp, final @Nonnull Point p) {
		if (pgp.pointInside(p)) {
			final WCommon a = top(this.event, pgp, p);
			final WCommon b = getContentPane().top(this.event, pgp, p);
			if (b!=null)
				return b;
			return a;
		}
		return null;
	}
}