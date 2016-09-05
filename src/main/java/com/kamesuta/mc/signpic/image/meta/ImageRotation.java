package com.kamesuta.mc.signpic.image.meta;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class ImageRotation implements ImageMeta.MetaParser {
	public static final float defaultOffset = 4f;

	private final List<Rotate> rotateTypes = new ArrayList<Rotate>();

	private final float[] rotates = new float[3];

	/**
	 * h=phi
	 * s=psi
	 * t=theta
	 * @param src
	 */
	public ImageRotation parseRotation(final Map<String, String> meta, final String src) {
		final int[] indexes = new int[3];
		indexes[0] = StringUtils.indexOf(src, "X");
		indexes[1] = StringUtils.indexOf(src, "Y");
		indexes[2] = StringUtils.indexOf(src, "Z");
		Arrays.sort(indexes);
		for (final int index : indexes) {
			if (index != -1) {
				final String key = StringUtils.substring(src, index, index+1);
				final String value = meta.get(key);
				final Rotate type = Rotate.valueOf(key);
				final int o = this.rotateTypes.size();
				this.rotateTypes.add(type);
				this.rotates[o] = (StringUtils.isEmpty(value)) ? defaultOffset : NumberUtils.toFloat(value, 0f);
			}
		}
		return this;
	}

	public void rotate() {
		for (final ListIterator<Rotate> it = this.rotateTypes.listIterator(); it.hasNext();) {
			final float rotate = this.rotates[it.nextIndex()];
			final Rotate type = it.next();
			type.rotate(rotate);
		}
	}

	/**
	 * h=phi
	 * s=psi
	 * t=theta
	 */
	@Override
	public String toString() {
		final StringBuilder stb = new StringBuilder();
		for (final ListIterator<Rotate> it = this.rotateTypes.listIterator(); it.hasNext();) {
			final float rotate = this.rotates[it.nextIndex()];
			final Rotate type = it.next();
			if (rotate==defaultOffset) stb.append(type.name());
			else stb.append(type.name()).append(signformat.format(rotate));
		}
		return stb.toString();
	}

	private enum Rotate {
		X {
			@Override
			public void rotate(final float f) {
				glRotatef(f*360f/8f, 1f, 0f, 0f);
			}
		},
		Y {
			@Override
			public void rotate(final float f) {
				glRotatef(f*360f/8f, 0f, 1f, 0f);
			}
		},
		Z {
			@Override
			public void rotate(final float f) {
				glRotatef(f*360f/8f, 0f, 0f, 1f);
			}
		},
		;

		public abstract void rotate(float f);
	}
}
