package com.kamesuta.mc.signpic.attr;

import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;
import com.kamesuta.mc.bnnwidget.motion.CompoundMotion;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.ICompoundMotion;

public class PropReaderAnimation<KeyFrame extends IPropInterpolatable<InterFrame>, InterFrame> {
	private final @Nonnull ICompoundMotion motion = new CompoundMotion().setLoop(true).start();
	private final @Nonnull TreeMap<Float, KeyFrame> frames = Maps.newTreeMap();
	private float lasttime;

	public PropReaderAnimation(final @Nonnull KeyFrame defaultframe) {
		this.frames.put(0f, defaultframe);
	}

	public @Nonnull InterFrame get() {
		if (this.frames.size()<=1)
			return this.frames.get(0f).per();
		final float t = this.motion.get();
		Entry<Float, KeyFrame> before = this.frames.floorEntry(t);
		final Entry<Float, KeyFrame> after = this.frames.higherEntry(t);
		if (before==null)
			before = this.frames.firstEntry();
		if (after!=null) {
			final float f1 = after.getKey();
			final float f2 = before.getKey();
			return after.getValue().per((t-f2)/(f1-f2), before.getValue().per());
		} else
			return before.getValue().per();
	}

	public @Nonnull PropReaderAnimation<KeyFrame, InterFrame> add(final float time, final @Nonnull KeyFrame frame, final @Nonnull Easings easing) {
		final float difftime = time-this.lasttime;
		this.motion.add(easing.move(difftime, time));
		this.lasttime = time;
		this.frames.put(time, frame);
		return this;
	}
}
