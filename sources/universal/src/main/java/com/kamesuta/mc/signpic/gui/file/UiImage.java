package com.kamesuta.mc.signpic.gui.file;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JComponent;

public class UiImage extends JComponent {
	protected @Nullable BufferedImage image;

	public UiImage() {
	}

	public UiImage(final @Nullable BufferedImage image) {
		setImage(image);
	}

	public void setImage(final @Nullable BufferedImage image) {
		this.image = image;
	}

	public @Nullable BufferedImage getImage() {
		return this.image;
	}

	public int getImageWidth() {
		final BufferedImage image = getImage();
		if (image!=null)
			return image.getWidth();
		return 0;
	}

	public int getImageHeight() {
		final BufferedImage image = getImage();
		if (image!=null)
			return image.getHeight();
		return 0;
	}

	public @Nonnull Dimension getImageSize() {
		return new Dimension(getImageWidth(), getImageHeight());
	}

	@Override
	public @Nullable Dimension getPreferredSize() {
		final BufferedImage image = getImage();
		if (image!=null)
			return new Dimension(image.getWidth(), image.getHeight());
		return super.getPreferredSize();
	}

	@Override
	public void paintComponent(final @Nullable Graphics g) {
		if (g!=null) {
			final Graphics2D g2 = (Graphics2D) g;
			//g2.setTransform(AffineTransform.getScaleInstance(getWidth(), getHeight()));
			g2.drawImage(getImage(), 0, 0, getWidth(), getHeight(), this);
		}
	}
}
