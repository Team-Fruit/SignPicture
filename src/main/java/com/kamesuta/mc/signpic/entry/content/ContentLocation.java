package com.kamesuta.mc.signpic.entry.content;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.codec.digest.DigestUtils;

import com.kamesuta.mc.signpic.Client;

public class ContentLocation {
	public final ContentId id;

	public ContentLocation(final ContentId id) {
		this.id = id;
	}

	public URI remoteLocation() throws URISyntaxException {
		return new URI(this.id.getURI());
	}

	public File metaLocation() {
		return new File(Client.location.metaDir, DigestUtils.md5Hex(this.id.getURI()));
	}

	public File tempLocation() {
		return new File(Client.location.tempDir, DigestUtils.md5Hex(this.id.getURI()));
	}

	public File cacheLocation() {
		return new File(Client.location.cacheDir, DigestUtils.md5Hex(this.id.getURI()));
	}
}
