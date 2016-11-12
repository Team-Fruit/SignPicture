package com.kamesuta.mc.bnnwidget.motion;

import java.util.Deque;

import com.google.common.collect.Queues;

public class CompoundMotion implements IMotion, ICompoundMotion {

	protected boolean paused = true;
	protected final Deque<IMotion> queue;
	protected IMotion current;
	protected float coord;
	protected boolean looplast;
	protected Runnable after;
	protected IMotion first;

	public CompoundMotion() {
		this.queue = Queues.newArrayDeque();
	}

	public CompoundMotion(final float coord) {
		this();
		this.coord = coord;
		this.first = this;
	}

	@Override
	public CompoundMotion add(final IMotion animation) {
		if (this.first!=null)
			this.first = animation;
		this.queue.offer(animation);
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
		this.queue.clear();
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

	@Override
	public CompoundMotion next() {
		setCurrent(this.queue.poll());
		start();
		return this;
	}

	@Override
	public CompoundMotion stopNext() {
		if (this.looplast&&this.current!=null&&this.queue.isEmpty())
			this.current.reset();
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
		return this.queue.peekLast();
	}

	@Override
	public float get() {
		return get(this.coord);
	}

	@Override
	public float get(float start) {
		final IMotion a = getAnimation();
		if (a!=this.first)
			start = this.coord;
		if (a!=null)
			return a.get(start);
		else
			return start;
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
	public IMotion reset() {
		stopFirst();
		return this;
	}

	@Override
	public IMotion finish() {
		stopLast();
		return this;
	}

	@Override
	public IMotion resume() {
		start();
		return this;
	}

	@Override
	public IMotion setTime(final float time) {
		float t = 0;
		while (true) {
			final IMotion m = this.queue.poll();
			if (m!=null) {
				setCurrent(m);
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
	public IMotion setAfter(final Runnable r) {
		this.after = r;
		return this;
	}

	@Override
	public float getDuration() {
		float d = 0;
		for (final IMotion m : this.queue)
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

	public static ICompoundMotion of(final IMotion... motions) {
		final ICompoundMotion compound = new CompoundMotion();
		for (final IMotion motion : motions)
			compound.add(motion);
		return compound;
	}
}