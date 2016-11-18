package com.kamesuta.mc.bnnwidget.var;

import com.kamesuta.mc.bnnwidget.motion.CompoundMotion;
import com.kamesuta.mc.bnnwidget.motion.ICompoundMotion;
import com.kamesuta.mc.bnnwidget.motion.IMotion;

public class VMotion extends VBase implements ICompoundMotion {
	protected final CompoundMotion compoundMotion;

	public VMotion(final float coord, final VType type) {
		super(coord, type);
		this.compoundMotion = new CompoundMotion(coord);
	}

	@Override
	public VMotion add(final IMotion animation) {
		this.compoundMotion.add(animation);
		return this;
	}

	@Override
	public VMotion setLoop(final boolean b) {
		this.compoundMotion.setLoop(b);
		return this;
	}

	@Override
	public VMotion stopFirst() {
		this.compoundMotion.stopFirst();
		return this;
	}

	@Override
	public VMotion stop() {
		this.compoundMotion.stop();
		return stopFirst();
	}

	@Override
	public VMotion stopLast() {
		this.compoundMotion.stopLast();
		return stopFirst();
	}

	@Override
	public VMotion pause() {
		this.compoundMotion.pause();
		return this;
	}

	@Override
	public VMotion start() {
		this.compoundMotion.start();
		return this;
	}

	@Override
	public VMotion next() {
		this.compoundMotion.next();
		return this;
	}

	@Override
	public VMotion stopNext() {
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
	public VMotion restart() {
		this.compoundMotion.restart();
		return this;
	}

	@Override
	public VMotion reset() {
		this.compoundMotion.reset();
		return this;
	}
}
