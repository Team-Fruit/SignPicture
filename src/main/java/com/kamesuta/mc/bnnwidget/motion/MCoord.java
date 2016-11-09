package com.kamesuta.mc.bnnwidget.motion;

import java.util.ArrayDeque;
import java.util.Deque;

import com.kamesuta.mc.bnnwidget.position.Coord;

public class MCoord extends Coord {
	protected boolean paused = true;
	protected final Deque<IMotion> queue;
	protected IMotion current;
	protected float coord;
	protected boolean looplast;

	public MCoord(final float coord, final CoordSide side, final CoordType type) {
		super(coord, side, type);
		this.queue = new ArrayDeque<IMotion>();
		this.coord = coord;
	}

	public MCoord(final float coord) {
		this(coord, CoordSide.Top, CoordType.Percent);
	}

	public MCoord add(final IMotion animation) {
		this.queue.offer(animation);
		return this;
	}

	public MCoord setLoop(final boolean b) {
		this.looplast = b;
		return this;
	}

	protected void setCurrent(final IMotion current) {
		if (this.current!=null)
			this.current.onFinished();
		this.current = current;
	}

	public MCoord stopFirst() {
		this.queue.clear();
		setCurrent(null);
		return this;
	}

	public MCoord stop() {
		this.coord = get();
		return stopFirst();
	}

	public MCoord stopLast() {
		this.coord = getLast();
		return stopFirst();
	}

	public MCoord pause() {
		this.paused = true;
		if (this.current!=null)
			this.current.pause();
		return this;
	}

	public MCoord start() {
		this.paused = false;
		if (this.current!=null)
			this.current.resume();
		return this;
	}

	public MCoord next() {
		setCurrent(this.queue.poll());
		start();
		return this;
	}

	public MCoord stopNext() {
		if (this.looplast&&this.current!=null&&this.queue.isEmpty())
			this.current.reset();
		else {
			if (this.current!=null)
				this.coord = this.current.getEnd(this.coord);
			next();
		}
		return this;
	}

	public IMotion getAnimation() {
		if ((this.current==null||this.current.isFinished())&&!this.paused)
			stopNext();
		return this.current;
	}

	public IMotion getAnimationLast() {
		return this.queue.peekLast();
	}

	@Override
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

	public boolean isFinished() {
		final IMotion a = getAnimationLast();
		if (this.current!=null&&!this.current.isFinished()||a!=null&&!a.isFinished())
			return false;
		else
			return true;
	}

	public static MCoord top(final float n) {
		return new MCoord(n, CoordSide.Top, CoordType.Absolute);
	}

	public static MCoord ptop(final float n) {
		return new MCoord(n, CoordSide.Top, CoordType.Percent);
	}

	public static MCoord left(final float n) {
		return new MCoord(n, CoordSide.Left, CoordType.Absolute);
	}

	public static MCoord pleft(final float n) {
		return new MCoord(n, CoordSide.Left, CoordType.Percent);
	}

	public static MCoord bottom(final float n) {
		return new MCoord(n, CoordSide.Bottom, CoordType.Absolute);
	}

	public static MCoord pbottom(final float n) {
		return new MCoord(n, CoordSide.Bottom, CoordType.Percent);
	}

	public static MCoord right(final float n) {
		return new MCoord(n, CoordSide.Right, CoordType.Absolute);
	}

	public static MCoord pright(final float n) {
		return new MCoord(n, CoordSide.Right, CoordType.Percent);
	}

	public static MCoord width(final float n) {
		return new MCoord(n, CoordSide.Width, CoordType.Absolute);
	}

	public static MCoord pwidth(final float n) {
		return new MCoord(n, CoordSide.Width, CoordType.Percent);
	}

	public static MCoord height(final float n) {
		return new MCoord(n, CoordSide.Height, CoordType.Absolute);
	}

	public static MCoord pheight(final float n) {
		return new MCoord(n, CoordSide.Height, CoordType.Percent);
	}
}
