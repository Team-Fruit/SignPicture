package net.teamfruit.emojicord.emoji;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class StandardEmojiIdPicker {
	public static StandardEmojiIdPicker instance = new StandardEmojiIdPickerBuilder().build();

	public final List<PickerGroup> categories;

	public StandardEmojiIdPicker(final List<PickerGroup> groups) {
		this.categories = groups;
	}

	public static class StandardEmojiIdPickerBuilder {
		public List<PickerGroup> groups = Lists.newArrayList();

		public StandardEmojiIdPickerBuilder addGroup(final String name, final List<String> items) {
			this.groups.add(new PickerGroup(name, items.stream().map(PickerItem::fromCategory).collect(Collectors.toList())));
			return this;
		}

		public StandardEmojiIdPicker build() {
			return new StandardEmojiIdPicker(this.groups);
		}
	}
}
