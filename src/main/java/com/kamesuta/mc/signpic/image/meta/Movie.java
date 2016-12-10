package com.kamesuta.mc.signpic.image.meta;

import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.common.collect.Maps;
import com.kamesuta.mc.bnnwidget.motion.CompoundMotion;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.ICompoundMotion;

public class Movie<E extends IMotionFrame<E>> {
	private final ICompoundMotion motion = new CompoundMotion().setLoop(true).start();
	private final TreeMap<Float, E> frames = Maps.newTreeMap();
	private float lasttime;

	public Movie(final E defaultframe) {
		this.frames.put(0f, defaultframe);
	}

	public E get() {
		if (this.frames.size()<=1)
			return this.frames.get(0f);
		final float t = this.motion.get();
		Entry<Float, E> before = this.frames.floorEntry(t);
		final Entry<Float, E> after = this.frames.higherEntry(t);
		if (before==null)
			before = this.frames.firstEntry();
		if (after!=null) {
			final float f1 = after.getKey();
			final float f2 = before.getKey();
			return after.getValue().per((t-f2)/(f1-f2), before.getValue());
		} else
			return before.getValue();
	}

	public Movie<E> add(final float time, final E frame) {
		final float difftime = time-this.lasttime;
		this.motion.add(Easings.easeLinear.move(difftime, time));
		this.lasttime = time;
		this.frames.put(time, frame);
		return this;
	}
}
