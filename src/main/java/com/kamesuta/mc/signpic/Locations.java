package com.kamesuta.mc.signpic;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

public class Locations {
	public @Nonnull File mcDir;
	public @Nonnull File signpicDir;
	public @Nonnull File tempDir;
	public @Nonnull File cacheDir;
	public @Nonnull File metaDir;
	public @Nonnull File modDir;
	public @Nonnull File modFile;

	public Locations(final @Nonnull File modFile, final @Nonnull File mcdir) {
		this.mcDir = mcdir;
		this.signpicDir = getSignPicDir(mcdir);
		securementDirectory(this.signpicDir);
		this.tempDir = new File(this.signpicDir, "temp");
		securementDirectory(this.tempDir);
		this.cacheDir = new File(this.signpicDir, "cache");
		securementDirectory(this.cacheDir);
		this.metaDir = new File(this.signpicDir, "meta");
		securementDirectory(this.metaDir);

		this.modDir = new File(mcdir, "mods");
		this.modFile = modFile;
	}

	public @Nonnull File createCache(final @Nonnull String pre) throws IOException {
		File f;
		do
			f = new File(this.tempDir, pre+RandomStringUtils.randomAlphanumeric(8));
		while (!f.createNewFile());
		return f;
	}

	private @Nonnull File getSignPicDir(final @Nonnull File defaultdir) {
		final File dir = new File(Config.getConfig().signpicDir.get());
		if (!StringUtils.isEmpty(Config.getConfig().signpicDir.get())) {
			if (dir.exists()&&dir.isDirectory()&&!dir.equals(defaultdir))
				return dir;
			Log.dev.error("invalid signpic dir location! use default dir.");
		}
		return new File(defaultdir, "signpic");
	}

	private boolean securementDirectory(final @Nonnull File cachedir) {
		if (cachedir.exists()&&!cachedir.isDirectory()) {
			File to;
			int i = 2;
			do {
				to = new File(cachedir.getParent(), cachedir.getName()+i);
				i++;
			} while (to.exists());
			cachedir.renameTo(to);
			Log.log.warn("non-directory conflicting file exists. renamed to "+to.getName());
			return true;
		}
		cachedir.mkdir();
		return false;
	}
}
