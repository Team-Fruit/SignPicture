package com.kamesuta.mc.guiwidget.animation;

import java.util.ArrayDeque;
import java.util.Deque;

public class MotionQueue {
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

	public MotionQueue stop() {
		this.coord = get();
		this.queue.clear();
		this.current = null;
		return this;
	}

	public MotionQueue stopLast() {
		this.coord = getLast();
		this.queue.clear();
		this.current = null;
		return stop();
	}

	public MotionQueue next() {
		this.coord = getLast();
		this.current = this.queue.poll();
		return this;
	}

	public MotionQueue stopNext() {
		if (this.current != null)
			this.coord = this.current.end;
		this.current = this.queue.poll();
		return this;
	}

	public Motion getAnimation() {
		if (this.current == null || this.current.isFinished())
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
}
