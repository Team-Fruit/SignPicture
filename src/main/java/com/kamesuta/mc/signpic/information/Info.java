package com.kamesuta.mc.signpic.information;

import java.util.Map;

public class Info {
	public Map<String, Version> versions;
	public String website;
	public String changelog;
	public String private_msg;
	public static class Version {
		public String version;
		public String remote;
		public String local;
		public String message;
		public Map<String, String> message_local;
		public String website;
		public String changelog;
	}
	public static class PrivateMsg {
		public boolean json;
		public String message;
		public Map<String, String> message_local;
	}
}
