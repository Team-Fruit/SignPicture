package com.kamesuta.mc.signpic.image.meta;

import java.util.LinkedList;
import java.util.List;

import javax.vecmath.AxisAngle4f;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.kamesuta.mc.signpic.image.meta.RotationData.DiffRotation;
import com.kamesuta.mc.signpic.image.meta.RotationData.KeyRotation;
import com.kamesuta.mc.signpic.image.meta.RotationData.Rotate;
import com.kamesuta.mc.signpic.image.meta.RotationData.RotateType;
import com.kamesuta.mc.signpic.image.meta.RotationData.RotationMath;

public class ImageRotation implements MetaMovie<DiffRotation, KeyRotation> {

	public final List<ImageRotate> rotates = new LinkedList<ImageRotate>();
	public float x = 0;
	public float y = 0;
	public float z = 0;
	public float angle = 0;
	public KeyRotation base;

	@Override
	public DiffRotation diff(final KeyRotation base) {
		final AxisAngle4f axis = new AxisAngle4f(this.x, this.y, this.z, this.angle);
		final Builder<Rotate> builder = ImmutableList.builder();
		for (final ImageRotate rotate : this.rotates)
			builder.add(rotate.build());
		return new DiffRotation(base, axis, builder.build());
	}

	@Override
	public boolean parse(final String src, final String key, final String value) {
		if (StringUtils.equals(key, RotateType.X.name()))
			this.rotates.add(new ImageRotate(RotateType.X, NumberUtils.toFloat(value, RotationData.defaultOffset)));
		else if (StringUtils.equals(key, RotateType.Y.name()))
			this.rotates.add(new ImageRotate(RotateType.Y, NumberUtils.toFloat(value, RotationData.defaultOffset)));
		else if (StringUtils.equals(key, RotateType.Z.name()))
			this.rotates.add(new ImageRotate(RotateType.Z, NumberUtils.toFloat(value, RotationData.defaultOffset)));
		else if (StringUtils.equals(key, "A"))
			this.angle += RotationMath.toRadians(NumberUtils.toFloat(value, RotationData.defaultAngle)*360f/8f);
		else if (StringUtils.equals(key, "P"))
			if (StringUtils.isEmpty(value))
				this.x += RotationData.defaultAxis;
			else
				this.x += NumberUtils.toFloat(value, RotationData.defaultAxis);
		else if (StringUtils.equals(key, "Q"))
			if (StringUtils.isEmpty(value))
				this.y += RotationData.defaultAxis;
			else
				this.y += NumberUtils.toFloat(value, RotationData.defaultAxis);
		else if (StringUtils.equals(key, "R"))
			if (StringUtils.isEmpty(value))
				this.z += RotationData.defaultAxis;
			else
				this.z += NumberUtils.toFloat(value, RotationData.defaultAxis);
		else
			return false;
		return true;
	}

	@Override
	public String compose() {
		return diff(null).compose();
	}

	@Override
	public String toString() {
		return compose();
	}

	public static class ImageRotate {
		public RotateType type;
		public float rotate;

		public ImageRotate(final RotateType type, final float rotate) {
			this.type = type;
			this.rotate = rotate;
		}

		public Rotate build() {
			return new Rotate(this.type, this.rotate);
		}
	}
}
