package com.kamesuta.mc.signpic.gui.file;

import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import com.kamesuta.mc.signpic.Log;

public class ExpUiUpload extends UiUpload {
	/**
	 * Launch the application.
	 */
	public static void main(final @Nonnull String[] args) {
		final UiUpload window = new ExpUiUpload();
		window.requestOpen();
	}

	@Override
	protected @Nullable BufferedImage getImage(final @Nonnull String path) {
		try {
			return ImageIO.read(UiUpload.class.getResource("/assets/signpic/"+path));
		} catch (final IOException e) {
		}
		return null;
	}

	@Override
	protected @Nonnull String getString(final @Nonnull String id) {
		return "exp:"+id;
	}

	@Override
	protected void close() {
		super.close();
		System.exit(0);
	}

	@Override
	protected void apply(final @Nonnull File f) {
		Log.log.info(f.getAbsolutePath());
	}

	@Override
	protected void transfer(final @Nonnull Transferable transferable) {
		Log.log.info(transferable);
	}
};