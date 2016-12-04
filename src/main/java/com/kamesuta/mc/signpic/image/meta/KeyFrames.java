package com.kamesuta.mc.signpic.image.meta;

import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.common.collect.Maps;
import com.kamesuta.mc.bnnwidget.motion.CompoundMotion;
import com.kamesuta.mc.bnnwidget.motion.Easings;

public class KeyFrames {
	private int modCount;
	private int motionModCount = -1;
	private transient CompoundMotion motion;

	public final TreeMap<Float, Float> frames = Maps.newTreeMap();

	public KeyFrames() {
	}

	public KeyFrames reset() {
		this.frames.clear();
		return this;
	}

	public KeyFrames setKey(final float time, final float value) {
		//final Float f = this.frames.get(time);
		//if (f!=null)
		//	value += f;
		this.frames.put(time, value);
		this.modCount++;
		return this;
	}

	public KeyFrames parse(final float time, final float value) {
		return setKey(time, value);
	}

	public float getStart() {
		final Entry<Float, Float> first = this.frames.firstEntry();
		if (first!=null)
			return first.getValue();
		else
			return 0;
	}

	public float getBefore(final float time) {
		final Entry<Float, Float> entry = this.frames.floorEntry(time);
		if (entry!=null)
			return entry.getValue();
		else
			return 0;
	}

	public float getAfter(final float time) {
		final Entry<Float, Float> entry = this.frames.ceilingEntry(time);
		if (entry!=null)
			return entry.getValue();
		else
			return 0;
	}

	public CompoundMotion getMotion() {
		if (this.modCount!=this.motionModCount||this.motion==null) {
			this.motionModCount = this.modCount;
			this.motion = makeMotion();
		}
		return this.motion;
	}

	public float get() {
		return getMotion().get(getStart());
	}

	private CompoundMotion makeMotion() {
		final CompoundMotion motion = new CompoundMotion().setLoop(true).start();
		float time = 0;
		for (final Entry<Float, Float> entry : this.frames.entrySet()) {
			final float key = entry.getKey();
			final float value = entry.getValue();
			motion.add(Easings.easeLinear.move(key-time, value));
			time = key;
		}
		return motion;
	}
}
