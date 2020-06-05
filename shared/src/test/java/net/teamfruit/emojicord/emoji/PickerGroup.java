package net.teamfruit.emojicord.emoji;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.teamfruit.emojicord.compat.Compat.CompatI18n;

public class PickerGroup {
	public final String name;
	public final List<PickerItem> items;
	private String langKey;

	public PickerGroup(final String name, final List<PickerItem> items) {
		this.name = name;
		this.items = items;
	}

	public String getTranslation() {
		if (!StringUtils.isEmpty(this.langKey))
			if (CompatI18n.hasKey(this.langKey))
				return CompatI18n.format(this.langKey);
		return this.name;
	}

	public PickerGroup setTranslation(final String langKey) {
		this.langKey = langKey;
		return this;
	}
}