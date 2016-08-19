package com.kamesuta.mc.signpic.util;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.image.ImageSize;

import net.minecraft.tileentity.TileEntitySign;

public class SignParser {
	protected String text;
	protected boolean vaild;
	protected String id;
	protected ImageSize size;

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
			this.size = new ImageSize(wid, hei);
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

	public ImageSize size() {
		if (!this.vaild) throw new IllegalStateException("Invaild Sign");
		return this.size;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.size == null) ? 0 : this.size.hashCode());
		result = prime * result + (this.vaild ? 1231 : 1237);
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
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		if (this.size == null) {
			if (other.size != null)
				return false;
		} else if (!this.size.equals(other.size))
			return false;
		if (this.vaild != other.vaild)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("SignParser[id=%s, size=%s, vaild=%s, text=%s]", this.id, this.size, this.vaild, this.text);
	}
}
