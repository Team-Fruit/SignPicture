package com.kamesuta.mc.signpic.attr;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableSet;
import com.kamesuta.mc.signpic.attr.PropReader.IPropReader;
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
	protected static final @Nonnull Pattern pg = Pattern.compile("\\((?:([^\\)]*?)~)?(.*?)\\)");
	protected static final @Nonnull Pattern pp = Pattern.compile("(?:([^\\d-\\+Ee\\.]?)([\\d-\\+Ee\\.]*)?)+?");
	public static final float defaultInterval = 0f;

	public static class Attr<ReaderDiffed, ReaderKeyFrame, ReaderReader extends IPropBuilder<ReaderDiffed, ReaderKeyFrame>> {
		private final @Nonnull ImmutableSet<String> ids;
		private final @Nonnull IAttrReader<ReaderDiffed, ReaderKeyFrame, ReaderReader> reader;

		private Attr(final @Nonnull IAttrReader<ReaderDiffed, ReaderKeyFrame, ReaderReader> reader, final @Nonnull ImmutableSet<String> ids) {
			this.reader = reader;
			this.ids = ids;
		}

		public @Nonnull ImmutableSet<String> getId() {
			return this.ids;
		}

		public @Nonnull IAttrReader<ReaderDiffed, ReaderKeyFrame, ReaderReader> getReader() {
			return this.reader;
		}

		public @Nonnull ReaderReader getWriter() {
			return getReader().builder();
		}

		private static @Nonnull <ReaderDiffed, ReaderKeyFrame, ReaderReader extends IPropBuilder<ReaderDiffed, ReaderKeyFrame>> Attr<ReaderDiffed, ReaderKeyFrame, ReaderReader> create(final @Nonnull ImmutableSet<String> ids, final @Nonnull IAttrReader<ReaderDiffed, ReaderKeyFrame, ReaderReader> reader) {
			return new Attr<ReaderDiffed, ReaderKeyFrame, ReaderReader>(reader, ids);
		}
	}

	public static final @Nonnull Attr<AnimationData, AnimationData, AnimationBuilder> ANIMATION = Attr.create(ImmutableSet.of(PropSyntax.ANIMATION_EASING.id, PropSyntax.ANIMATION_REDSTONE.id), new IAttrReader<AnimationData, AnimationData, AnimationBuilder>() {
		@Override
		public @Nonnull AnimationBuilder builder() {
			return new AnimationBuilder();
		}
	});
	public static final @Nonnull Attr<SizeData, SizeData, SizeBuilder> SIZE = Attr.create(ImmutableSet.of(PropSyntax.SIZE_W.id, PropSyntax.SIZE_H.id), new IAttrReader<SizeData, SizeData, SizeBuilder>() {
		@Override
		public @Nonnull SizeBuilder builder() {
			return new SizeBuilder();
		}
	});

	public static final @Nonnull Attr<OffsetData, OffsetData, OffsetBuilder> OFFSET = Attr.create(
			ImmutableSet.of(PropSyntax.OFFSET_LEFT.id, PropSyntax.OFFSET_RIGHT.id,
					PropSyntax.OFFSET_DOWN.id, PropSyntax.OFFSET_UP.id,
					PropSyntax.OFFSET_BACK.id, PropSyntax.OFFSET_FRONT.id),
			new IAttrReader<OffsetData, OffsetData, OffsetBuilder>() {
				@Override
				public @Nonnull OffsetBuilder builder() {
					return new OffsetBuilder(
							new OffsetDoublePropBuilder(PropSyntax.OFFSET_LEFT.id, PropSyntax.OFFSET_RIGHT.id),
							new OffsetDoublePropBuilder(PropSyntax.OFFSET_DOWN.id, PropSyntax.OFFSET_UP.id),
							new OffsetDoublePropBuilder(PropSyntax.OFFSET_BACK.id, PropSyntax.OFFSET_FRONT.id));
				}
			});
	public static final @Nonnull Attr<OffsetData, OffsetData, OffsetBuilder> OFFSET_CENTER = Attr.create(ImmutableSet.of(
			PropSyntax.OFFSET_CENTER_X.id, PropSyntax.OFFSET_CENTER_Y.id, PropSyntax.OFFSET_CENTER_Z.id), new IAttrReader<OffsetData, OffsetData, OffsetBuilder>() {
				@Override
				public @Nonnull OffsetBuilder builder() {
					return new OffsetBuilder(
							new OffsetPropBuilder(PropSyntax.OFFSET_CENTER_X.id),
							new OffsetPropBuilder(PropSyntax.OFFSET_CENTER_Y.id),
							new OffsetPropBuilder(PropSyntax.OFFSET_CENTER_Z.id));
				}
			});
	public static final @Nonnull Attr<DiffRotation, KeyRotation, RotationBuilder> ROTATION = Attr.create(ImmutableSet.of(
			PropSyntax.ROTATION_X.id, PropSyntax.ROTATION_Y.id, PropSyntax.ROTATION_Z.id,
			PropSyntax.ROTATION_ANGLE.id, PropSyntax.ROTATION_AXIS_X.id, PropSyntax.ROTATION_AXIS_Y.id, PropSyntax.ROTATION_AXIS_Z.id), new IAttrReader<DiffRotation, KeyRotation, RotationBuilder>() {
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

	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_X = Attr.create(ImmutableSet.of(PropSyntax.TEXTURE_X.id), new TexFloatBuilder(TextureFloatType.U));
	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_Y = Attr.create(ImmutableSet.of(PropSyntax.TEXTURE_Y.id), new TexFloatBuilder(TextureFloatType.V));
	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_W = Attr.create(ImmutableSet.of(PropSyntax.TEXTURE_W.id), new TexFloatBuilder(TextureFloatType.W));
	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_H = Attr.create(ImmutableSet.of(PropSyntax.TEXTURE_H.id), new TexFloatBuilder(TextureFloatType.H));
	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_SPLIT_W = Attr.create(ImmutableSet.of(PropSyntax.TEXTURE_SPLIT_W.id), new TexFloatBuilder(TextureFloatType.C));
	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_SPLIT_H = Attr.create(ImmutableSet.of(PropSyntax.TEXTURE_SPLIT_H.id), new TexFloatBuilder(TextureFloatType.S));
	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_OPACITY = Attr.create(ImmutableSet.of(PropSyntax.TEXTURE_OPACITY.id), new TexFloatBuilder(TextureFloatType.O));
	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_LIGHT_X = Attr.create(ImmutableSet.of(PropSyntax.TEXTURE_LIGHT_X.id), new TexFloatBuilder(TextureFloatType.F));
	public static final @Nonnull Attr<TextureFloat, TextureFloat, TextureFloatBuilder> TEXTURE_LIGHT_Y = Attr.create(ImmutableSet.of(PropSyntax.TEXTURE_LIGHT_Y.id), new TexFloatBuilder(TextureFloatType.G));

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

	public static final @Nonnull Attr<TextureBoolean, TextureBoolean, TextureBooleanBuilder> TEXTURE_REPEAT = Attr.create(ImmutableSet.of(PropSyntax.TEXTURE_REPEAT.id), new TexBooleanBuilder(TextureBooleanType.R));
	public static final @Nonnull Attr<TextureBoolean, TextureBoolean, TextureBooleanBuilder> TEXTURE_MIPMAP = Attr.create(ImmutableSet.of(PropSyntax.TEXTURE_MIPMAP.id), new TexBooleanBuilder(TextureBooleanType.M));
	public static final @Nonnull Attr<TextureBoolean, TextureBoolean, TextureBooleanBuilder> TEXTURE_LIGHTING = Attr.create(ImmutableSet.of(PropSyntax.TEXTURE_LIGHTING.id), new TexBooleanBuilder(TextureBooleanType.L));

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

	public static final @Nonnull Attr<TextureBlend, TextureBlend, TextureBlendBuilder> TEXTURE_BLEND_SRC = Attr.create(ImmutableSet.of(PropSyntax.TEXTURE_BLEND_SRC.id), new TexBlendBuilder(TextureBlendType.B));
	public static final @Nonnull Attr<TextureBlend, TextureBlend, TextureBlendBuilder> TEXTURE_BLEND_DST = Attr.create(ImmutableSet.of(PropSyntax.TEXTURE_BLEND_DST.id), new TexBlendBuilder(TextureBlendType.D));

	public static interface IAttrReader<Diffed, KeyFrame, Reader extends IPropBuilder<Diffed, KeyFrame>> extends IPropReader<Diffed, KeyFrame> {
		@Override
		@Nonnull
		Reader builder();
	}
}
