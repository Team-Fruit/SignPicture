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

public class RotationData implements IMotionFrame<RotationData> {
	public static final float defaultOffset = 4f;
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

		public Quat4f getRotate(final float scale) {
			return this.type.getRotate(this.rotate*scale);
		}

		public Quat4f getRotate() {
			return getRotate(1f);
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

		@Override
		public String toString() {
			return "Rotate [type="+this.type+", rotate="+this.rotate+"]";
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
