package com.kamesuta.mc.signpic;

import com.kamesuta.mc.signpic.image.ImageSizes;

public class Debug {
	public static void main(final String[] args) {
		Reference.logger.info(ImageSizes.LIMIT.size(32, 24, 10, 12));
	}
}
