package com.kamesuta.mc.signpic.image;

public class ImageSize {
	public final float width;
	public final float height;

	public ImageSize(final float width, final float height) {
		this.width = width;
		this.height = height;
	}

	//public static ImageSize from

	public static enum Size {
		RAW {
			@Override
			public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
				return new ImageSize(w, h);
			}
		},
		MAX {
			@Override
			public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
				return new ImageSize(maxw, maxh);
			}
		},
		WIDTH {
			@Override
			public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
				return new ImageSize(w, maxh*w/maxw);
			}
		},
		HEIGHT {
			@Override
			public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
				return new ImageSize(maxw*h/maxh, h);
			}
		},
		OUTER {
			@Override
			public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
				return new ImageSize((maxw/w>maxh/h)?w:maxw*h/maxh, (maxw/w>maxh/h)?maxh*w/maxw:h);
			}
		},
		INNER {
			@Override
			public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
				return new ImageSize((maxw/w<maxh/h)?w:maxw*h/maxh, (maxw/w<maxh/h)?maxh*w/maxw:h);
			}
		},
		WIDTH_LIMIT {
			@Override
			public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
				if (w<maxw)
					return new ImageSize(w, h);
				else
					return new ImageSize(maxw, maxw*h/w);
			}
		},
		HEIGHT_LIMIT {
			@Override
			public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
				if (h<maxh)
					return new ImageSize(w, h);
				else
					return new ImageSize(maxh, maxh*w/h);
			}
		},
		LIMIT {
			@Override
			public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
				if (w>h)
					if (w<maxw)
						return new ImageSize(w, h);
					else
						return new ImageSize(maxw, maxw*h/w);
				else
					if (h<maxh)
						return new ImageSize(w, h);
					else
						return new ImageSize(maxh, maxh*w/h);
			}
		},
		;

		public abstract ImageSize size(float w, float h, float maxw, float maxh);
	}
}
