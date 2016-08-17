package com.kamesuta.mc.signpic.image;

import java.io.File;
import java.net.IDN;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

public class ImageLocation {
	public File localroot;
	public ImageLocation(final File localroot) {
		this.localroot = localroot;
	}

	public URI remoteLocation(final Image image) throws URISyntaxException {
		return new URI(image.id);
	}

	public File localLocation(final Image image) throws URISyntaxException, MalformedURLException {
		final URI i = remoteLocation(image);

		final URL l = i.toURL();

		final String _scheme = l.getProtocol();
		final String _host = l.getHost();
		final int _port = l.getPort();
		final String _path = i.getPath();
		final String _query = i.getQuery();

		final String host = IDN.toUnicode(_host);
		final String port =
				(_port == -1 || (StringUtils.equals(_scheme, "http") && _port == 80)
				|| (StringUtils.equals(_scheme, "https") && _port == 443))
				? "" : ("#" + _port);
		final String path = (_path == null) ? "" : _path;
		final String query = (_query == null) ? "" : ("#"+_query);

		final String url = String.format("%s/%s%s/%s%s", _scheme, host, port, path, query);

		return new File(this.localroot, url);
	}

	public File localLocationPrepare(final Image image) throws MalformedURLException, URISyntaxException {
		final File file = localLocation(image);
		final File parent = file.getParentFile();
		if (!parent.exists())
			parent.mkdirs();
		return file;
	}
}
