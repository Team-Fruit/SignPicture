package net.teamfruit.emojicord.emoji;

import java.util.List;

import com.google.common.collect.Lists;

public class Models {
	public static class EmojiGateway {
		public EmojiGatewayApi api = new EmojiGatewayApi();
		public List<String> emojis = Lists.newArrayList();
		public List<String> picker = Lists.newArrayList();

		public static class EmojiGatewayApi {
			public String importings;
			public String analytics;
		}
	}

	public static class EmojiStandard {
		public String location;
		public String name;
		public List<String> strings;
		public String surrogates;
	}

	public static class EmojiStandardGroup {
		public List<EmojiStandard> emojis;
		public String location;
	}

	public static class EmojiStandardList {
		public List<EmojiStandardGroup> groups;
	}

	public static class EmojiDiscord {
		public String name;
		public String id;
	}

	public static class EmojiDiscordGroup {
		public List<EmojiDiscord> emojis;
		public String name;
		public String id;
	}

	public static class EmojiDiscordList {
		public List<EmojiDiscordGroup> groups;
		public String name;
		public String id;
	}

	public static class EmojiDiscordIndexGroup {
		public String name;
		public String id;
	}

	public static class EmojiDiscordIndexList {
		public List<EmojiDiscordIndexGroup> groups;
		public String name;
		public String id;
	}

	public static class EmojiDiscordIndexFolder {
		public List<EmojiDiscordIndexList> lists;
	}

	public static class EmojiStandardPickerCategory {
		public String name;
		public List<String> emojis;
	}

	public static class EmojiStandardPicker {
		public List<EmojiStandardPickerCategory> category;
	}
}
