package com.kamesuta.mc.bnnwidget.var;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.motion.CompoundMotion;
import com.kamesuta.mc.bnnwidget.motion.ICompoundMotion;
import com.kamesuta.mc.bnnwidget.motion.IMotion;

public class VMotion extends VBase implements ICompoundMotion {
	protected final @Nonnull CompoundMotion compoundMotion;

	public VMotion(final float coord, final @Nonnull VType type) {
		super(coord, type);
		this.compoundMotion = new CompoundMotion(coord);
	}

	@Override
	public @Nonnull VMotion add(final @Nonnull IMotion animation) {
		this.compoundMotion.add(animation);
		return this;
	}

	@Override
	public @Nonnull VMotion setLoop(final boolean b) {
		this.compoundMotion.setLoop(b);
		return this;
	}

	@Override
	public @Nonnull VMotion stopFirst() {
		this.compoundMotion.stopFirst();
		return this;
	}

	@Override
	public @Nonnull VMotion stop() {
		this.compoundMotion.stop();
		return stopFirst();
	}

	@Override
	public @Nonnull VMotion stopLast() {
		this.compoundMotion.stopLast();
		return stopFirst();
	}

	@Override
	public @Nonnull VMotion pause() {
		this.compoundMotion.pause();
		return this;
	}

	@Override
	public @Nonnull VMotion start() {
		this.compoundMotion.start();
		return this;
	}

	@Override
	public @Nonnull VMotion next() {
		this.compoundMotion.next();
		return this;
	}

	@Override
	public @Nonnull VMotion stopNext() {
		this.compoundMotion.stopNext();
		return this;
	}

	@Override
	public @Nullable IMotion getAnimation() {
		return this.compoundMotion.getAnimation();
	}

	@Override
	public @Nullable IMotion getAnimationLast() {
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
	public @Nonnull VMotion restart() {
		this.compoundMotion.restart();
		return this;
	}

	@Override
	public @Nonnull VMotion reset() {
		this.compoundMotion.reset();
		return this;
	}
}
