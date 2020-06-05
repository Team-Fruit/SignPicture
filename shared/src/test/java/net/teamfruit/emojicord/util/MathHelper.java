package net.teamfruit.emojicord.util;

public class MathHelper {
	public static int clamp(final int num, final int min, final int max) {
		if (max<min)
			return clamp(num, max, min);
		if (num<min)
			return min;
		else
			return num>max ? max : num;
	}

	public static float lerp(final float start, final float end, final float percent) {
		return start+Math.max(0, Math.min(1, percent))*(end-start);
	}

	public static int repeat(final int num, final int max) {
		return (num%max+max)%max;
	}
}
