package com.kamesuta.mc.signpic.information;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class Info {
	public @Nullable Map<String, Version> versions;
	public @Nullable String website;
	public @Nullable String changelog;
	public @Nullable String private_msg;
	public @Nullable Api apis;

	public static class Version {
		public @Nullable String version;
		public @Nullable String remote;
		public @Nullable String local;
		public @Nullable String message;
		public @Nullable String image;
		public @Nullable Map<String, String> message_local;
		public @Nullable String website;
		public @Nullable String changelog;
	}

	public static class Api {
		public @Nullable Image image;
		public @Nullable Shortener shortener;

		public static class Image {
			public @Nullable Gyazo gyazo;
			public @Nullable Imgur imgur;

			public static class Gyazo {
				public @Nullable List<Config> config;

				public static class Config {
					public @Nullable String key;
				}
			}

			public static class Imgur {
				public @Nullable List<Config> config;

				public static class Config {
					public @Nullable String clientid;
				}
			}
		}

		public static class Shortener {
			public @Nullable Bitly bitly;
			public @Nullable Googl googl;

			public static class Bitly {
				public @Nullable List<Config> config;

				public static class Config {
					public @Nullable String key;
				}
			}

			public static class Googl {
				public @Nullable List<Config> config;

				public static class Config {
					public @Nullable String key;
				}
			}
		}
	}

	public static class PrivateMsg {
		public boolean json;
		public @Nullable String message;
		public @Nullable Map<String, String> message_local;
	}
}
