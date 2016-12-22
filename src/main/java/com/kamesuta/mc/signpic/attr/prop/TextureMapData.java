package com.kamesuta.mc.signpic.attr.prop;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;
import com.kamesuta.mc.signpic.attr.IPropInterpolatable;
import com.kamesuta.mc.signpic.attr.IPropBuilder;

public class TextureMapData implements IPropInterpolatable<TextureMapData> {
	public static final float defaultUV = 0f;
	// Width Height
	public static final float defaultWH = 1f;
	// Crossing Slitting
	public static final float defaultCS = 1f;
	public static final float defaultOpacity = 10f;
	public static final boolean defaultRepeat = true;
	public static final boolean defaultMipMap = true;

	public final DataType type;
	public final float data;

	public TextureMapData(final DataType type, final float data) {
		this.type = type;
		this.data = data;
	}

	@Override
	public TextureMapData per() {
		return this;
	}

	@Override
	public TextureMapData per(final float per, final TextureMapData before) {
		return new TextureMapData(this.type, this.data*per+before.data*(1f-per));
	}

	public static class TextureMapDataBoolean implements IPropInterpolatable<TextureMapDataBoolean> {
		public final DataTypeBoolean type;
		public final boolean data;

		public TextureMapDataBoolean(final DataTypeBoolean type, final boolean data) {
			this.type = type;
			this.data = data;
		}

		@Override
		public TextureMapDataBoolean per() {
			return this;
		}

		@Override
		public TextureMapDataBoolean per(final float per, final TextureMapDataBoolean before) {
			return this;
		}
	}

	public static enum DataType {
		U("u", defaultUV), V("v", defaultUV), W("w", defaultWH), H("h", defaultWH), C("c", defaultCS), S("s", defaultCS), O("o", defaultOpacity),
		;

		public final String identifier;
		public final float defaultValue;

		DataType(final String identifier, final float defaultValue) {
			this.identifier = identifier;
			this.defaultValue = defaultValue;
		}
	}

	public static enum DataTypeBoolean {
		R("r", defaultRepeat), M("m", defaultMipMap),
		;

		public final String identifier;
		public final boolean defaultValue;

		DataTypeBoolean(final String identifier, final boolean defaultValue) {
			this.identifier = identifier;
			this.defaultValue = defaultValue;
		}
	}

	public static class TextureMapBuilder implements IPropBuilder<TextureMapData, TextureMapData> {
		private final DataType type;
		private float data;

		public TextureMapBuilder(final DataType type) {
			Validate.notNull(type);
			this.type = type;
			this.data = type.defaultValue;
		}

		@Override
		public TextureMapData diff(final TextureMapData base) {
			if (base==null)
				return new TextureMapData(this.type, this.data);
			else
				return new TextureMapData(this.type, base.data+this.data);
		}

		@Override
		public boolean parse(final String src, final String key, final String value) {
			if (StringUtils.equals(key, this.type.identifier)) {
				this.data = NumberUtils.toFloat(value, this.type.defaultValue);
				return true;
			}
			return false;
		}

		@Override
		public String compose() {
			if (this.data!=this.type.defaultValue)
				return this.type.identifier+ShortestFloatFormatter.format(this.data);
			return "";
		}
	}

	public static class TextureMapBooleanBuilder implements IPropBuilder<TextureMapDataBoolean, TextureMapDataBoolean> {
		private final DataTypeBoolean type;
		private boolean data;

		public TextureMapBooleanBuilder(final DataTypeBoolean type) {
			this.type = type;
			this.data = type.defaultValue;
		}

		@Override
		public TextureMapDataBoolean diff(final TextureMapDataBoolean base) {
			return new TextureMapDataBoolean(this.type, this.data);
		}

		@Override
		public boolean parse(final String src, final String key, final String value) {
			if (StringUtils.equals(key, this.type.identifier)) {
				this.data = !this.type.defaultValue;
				return true;
			}
			return false;
		}

		@Override
		public String compose() {
			if (this.data!=this.type.defaultValue)
				return this.type.identifier;
			return "";
		}
	}
}
