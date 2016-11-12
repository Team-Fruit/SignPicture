package com.kamesuta.mc.bnnwidget.motion;

import java.util.List;

import com.google.common.collect.Lists;

public class CompoundMotion implements IMotion, ICompoundMotion {

	protected boolean paused = true;
	protected TaskList tasks;
	protected IMotion current;
	protected float coord;
	protected boolean looplast;
	protected Runnable after;
	protected boolean usecoord;

	public CompoundMotion() {
		this.tasks = new TaskList();
	}

	public CompoundMotion(final float coord) {
		this();
		this.coord = coord;
		this.usecoord = true;
	}

	@Override
	public CompoundMotion add(final IMotion animation) {
		this.tasks.addTask(animation);
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
		this.tasks.finishTask();
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
		final IMotion m = this.tasks.pollTask();
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
		if (this.looplast&&this.current!=null&&this.tasks.isFinishedTask())
			reset();
		else {
			if (this.current!=null)
				this.coord = this.current.getEnd(this.coord);
			next();
		}
		return this;
	}

	@Override
	public IMotion getAnimation() {
		if ((this.current==null||this.current.isFinished())&&!this.paused)
			stopNext();
		return this.current;
	}

	@Override
	public IMotion getAnimationLast() {
		this.tasks.lastTask();
		return this.tasks.getTask();
	}

	@Override
	public float get() {
		return get(this.coord);
	}

	@Override
	public float get(final float start) {
		final IMotion a = getAnimation();
		if (!this.usecoord) {
			this.coord = start;
			this.usecoord = true;
		}
		if (a!=null)
			return a.get(this.coord);
		else
			return this.coord;
	}

	@Override
	public float getLast() {
		final IMotion a = getAnimationLast();
		if (a!=null)
			return a.getEnd(this.coord);
		else
			return this.coord;
	}

	@Override
	public boolean isFinished() {
		final IMotion a = getAnimationLast();
		if (this.current!=null&&!this.current.isFinished()||a!=null&&!a.isFinished())
			return false;
		else
			return true;
	}

	@Override
	public CompoundMotion reset() {
		this.tasks.restartTask();
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
		for (final IMotion m : this.tasks.tasks)
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

	public static class TaskList {

		protected final List<IMotion> tasks = Lists.newArrayList();
		protected int pos;

		protected void addTask(final IMotion e) {
			this.tasks.add(e);
		}

		protected IMotion getTask() {
			if (0<=this.pos&&this.pos<this.tasks.size())
				return this.tasks.get(this.pos);
			return null;
		}

		protected IMotion pollTask() {
			final int size = this.tasks.size();
			if (0<=this.pos&&this.pos<size)
				return this.tasks.get(this.pos++);
			return null;
		}

		protected void restartTask() {
			this.pos = 0;
		}

		protected void finishTask() {
			this.pos = this.tasks.size();
		}

		protected void lastTask() {
			this.pos = this.tasks.size()-1;
		}

		protected boolean isFinishedTask() {
			return this.pos>=this.tasks.size();
		}

	}
}