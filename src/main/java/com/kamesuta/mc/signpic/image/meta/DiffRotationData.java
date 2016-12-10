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

public abstract class DiffRotationData {
	public static final float defaultOffset = 4f;

	public Quat4f getRotate() {
		return getRotate(1f);
	}

	public abstract Quat4f getRotate(float scale);

	public static DiffRotationDataDiff create(DiffRotationDataKey base, final List<ImageRotate> rotates) {
		if (base==null)
			base = new BaseRotationData();
		final Builder<Rotate> builder = ImmutableList.builder();
		for (final ImageRotate rotate : rotates)
			builder.add(new Rotate(rotate));
		return new DiffRotationDataDiff(base, builder.build());
	}

	public static class DiffRotationDataPer extends DiffRotationData {
		private Quat4f rotate;

		public DiffRotationDataPer(final Quat4f rotate) {
			this.rotate = rotate;
		}

		@Override
		public Quat4f getRotate(final float scale) {
			return this.rotate;
		}

		@Override
		public String toString() {
			return "DiffRotationDataPer [rotate="+this.rotate+"]";
		}
	}

	public static abstract class DiffRotationDataKey extends DiffRotationData implements IMotionFrame<DiffRotationData>, IComposable {
		@Override
		public DiffRotationData per() {
			return this;
		}
	}

	public static class BaseRotationData extends DiffRotationDataKey {
		protected final Quat4f diff;

		private BaseRotationData() {
			this.diff = new Quat4f(0, 0, 0, 1);
		}

		@Override
		public final Quat4f getRotate(final float scale) {
			return this.diff;
		}

		@Override
		public final String compose() {
			return "";
		}

		@Override
		public final DiffRotationData per(final float per, final DiffRotationData before) {
			return this;
		}

		@Override
		public final String toString() {
			return "BaseRotationData []";
		}
	}

	public static class DiffRotationDataDiff extends DiffRotationDataKey {
		private final DiffRotationDataKey base;
		private final ImmutableList<Rotate> diff;

		public DiffRotationDataDiff(final DiffRotationDataKey base, final ImmutableList<Rotate> diff) {
			this.base = base;
			this.diff = diff;
		}

		@Override
		public Quat4f getRotate(final float scale) {
			final Quat4f base = new Quat4f(this.base.getRotate(1f));
			final Quat4f qdiff = getDiffQuat(scale);
			// qdiff.interpolate(new Quat4f(0, 0, 0, 1), qdiff, scale);
			qdiff.mul(base);
			return qdiff;
			// base.mul(qdiff);
			// return base;
		}

		private Quat4f getDiffQuat(final float scale) {
			final Quat4f quat = new Quat4f(0, 0, 0, 1);
			for (final ListIterator<Rotate> it = this.diff.listIterator(this.diff.size()); it.hasPrevious();)
				quat.mul(it.previous().getRotate(scale));
			return quat;
		}

		@Override
		public DiffRotationData per(final float per, final DiffRotationData before) {
			return new DiffRotationDataPer(getRotate(per));
		}

		@Override
		public String compose() {
			final StringBuilder stb = new StringBuilder(this.base.compose());
			for (final Rotate rotate : this.diff)
				stb.append(rotate.compose());
			return stb.toString();
		}

		@Override
		public String toString() {
			return "DiffRotationDataDiff [base="+this.base+", diff="+this.diff+"]";
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
