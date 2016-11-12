package com.kamesuta.mc.bnnwidget.motion;

/**
 * Easing functions.
 * @author Kamesuta
 */
public enum Easings implements Easing {
	easeLinear {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return c*t/d+b;
		}
	},
	easeInQuad {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			return c*t*t+b;
		}
	},
	easeOutQuad {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			return -c*t*(t-2)+b;
		}
	},
	easeInOutQuad {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d/2;
			if (t<1)
				return c/2*t*t+b;
			t--;
			return -c/2*(t*(t-2)-1)+b;
		}
	},
	easeInCubic {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			return c*t*t*t+b;
		}
	},
	easeOutCubic {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			t--;
			return c*(t*t*t+1)+b;
		}
	},
	easeInOutCubic {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			if ((t /= d/2)<1)
				return c/2*t*t*t+b;
			return c/2*((t -= 2)*t*t+2)+b;
		}
	},
	easeInQuart {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			return c*(t /= d)*t*t*t+b;
		}
	},
	easeOutQuart {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			t--;
			return -c*(t*t*t*t-1)+b;
		}
	},
	easeInOutQuart {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d/2;
			if (t<1)
				return c/2*t*t*t*t+b;
			t -= 2;
			return -c/2*(t*t*t*t-2)+b;
		}
	},
	easeInQuint {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			return c*t*t*t*t*t+b;
		}
	},
	easeOutQuint {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			t--;
			return c*(t*t*t*t*t+1)+b;
		}
	},
	easeInOutQuint {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d/2;
			if (t<1)
				return c/2*t*t*t*t*t+b;
			t -= 2;
			return c/2*(t*t*t*t*t+2)+b;
		}
	},
	easeInSine {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return -c*Math.cos(t/d*(Math.PI/2))+c+b;
		}
	},
	easeOutSine {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return c*Math.sin(t/d*(Math.PI/2))+b;
		}
	},
	easeInOutSine {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return -c/2*(Math.cos(Math.PI*t/d)-1)+b;
		}
	},
	easeInExpo {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return c*Math.pow(2, 10*(t/d-1))+b;
		}
	},
	easeOutExpo {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return c*(-Math.pow(2, -10*t/d)+1)+b;
		}
	},
	easeInOutExpo {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d/2;
			if (t<1)
				return c/2*Math.pow(2, 10*(t-1))+b;
			t--;
			return c/2*(-Math.pow(2, -10*t)+2)+b;
		}
	},
	easeInCirc {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			return -c*(Math.sqrt(1-t*t)-1)+b;
		}
	},
	easeOutCirc {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			t--;
			return c*Math.sqrt(1-t*t)+b;
		}
	},
	easeInOutCirc {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d/2;
			if (t<1)
				return -c/2*(Math.sqrt(1-t*t)-1)+b;
			t -= 2;
			return c/2*(Math.sqrt(1-t*t)+1)+b;
		}
	},
	easeInElastic {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			double s = 1.70158;
			double p = 0;
			double a = c;
			if (t==0)
				return b;
			if ((t /= d)==1)
				return b+c;
			p = d*.3;
			if (a<Math.abs(c)) {
				a = c;
				s = p/4;
			} else
				s = p/(2*Math.PI)*Math.asin(c/a);
			return -(a*Math.pow(2, 10*(t -= 1))*Math.sin((t*d-s)*(2*Math.PI)/p))+b;
		}
	},
	easeOutElastic {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			double s = 1.70158;
			double p = 0;
			double a = c;
			if (t==0)
				return b;
			if ((t /= d)==1)
				return b+c;
			p = d*.3;
			if (a<Math.abs(c)) {
				a = c;
				s = p/4;
			} else
				s = p/(2*Math.PI)*Math.asin(c/a);
			return a*Math.pow(2, -10*t)*Math.sin((t*d-s)*(2*Math.PI)/p)+c+b;
		}
	},
	easeInOutElastic {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			double s = 1.70158;
			double p = 0;
			double a = c;
			if (t==0)
				return b;
			if ((t /= d/2)==2)
				return b+c;
			p = d*(.3*1.5);
			if (a<Math.abs(c)) {
				a = c;
				s = p/4;
			} else
				s = p/(2*Math.PI)*Math.asin(c/a);
			if (t<1)
				return -.5*(a*Math.pow(2, 10*(t -= 1))*Math.sin((t*d-s)*(2*Math.PI)/p))+b;
			return a*Math.pow(2, -10*(t -= 1))*Math.sin((t*d-s)*(2*Math.PI)/p)*.5+c+b;
		}
	},
	easeInBack {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			final double s = 1.70158;
			return c*(t /= d)*t*((s+1)*t-s)+b;
		}
	},
	easeOutBack {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			final double s = 1.70158;
			return c*((t = t/d-1)*t*((s+1)*t+s)+1)+b;
		}
	},
	easeInOutBack {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			double s = 1.70158;
			if ((t /= d/2)<1)
				return c/2*(t*t*(((s *= 1.525)+1)*t-s))+b;
			return c/2*((t -= 2)*t*(((s *= 1.525)+1)*t+s)+2)+b;
		}
	},
	easeInBounce {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return c-easeOutBounce.easing(d-t, 0, c, d)+b;
		}
	},
	easeOutBounce {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			if ((t /= d)<1/2.75)
				return c*(7.5625*t*t)+b;
			else if (t<2/2.75)
				return c*(7.5625*(t -= 1.5/2.75)*t+.75)+b;
			else if (t<2.5/2.75)
				return c*(7.5625*(t -= 2.25/2.75)*t+.9375)+b;
			else
				return c*(7.5625*(t -= 2.625/2.75)*t+.984375)+b;
		}
	},
	easeInOutBounce {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			if (t<d/2)
				return easeInBounce.easing(t*2, 0, c, d)*.5+b;
			return easeOutBounce.easing(t*2-d, 0, c, d)*.5+c*.5+b;
		}
	},
	;

	public IMotion move(final float time, final float to) {
		return Motion.easing(time, this, to);
	}
}
