package com.kamesuta.mc.signpic;

public class Debug {
	public static void main(final String[] args) {
		// Reference.logger.info(ImageSizes.LIMIT.size(32, 24, 10, 12));
		//		final IPositionAbsolute abs1 = new PositionAbsolute(0, 0, 0, 0, 100, 100);
		//		final GuiPosition gp = GuiPosition.createBase(abs1)
		//				.child(new RelativePosition(10, 10, -11, -11, true))
		//				.child(new RelativeSizedPosition(0, 0, 21, 21, false));
		//		Reference.logger.info(gp.getAbsolute());


		final int p_73734_4_ = -16777216;
		final float f3 = (p_73734_4_ >> 24 & 255) / 255.0F;
		final float f = (p_73734_4_ >> 16 & 255) / 255.0F;
		final float f1 = (p_73734_4_ >> 8 & 255) / 255.0F;
		final float f2 = (p_73734_4_ & 255) / 255.0F;
		Reference.logger.info(f);
		Reference.logger.info(f1);
		Reference.logger.info(f2);
		Reference.logger.info(f3);
	}
}
