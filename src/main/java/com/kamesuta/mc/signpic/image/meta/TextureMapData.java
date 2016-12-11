package com.kamesuta.mc.signpic.image.meta;

public class TextureMapData implements IMotionFrame<TextureMapData> {
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

	public static class TextureMapDataBoolean implements IMotionFrame<TextureMapDataBoolean> {
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
}
