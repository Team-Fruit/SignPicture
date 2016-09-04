package com.kamesuta.mc.signpic.image;

public enum ImageSizes {
	RAW {
		@Override
		public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
			return ImageSize.size(w, h);
		}
	},
	MAX {
		@Override
		public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
			return ImageSize.size(maxw, maxh);
		}
	},
	WIDTH {
		@Override
		public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
			return ImageSize.size(maxw, h*maxw/w);
		}
	},
	HEIGHT {
		@Override
		public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
			return ImageSize.size(w*maxh/h, maxh);
		}
	},
	INNER {
		@Override
		public ImageSize size(final float w, final float h, float maxw, float maxh) {
			if (w<0) maxw*=-1;
			if (h<0) maxh*=-1;
			final boolean b = ((w/maxw)>(h/maxh));
			return ImageSize.size(b?maxw:w*maxh/h, b?h*maxw/w:maxh);
		}
	},
	OUTER {
		@Override
		public ImageSize size(final float w, final float h, float maxw, float maxh) {
			if (w<0) maxw*=-1;
			if (h<0) maxh*=-1;
			final boolean b = ((w/maxw)<(h/maxh));
			return ImageSize.size(b?maxw:w*maxh/h, b?h*maxw/w:maxh);
		}
	},
	WIDTH_LIMIT {
		@Override
		public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
			if (w<maxw)
				return ImageSize.size(w, h);
			else
				return ImageSize.size(maxw, maxw*h/w);
		}
	},
	HEIGHT_LIMIT {
		@Override
		public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
			if (h<maxh)
				return ImageSize.size(w, h);
			else
				return ImageSize.size(maxh*w/h, maxh);
		}
	},
	LIMIT {
		@Override
		public ImageSize size(final float w, final float h, final float maxw, final float maxh) {
			if (w>h)
				if (w<maxw)
					return ImageSize.size(w, h);
				else
					return ImageSize.size(maxw, maxw*h/w);
			else
				if (h<maxh)
					return ImageSize.size(w, h);
				else
					return ImageSize.size(maxh*w/h, maxh);
		}
	},
	;

	public abstract ImageSize size(float w, float h, float maxw, float maxh);
}