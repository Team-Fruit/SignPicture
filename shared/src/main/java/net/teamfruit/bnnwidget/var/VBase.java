package net.teamfruit.bnnwidget.var;

import javax.annotation.Nonnull;

import net.teamfruit.bnnwidget.OverridablePoint;

/**
 * 絶対的な値と相対的な値を定義た値です
 *
 * @author TeamFruit
 */
public class VBase implements VCommon {
	private final float coord;
	private @Nonnull VType type;

	public VBase(final float coord, final @Nonnull VType type) {
		this.coord = coord;
		this.type = type;
	}

	@Override
	@OverridablePoint
	public float get() {
		return this.coord;
	}

	@Override
	public float getAbsCoord(final float a, final float b) {
		return this.type.calc(a, b, get());
	}

	@Override
	public String toString() {
		return String.format("VBase [coord=%s, type=%s]", this.coord, this.type);
	}
}