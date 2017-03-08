package com.kamesuta.mc.signpic.attr;

import javax.annotation.Nonnull;

import com.kamesuta.mc.signpic.attr.prop.AnimationData;
import com.kamesuta.mc.signpic.attr.prop.AnimationData.AnimationBuilder;
import com.kamesuta.mc.signpic.attr.prop.OffsetData;
import com.kamesuta.mc.signpic.attr.prop.OffsetData.OffsetBuilder;
import com.kamesuta.mc.signpic.attr.prop.OffsetData.OffsetDoublePropBuilder;
import com.kamesuta.mc.signpic.attr.prop.OffsetData.OffsetPropBuilder;
import com.kamesuta.mc.signpic.attr.prop.PropSyntax;
import com.kamesuta.mc.signpic.attr.prop.RotationData.DiffRotation;
import com.kamesuta.mc.signpic.attr.prop.RotationData.KeyRotation;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationBuilder;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.attr.prop.SizeData.SizeBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBlend;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBlend.TextureBlendBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBlend.TextureBlendType;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBoolean;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBoolean.TextureBooleanBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureBoolean.TextureBooleanType;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureFloat;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureFloat.TextureFloatBuilder;
import com.kamesuta.mc.signpic.attr.prop.TextureData.TextureFloat.TextureFloatType;

public class Attrs {
	public static class Attr<ReaderDiffed, ReaderKeyFrame, ReaderReader extends IPropBuilder<ReaderDiffed, ReaderKeyFrame>> {
		private final @Nonnull IAttrReader<ReaderDiffed, ReaderKeyFrame, ReaderReader> reader;

		private Attr(final @Nonnull IAttrReader<ReaderDiffed, ReaderKeyFrame, ReaderReader> reader) {
			this.reader = reader;
		}

		public @Nonnull IAttrReader<ReaderDiffed, ReaderKeyFrame, ReaderReader> getReader() {
			return this.reader;
		}

		public @Nonnull ReaderReader getWriter() {
			return getReader().builder();
		}

		private static @Nonnull <ReaderDiffed, ReaderKeyFrame, ReaderReader extends IPropBuilder<ReaderDiffed, ReaderKeyFrame>> Attr<ReaderDiffed, ReaderKeyFrame, ReaderReader> create(final @Nonnull IAttrReader<ReaderDiffed, ReaderKeyFrame, ReaderReader> reader) {
			return new Attr<ReaderDiffed, ReaderKeyFrame, ReaderReader>(reader);
		}
	}

	public static final @Nonnull Attr<AnimationData, AnimationData, AnimationBuilder> ANIMATION = Attr.create(new IAttrReader<AnimationData, AnimationData, AnimationBuilder>() {
		@Override
		public @Nonnull AnimationBuilder builder() {
			return new AnimationBuilder();
		}
	});
	public static final @Nonnull Attr<SizeData, SizeData, SizeBuilder> SIZE = Attr.create(new IAttrReader<SizeData, SizeData, SizeBuilder>() {
		@Override
		public @Nonnull SizeBuilder builder() {
			return new SizeBuilder();
		}
	});

	public static final @Nonnull Attr<OffsetData, OffsetData, OffsetBuilder> OFFSET = Attr.create(new IAttrReader<OffsetData, OffsetData, OffsetBuilder>() {
		@Override
		public @Nonnull OffsetBuilder builder() {
			return new OffsetBuilder(
					new OffsetDoublePropBuilder(PropSyntax.OFFSET_LEFT.id, PropSyntax.OFFSET_RIGHT.id),
					new OffsetDoublePropBuilder(PropSyntax.OFFSET_DOWN.id, PropSyntax.OFFSET_UP.id),
					new OffsetDoublePropBuilder(PropSyntax.OFFSET_BACK.id, PropSyntax.OFFSET_FRONT.id));
		}
	});
	public static final @Nonnull Attr<OffsetData, OffsetData, OffsetBuilder> OFFSET_CENTER = Attr.create(new IAttrReader<OffsetData, OffsetData, OffsetBuilder>() {
		@Override
		public @Nonnull OffsetBuilder builder() {
			return new OffsetBuilder(
					new OffsetPropBuilder(PropSyntax.OFFSET_CENTER_X.id),
					new OffsetPropBuilder(PropSyntax.OFFSET_CENTER_Y.id),
					new OffsetPropBuilder(PropSyntax.OFFSET_CENTER_Z.id));
		}
	});
	public static final @Nonnull Attr<DiffRotation, KeyRotation, RotationBuilder> ROTATION = Attr.create(new IAttrReader<DiffRotation, KeyRotation, RotationBuilder>() {
		@Override
		public @Nonnull RotationBuilder builder() {
			return new RotationBuilder();
		}
	});

