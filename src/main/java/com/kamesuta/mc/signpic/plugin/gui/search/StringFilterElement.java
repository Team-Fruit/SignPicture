package com.kamesuta.mc.signpic.plugin.gui.search;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.plugin.SignData;
import com.kamesuta.mc.signpic.plugin.gui.search.StringFilterElement.StringFilterProperty;

public abstract class StringFilterElement extends EnumFilterElement<StringFilterProperty> {

	public final @Nullable String src;

	public StringFilterElement(final StringFilterProperty property, final @Nullable String src) {
		super(property);
		this.src = src;
	}

	public abstract boolean filter(SignData data, String str);

	protected @Nullable String get(final SignData data) {
		switch (this.property) {
			case ID:
				return String.valueOf(data.getId());
			case SIGN:
				return data.getSign();
			case PLAYERNAME:
				return data.getPlayerName();
			case PLAYERUUID:
				return data.getPlayerUUID();
			case WORLDNAME:
				return data.getWorldName();
			default:
				return null;
		}
	}

	public static class EqualsStringFilterElement extends StringFilterElement {

		public EqualsStringFilterElement(final StringFilterProperty property, final String src) {
			super(property, src);
		}

		@Override
		public boolean filter(final SignData data, final String str) {
			return StringUtils.equals(get(data), str);
		}

	}

	public static class EqualsIgnoreCaseStringFilterElement extends StringFilterElement {

		public EqualsIgnoreCaseStringFilterElement(final StringFilterProperty property, final String src) {
			super(property, src);
		}

		@Override
		public boolean filter(final SignData data, final String str) {
			return StringUtils.equalsIgnoreCase(get(data), str);
		}

	}

	public static enum StringFilterProperty {
		ID,
		SIGN,
		PLAYERNAME,
		PLAYERUUID,
		WORLDNAME;
	}

}
