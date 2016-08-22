package com.kamesuta.mc.guiwidget.animation;

/**
 * Easing functions.
 * @see <a href="http://gizma.com/easing/">gizma</a>
 * @author Kamesuta
 */
public enum Easings implements Easing {
	linearTween {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return c*t/d + b;
		}
	},
	easeInQuad {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			return c*t*t + b;
		}
	},
	easeOutQuad {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			return -c * t*(t-2) + b;
		}
	},
	easeInOutQuad {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d/2;
			if (t < 1) return c/2*t*t + b;
			t--;
			return -c/2 * (t*(t-2) - 1) + b;
		}
	},
	easeInCubic {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			return c*t*t*t + b;
		}
	},
	easeOutCubic {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			t--;
			return c*(t*t*t + 1) + b;
		}
	},
	easeInOutCubic {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			return c*t*t*t*t + b;
		}
	},
	easeOutQuart {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			t--;
			return -c * (t*t*t*t - 1) + b;
		}
	},
	easeInOutQuart {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d/2;
			if (t < 1) return c/2*t*t*t*t + b;
			t -= 2;
			return -c/2 * (t*t*t*t - 2) + b;
		}
	},
	easeInQuint {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			return c*t*t*t*t*t + b;
		}
	},
	easeOutQuint {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			t--;
			return c*(t*t*t*t*t + 1) + b;
		}
	},
	easeInOutQuint {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d/2;
			if (t < 1) return c/2*t*t*t*t*t + b;
			t -= 2;
			return c/2*(t*t*t*t*t + 2) + b;
		}
	},
	easeInSine {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return -c * Math.cos(t/d * (Math.PI/2)) + c + b;
		}
	},
	easeOutSine {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return c * Math.sin(t/d * (Math.PI/2)) + b;
		}
	},
	easeInOutSine {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return -c/2 * (Math.cos(Math.PI*t/d) - 1) + b;
		}
	},
	easeInExpo {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return c * Math.pow( 2, 10 * (t/d - 1) ) + b;
		}
	},
	easeOutExpo {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return c * ( -Math.pow( 2, -10 * t/d ) + 1 ) + b;
		}
	},
	easeInOutExpo {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d/2;
			if (t < 1) return c/2 * Math.pow( 2, 10 * (t - 1) ) + b;
			t--;
			return c/2 * ( -Math.pow( 2, -10 * t) + 2 ) + b;
		}
	},
	easeInCirc {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			return -c * (Math.sqrt(1 - t*t) - 1) + b;
		}
	},
	easeOutCirc {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			t--;
			return c * Math.sqrt(1 - t*t) + b;
		}
	},
	easeInOutCirc {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d/2;
			if (t < 1) return -c/2 * (Math.sqrt(1 - t*t) - 1) + b;
			t -= 2;
			return c/2 * (Math.sqrt(1 - t*t) + 1) + b;
		}
	},
}
