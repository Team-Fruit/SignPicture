package com.kamesuta.mc.signpic.entry.content;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.codec.digest.DigestUtils;

import com.kamesuta.mc.signpic.Client;

public class ContentLocation {
	public static URI remoteLocation(final String url) throws URISyntaxException {
		return new URI(url);
	}

	public static String hash(final String url) {
		return DigestUtils.md5Hex(url);
	}

	public static File metaLocation(final String name) {
		return new File(Client.location.metaDir, name+".json");
	}

	public static File cacheLocation(final String name) {
		return new File(Client.location.cacheDir, name);
	}
}
