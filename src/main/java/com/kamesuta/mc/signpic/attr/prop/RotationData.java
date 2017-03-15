package com.kamesuta.mc.signpic.attr.prop;

import java.util.List;
import java.util.ListIterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Quat4f;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;
import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.signpic.attr.IPropBuilder;
import com.kamesuta.mc.signpic.attr.IPropComposable;
import com.kamesuta.mc.signpic.attr.IPropInterpolatable;

public abstract class RotationData {
	public static final float defaultOffset = 4f;
	public static final float defaultAngle = 0f;
	public static final float defaultAxis = 1f;

	public @Nonnull Quat4f getRotate() {
		return getRotate(1f);
	}

	public abstract @Nonnull Quat4f getRotate(float scale);

	public static class PerRotation extends RotationData {
		private @Nonnull Quat4f rotate;

		public PerRotation(final @Nonnull Quat4f rotate) {
			this.rotate = rotate;
		}

		@Override
		public @Nonnull Quat4f getRotate(final float scale) {
			return this.rotate;
		}

		@Override
		public @Nonnull String toString() {
			return "PerDiffRotation [rotate="+this.rotate+"]";
		}
	}

	public static abstract class KeyRotation extends RotationData implements IPropInterpolatable<RotationData>, IPropComposable {
		@Override
		public @Nonnull RotationData per() {
			return this;
		}
	}

	public static final class BaseRotation extends KeyRotation {
		protected final @Nonnull Quat4f diff;

		private BaseRotation() {
			this.diff = RotationMath.newQuat();
		}

		@Override
		public final @Nonnull Quat4f getRotate(final float scale) {
			return this.diff;
		}

		@Override
		public final @Nonnull String compose() {
			return "";
		}

		@Override
		public final @Nonnull RotationData per(final float per, final @Nullable RotationData before) {
			return this;
		}

		@Override
		public final @Nonnull String toString() {
			return "BaseRotationData []";
		}
	}

	public static class DiffRotation extends KeyRotation {
		private final @Nonnull KeyRotation base;
		private final @Nonnull AxisAngle4f diffangleaxis;
		private final @Nonnull ImmutableList<Rotate> diffglobalaxis;

		public DiffRotation(final @Nonnull KeyRotation base, final @Nonnull AxisAngle4f diffangleaxis, final @Nonnull ImmutableList<Rotate> diffglobalaxis) {
			this.base = base;
			this.diffangleaxis = diffangleaxis;
			this.diffglobalaxis = diffglobalaxis;
		}

		@Override
		public @Nonnull Quat4f getRotate(final float scale) {
			final Quat4f quat = new Quat4f(this.base.getRotate(1f));
			quat.mul(getDiffAngleQuat(scale));
			quat.mul(getDiffGlobalQuat(scale));
			return quat;
		}

		private @Nonnull Quat4f getDiffGlobalQuat(final float scale) {
			final Quat4f quat = RotationMath.newQuat();
			for (final ListIterator<Rotate> it = this.diffglobalaxis.listIterator(this.diffglobalaxis.size()); it.hasPrevious();)
				quat.mul(it.previous().getRotate(scale));
			return quat;
		}

		private @Nonnull Quat4f getDiffAngleQuat(final float scale) {
			return RotationMath.toQuat(new AxisAngle4f(this.diffangleaxis.x, this.diffangleaxis.y, this.diffangleaxis.z, this.diffangleaxis.angle*scale));
		}

		@Override
		public @Nonnull RotationData per(final float per, final @Nullable RotationData before) {
			return new PerRotation(getRotate(per));
		}

