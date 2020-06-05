package net.teamfruit.emojicord;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Charsets;

public enum OSUtils {
	LINUX,
	SOLARIS,
	WINDOWS {
		@Override
		protected String[] getOpenCommandLine(final URL url) {
			return new String[] { "rundll32", "url.dll,FileProtocolHandler", url.toString() };
		}
	},
	OSX {
		@Override
		protected String[] getOpenCommandLine(final URL url) {
			return new String[] { "open", url.toString() };
		}
	},
	UNKNOWN;

	private OSUtils() {
	}

	public void openURL(final URL url) {
		try {
			final Process process = AccessController.doPrivileged((PrivilegedExceptionAction<Process>) () -> {
				return Runtime.getRuntime().exec(getOpenCommandLine(url));
			});

			for (final String s : IOUtils.readLines(process.getErrorStream(), Charsets.UTF_8))
				Log.log.error(s);

			process.getInputStream().close();
			process.getErrorStream().close();
			process.getOutputStream().close();
		} catch (IOException|PrivilegedActionException privilegedactionexception) {
			Log.log.error("Couldn't open url '{}'", url, privilegedactionexception);
		}
	}

	public void openURI(final URI uri) {
		try {
			openURL(uri.toURL());
		} catch (final MalformedURLException malformedurlexception) {
			Log.log.error("Couldn't open uri '{}'", uri, malformedurlexception);
		}
	}

	public void openFile(final File fileIn) {
		try {
			openURL(fileIn.toURI().toURL());
		} catch (final MalformedURLException malformedurlexception) {
			Log.log.error("Couldn't open file '{}'", fileIn, malformedurlexception);
		}
	}

	protected String[] getOpenCommandLine(final URL url) {
		String s = url.toString();
		if ("file".equals(url.getProtocol()))
			s = s.replace("file:", "file://");

		return new String[] { "xdg-open", s };
	}

	public void openURI(final String uri) {
		try {
			openURL(new URI(uri).toURL());
		} catch (MalformedURLException|IllegalArgumentException|URISyntaxException urisyntaxexception) {
			Log.log.error("Couldn't open uri '{}'", uri, urisyntaxexception);
		}
	}

	public static OSUtils getOSType() {
		final String s = System.getProperty("os.name").toLowerCase(Locale.ROOT);
		if (s.contains("win"))
			return WINDOWS;
		else if (s.contains("mac"))
			return OSX;
		else if (s.contains("solaris"))
			return SOLARIS;
		else if (s.contains("sunos"))
			return SOLARIS;
		else if (s.contains("linux"))
			return LINUX;
		else
			return s.contains("unix") ? LINUX : UNKNOWN;
	}
}
