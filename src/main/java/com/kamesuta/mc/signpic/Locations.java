package com.kamesuta.mc.signpic;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Locations {
	public File mcDir;
	public File signpicDir;
	public File tempDir;
	public File cacheDir;
	public File metaDir;
	public File modDir;
	public File modFile;

	public Locations(final FMLPreInitializationEvent event, final File mcdir) {
		this.signpicDir = getSignPicDir(mcdir);
		securementDirectory(this.signpicDir);
		this.tempDir = new File(this.signpicDir, "temp");
		securementDirectory(this.tempDir);
		this.cacheDir = new File(this.signpicDir, "cache");
		securementDirectory(this.cacheDir);
		this.metaDir = new File(this.signpicDir, "meta");
		securementDirectory(this.metaDir);

		this.modDir = new File(mcdir, "mods");
		this.modFile = event.getSourceFile();
	}

	private File getSignPicDir(final File defaultdir) {
		final File dir = new File(Config.instance.signpicDir);
		if (!StringUtils.isEmpty(Config.instance.signpicDir)) {
			if (dir.exists()&&dir.isDirectory()&&!dir.equals(defaultdir))
				return dir;
			Reference.logger.debug("invalid signpic dir location! use default dir.");
		}
		return new File(defaultdir, "signpic");
	}

	private boolean securementDirectory(final File cachedir) {
		if (cachedir.exists()&&!cachedir.isDirectory()) {
			File to;
			int i = 2;
			do {
				to = new File(cachedir.getParent(), cachedir.getName()+i);
				i++;
			} while (to.exists());
			cachedir.renameTo(to);
			Reference.logger.warn("non-directory conflicting file exists. renamed to "+to.getName());
			return true;
		}
		cachedir.mkdir();
		return false;
	}
}
