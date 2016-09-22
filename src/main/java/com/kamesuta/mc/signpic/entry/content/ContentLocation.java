package com.kamesuta.mc.signpic.entry.content;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.codec.digest.DigestUtils;

public class ContentLocation {
	public File localroot;
	public ContentLocation(final File localroot) {
		this.localroot = localroot;
	}

	public URI remoteLocation(final ContentId path) throws URISyntaxException {
		return new URI(path.getURI());
	}

	public File localLocation(final ContentId path) {
		return new File(this.localroot, DigestUtils.md5Hex(path.getURI()));
	}
}
