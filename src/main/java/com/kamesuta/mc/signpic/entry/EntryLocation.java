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

	public URI remoteLocation(final EntryId id) throws URISyntaxException {
		return new URI(id.id());
	}

	public File localLocation(final EntryId id) {
		return new File(this.localroot, DigestUtils.md5Hex(id.id()));
	}
}
