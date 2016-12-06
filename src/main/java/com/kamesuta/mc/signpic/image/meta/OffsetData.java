package com.kamesuta.mc.signpic.image.meta;

import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;

public class OffsetData {
	public static final float defaultOffset = 0.5f;

	public final float x;
	public final float y;
	public final float z;

	public OffsetData(final float x, final float y, final float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public OffsetData scale(final float scale) {
		return new OffsetData(this.x*scale, this.y*scale, this.z*scale);
	}

	public OffsetData per(final float per, final OffsetData before) {
		return new OffsetData(this.x*per+before.x*(1f-per), this.y*per+before.y*(1f-per), this.z*per+before.z*(1f-per));
	}

	/**
	 * L=left
	 * R=right
	 * D=down
	 * U=up
	 * B=back
	 * F=front
	 */
	public String compose() {
		/* @formatter:off */
		final StringBuilder stb = new StringBuilder();
		if (this.x!=0)
			if (this.x<0)
				if (this.x==-defaultOffset) stb.append("L");
				else stb.append("L").append(ShortestFloatFormatter.format(-this.x));
			else
				if (this.x==defaultOffset) stb.append("R");
				else stb.append("R").append(ShortestFloatFormatter.format(this.x));
		if (this.y!=0)
			if (this.y<0)
				if (this.y==-defaultOffset) stb.append("D");
				else stb.append("D").append(ShortestFloatFormatter.format(-this.y));
			else
				if (this.y==defaultOffset) stb.append("U");
				else stb.append("U").append(ShortestFloatFormatter.format(this.y));
		if (this.z!=0)
			if (this.z<0)
				if (this.z==-defaultOffset) stb.append("B");
				else stb.append("B").append(ShortestFloatFormatter.format(-this.z));
			else
				if (this.z==defaultOffset) stb.append("F");
				else stb.append("F").append(ShortestFloatFormatter.format(this.z));
		return stb.toString();
		/* @formatter:on */
	}
}
