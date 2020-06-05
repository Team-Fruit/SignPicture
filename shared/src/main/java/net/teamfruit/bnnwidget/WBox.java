package net.teamfruit.bnnwidget;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.position.Point;
import net.teamfruit.bnnwidget.position.R;

/**
 * 内容を一つだけ含むことのできるパネルです。
 * <p>
 * 既に内容を含んでいる場合、内容が削除された後、追加されます。
 *
 * @author TeamFruit
 */
public class WBox extends WPanel {
	protected @Nullable WCommon addtask;

	public WBox(final @Nonnull R position) {
		super(position);
	}

	@Override
	public boolean add(final @Nonnull WCommon widget) {
		set(widget);
		return true;
	}

	/**
	 * 内容を設定します。
	 * <p>
	 * nullを設定した場合、内容は消去されます。
	 * @param widget
	 */
	public void set(final @Nullable WCommon widget) {
		this.addtask = widget;
		removeAll();
	}

	public void reset() {
		set(null);
	}

	private void removeAll() {
		for (final Iterator<WCommon> itr = getContainer().iterator(); itr.hasNext();) {
			final WCommon widget = itr.next();
			if (widget.onCloseRequest())
				itr.remove();
			else
				this.removelist.offer(widget);
		}
	}

	@Override
	public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
		super.update(ev, pgp, p);
		if (getContainer().size()<=0)
			if (this.addtask!=null) {
				super.add(this.addtask);
				this.addtask = null;
			}
	}
}
