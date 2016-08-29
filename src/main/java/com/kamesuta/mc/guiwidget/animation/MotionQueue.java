package com.kamesuta.mc.guiwidget.animation;

import java.util.ArrayDeque;
import java.util.Deque;

public class MotionQueue {
	protected boolean paused = true;
	protected final Deque<Motion> queue;
	protected Motion current;
	protected float coord;

	public MotionQueue(final float coord) {
		this.queue = new ArrayDeque<Motion>();
		this.coord = coord;
	}

	public MotionQueue add(final Motion animation) {
		this.queue.offer(animation);
		return this;
	}

	protected void setCurrent(final Motion current) {
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
			this.coord = this.current.end;
		setCurrent(this.queue.poll());
		start();
		return this;
	}

	public Motion getAnimation() {
		if ((this.current == null || this.current.isFinished()) && !this.paused)
			stopNext();
		return this.current;
	}

	public Motion getAnimationLast() {
		return this.queue.peekLast();
	}

	public float get() {
		final Motion a = getAnimation();
		if (a != null)
			return (float) a.easing(this.coord);
		else
			return this.coord;
	}

	public float getLast() {
		final Motion a = getAnimationLast();
		if (a != null)
			return a.end;
		else
			return this.coord;
	}

	public MotionQueue addAfter(final MotionQueue q) {
		final Motion a = getAnimationLast();
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
		final Motion a = getAnimationLast();
		if (a != null)
			a.after(r);
		return this;
	}
}
