package com.kamesuta.mc.signpic.image.meta;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;
import com.kamesuta.mc.signpic.image.meta.TextureMapData.DataType;
import com.kamesuta.mc.signpic.image.meta.TextureMapData.DataTypeBoolean;
import com.kamesuta.mc.signpic.image.meta.TextureMapData.TextureMapDataBoolean;

public class ImageTextureMap implements MetaMovie<TextureMapData, TextureMapData> {
	private final DataType type;
	private float data;

	public ImageTextureMap(final DataType type) {
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

	public static class ImageTextureMapBoolean implements MetaMovie<TextureMapDataBoolean, TextureMapDataBoolean> {
		private final DataTypeBoolean type;
		private boolean data;

		public ImageTextureMapBoolean(final DataTypeBoolean type) {
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
