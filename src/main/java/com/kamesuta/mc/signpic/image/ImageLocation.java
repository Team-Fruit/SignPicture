package com.kamesuta.mc.signpic.image;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.codec.digest.DigestUtils;

public class ImageLocation {
	public File localroot;
	public ImageLocation(final File localroot) {
		this.localroot = localroot;
	}

	public URI remoteLocation(final Image image) throws URISyntaxException {
		return new URI(image.id);
	}

	public File localLocation(final Image image) {
		return new File(this.localroot, DigestUtils.md5Hex(image.id));
	}
}
