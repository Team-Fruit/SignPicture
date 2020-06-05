package net.teamfruit.bnnwidget.motion;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

/**
 * Easing functions.
 * @author Kamesuta
 */
public enum Easings implements Easing {
	easeLinear(0) {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return c*t/d+b;
		}
	},
	easeInQuad(1) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			return c*t*t+b;
		}
	},
	easeOutQuad(2) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			return -c*t*(t-2)+b;
		}
	},
	easeInOutQuad(3) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d/2;
			if (t<1)
				return c/2*t*t+b;
			t--;
			return -c/2*(t*(t-2)-1)+b;
		}
	},
	easeInCubic(4) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			return c*t*t*t+b;
		}
	},
	easeOutCubic(5) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			t--;
			return c*(t*t*t+1)+b;
		}
	},
	easeInOutCubic(6) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			if ((t /= d/2)<1)
				return c/2*t*t*t+b;
			return c/2*((t -= 2)*t*t+2)+b;
		}
	},
	easeInQuart(7) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			return c*(t /= d)*t*t*t+b;
		}
	},
	easeOutQuart(8) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			t--;
			return -c*(t*t*t*t-1)+b;
		}
	},
	easeInOutQuart(9) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d/2;
			if (t<1)
				return c/2*t*t*t*t+b;
			t -= 2;
			return -c/2*(t*t*t*t-2)+b;
		}
	},
	easeInQuint(10) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			return c*t*t*t*t*t+b;
		}
	},
	easeOutQuint(11) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			t--;
			return c*(t*t*t*t*t+1)+b;
		}
	},
	easeInOutQuint(12) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d/2;
			if (t<1)
				return c/2*t*t*t*t*t+b;
			t -= 2;
			return c/2*(t*t*t*t*t+2)+b;
		}
	},
	easeInSine(13) {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return -c*Math.cos(t/d*(Math.PI/2))+c+b;
		}
	},
	easeOutSine(14) {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return c*Math.sin(t/d*(Math.PI/2))+b;
		}
	},
	easeInOutSine(15) {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return -c/2*(Math.cos(Math.PI*t/d)-1)+b;
		}
	},
	easeInExpo(16) {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return c*Math.pow(2, 10*(t/d-1))+b;
		}
	},
	easeOutExpo(17) {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return c*(-Math.pow(2, -10*t/d)+1)+b;
		}
	},
	easeInOutExpo(18) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d/2;
			if (t<1)
				return c/2*Math.pow(2, 10*(t-1))+b;
			t--;
			return c/2*(-Math.pow(2, -10*t)+2)+b;
		}
	},
	easeInCirc(19) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			return -c*(Math.sqrt(1-t*t)-1)+b;
		}
	},
	easeOutCirc(20) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d;
			t--;
			return c*Math.sqrt(1-t*t)+b;
		}
	},
	easeInOutCirc(21) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			t /= d/2;
			if (t<1)
				return -c/2*(Math.sqrt(1-t*t)-1)+b;
			t -= 2;
			return c/2*(Math.sqrt(1-t*t)+1)+b;
		}
	},
	easeInElastic(22) {
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
	easeOutElastic(23) {
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
	easeInOutElastic(24) {
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
	easeInBack(25) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			final double s = 1.70158;
			return c*(t /= d)*t*((s+1)*t-s)+b;
		}
	},
	easeOutBack(26) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			final double s = 1.70158;
			return c*((t = t/d-1)*t*((s+1)*t+s)+1)+b;
		}
	},
	easeInOutBack(27) {
		@Override
		public double easing(double t, final double b, final double c, final double d) {
			double s = 1.70158;
			if ((t /= d/2)<1)
				return c/2*(t*t*(((s *= 1.525)+1)*t-s))+b;
			return c/2*((t -= 2)*t*(((s *= 1.525)+1)*t+s)+2)+b;
		}
	},
	easeInBounce(28) {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			return c-easeOutBounce.easing(d-t, 0, c, d)+b;
		}
	},
	easeOutBounce(29) {
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
	easeInOutBounce(30) {
		@Override
		public double easing(final double t, final double b, final double c, final double d) {
			if (t<d/2)
				return easeInBounce.easing(t*2, 0, c, d)*.5+b;
			return easeOutBounce.easing(t*2-d, 0, c, d)*.5+c*.5+b;
		}
	},
	;

	/**
	 * 内部ID
	 */
	public final int id;

	private Easings(final int id) {
		this.id = id;
	}

	/**
	 * Easingモーションを作成します
	 * @param time 移動時間
	 * @param to 移動後値
	 * @return
	 */
	public @Nonnull IMotion move(final float time, final float to) {
		return Motion.easing(time, this, to);
	}

	@Override
	public String toString() {
		return name()+"("+this.id+")";
	}

	private static final @Nonnull ImmutableMap<Integer, Easings> easingIds;

	/**
	 * 内部IDからEasingを取得します
	 * @param id 内部ID
	 * @return Easing
	 */
	public static @Nonnull Easings fromId(final int id) {
		Easings easing = easingIds.get(id);
		if (easing==null)
			easing = easeLinear;
		return easing;
	}

	/**
	 * StringからEasingを取得します<br>
	 * 大文字と小文字を区別しません
	 * @param easing
	 * @return
	 */
	public static Easings fromString(final String easing) {
		for (final Easings line : values())
			if (line.name().equalsIgnoreCase(easing))
				return line;
		throw new IllegalArgumentException();
	}

	static {
		final Builder<Integer, Easings> builder = ImmutableMap.builder();
		for (final Easings easing : Easings.values())
			builder.put(easing.id, easing);
		easingIds = builder.build();
	}
}
