package com.kamesuta.mc.signpic.image;

public enum ImageSizes {
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
			return new ImageSize(maxw, h*maxw/w);
		}
	},
	HEIGHT {
		@Override
		public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
			return new ImageSize(w*maxh/h, maxh);
		}
	},
	INNER {
		@Override
		public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
			return new ImageSize((w/maxw>h/maxh)?maxw:w*maxh/h, (w/maxw>h/maxh)?h*maxw/w:maxh);
		}
	},
	OUTER {
		@Override
		public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
			return new ImageSize((w/maxw<h/maxh)?maxw:w*maxh/h, (w/maxw<h/maxh)?h*maxw/w:maxh);
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
				return new ImageSize(maxh*w/h, maxh);
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
					return new ImageSize(maxh*w/h, maxh);
		}
	},
	;

	public abstract ImageSize size(float w, float h, float maxw, float maxh);
}