package com.kamesuta.mc.bnnwidget.motion;

import java.util.ArrayDeque;
import java.util.Deque;

public class MotionQueue {
	protected boolean paused = true;
	protected final Deque<IMotion> queue;
	protected IMotion current;
	protected float coord;

	public MotionQueue(final float coord) {
		this.queue = new ArrayDeque<IMotion>();
		this.coord = coord;
	}

	public MotionQueue add(final IMotion animation) {
		this.queue.offer(animation);
		return this;
	}

	protected void setCurrent(final IMotion current) {
		if (this.current != null)
			this.current.onFinished();
		this.current = current;
	}

	public MotionQueue stop() {
		this.coord = get();
		this.queue.clear();
		setCurrent(null);
		return this;
	}

	public MotionQueue stopLast() {
		this.coord = getLast();
		this.queue.clear();
		setCurrent(null);
		return stop();
	}

	public MotionQueue pause() {
		this.paused = true;
		if (this.current != null)
			this.current.pause();
		return this;
	}

	public MotionQueue start() {
		this.paused = false;
		if (this.current != null)
			this.current.resume();
		return this;
	}

	public MotionQueue next() {
		setCurrent(this.queue.poll());
		start();
		return this;
	}

	public MotionQueue stopNext() {
		if (this.current != null)
			this.coord = this.current.getEnd(this.coord);
		setCurrent(this.queue.poll());
		start();
		return this;
	}

	public IMotion getAnimation() {
		if ((this.current == null || this.current.isFinished()) && !this.paused)
			stopNext();
		return this.current;
	}

	public IMotion getAnimationLast() {
		return this.queue.peekLast();
	}

	public float get() {
		final IMotion a = getAnimation();
		if (a != null)
			return (float) a.get(this.coord);
		else
			return this.coord;
	}

	public float getLast() {
		final IMotion a = getAnimationLast();
		if (a != null)
			return a.getEnd(this.coord);
		else
			return this.coord;
	}

	public MotionQueue addAfter(final MotionQueue q) {
		final IMotion a = getAnimationLast();
		if (a != null)
			a.after(new Runnable() {
				@Override
				public void run() {
					q.start();
				}
			});
		return this;
	}

	public MotionQueue addAfter(final Runnable r) {
		final IMotion a = getAnimationLast();
		if (a != null)
			a.after(r);
		return this;
	}

	public boolean isFinished() {
		final IMotion a = getAnimationLast();
		if ((this.current!=null && !this.current.isFinished()) || (a!=null && !a.isFinished()))
			return false;
		else
			return true;
	}
}
