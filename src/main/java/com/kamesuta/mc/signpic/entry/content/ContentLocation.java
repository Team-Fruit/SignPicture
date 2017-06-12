package com.kamesuta.mc.signpic.entry.content;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.Nonnull;

import org.apache.commons.codec.digest.DigestUtils;

import com.kamesuta.mc.signpic.Client;

public class ContentLocation {
	public static URI remoteLocation(final @Nonnull String url) throws URISyntaxException {
		return new URI(url);
	}

	public static @Nonnull String hash(final @Nonnull String url) {
		final String hash = DigestUtils.md5Hex(url);
		if (hash==null)
			return "null";
		return hash;
	}

	public static @Nonnull File metaLocation(final @Nonnull String name) {
		return new File(Client.getLocation().metaDir, name+".json");
	}

	public static @Nonnull File cachemetaLocation(final @Nonnull String name) {
		return new File(Client.getLocation().cacheDir, name+".json");
	}

	public static @Nonnull File cacheLocation(final @Nonnull String name) {
		return new File(Client.getLocation().cacheDir, name);
	}
}
