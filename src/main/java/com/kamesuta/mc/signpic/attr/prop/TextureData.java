package com.kamesuta.mc.signpic.attr.prop;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;
import com.kamesuta.mc.bnnwidget.render.WRenderer.BlendType;
import com.kamesuta.mc.signpic.attr.IPropBuilder;
import com.kamesuta.mc.signpic.attr.IPropInterpolatable;

public interface TextureData {
	public static class TextureFloat implements IPropInterpolatable<TextureFloat> {
		public static final float defaultUV = 0f;
		// Width Height
		public static final float defaultWH = 1f;
		// Crossing Slitting
		public static final float defaultCS = 1f;
		public static final float defaultOpacity = 10f;
		public static final float defaultLight = -1f;
		public static final float failedLight = -2f;

		public final @Nonnull TextureFloatType type;
		public final float data;

		public TextureFloat(final @Nonnull TextureFloatType type, final float data) {
			this.type = type;
			this.data = data;
		}

		@Override
		public @Nonnull TextureFloat per() {
			return this;
		}

		@Override
		public @Nonnull TextureFloat per(final float per, final @Nullable TextureFloat before) {
			if (before==null)
				return this;
			else
				return new TextureFloat(this.type, this.data*per+before.data*(1f-per));
		}

		public static enum TextureFloatType {
			U(PropSyntax.TEXTURE_X.id, defaultUV),
			V(PropSyntax.TEXTURE_Y.id, defaultUV),
			W(PropSyntax.TEXTURE_W.id, defaultWH),
			H(PropSyntax.TEXTURE_H.id, defaultWH),
			C(PropSyntax.TEXTURE_SPLIT_W.id, defaultCS),
			S(PropSyntax.TEXTURE_SPLIT_H.id, defaultCS),
			O(PropSyntax.TEXTURE_OPACITY.id, defaultOpacity),
			E(PropSyntax.TEXTURE_LIGHT_X.id, defaultLight, failedLight),
			F(PropSyntax.TEXTURE_LIGHT_Y.id, defaultLight, failedLight),
			;

			public final @Nonnull String identifier;
			public final float defaultValue;
			public final float failedValue;

			TextureFloatType(final @Nonnull String identifier, final float defaultValue, final float failedValue) {
				this.identifier = identifier;
				this.defaultValue = defaultValue;
				this.failedValue = failedValue;
			}

			TextureFloatType(final @Nonnull String identifier, final float defaultAndFailedValue) {
				this(identifier, defaultAndFailedValue, defaultAndFailedValue);
			}
		}

		public static class TextureFloatBuilder implements IPropBuilder<TextureFloat, TextureFloat> {
			private final @Nonnull TextureFloatType type;
			private float data;

			public TextureFloatBuilder(final @Nonnull TextureFloatType type) {
				Validate.notNull(type);
				this.type = type;
				this.data = type.defaultValue;
			}

			@Override
			public @Nonnull TextureFloat diff(final @Nullable TextureFloat base) {
				if (base==null)
					return new TextureFloat(this.type, this.data);
				else
					return new TextureFloat(this.type, base.data+this.data);
			}

			@Override
			public boolean parse(final @Nonnull String src, final @Nonnull String key, final @Nonnull String value) {
				if (StringUtils.equals(key, this.type.identifier)) {
					this.data = NumberUtils.toFloat(value, this.type.failedValue);
					return true;
				}
				return false;
			}

			@Override
			public @Nonnull String compose() {
				if (this.data==this.type.defaultValue)
					return "";
				else if (this.data==type.failedValue)
					return type.identifier;
				return this.type.identifier+ShortestFloatFormatter.format(this.data);
			}
		}
	}

	public static class TextureBoolean implements IPropInterpolatable<TextureBoolean> {
		public static final boolean defaultRepeat = true;
		public static final boolean defaultMipMap = true;
		public static final boolean defaultLighting = false;

