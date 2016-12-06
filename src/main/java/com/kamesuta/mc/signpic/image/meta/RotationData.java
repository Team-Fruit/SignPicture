package com.kamesuta.mc.signpic.image.meta;

import java.util.List;
import java.util.ListIterator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;
import com.kamesuta.mc.signpic.image.meta.ImageRotation.ImageRotate;
import com.kamesuta.mc.signpic.render.OpenGL;

public class RotationData {
	public static final float defaultOffset = 4f;

	public final ImmutableList<Rotate> rotates;

	protected RotationData(final ImmutableList<Rotate> rotates) {
		this.rotates = rotates;
	}

	public RotationData(final List<ImageRotate> rotates) {
		final Builder<Rotate> builder = ImmutableList.builder();
		for (final ImageRotate rotate : rotates)
			builder.add(new Rotate(rotate));
		this.rotates = builder.build();
	}

	public void rotate() {
		for (final ListIterator<Rotate> it = this.rotates.listIterator(this.rotates.size()); it.hasPrevious();)
			it.previous().rotate();
	}

	public RotationData scale(final float scale) {
		final Builder<Rotate> builder = ImmutableList.builder();
		for (final Rotate rotate : this.rotates)
			builder.add(new Rotate(rotate.type, rotate.rotate*scale));
		return new RotationData(builder.build());
	}

	public RotationData per(final float per, final RotationData before) {
		return this;
	}

	public String compose() {
		final StringBuilder stb = new StringBuilder();
		for (final Rotate rotate : this.rotates)
			stb.append(rotate.compose());
		return stb.toString();
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
				return this.type.name()+ShortestFloatFormatter.format(rotate);
		}
	}

	public static enum RotateType {
		X {
			@Override
			public void rotate(final float f) {
				OpenGL.glRotatef(f*360f/8f, 1f, 0f, 0f);
			}
		},
		Y {
			@Override
			public void rotate(final float f) {
				OpenGL.glRotatef(f*360f/8f, 0f, 1f, 0f);
			}
		},
		Z {
			@Override
			public void rotate(final float f) {
				OpenGL.glRotatef(f*360f/8f, 0f, 0f, 1f);
			}
		},
		;

		public abstract void rotate(float f);
	}
}