		@Override
		public @Nonnull String compose() {
			final StringBuilder stb = new StringBuilder(this.base.compose());
			if (this.diffangleaxis.angle!=defaultAngle)
				stb.append(PropSyntax.ROTATION_ANGLE.id).append(ShortestFloatFormatter.format(RotationMath.toDegrees(this.diffangleaxis.angle)*8f/360f));
			if (this.diffangleaxis.x!=0)
				if (this.diffangleaxis.x==defaultAxis)
					stb.append(PropSyntax.ROTATION_AXIS_X.id);
				else
					stb.append(PropSyntax.ROTATION_AXIS_X.id).append(ShortestFloatFormatter.format(this.diffangleaxis.x));
			if (this.diffangleaxis.y!=0)
				if (this.diffangleaxis.y==defaultAxis)
					stb.append(PropSyntax.ROTATION_AXIS_Y.id);
				else
					stb.append(PropSyntax.ROTATION_AXIS_Y.id).append(ShortestFloatFormatter.format(this.diffangleaxis.y));
			if (this.diffangleaxis.z!=0)
				if (this.diffangleaxis.z==defaultAxis)
					stb.append(PropSyntax.ROTATION_AXIS_Z.id);
				else
					stb.append(PropSyntax.ROTATION_AXIS_Z.id).append(ShortestFloatFormatter.format(this.diffangleaxis.z));
			for (final Rotate rotate : this.diffglobalaxis)
				stb.append(rotate.compose());
			return stb.toString();
		}

		@Override
		public @Nonnull String toString() {
			return "DiffRotation [base="+this.base+", diffAxisAngle="+this.diffangleaxis+", diffGlobal="+this.diffglobalaxis+"]";
		}
	}

	public static class Rotate {
		public @Nonnull RotateType type;
		public float rotate;

		public Rotate(final @Nonnull RotateType type, final float rotate) {
			this.type = type;
			this.rotate = rotate;
		}

		public @Nonnull Quat4f getRotate(final float scale) {
			return this.type.getRotate(this.rotate*scale);
		}

		public @Nonnull Quat4f getRotate() {
			return getRotate(1f);
		}

		public @Nonnull String compose() {
			final float rotate = this.rotate;
			if (rotate==0)
				return "";
			else if (rotate==defaultOffset)
				return this.type.id;
			else
				return this.type.id+ShortestFloatFormatter.format(rotate);
		}

		@Override
		public @Nonnull String toString() {
			return "Rotate [type="+this.type+", rotate="+this.rotate+"]";
		}
	}

	public static enum RotateType {
		X(PropSyntax.ROTATION_X.id) {
			@Override
			public @Nonnull Quat4f getRotate(final float f) {
				return RotationMath.quatDeg(f*360f/8f, 1f, 0f, 0f);
			}
		},
		Y(PropSyntax.ROTATION_Y.id) {
			@Override
			public @Nonnull Quat4f getRotate(final float f) {
				return RotationMath.quatDeg(f*360f/8f, 0f, 1f, 0f);
			}
		},
		Z(PropSyntax.ROTATION_Z.id) {
			@Override
			public @Nonnull Quat4f getRotate(final float f) {
				return RotationMath.quatDeg(f*360f/8f, 0f, 0f, 1f);
			}
		},
		;

		public final @Nonnull String id;

		private RotateType(@Nonnull final String id) {
			this.id = id;
		}

		public abstract @Nonnull Quat4f getRotate(float f);
	}

	public static class RotationMath {
		public static final float PI = (float) Math.PI;

		public static @Nonnull Quat4f newQuat() {
			return new Quat4f(0, 0, 0, 1);
		}

		public static float toRadians(final float angdeg) {
			return angdeg/180f*PI;
		}

		public static float toDegrees(final float angrad) {
			return angrad*180f/PI;
		}

		public static @Nonnull Quat4f toQuat(final @Nonnull AxisAngle4f axis) {
			if (axis.angle==0f)
				return newQuat();
			final Quat4f q = new Quat4f();
			q.set(axis);
			return q;
		}

		public static @Nonnull AxisAngle4f toAxis(final @Nonnull Quat4f quat) {
			final @Nonnull AxisAngle4f q = new AxisAngle4f();
			q.set(quat);
			return q;
		}

		public static @Nonnull AxisAngle4f axisRad(final float angle, final float x, final float y, final float z) {
			return new AxisAngle4f(x, y, z, angle);
		}

		public static @Nonnull AxisAngle4f axisDeg(final float angle, final float x, final float y, final float z) {
			return new AxisAngle4f(x, y, z, RotationMath.toRadians(angle));
		}

