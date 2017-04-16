package com.kamesuta.mc.signpic.plugin.gui.search;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.plugin.SignData;

public abstract class StringFilterElement implements IFilterElement {

	public final @Nullable String src;

	public StringFilterElement(final @Nullable String src) {
		this.src = src;
	}

	public abstract boolean filter(SignData data, StringFilterProperty property, String str);

	protected @Nullable String get(final SignData data, final StringFilterProperty property) {
		switch (property) {
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

		public EqualsStringFilterElement(final String src) {
			super(src);
		}

		@Override
		public boolean filter(final SignData data, final StringFilterProperty property, final String str) {
			return StringUtils.equals(get(data, property), str);
		}

	}

	public static class EqualsIgnoreCaseStringFilterElement extends StringFilterElement {

		public EqualsIgnoreCaseStringFilterElement(final String src) {
			super(src);
		}

		@Override
		public boolean filter(final SignData data, final StringFilterProperty property, final String str) {
			return StringUtils.equalsIgnoreCase(get(data, property), str);
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
