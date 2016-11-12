package com.kamesuta.mc.bnnwidget.motion;

import com.kamesuta.mc.bnnwidget.position.Coord;

public class MCoord extends Coord implements ICompoundMotion {
	protected final CompoundMotion compoundMotion;

	public MCoord(final float coord, final CoordSide side, final CoordType type) {
		super(coord, side, type);
		this.compoundMotion = new CompoundMotion(coord);
	}

	public MCoord(final float coord) {
		this(coord, CoordSide.Top, CoordType.Percent);
	}

	@Override
	public MCoord add(final IMotion animation) {
		this.compoundMotion.add(animation);
		return this;
	}

	@Override
	public MCoord setLoop(final boolean b) {
		this.compoundMotion.setLoop(b);
		return this;
	}

	@Override
	public MCoord stopFirst() {
		this.compoundMotion.stopFirst();
		return this;
	}

	@Override
	public MCoord stop() {
		this.compoundMotion.stop();
		return stopFirst();
	}

	@Override
	public MCoord stopLast() {
		this.compoundMotion.stopLast();
		return stopFirst();
	}

	@Override
	public MCoord pause() {
		this.compoundMotion.pause();
		return this;
	}

	@Override
	public MCoord start() {
		this.compoundMotion.start();
		return this;
	}

	@Override
	public MCoord next() {
		this.compoundMotion.next();
		return this;
	}

	@Override
	public MCoord stopNext() {
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
	public MCoord restart() {
		this.compoundMotion.restart();
		return this;
	}

	@Override
	public MCoord reset() {
		this.compoundMotion.reset();
		return this;
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
