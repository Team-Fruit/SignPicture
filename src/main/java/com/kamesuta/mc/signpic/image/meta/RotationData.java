package com.kamesuta.mc.signpic.image.meta;

import java.util.List;
import java.util.ListIterator;

import javax.vecmath.Quat4f;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.kamesuta.mc.signpic.image.meta.DiffRotationData.Rotate;
import com.kamesuta.mc.signpic.image.meta.ImageRotation.ImageRotate;

public class RotationData implements IMotionFrame<RotationData> {
	protected final Quat4f quat;

	private RotationData(final Quat4f quat) {
		this.quat = quat;
	}

	@Override
	public RotationData per() {
		return this;
	}

	@Override
	public RotationData per(final float per, final RotationData before) {
		final Quat4f quat = new Quat4f();
		quat.set(getRotate());
		quat.interpolate(before.getRotate(), per);
		return new RotationData(quat);
	}

	public Quat4f getRotate() {
		return this.quat;
	}

	public static SourceRotationData create(final List<ImageRotate> rotates) {
		final Builder<Rotate> builder = ImmutableList.builder();
		for (final ImageRotate rotate : rotates)
			builder.add(new Rotate(rotate));
		return new SourceRotationData(builder.build());
	}

	@Override
	public String toString() {
		return String.format("x=%s, y=%s, z=%s, w=%s", this.quat.x, this.quat.y, this.quat.z, this.quat.w);
	}

	public static class SourceRotationData extends RotationData implements IComposable {
		private final ImmutableList<Rotate> rotates;

		public SourceRotationData(final ImmutableList<Rotate> rotates) {
			super(getQuat(rotates));
			this.rotates = rotates;
		}

		private static Quat4f getQuat(final ImmutableList<Rotate> rotates) {
			final Quat4f quat = new Quat4f(0, 0, 0, 1);
			for (final ListIterator<Rotate> it = rotates.listIterator(rotates.size()); it.hasPrevious();)
				quat.mul(it.previous().getRotate());
			return quat;
		}

		@Override
		public String compose() {
			final StringBuilder stb = new StringBuilder();
			for (final Rotate rotate : this.rotates)
				stb.append(rotate.compose());
			return stb.toString();
		}
	}

}
