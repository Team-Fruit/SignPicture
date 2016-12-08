package com.kamesuta.mc.signpic.image.meta;

import java.util.List;
import java.util.ListIterator;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Quat4f;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;
import com.kamesuta.mc.signpic.image.meta.ImageRotation.ImageRotate;
import com.kamesuta.mc.signpic.render.OpenGL;

public abstract class RotationData implements IMotionFrame<RotationData>, IComposable {
	public static final float defaultOffset = 4f;

	public abstract Quat4f getRotate();

	@Override
	public RotationData per(final float per, final RotationData before) {
		return new PerRotationData(this, before, per);
	}

	public static RotationData create(final List<ImageRotate> rotates) {
		final Builder<Rotate> builder = ImmutableList.builder();
		for (final ImageRotate rotate : rotates)
			builder.add(new Rotate(rotate));
		return new AbsRotationData(builder.build());
	}

	private static class AbsRotationData extends RotationData {
		private final ImmutableList<Rotate> rotates;
		private Quat4f quat = new Quat4f(0, 0, 0, 1);

		private AbsRotationData(final ImmutableList<Rotate> rotates) {
			this.rotates = rotates;
			for (final ListIterator<Rotate> it = this.rotates.listIterator(this.rotates.size()); it.hasPrevious();)
				this.quat.mul(it.previous().getRotate());
		}

		@Override
		public String compose() {
			final StringBuilder stb = new StringBuilder();
			for (final Rotate rotate : this.rotates)
				stb.append(rotate.compose());
			return stb.toString();
		}

		@Override
		public Quat4f getRotate() {
			return this.quat;
		}
	}

	private static class PerRotationData extends RotationData {
		private final RotationData after;
		private final RotationData before;
		private final float per;

		private PerRotationData(final RotationData after, final RotationData before, final float per) {
			this.after = after;
			this.before = before;
			this.per = per;
		}

		@Override
		@Deprecated
		public String compose() {
			return this.after.compose();
		}

		@Override
		public Quat4f getRotate() {
			final Quat4f quat = new Quat4f();
			quat.set(this.after.getRotate());
			quat.interpolate(this.before.getRotate(), this.per);
			return quat;
		}
	}

	public static class Rotate {
		public RotateType type;
		public float rotate;

		protected Rotate(final RotateType type, final float rotate) {
			this.type = type;
			this.rotate = rotate;
		}

		public Rotate(final ImageRotate rotate) {
			this(rotate.type, rotate.rotate);
		}

		public Quat4f getRotate() {
			return this.type.getRotate(this.rotate);
		}

		public String compose() {
			final float rotate = (this.rotate%8+8)%8;
			if (rotate==0)
				return "";
			else if (rotate==defaultOffset)
				return this.type.name();
			else
				return this.type.name()+ShortestFloatFormatter.format(rotate);
		}
	}

	public static enum RotateType {
		X {
			@Override
			public Quat4f getRotate(final float f) {
				return RotationMath.quatDeg(f*360f/8f, 1f, 0f, 0f);
			}
		},
		Y {
			@Override
			public Quat4f getRotate(final float f) {
				return RotationMath.quatDeg(f*360f/8f, 0f, 1f, 0f);
			}
		},
		Z {
			@Override
			public Quat4f getRotate(final float f) {
				return RotationMath.quatDeg(f*360f/8f, 0f, 0f, 1f);
			}
		},
		;

		public abstract Quat4f getRotate(float f);
	}

	public static class RotationMath {
		public static final float PI = (float) Math.PI;

		public static float toRadians(final float angdeg) {
			return angdeg/180f*PI;
		}

		public static float toDegrees(final float angrad) {
			return angrad*180f/PI;
		}

		public static Quat4f toQuat(final AxisAngle4f axis) {
			final Quat4f q = new Quat4f();
			q.set(axis);
			return q;
		}

		public static AxisAngle4f toAxis(final Quat4f quat) {
			final AxisAngle4f q = new AxisAngle4f();
			q.set(quat);
			return q;
		}

		public static AxisAngle4f axisRad(final float angle, final float x, final float y, final float z) {
			return new AxisAngle4f(x, y, z, angle);
		}

		public static AxisAngle4f axisDeg(final float angle, final float x, final float y, final float z) {
			return new AxisAngle4f(x, y, z, RotationMath.toRadians(angle));
		}

		public static Quat4f quatRad(final float angle, final float x, final float y, final float z) {
			return toQuat(axisRad(angle, x, y, z));
		}

		public static Quat4f quatDeg(final float angle, final float x, final float y, final float z) {
			return toQuat(axisDeg(angle, x, y, z));
		}
	}

	public static class RotationGL {
		public static void glRotate(final AxisAngle4f axis) {
			OpenGL.glRotatef(RotationMath.toDegrees(axis.angle), axis.x, axis.y, axis.z);
		}

		public static void glRotate(final Quat4f quat) {
			glRotate(RotationMath.toAxis(quat));
		}
	}
}
