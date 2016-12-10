package com.kamesuta.mc.signpic.image.meta;

import java.util.List;
import java.util.ListIterator;

import javax.vecmath.Quat4f;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.kamesuta.mc.signpic.image.meta.ImageRotation.ImageRotate;
import com.kamesuta.mc.signpic.image.meta.RotationData.Rotate;

public abstract class DiffRotationData {
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
			final Quat4f qdiff = getDiffQuat(scale);
			// qdiff.interpolate(new Quat4f(0, 0, 0, 1), qdiff, scale);
			qdiff.mul(this.base.getRotate(1f));
			return qdiff;
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
}
