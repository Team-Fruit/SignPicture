package net.teamfruit.bnnwidget;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.position.Point;
import net.teamfruit.bnnwidget.position.R;
import net.teamfruit.bnnwidget.render.RenderOption;

/**
 * {@link W}型のコンポーネントを含むことのできるパネルです。
 * @param <W> コンポーネントが含むことのできる型
 * @author TeamFruit
 */
public abstract class WTypedPanel<W extends WCommon> extends WBase implements WContainer<W> {
	private final @Nonnull List<W> widgets = Lists.newArrayList();
	/**
	 * このキューに追加されたコンポーネントは逐次、消去されます。
	 */
	protected final @Nonnull Deque<W> removelist = Queues.newArrayDeque();
	/**
	 * ウィジェットが初期化されたかどうかを保持します。
	 * <p>
	 * {@link #initWidget()}が呼び出された後trueになります。
	 * <br>
	 * これは{@link #initWidget()}を一度だけ呼び出すのに役立ちます。
	 */
	protected boolean initialized;
	/**
	 * このキューに追加されたタスクは逐次、実行されます。
	 */
	protected @Nonnull Deque<Runnable> eventQueue = Queues.newArrayDeque();

	public WTypedPanel(final @Nonnull R position) {
		super(position);
	}

	/**
	 * 安全なタイミングで実行されるタスクを追加します
	 * <p>
	 * 安全に追加、消去などを行うことに使用します。
	 * @param doRun タスク
	 */
	public void invokeLater(final @Nullable Runnable doRun) {
		if (doRun!=null)
			this.eventQueue.push(doRun);
	}

	@Override
	public @Nonnull List<W> getContainer() {
		return this.widgets;
	}

	@Override
	public boolean add(final @Nonnull W widget) {
		final boolean b = getContainer().add(widget);
		widget.onAdded();
		return b;
	}

	@Override
	public boolean remove(final @Nonnull W widget) {
		if (widget.onCloseRequest()) {
			getContainer().remove(widget);
			return true;
		} else {
			this.removelist.offer(widget);
			return false;
		}
	}

	@Override
	public void onAdded() {
		if (this.initialized)
			for (final W widget : getContainer())
				widget.onAdded();
		else {
			initWidget();
			this.initialized = true;
		}
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

	@Override
	public void onInit(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
		final Area gp = getGuiPosition(pgp);
		for (final W widget : getContainer())
			widget.onInit(ev, gp, p);
	}

	@Override
	public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, @Nonnull final RenderOption opt) {
		final Area gp = getGuiPosition(pgp);
		final float opacity = getGuiOpacity(popacity);
		for (final W widget : getContainer())
			widget.draw(ev, gp, p, frame, opacity, opt);
		super.draw(ev, pgp, p, frame, popacity, opt);
	}

	@Override
	public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
		Runnable doRun;
		while ((doRun = this.eventQueue.poll())!=null)
			doRun.run();

		final Area gp = getGuiPosition(pgp);
		for (final W widget : getContainer())
			widget.update(ev, gp, p);
		for (final Iterator<W> itr = this.removelist.iterator(); itr.hasNext();) {
			final W widget = itr.next();
			if (widget.onClosing(ev, gp, p)) {
				getContainer().remove(widget);
				itr.remove();
			}
		}
	}

	@Override
	public boolean keyTyped(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final char c, final int keycode) {
		final Area gp = getGuiPosition(pgp);
		for (final ListIterator<W> itr = getContainer().listIterator(getContainer().size()); itr.hasPrevious();) {
			final W widget = itr.previous();
			if (widget.keyTyped(ev, gp, p, c, keycode))
				return true;
		}
		return false;
	}

	@Override
	public boolean mouseScrolled(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int scroll) {
		final Area gp = getGuiPosition(pgp);
		for (final ListIterator<W> itr = getContainer().listIterator(getContainer().size()); itr.hasPrevious();) {
			final W widget = itr.previous();
			if (widget.mouseScrolled(ev, gp, p, scroll))
				return true;
		}
		return false;
	}

	@Override
	public boolean mouseMoved(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
		final Area gp = getGuiPosition(pgp);
		for (final ListIterator<W> itr = getContainer().listIterator(getContainer().size()); itr.hasPrevious();) {
			final W widget = itr.previous();
			if (widget.mouseMoved(ev, gp, p, button))
				return true;
		}
		return false;
	}

	@Override
	public boolean mouseClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
		final Area gp = getGuiPosition(pgp);
		for (final ListIterator<W> itr = getContainer().listIterator(getContainer().size()); itr.hasPrevious();) {
			final W widget = itr.previous();
			if (widget.mouseClicked(ev, gp, p, button))
				return true;
		}
		return false;
	}

	@Override
	public boolean mouseDragged(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button, final long time) {
		final Area gp = getGuiPosition(pgp);
		for (final ListIterator<W> itr = getContainer().listIterator(getContainer().size()); itr.hasPrevious();) {
			final W widget = itr.previous();
			if (widget.mouseDragged(ev, gp, p, button, time))
				return true;
		}
		return false;
	}

	@Override
	public boolean mouseReleased(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
		final Area gp = getGuiPosition(pgp);
		for (final ListIterator<W> itr = getContainer().listIterator(getContainer().size()); itr.hasPrevious();) {
			final W widget = itr.previous();
			if (widget.mouseReleased(ev, gp, p, button))
				return true;
		}
		return false;
	}

	@Override
	public boolean onCloseRequest() {
		boolean closable = true;
		for (final Iterator<W> itr = getContainer().iterator(); itr.hasNext();) {
			final W widget = itr.next();
			if (!widget.onCloseRequest()) {
				this.removelist.offer(widget);
				closable = false;
			}
		}
		return closable;
	}

	@Override
	public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
		final Area gp = getGuiPosition(pgp);
		boolean closable = true;
		for (final Iterator<W> itr = this.removelist.iterator(); itr.hasNext();) {
			final W widget = itr.next();
			if (widget.onClosing(ev, gp, p)) {
				getContainer().remove(widget);
				itr.remove();
			} else
				closable = false;
		}
		return closable;
	}

	@Override
	public @Nullable WCommon top(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point point) {
		final Area gp = getGuiPosition(pgp);
		if (gp.pointInside(point)) {
			WCommon topwidget = null;
			for (final W widget : getContainer()) {
				final WCommon top = widget.top(ev, gp, point);
				if (top!=null)
					topwidget = top;
			}
			return topwidget;
		}
		return null;
	}
}
