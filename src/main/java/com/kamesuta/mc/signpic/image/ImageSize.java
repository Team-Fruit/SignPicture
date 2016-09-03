package com.kamesuta.mc.signpic.image;

import java.text.DecimalFormat;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.guiwidget.position.Area;

public class ImageSize {
	public static final float defaultSize = 1f;
	public static final float unknownSize = Float.NaN;
	public static final ImageSize DefaultSize = new ImageSize(defaultSize, defaultSize);
	public static final ImageSize UnknownSize = new ImageSize(unknownSize, unknownSize);
	public static final DecimalFormat signformat = new DecimalFormat("0.#");

	public final float width;
	public final float height;

	public ImageSize(final float width, final float height) {
		this.width = width;
		this.height = height;
	}

	public ImageSize(final Area a) {
		this(a.w(), a.h());
	}

	public boolean vaildWidth() {
		return !Float.isNaN(this.width);
	}

	public boolean vaildHeight() {
		return !Float.isNaN(this.height);
	}

	public float max() {
		return Math.max(this.width, this.height);
	}

	public float min() {
		return Math.min(this.width, this.height);
	}

	public ImageSize getAspectSize(final ImageSize availableaspect) {
		if (availableaspect == null)
			return this;
		else if (vaildWidth() && vaildHeight())
			return this;
		else if (vaildWidth())
			return createSize(ImageSizes.WIDTH, availableaspect, this.width, unknownSize);
		else if (vaildHeight())
			return createSize(ImageSizes.HEIGHT, availableaspect, unknownSize, this.height);
		else
			return createSize(ImageSizes.HEIGHT, availableaspect, unknownSize, 1);
	}

	public ImageSize getLimitSize(final ImageSize imagesize) {
		if (imagesize == null)
			return this;
		else
			return imagesize.getAspectSize(this);
	}

	public static ImageSize createSize(final ImageSizes s, final float rawWidth, final float rawHeight, final float maxWidth, final float maxHeight) {
		return s.size(rawWidth, rawHeight, maxWidth, maxHeight);
	}

	public static ImageSize createSize(final ImageSizes s, final ImageSize raw, final float maxWidth, final float maxHeight) {
		if (raw == null) return new ImageSize(maxWidth, maxHeight);
		return createSize(s, raw.width, raw.height, maxWidth, maxHeight);
	}

	public static ImageSize createSize(final ImageSizes s, final float rawWidth, final float rawHeight, final ImageSize max) {
		if (max == null) return new ImageSize(rawWidth, rawHeight);
		return createSize(s, rawWidth, rawHeight, max.width, max.height);
	}

	public static ImageSize createSize(final ImageSizes s, final ImageSize raw, final ImageSize max) {
		if (raw == null && max == null) throw new IllegalArgumentException("No Size Defined");
		else if (raw == null) return max;
		else if (max == null) return raw;
		return createSize(s, raw.width, raw.height, max.width, max.height);
	}

	public static ImageSize parseSize(final Map<String, String> meta) {
		final float wid = (meta.containsKey("")) ? NumberUtils.toFloat(meta.get(""), unknownSize) : unknownSize;
		final float hei = (meta.containsKey("x")) ? NumberUtils.toFloat(meta.get("x"), unknownSize) : unknownSize;
		return new ImageSize(wid, hei);
	}

	public static ImageSize parseSize(final String w, final String h) {
		final float wid = NumberUtils.toFloat(w, unknownSize);
		final float hei = NumberUtils.toFloat(h, unknownSize);
		return new ImageSize(wid, hei);
	}

	public ImageSize imageWidth(final float width) {
		return new ImageSize(width, this.height);
	}

	public ImageSize imageHeight(final float height) {
		return new ImageSize(this.width, height);
	}

	public ImageSize imageWidth(final String width) {
		return new ImageSize(NumberUtils.toFloat(width, unknownSize), this.height);
	}

	public ImageSize imageHeight(final String height) {
		return new ImageSize(this.width, NumberUtils.toFloat(height, unknownSize));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(this.height);
		result = prime * result + Float.floatToIntBits(this.width);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ImageSize))
			return false;
		final ImageSize other = (ImageSize) obj;
		if (Float.floatToIntBits(this.height) != Float.floatToIntBits(other.height))
			return false;
		if (Float.floatToIntBits(this.width) != Float.floatToIntBits(other.width))
			return false;
		return true;
	}

	public String text() {
		return String.format("%s%s",
				vaildWidth() ? signformat.format(this.width) : "",
						(vaildHeight() && this.width!=this.height) ? "x" + signformat.format(this.height) : "");
	}

	@Override
	public String toString() {
		return text();
	}
}