		public final @Nonnull TextureBooleanType type;
		public final boolean data;

		public TextureBoolean(final @Nonnull TextureBooleanType type, final boolean data) {
			this.type = type;
			this.data = data;
		}

		@Override
		public @Nonnull TextureBoolean per() {
			return this;
		}

		@Override
		public @Nonnull TextureBoolean per(final float per, final @Nullable TextureBoolean before) {
			return this;
		}

		public static enum TextureBooleanType {
			R(PropSyntax.TEXTURE_REPEAT.id, defaultRepeat),
			M(PropSyntax.TEXTURE_MIPMAP.id, defaultMipMap),
			L(PropSyntax.TEXTURE_LIGHTING.id, defaultLighting),
			;

			public final @Nonnull String identifier;
			public final boolean defaultValue;

			TextureBooleanType(final @Nonnull String identifier, final boolean defaultValue) {
				this.identifier = identifier;
				this.defaultValue = defaultValue;
			}
		}

		public static class TextureBooleanBuilder implements IPropBuilder<TextureBoolean, TextureBoolean> {
			private final @Nonnull TextureBooleanType type;
			private boolean data;

			public TextureBooleanBuilder(final @Nonnull TextureBooleanType type) {
				this.type = type;
				this.data = type.defaultValue;
			}

			@Override
			public @Nonnull TextureBoolean diff(final @Nullable TextureBoolean base) {
				return new TextureBoolean(this.type, this.data);
			}

			@Override
			public boolean parse(final @Nonnull String src, final @Nonnull String key, final @Nonnull String value) {
				if (StringUtils.equals(key, this.type.identifier)) {
					this.data = !this.type.defaultValue;
					return true;
				}
				return false;
			}

			@Override
			public @Nonnull String compose() {
				if (this.data!=this.type.defaultValue)
					return this.type.identifier;
				return "";
			}
		}
	}

	public static class TextureBlend implements IPropInterpolatable<TextureBlend> {
		public static final @Nullable BlendType defaultBlend = null;

		public final @Nonnull TextureBlendType type;
		public final @Nullable BlendType data;

		public TextureBlend(final @Nonnull TextureBlendType type, final @Nullable BlendType data) {
			this.type = type;
			this.data = data;
		}

		@Override
		public @Nonnull TextureBlend per() {
			return this;
		}

		@Override
		public @Nonnull TextureBlend per(final float per, final @Nullable TextureBlend before) {
			return this;
		}

		public static enum TextureBlendType {
			B(PropSyntax.TEXTURE_BLEND_SRC.id, defaultBlend),
			D(PropSyntax.TEXTURE_BLEND_DST.id, defaultBlend),
			;

			public final @Nonnull String identifier;
			public final @Nullable BlendType defaultValue;

			TextureBlendType(final @Nonnull String identifier, final @Nullable BlendType defaultValue) {
				this.identifier = identifier;
				this.defaultValue = defaultValue;
			}
		}

		public static class TextureBlendBuilder implements IPropBuilder<TextureBlend, TextureBlend> {
			private final @Nonnull TextureBlendType type;
			private @Nullable BlendType data;

			public TextureBlendBuilder(final @Nonnull TextureBlendType type) {
				this.type = type;
				this.data = type.defaultValue;
			}

			@Override
			public @Nonnull TextureBlend diff(final @Nullable TextureBlend base) {
				return new TextureBlend(this.type, this.data);
			}

			@Override
			public boolean parse(final @Nonnull String src, final @Nonnull String key, final @Nonnull String value) {
				if (StringUtils.equals(key, this.type.identifier)) {
					if (NumberUtils.isNumber(value))
						data = BlendType.fromId(NumberUtils.toInt(value));
					return true;
				}
				return false;
			}

			@Override
			public @Nonnull String compose() {
				final BlendType data = this.data;
				if (data!=null&&data!=this.type.defaultValue)
					return this.type.identifier+data.id;
				return "";
			}
		}
	}
}
