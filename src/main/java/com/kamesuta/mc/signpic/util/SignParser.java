package com.kamesuta.mc.signpic.util;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.tileentity.TileEntitySign;

public class SignParser {
	protected String text;
	protected boolean vaild;
	protected String id;
	protected float w;
	protected float h;

	public SignParser(final String text) {
		this.text = text;

		if (this.vaild = this.text.endsWith("]") && this.text.contains("[")) {
			final int start = this.text.lastIndexOf("[");
			String url = this.text.substring(0, start);
			if (url.startsWith("$")) {
				url = "https://" + url.substring(1);
			} else if (url.startsWith("//")) {
				url = "http://" + url.substring(2);
			} else if (!(url.startsWith("http://") || url.startsWith("https://"))) {
				url = "http://" + url;
			}
			this.id = url;

			final String size = this.text.substring(start+1, this.text.length()-1);
			final String[] sp_size = size.split("x");
			float wid = 1;
			try {
				if (sp_size.length >= 1)
					wid = Float.parseFloat(sp_size[0]);
			} catch (final NumberFormatException e) {}
			float hei = 1;
			try {
				if (sp_size.length >= 2)
					hei = Float.parseFloat(sp_size[1]);
				else
					hei = wid;
			} catch (final NumberFormatException e) {}
			this.w = wid;
			this.h = hei;
		}
	}

	public SignParser(final String[] sign) {
		this(StringUtils.join(sign));
	}

	public SignParser(final TileEntitySign tile) {
		this(tile.signText);
	}

	public boolean isVaild() {
		return this.vaild;
	}

	public String text() {
		return this.text;
	}

	public String id() {
		if (!this.vaild) throw new IllegalStateException("Invaild Sign");
		return this.id;
	}

	public float width() {
		if (!this.vaild) throw new IllegalStateException("Invaild Sign");
		return this.w;
	}

	public float height() {
		if (!this.vaild) throw new IllegalStateException("Invaild Sign");
		return this.h;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(this.h);
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + Float.floatToIntBits(this.w);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SignParser))
			return false;
		final SignParser other = (SignParser) obj;
		if (Float.floatToIntBits(this.h) != Float.floatToIntBits(other.h))
			return false;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		if (Float.floatToIntBits(this.w) != Float.floatToIntBits(other.w))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("SignParser[id=%s, width=%s, height=%s]", this.id, this.w, this.h);
	}
}