	private static class TexFloatBuilder implements IAttrReader<TextureFloat, TextureFloat, TextureFloatBuilder> {
		private @Nonnull TextureFloatType type;

		public TexFloatBuilder(final @Nonnull TextureFloatType type) {
			this.type = type;
		}

		@Override
		public @Nonnull TextureFloatBuilder builder() {
			return new TextureFloatBuilder(this.type);
		}
	}

	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_X = Attr.create(new TexFloatBuilder(TextureFloatType.U));
	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_Y = Attr.create(new TexFloatBuilder(TextureFloatType.V));
	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_W = Attr.create(new TexFloatBuilder(TextureFloatType.W));
	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_H = Attr.create(new TexFloatBuilder(TextureFloatType.H));
	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_SPLIT_W = Attr.create(new TexFloatBuilder(TextureFloatType.C));
	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_SPLIT_H = Attr.create(new TexFloatBuilder(TextureFloatType.S));
	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_OPACITY = Attr.create(new TexFloatBuilder(TextureFloatType.O));
	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_LIGHT_X = Attr.create(new TexFloatBuilder(TextureFloatType.F));
	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_LIGHT_Y = Attr.create(new TexFloatBuilder(TextureFloatType.G));

	private static class TexBooleanBuilder implements IAttrReader<TextureBoolean, TextureBoolean, TextureBooleanBuilder> {
		private @Nonnull TextureBooleanType type;

		public TexBooleanBuilder(final @Nonnull TextureBooleanType type) {
			this.type = type;
		}

		@Override
		public @Nonnull TextureBooleanBuilder builder() {
			return new TextureBooleanBuilder(this.type);
		}
	}

	public static final @Nonnull Attr<TextureBoolean, TextureBoolean, TextureBooleanBuilder> TEXTURE_REPEAT = Attr.create(new TexBooleanBuilder(TextureBooleanType.R));
	public static final @Nonnull Attr<TextureBoolean, TextureBoolean, TextureBooleanBuilder> TEXTURE_MIPMAP = Attr.create(new TexBooleanBuilder(TextureBooleanType.M));
	public static final @Nonnull Attr<TextureBoolean, TextureBoolean, TextureBooleanBuilder> TEXTURE_LIGHTING = Attr.create(new TexBooleanBuilder(TextureBooleanType.L));

	private static class TexBlendBuilder implements IAttrReader<TextureBlend, TextureBlend, TextureBlendBuilder> {
		private @Nonnull TextureBlendType type;

		public TexBlendBuilder(final @Nonnull TextureBlendType type) {
			this.type = type;
		}

		@Override
		public @Nonnull TextureBlendBuilder builder() {
			return new TextureBlendBuilder(this.type);
		}
	}

	public static final @Nonnull Attr<TextureBlend, TextureBlend, TextureBlendBuilder> TEXTURE_BLEND_SRC = Attr.create(new TexBlendBuilder(TextureBlendType.B));
	public static final @Nonnull Attr<TextureBlend, TextureBlend, TextureBlendBuilder> TEXTURE_BLEND_DST = Attr.create(new TexBlendBuilder(TextureBlendType.D));

	public static interface IAttrReader0<Diffed, KeyFrame> {
		@Nonnull
		IPropBuilder<Diffed, KeyFrame> builder();
	}

	public static interface IAttrReader<Diffed, KeyFrame, Reader extends IPropBuilder<Diffed, KeyFrame>> extends IAttrReader0<Diffed, KeyFrame> {
		@Override
		@Nonnull
		Reader builder();
	}
}
