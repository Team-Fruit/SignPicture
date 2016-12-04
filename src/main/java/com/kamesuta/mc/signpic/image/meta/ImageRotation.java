package com.kamesuta.mc.signpic.image.meta;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.signpic.image.meta.RotationData.RotateType;

public class ImageRotation extends ImageMeta.MetaParser {
	public final List<ImageRotate> rotates = new LinkedList<ImageRotate>();

	public RotationData get() {
		return new RotationData(this.rotates);
	}

	@Override
	public ImageRotation reset() {
		this.rotates.clear();
		return this;
	}

	@Override
	public boolean parse(final String src, final String key, final String value) {
		if (StringUtils.equals(key, RotateType.X.name()))
			this.rotates.add(new ImageRotate(RotateType.X, NumberUtils.toFloat(value, RotationData.defaultOffset)));
		else if (StringUtils.equals(key, RotateType.Y.name()))
			this.rotates.add(new ImageRotate(RotateType.Y, NumberUtils.toFloat(value, RotationData.defaultOffset)));
		else if (StringUtils.equals(key, RotateType.Z.name()))
			this.rotates.add(new ImageRotate(RotateType.Z, NumberUtils.toFloat(value, RotationData.defaultOffset)));
		else
			return false;
		return true;
	}

	@Override
	public String compose() {
		return get().compose();
	}

	@Override
	public String toString() {
		return compose();
	}

	public void rotate() {
		get().rotate();
	}

	public static class ImageRotate {
		public RotateType type;
		public float rotate;

		public ImageRotate(final RotateType type, final float rotate) {
			this.type = type;
			this.rotate = rotate;
		}
	}
}
