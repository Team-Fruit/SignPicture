package com.kamesuta.mc.signpic.gui.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.kamesuta.mc.signpic.Reference;

public class ExpUiUpload extends UiUpload {
	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		final UiUpload window = new ExpUiUpload();
		window.requestOpen();
	}

	@Override
	protected BufferedImage getImage(final String path) {
		try {
			return ImageIO.read(UiUpload.class.getResource("/assets/signpic/"+path));
		} catch (final IOException e) {
		}
		return null;
	}

	@Override
	protected String getString(final String id) {
		;
		return "exp:"+id;
	}

	@Override
	protected void close() {
		super.close();
		System.exit(0);
	}

	@Override
	protected void apply(final File f) {
		Reference.logger.info(f.getAbsolutePath());
	}
};