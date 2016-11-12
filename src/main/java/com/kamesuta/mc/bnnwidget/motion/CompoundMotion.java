package com.kamesuta.mc.bnnwidget.motion;

import java.util.ArrayDeque;
import java.util.Deque;

public class CompoundMotion implements IMotion {

	protected boolean paused = true;
	protected final Deque<IMotion> queue;
	protected IMotion current;
	protected float coord;
	protected boolean looplast;

	public CompoundMotion(final float coord) {
		this.queue = new ArrayDeque<IMotion>();
		this.coord = coord;
	}

	public void add(final IMotion animation) {
		this.queue.offer(animation);
	}

	public void setLoop(final boolean b) {
		this.looplast = b;
	}

	protected void setCurrent(final IMotion current) {
		if (this.current!=null)
			this.current.onFinished();
		this.current = current;
	}

	public void stopFirst() {
		this.queue.clear();
		setCurrent(null);
	}

	public void stop() {
		this.coord = get();
		stopFirst();
	}

	public void stopLast() {
		this.coord = getLast();
		stopFirst();
	}

	@Override
	public IMotion pause() {
		this.paused = true;
		if (this.current!=null)
			this.current.pause();
		return this;
	}

	public void start() {
		this.paused = false;
		if (this.current!=null)
			this.current.resume();
	}

	public void next() {
		setCurrent(this.queue.poll());
		start();
	}

	public void stopNext() {
		if (this.looplast&&this.current!=null&&this.queue.isEmpty())
			this.current.reset();
		else {
			if (this.current!=null)
				this.coord = this.current.getEnd(this.coord);
			next();
		}
	}

	public IMotion getAnimation() {
		if ((this.current==null||this.current.isFinished())&&!this.paused)
			stopNext();
		return this.current;
	}

	public IMotion getAnimationLast() {
		return this.queue.peekLast();
	}

	public float get() {
		final IMotion a = getAnimation();
		if (a!=null)
			return a.get(this.coord);
		else
			return this.coord;
	}

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
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public float getDuration() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public float getEnd(final float start) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public Runnable getAfter() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void onFinished() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public float get(final float start) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

}