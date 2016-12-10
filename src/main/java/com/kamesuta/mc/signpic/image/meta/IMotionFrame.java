package com.kamesuta.mc.signpic.image.meta;

public interface IMotionFrame<E> {
	public E per();

	public E per(final float per, final E before);
}
