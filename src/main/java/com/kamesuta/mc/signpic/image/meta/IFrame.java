package com.kamesuta.mc.signpic.image.meta;

public interface IFrame extends IMotionFrame<IFrame> {

	AnimationData getFrame();

	TextureMapData getMap();

	RotationData getRotation();

	OffsetData getOffset();

	SizeData getSize();

}
