package com.kamesuta.mc.bnnwidget.motion;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class CompoundMotion implements IMotion, ICompoundMotion {

	protected boolean paused = true;
	protected TaskList<IMotion> tasks;
	protected IMotion current;
	protected float firstcoord;
	protected float coord;
	protected boolean looplast;
	protected Runnable after;
	protected boolean usecoord;

	public CompoundMotion() {
		this.tasks = new TaskList<IMotion>();
	}

	public CompoundMotion(final float coord) {
		this();
		this.firstcoord = coord;
		this.usecoord = true;
		this.coord = coord;
	}

	@Override
	public CompoundMotion add(final IMotion animation) {
		this.tasks.add(animation);
		return this;
	}

	@Override
	public CompoundMotion setLoop(final boolean b) {
		this.looplast = b;
		return this;
	}

	protected void setCurrent(final IMotion current) {
		if (this.current!=null)
			this.current.onFinished();
		this.current = current;
	}

	@Override
	public CompoundMotion stopFirst() {
		this.tasks.finish();
		setCurrent(null);
		return this;
	}

	@Override
	public CompoundMotion stop() {
		this.coord = get();
		stopFirst();
		return this;
	}

	@Override
	public CompoundMotion stopLast() {
		this.coord = getLast();
		stopFirst();
		return this;
	}

	@Override
	public CompoundMotion pause() {
		this.paused = true;
		if (this.current!=null)
			this.current.pause();
		return this;
	}

	@Override
	public CompoundMotion start() {
		this.paused = false;
		if (this.current!=null)
			this.current.resume();
		return this;
	}

	protected IMotion nextCurrent() {
		final IMotion m = this.tasks.poll();
		setCurrent(m);
		return m;
	}

	@Override
	public CompoundMotion next() {
		nextCurrent();
		start();
		return this;
	}

	@Override
	public CompoundMotion stopNext() {
		if ((this.current==null||this.current.isFinished())&&!this.paused) {
			if (this.current!=null)
				this.coord = this.current.getEnd(this.coord);
			next();
		}
		if (this.looplast&&(this.current==null||this.current.isFinished())&&this.tasks.isFinished())
			restart();
		return this;
	}

	@Override
	public IMotion getAnimation() {
		stopNext();
		return this.current;
	}

	@Override
	public IMotion getAnimationLast() {
		return this.tasks.getLast();
	}

	@Override
	public float get() {
		return get(this.coord);
	}

	@Override
	public float get(final float start) {
		final IMotion a = getAnimation();
		if (!this.usecoord) {
			this.usecoord = true;
			this.firstcoord = start;
			this.coord = start;
		}
		if (a!=null)
			return a.get(this.coord);
		else
			return this.coord;
	}

	@Override
	public float getLast() {
		final IMotion a = this.tasks.getLast();
		if (a!=null)
			return a.getEnd(this.coord);
		else
			return this.coord;
	}

	@Override
	public boolean isFinished() {
		final IMotion a = this.tasks.getLast();
		return !(this.looplast||this.current!=null&&!this.current.isFinished()||a!=null&&!a.isFinished());
	}

	@Override
	public CompoundMotion restart() {
		for (final IMotion m : this.tasks)
			m.pause().restart();
		this.coord = this.firstcoord;
		this.tasks.restart();
		next();
		return this;
	}

	@Override
	public CompoundMotion reset() {
		this.tasks.reset();
		this.coord = this.firstcoord;
		setCurrent(null);
		return this;
	}

	@Override
	public CompoundMotion finish() {
		stopLast();
		return this;
	}

	@Override
	public CompoundMotion resume() {
		start();
		return this;
	}

	@Override
	public CompoundMotion setTime(final float time) {
		float t = 0;
		while (true) {
			final IMotion m = nextCurrent();
			if (m!=null) {
				final float d = m.getDuration();
				final float newtime = t+d;
				if (newtime>time) {
					m.setTime(time-t);
					break;
				}
				t = newtime;
			} else {
				stopLast();
				break;
			}
		}
		return this;
	}

	@Override
	public CompoundMotion setAfter(final Runnable r) {
		this.after = r;
		return this;
	}

	@Override
	public float getDuration() {
		float d = 0;
		for (final IMotion m : this.tasks)
			d += m.getDuration();
		return d;
	}

	@Override
	public float getEnd(final float start) {
		return getLast();
	}

	@Override
	public Runnable getAfter() {
		return this.after;
	}

	@Override
	public void onFinished() {
		if (this.after!=null)
			this.after.run();
	}

	public static CompoundMotion of(final IMotion... motions) {
		final CompoundMotion compound = new CompoundMotion();
		for (final IMotion motion : motions)
			compound.add(motion);
		return compound;
	}

	public static CompoundMotion of(final float coord, final IMotion... motions) {
		final CompoundMotion compound = new CompoundMotion(coord);
		for (final IMotion motion : motions)
			compound.add(motion);
		return compound;
	}

	public static class TaskList<E> implements Iterable<E> {

		private final List<E> tasks = Lists.newArrayList();
		private int pos;

		public void add(final E e) {
			this.tasks.add(e);
		}

		protected E get(final int pos) {
			if (0<=pos&&pos<this.tasks.size())
				return this.tasks.get(pos);
			return null;
		}

		public E get() {
			return get(this.pos);
		}

		public E poll() {
			return get(this.pos++);
		}

		public void reset() {
			this.tasks.clear();
			restart();
		}

		public void restart() {
			this.pos = 0;
		}

		public void finish() {
			this.pos = this.tasks.size();
		}

		public E getLast() {
			return get(this.tasks.size()-1);
		}

		public boolean isFinished() {
			return this.pos>=this.tasks.size();
		}

		@Override
		public Iterator<E> iterator() {
			return this.tasks.iterator();
		}

	}
}