		public static @Nonnull Quat4f quatRad(final float angle, final float x, final float y, final float z) {
			return toQuat(axisRad(angle, x, y, z));
		}

		public static @Nonnull Quat4f quatDeg(final float angle, final float x, final float y, final float z) {
			return toQuat(axisDeg(angle, x, y, z));
		}
	}

	public static class RotationGL {
		public static void glRotate(final @Nonnull AxisAngle4f axis) {
			OpenGL.glRotatef(RotationMath.toDegrees(axis.angle), axis.x, axis.y, axis.z);
		}

		public static void glRotate(final @Nonnull Quat4f quat) {
			glRotate(RotationMath.toAxis(quat));
		}
	}

	public static class RotationBuilder implements IPropBuilder<DiffRotation, KeyRotation> {

		public final @Nonnull List<ImageRotate> rotates = Lists.newLinkedList();
		public float x = 0;
		public float y = 0;
		public float z = 0;
		public float angle = 0;
		public @Nullable KeyRotation base;

		@Override
		public @Nonnull DiffRotation diff(@Nullable KeyRotation base) {
			if (base==null)
				base = new BaseRotation();
			final AxisAngle4f axis = new AxisAngle4f(this.x, this.y, this.z, this.angle);
			final Builder<Rotate> builder = ImmutableList.builder();
			for (final ImageRotate rotate : this.rotates)
				builder.add(rotate.build());
			return new DiffRotation(base, axis, builder.build());
		}

		@Override
		public boolean parse(final @Nonnull String src, final @Nonnull String key, final @Nonnull String value) {
			if (StringUtils.equals(key, PropSyntax.ROTATION_X.id))
				this.rotates.add(new ImageRotate(RotateType.X, NumberUtils.toFloat(value, RotationData.defaultOffset)));
			else if (StringUtils.equals(key, PropSyntax.ROTATION_Y.id))
				this.rotates.add(new ImageRotate(RotateType.Y, NumberUtils.toFloat(value, RotationData.defaultOffset)));
			else if (StringUtils.equals(key, PropSyntax.ROTATION_Z.id))
				this.rotates.add(new ImageRotate(RotateType.Z, NumberUtils.toFloat(value, RotationData.defaultOffset)));
			else if (StringUtils.equals(key, PropSyntax.ROTATION_ANGLE.id))
				this.angle += RotationMath.toRadians(NumberUtils.toFloat(value, RotationData.defaultAngle)*360f/8f);
			else if (StringUtils.equals(key, PropSyntax.ROTATION_AXIS_X.id))
				if (StringUtils.isEmpty(value))
					this.x += RotationData.defaultAxis;
				else
					this.x += NumberUtils.toFloat(value, RotationData.defaultAxis);
			else if (StringUtils.equals(key, PropSyntax.ROTATION_AXIS_Y.id))
				if (StringUtils.isEmpty(value))
					this.y += RotationData.defaultAxis;
				else
					this.y += NumberUtils.toFloat(value, RotationData.defaultAxis);
			else if (StringUtils.equals(key, PropSyntax.ROTATION_AXIS_Z.id))
				if (StringUtils.isEmpty(value))
					this.z += RotationData.defaultAxis;
				else
					this.z += NumberUtils.toFloat(value, RotationData.defaultAxis);
			else
				return false;
			return true;
		}

		@Override
		public @Nonnull String compose() {
			return diff(null).compose();
		}

		@Override
		public String toString() {
			return String.format("RotationBuilder [rotates=%s, x=%s, y=%s, z=%s, angle=%s, base=%s]", this.rotates, this.x, this.y, this.z, this.angle, this.base);
		}

		public static class ImageRotate {
			public @Nonnull RotateType type;
			public float rotate;

			public ImageRotate(final @Nonnull RotateType type, final float rotate) {
				this.type = type;
				this.rotate = rotate;
			}

			public @Nonnull Rotate build() {
				return new Rotate(this.type, this.rotate);
			}
		}
	}
}
