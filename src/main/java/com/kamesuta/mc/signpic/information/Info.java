package com.kamesuta.mc.signpic.information;

import java.util.Map;

public class Info {
	public Map<String, Version> versions;
	public static class Version {
		public String version;
		public String remote;
		public String local;
	}
}
