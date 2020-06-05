package net.teamfruit.emojicord.emoji;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class PickerItem {
	public final String text;
	public final String name;
	public final List<String> alias;
	public final EmojiId id;

	public PickerItem(final String text, final String name, final List<String> alias, final EmojiId id) {
		this.text = text;
		this.name = name;
		this.alias = alias;
		this.id = id;
	}

	public static @Nullable PickerItem fromCategory(final String text) {
		if (text==null)
			return null;
		final String[] split = StringUtils.split(text);
		if (split.length<=0)
			return null;
		final String name = split[0];
		final List<String> alias = Stream.of(split).map(e -> StringUtils.strip(e, ":")).collect(Collectors.toList());
		final EmojiId id = StandardEmojiIdDictionary.instance.aliasDictionary.get(StringUtils.strip(name, ":"));
		if (id==null)
			return null;
		return new PickerItem(text, name, alias, id);
	}

	public static @Nullable PickerItem fromUsed(final String text, final EmojiId id) {
		if (text==null)
			return null;
		final List<String> alias = Lists.newArrayList(StringUtils.strip(text, ":"));
		if (id==null)
			return null;
		return new PickerItem(text, text, alias, id);
	}

	public static List<PickerItem> fromText(final EmojiText emojiText) {
		return emojiText.emojis.stream().filter(e -> e.id!=null&&e.raw!=null)
				.map(e -> new PickerItem(e.source, e.encoded, Lists.newArrayList(StringUtils.strip(e.source, ":")), e.id))
				.filter(e -> e!=null).collect(Collectors.toList());
	}
}