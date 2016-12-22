package com.kamesuta.mc.signpic.image.meta;

import java.util.ListIterator;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Quat4f;

import com.google.common.collect.ImmutableList;
import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;
import com.kamesuta.mc.signpic.render.OpenGL;

public abstract class RotationData {
	public static final float defaultOffset = 4f;
	public static final float defaultAngle = 0f;
	public static final float defaultAxis = 1f;

	public Quat4f getRotate() {
		return getRotate(1f);
	}

	public abstract Quat4f getRotate(float scale);

	public static class PerRotation extends RotationData {
		private Quat4f rotate;

		public PerRotation(final Quat4f rotate) {
			this.rotate = rotate;
		}

		@Override
		public Quat4f getRotate(final float scale) {
			return this.rotate;
		}

		@Override
		public String toString() {
			return "PerDiffRotation [rotate="+this.rotate+"]";
		}
	}

	public static abstract class KeyRotation extends RotationData implements IMotionFrame<RotationData>, IComposable {
		@Override
		public RotationData per() {
			return this;
		}
	}

	public static class BaseRotation extends KeyRotation {
		protected final Quat4f diff;

		private BaseRotation() {
			this.diff = RotationMath.newQuat();
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
		public final RotationData per(final float per, final RotationData before) {
			return this;
		}

		@Override
		public final String toString() {
			return "BaseRotationData []";
		}
	}

	public static class DiffRotation extends KeyRotation {
		private final KeyRotation base;
		private final AxisAngle4f diffangleaxis;
		private final ImmutableList<Rotate> diffglobalaxis;

		public DiffRotation(final KeyRotation base, final AxisAngle4f diffangleaxis, final ImmutableList<Rotate> diffglobalaxis) {
			this.base = base;
			this.diffangleaxis = diffangleaxis;
			this.diffglobalaxis = diffglobalaxis;
		}

		@Override
		public Quat4f getRotate(final float scale) {
			final Quat4f quat = new Quat4f(this.base.getRotate(1f));
			quat.mul(getDiffAngleQuat(scale));
			quat.mul(getDiffGlobalQuat(scale));
			return quat;
		}

		private Quat4f getDiffGlobalQuat(final float scale) {
			final Quat4f quat = RotationMath.newQuat();
			for (final ListIterator<Rotate> it = this.diffglobalaxis.listIterator(this.diffglobalaxis.size()); it.hasPrevious();)
				quat.mul(it.previous().getRotate(scale));
			return quat;
		}

		private Quat4f getDiffAngleQuat(final float scale) {
			return RotationMath.toQuat(new AxisAngle4f(this.diffangleaxis.x, this.diffangleaxis.y, this.diffangleaxis.z, this.diffangleaxis.angle*scale));
		}

		@Override
		public RotationData per(final float per, final RotationData before) {
			return new PerRotation(getRotate(per));
		}

		@Override
		public String compose() {
			final StringBuilder stb = new StringBuilder(this.base.compose());
			if (this.diffangleaxis.angle!=defaultAngle)
				stb.append("A").append(ShortestFloatFormatter.format(RotationMath.toDegrees(this.diffangleaxis.angle)*8f/360f));
			if (this.diffangleaxis.x!=0)
				if (this.diffangleaxis.x==defaultAxis)
					stb.append("P");
				else
					stb.append("P").append(ShortestFloatFormatter.format(this.diffangleaxis.x));
			if (this.diffangleaxis.y!=0)
				if (this.diffangleaxis.y==defaultAxis)
					stb.append("Q");
				else
					stb.append("Q").append(ShortestFloatFormatter.format(this.diffangleaxis.y));
			if (this.diffangleaxis.z!=0)
				if (this.diffangleaxis.z==defaultAxis)
					stb.append("R");
				else
					stb.append("R").append(ShortestFloatFormatter.format(this.diffangleaxis.z));
			for (final Rotate rotate : this.diffglobalaxis)
				stb.append(rotate.compose());
			return stb.toString();
		}

		@Override
		public String toString() {
			return "DiffRotation [base="+this.base+", diffAxisAngle="+this.diffangleaxis+", diffGlobal="+this.diffglobalaxis+"]";
		}
	}

	public static class Rotate {
		public RotateType type;
		public float rotate;

		public Rotate(final RotateType type, final float rotate) {
			this.type = type;
			this.rotate = rotate;
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

		public static Quat4f newQuat() {
			return new Quat4f(0, 0, 0, 1);
		}

		public static float toRadians(final float angdeg) {
			return angdeg/180f*PI;
		}

		public static float toDegrees(final float angrad) {
			return angrad*180f/PI;
		}

		public static Quat4f toQuat(final AxisAngle4f axis) {
			if (axis.angle==0f)
				return newQuat();
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
