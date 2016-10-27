package com.kamesuta.mc.signpic.gui.file;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class UiImage extends JComponent {
	protected BufferedImage image;

	public UiImage() {
	}

	public UiImage(final BufferedImage image) {
		setImage(image);
	}

	public void setImage(final BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return this.image;
	}

	public int getImageWidth() {
		if (getImage()!=null)
			return getImage().getWidth();
		return 0;
	}

	public int getImageHeight() {
		if (getImage()!=null)
			return getImage().getHeight();
		return 0;
	}

	public Dimension getImageSize() {
		return new Dimension(getImageWidth(), getImageHeight());
	}

	@Override
	public Dimension getPreferredSize() {
		if (getImage()!=null)
			return new Dimension(getImage().getWidth(), getImage().getHeight());
		return super.getPreferredSize();
	}

	@Override
	public void paintComponent(final Graphics g) {
		final Graphics2D g2 = (Graphics2D) g;
		//g2.setTransform(AffineTransform.getScaleInstance(getWidth(), getHeight()));
		g2.drawImage(getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}
