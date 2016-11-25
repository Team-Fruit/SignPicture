package com.kamesuta.mc.bnnwidget;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.util.NotifyCollections.IModCount;

public abstract class WList<T, W extends WCommon> extends WTypedPanel<W> {
	protected IModCount<T> check;

	public WList(final R position, final IModCount<T> check) {
		super(position);
		this.check = check;
	}

	public void setList(final IModCount<T> check) {
		this.check = check;
	}

	@Override
	public boolean add(final W widget) {
		return false;
	}

	@Override
	public boolean remove(final W widget) {
		return false;
	}

	int cachedModCount = -1;

	@Override
	public void update(final WEvent ev, final Area pgp, final Point p) {
		final int mod = this.check.getModCount();
		if (mod!=this.cachedModCount) {
			this.cachedModCount = mod;
			update();
		}
		super.update(ev, pgp, p);
	}

	private final Map<W, T> toT = Maps.newHashMap();
	private final Map<T, W> toW = Maps.newHashMap();
	private final Map<T, Integer> Tindex = Maps.newHashMap();
	private final Set<W> cws = Sets.newHashSet();
	public void update() {
		final List<W> ws = getContainer();
		this.cws.clear();
		this.cws.addAll(ws);
		int it = -1;
		synchronized (this.check) {
			for (final T t : this.check) {
				it++;
				W w = this.toW.get(t);
				if (w==null) {
					w = createWidget(t, it);
					this.toT.put(w, t);
					this.toW.put(t, w);
				}
				this.cws.remove(w);
				if (!ws.contains(w)) {
					this.Tindex.put(t, it);
					super.add(w);
					onAdded(t, w);
				}
				final int it0 = this.Tindex.get(t);
				if (it!=it0)
					onMoved(t, w, it0, it);
			}
		}
		for (final W w : this.cws) {
			final T t = this.toT.get(w);
			if (t!=null) {
				super.remove(w);
				onRemove(t, w);
				this.toT.remove(t);
				this.toW.remove(w);
				this.Tindex.remove(t);
			}
		}
	}

	protected abstract W createWidget(T t, int i);

	protected void onAdded(final T t, final W w) {}

	protected void onRemove(final T t, final W w) {}

	protected void onMoved(final T t, final W w, final int from, final int to) {}
}
