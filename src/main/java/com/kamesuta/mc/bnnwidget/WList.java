package com.kamesuta.mc.bnnwidget;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.util.NotifyCollections.IModCount;

public abstract class WList<T, W extends WCommon> extends WTypedPanel<W> {
	protected @Nonnull IModCount<T> check;

	public WList(final @Nonnull R position, final @Nonnull IModCount<T> check) {
		super(position);
		this.check = check;
	}

	public void setList(final @Nonnull IModCount<T> check) {
		this.check = check;
	}

	@Override
	public boolean add(final @Nonnull W widget) {
		return false;
	}

	@Override
	public boolean remove(final @Nonnull W widget) {
		return false;
	}

	int cachedModCount = -1;

	@Override
	public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
		final int mod = this.check.getModCount();
		if (mod!=this.cachedModCount) {
			this.cachedModCount = mod;
			update();
		}
		super.update(ev, pgp, p);
	}

	private final @Nonnull Map<W, T> toT = Maps.newHashMap();
	private final @Nonnull Map<T, W> toW = Maps.newHashMap();
	private final @Nonnull Map<T, Integer> Tindex = Maps.newHashMap();
	private final @Nonnull Set<W> cws = Sets.newHashSet();

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

	protected abstract @Nonnull W createWidget(@Nonnull T t, int i);

	protected void onAdded(final @Nonnull T t, final @Nonnull W w) {
	}

	protected void onRemove(final @Nonnull T t, final @Nonnull W w) {
	}

	protected void onMoved(final @Nonnull T t, final @Nonnull W w, final int from, final int to) {
	}
}
