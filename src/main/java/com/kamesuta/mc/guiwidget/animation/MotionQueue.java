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
		final Motion a = this.queue.peekLast();
		if (a != null) this.coord = a.end;
		this.queue.clear();
		return this;
	}

	public MotionQueue next() {
		if (this.current != null) this.coord = this.current.end;
		this.current = this.queue.poll();
		return this;
	}

	public Motion getAnimation() {
		if (this.current == null || this.current.isFinished())
			next();
		return this.current;
	}

	public double get() {
		final Motion a = getAnimation();
		if (a != null)
			return a.easing(this.coord);
		else
			return this.coord;
	}
}
