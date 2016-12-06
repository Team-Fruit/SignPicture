package com.kamesuta.mc.signpic.image.meta;

public class Frame implements IFrame {
	private final SizeData size;
	private final OffsetData offset;
	private final RotationData rotation;
	private final TextureMapData map;
	private final AnimationData frame;

	public Frame(final SizeData size, final OffsetData offset, final RotationData rotation, final TextureMapData map, final AnimationData frame) {
		this.size = size;
		this.offset = offset;
		this.rotation = rotation;
		this.map = map;
		this.frame = frame;
	}

	@Override
	public SizeData getSize() {
		return this.size;
	}

	@Override
	public OffsetData getOffset() {
		return this.offset;
	}

	@Override
	public RotationData getRotation() {
		return this.rotation;
	}

	@Override
	public TextureMapData getMap() {
		return this.map;
	}

	@Override
	public AnimationData getFrame() {
		return this.frame;
	}

	@Override
	public IFrame per(final float per, final IFrame before) {
		return new Frame(
				this.size.per(per, before.getSize()),
				this.offset.per(per, before.getOffset()),
				this.rotation.per(per, before.getRotation()),
				this.map.per(per, before.getMap()),
				this.frame.per(per, before.getFrame()));
	}
}
