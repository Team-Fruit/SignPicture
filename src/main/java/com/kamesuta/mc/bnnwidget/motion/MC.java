package com.kamesuta.mc.bnnwidget.motion;

import com.kamesuta.mc.bnnwidget.position.C;

public class MC extends C implements ICompoundMotion {
	protected final CompoundMotion compoundMotion;

	public MC(final float coord, final CoordType type) {
		super(coord, type);
		this.compoundMotion = new CompoundMotion(coord);
	}

	@Override
	public MC add(final IMotion animation) {
		this.compoundMotion.add(animation);
		return this;
	}

	@Override
	public MC setLoop(final boolean b) {
		this.compoundMotion.setLoop(b);
		return this;
	}

	@Override
	public MC stopFirst() {
		this.compoundMotion.stopFirst();
		return this;
	}

	@Override
	public MC stop() {
		this.compoundMotion.stop();
		return stopFirst();
	}

	@Override
	public MC stopLast() {
		this.compoundMotion.stopLast();
		return stopFirst();
	}

	@Override
	public MC pause() {
		this.compoundMotion.pause();
		return this;
	}

	@Override
	public MC start() {
		this.compoundMotion.start();
		return this;
	}

	@Override
	public MC next() {
		this.compoundMotion.next();
		return this;
	}

	@Override
	public MC stopNext() {
		this.compoundMotion.stopNext();
		return this;
	}

	@Override
	public IMotion getAnimation() {
		return this.compoundMotion.getAnimation();
	}

	@Override
	public IMotion getAnimationLast() {
		return this.compoundMotion.getAnimationLast();
	}

	@Override
	public float get() {
		return this.compoundMotion.get();
	}

	@Override
	public float getLast() {
		return this.compoundMotion.getLast();
	}

	@Override
	public boolean isFinished() {
		return this.compoundMotion.isFinished();
	}

	@Override
	public MC restart() {
		this.compoundMotion.restart();
		return this;
	}

	@Override
	public MC reset() {
		this.compoundMotion.reset();
		return this;
	}

	public static MC a(final float n) {
		return new MC(n, CoordType.Absolute);
	}

	public static MC p(final float n) {
		return new MC(n, CoordType.Percent);
	}
}
