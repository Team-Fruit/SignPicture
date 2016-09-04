package com.kamesuta.mc.bnnwidget.motion;

/**
 * Easing Animation
 * @author Kamesuta
 */
public interface Easing {

	/**
	 * return easing value
	 * @param t current time
	 * @param b start value
	 * @param c change value
	 * @param d duration
	 * @return easing value
	 */
	double easing(double t, double b, double c, double d);

}
