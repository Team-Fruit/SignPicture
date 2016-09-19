package com.kamesuta.mc.signpic.entry;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.codec.digest.DigestUtils;

public class EntryLocation {
	public File localroot;
	public EntryLocation(final File localroot) {
		this.localroot = localroot;
	}

	public URI remoteLocation(final EntryPath path) throws URISyntaxException {
		return new URI(path.path());
	}

	public File localLocation(final EntryPath path) {
		return new File(this.localroot, DigestUtils.md5Hex(path.path()));
	}
}
