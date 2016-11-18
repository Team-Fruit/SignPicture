package com.kamesuta.mc.signpic.image.meta;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import net.minecraft.client.renderer.GlStateManager;

public class ImageRotation extends ImageMeta.MetaParser {
	public static final float defaultOffset = 4f;

	public final List<Rotate> rotates = new LinkedList<Rotate>();

	@Override
	public ImageRotation reset() {
		this.rotates.clear();
		return this;
	}

	@Override
	public boolean parse(final String src, final String key, final String value) {
		if (StringUtils.equals(key, RotateType.X.name()))
			this.rotates.add(new Rotate(RotateType.X, NumberUtils.toFloat(value, defaultOffset)));
		else if (StringUtils.equals(key, RotateType.Y.name()))
			this.rotates.add(new Rotate(RotateType.Y, NumberUtils.toFloat(value, defaultOffset)));
		else if (StringUtils.equals(key, RotateType.Z.name()))
			this.rotates.add(new Rotate(RotateType.Z, NumberUtils.toFloat(value, defaultOffset)));
		else
			return false;
		return true;
	}

	@Override
	public String compose() {
		final StringBuilder stb = new StringBuilder();
		for (final Rotate rotate : this.rotates)
			stb.append(rotate.compose());
		return stb.toString();
	}

	@Override
	public String toString() {
		return compose();
	}

	public void rotate() {
		for (final ListIterator<Rotate> it = this.rotates.listIterator(this.rotates.size()); it.hasPrevious();)
			it.previous().rotate();
	}

	public static class Rotate {
		public RotateType type;
		public float rotate;

		public Rotate(final RotateType type, final float rotate) {
			this.type = type;
			this.rotate = rotate;
		}

		public void rotate() {
			this.type.rotate(this.rotate);
		}

		public String compose() {
			final float rotate = (this.rotate%8+8)%8;
			if (rotate==0)
				return "";
			else if (rotate==defaultOffset)
				return this.type.name();
			else
				return this.type.name()+format(rotate);
		}
	}

	public static enum RotateType {
		X {
			@Override
			public void rotate(final float f) {
				GlStateManager.rotate(f*360f/8f, 1f, 0f, 0f);
			}
		},
		Y {
			@Override
			public void rotate(final float f) {
				GlStateManager.rotate(f*360f/8f, 0f, 1f, 0f);
			}
		},
		Z {
			@Override
			public void rotate(final float f) {
				GlStateManager.rotate(f*360f/8f, 0f, 0f, 1f);
			}
		},
		;

		public abstract void rotate(float f);
	}
}
