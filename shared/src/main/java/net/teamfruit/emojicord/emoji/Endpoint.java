package net.teamfruit.emojicord.emoji;

import net.teamfruit.emojicord.Reference;
import net.teamfruit.emojicord.emoji.Models.EmojiGateway;
import net.teamfruit.emojicord.emoji.Models.EmojiStandard;
import net.teamfruit.emojicord.emoji.Models.EmojiStandardGroup;
import net.teamfruit.emojicord.emoji.Models.EmojiStandardList;
import net.teamfruit.emojicord.emoji.Models.EmojiStandardPicker;
import net.teamfruit.emojicord.emoji.Models.EmojiStandardPickerCategory;
import net.teamfruit.emojicord.emoji.StandardEmojiIdDictionary.StandardEmojiIdDictionaryBuilder;
import net.teamfruit.emojicord.emoji.StandardEmojiIdPicker.StandardEmojiIdPickerBuilder;
import net.teamfruit.emojicord.util.DataUtils;

public class Endpoint {
	public static EmojiGateway EMOJI_API = new EmojiGateway();

	public static boolean loadGateway() {
		final EmojiGateway data = DataUtils.loadUrl(Reference.EMOJI_GATEWAY, EmojiGateway.class, "Emojicord API");
		if (data!=null) {
			EMOJI_API = data;
			return true;
		}
		return false;
	}

	public static void loadStandardEmojis() {
		final StandardEmojiIdDictionaryBuilder builder = new StandardEmojiIdDictionaryBuilder();
		for (final String emojiUrls : EMOJI_API.emojis) {
			final EmojiStandardList emojiList = DataUtils.loadUrl(emojiUrls, EmojiStandardList.class,
					"Standard Emojis");
			if (emojiList!=null)
				for (final EmojiStandardGroup emojiGroup : emojiList.groups)
					for (final EmojiStandard emoji : emojiGroup.emojis) {
						final EmojiId id = new EmojiId.StandardEmojiId(emojiGroup.location+emoji.location, emoji.name);
						builder.putName(emoji.name, id);
						builder.putUtf(emoji.surrogates, id);
						for (final String string : emoji.strings)
							builder.putAlias(string, id);
					}
		}
		StandardEmojiIdDictionary.instance = builder.build();
	}

	public static void loadStandardPicker() {
		final StandardEmojiIdPickerBuilder builder = new StandardEmojiIdPickerBuilder();
		for (final String emojiUrls : EMOJI_API.picker) {
			final EmojiStandardPicker emojiPicker = DataUtils.loadUrl(emojiUrls, EmojiStandardPicker.class,
					"Standard Emoji Picker");
			if (emojiPicker!=null)
				for (final EmojiStandardPickerCategory category : emojiPicker.category)
					builder.addGroup(category.name, category.emojis);
		}
		StandardEmojiIdPicker.instance = builder.build();
	}
}
