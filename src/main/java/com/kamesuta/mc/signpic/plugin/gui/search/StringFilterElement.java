package com.kamesuta.mc.signpic.plugin.gui.search;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.plugin.SignData;
import com.kamesuta.mc.signpic.plugin.gui.search.StringFilterElement.StringFilterProperty;

public abstract class StringFilterElement extends EnumFilterElement<String, StringFilterProperty> {

	public final @Nullable String str;

	public StringFilterElement(final StringFilterProperty property, final @Nullable String src) {
		super(property);
		this.str = src;
	}

	public static class EqualsStringFilterElement extends StringFilterElement {

		public EqualsStringFilterElement(final StringFilterProperty property, final String src) {
			super(property, src);
		}

		@Override
		public boolean filter(final SignData data) {
			return StringUtils.equals(this.property.get(data), this.str);
		}

	}

	public static class EqualsIgnoreCaseStringFilterElement extends StringFilterElement {

		public EqualsIgnoreCaseStringFilterElement(final StringFilterProperty property, final String src) {
			super(property, src);
		}

		@Override
		public boolean filter(final SignData data) {
			return StringUtils.equalsIgnoreCase(this.property.get(data), this.str);
		}

	}

	public static enum StringFilterProperty implements IFilterProperty<String> {
		ID {
			@Override
			public String get(SignData data) {
				return String.valueOf(data.getId());
			}
		},
		SIGN {
			@Override
			public String get(SignData data) {
				return data.getSign();
			}
		},
		PLAYERNAME {
			@Override
			public String get(SignData data) {
				return data.getPlayerName();
			}
		},
		PLAYERUUID {
			@Override
			public String get(SignData data) {
				return data.getPlayerUUID();
			}
		},
		WORLDNAME {
			@Override
			public String get(SignData data) {
				return data.getWorldName();
			}
		};
	}

}
