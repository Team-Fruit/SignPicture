package com.kamesuta.mc.signpic.information;

import java.util.List;
import java.util.Map;

public class Info {
	public Map<String, Version> versions;
	public String website;
	public String changelog;
	public String private_msg;
	public Api apis;

	public static class Version {
		public String version;
		public String remote;
		public String local;
		public String message;
		public Map<String, String> message_local;
		public String website;
		public String changelog;
	}

	public static class Api {
		public Image image;

		public static class Image {
			public Gyazo gyazo;
			public Imgur imgur;

			public static class Gyazo {
				public List<Config> config;

				public static class Config {
					public String clientid;
				}
			}

			public static class Imgur {
				public List<Config> config;

				public static class Config {
					public String clientid;
				}
			}
		}
	}

	public static class PrivateMsg {
		public boolean json;
		public String message;
		public Map<String, String> message_local;
	}
}
