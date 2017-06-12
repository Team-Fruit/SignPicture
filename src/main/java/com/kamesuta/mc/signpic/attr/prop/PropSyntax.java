package com.kamesuta.mc.signpic.attr.prop;

import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;

/**
 * Syntax Define
 *
 * @author TeamFruit
 */
public enum PropSyntax {
	/**
	 * Easing ID [t]
	 * @see AnimationData
	 */
	ANIMATION_EASING("t"),
	/**
	 * Redstone Type [k]
	 * <p>
	 * Not Implemented
	 * @see AnimationData
	 */
	ANIMATION_REDSTONE("k"),
	/**
	 * Offset Left [L]
	 * @see OffsetData
	 */
	OFFSET_LEFT("L"),
	/**
	 * Offset Right [R]
	 * @see OffsetData
	 */
	OFFSET_RIGHT("R"),
	/**
	 * Offset Down [D]
	 * @see OffsetData
	 */
	OFFSET_DOWN("D"),
	/**
	 * Offset Up [U]
	 * @see OffsetData
	 */
	OFFSET_UP("U"),
	/**
	 * Offset Back [B]
	 * @see OffsetData
	 */
	OFFSET_BACK("B"),
	/**
	 * Offset Front [F]
	 * @see OffsetData
	 */
	OFFSET_FRONT("F"),
	/**
	 * Offset Center X [M]
	 * @see OffsetData
	 */
	OFFSET_CENTER_X("M"),
	/**
	 * Offset Center Y [N]
	 * @see OffsetData
	 */
	OFFSET_CENTER_Y("N"),
	/**
	 * Offset Center Z [O]
	 * @see OffsetData
	 */
	OFFSET_CENTER_Z("O"),
	/**
	 * Rotation X [X]
	 * @see OffsetData
	 */
	ROTATION_X("X"),
	/**
	 * Rotation Y [Y]
	 * @see OffsetData
	 */
	ROTATION_Y("Y"),
	/**
	 * Rotation Z [Z]
	 * @see OffsetData
	 */
	ROTATION_Z("Z"),
	/**
	 * AxisAngle Rotation Angle [A]
	 * @see OffsetData
	 */
	ROTATION_ANGLE("A"),
	/**
	 * AxisAngle Rotation X [I]
	 * @see OffsetData
	 */
	ROTATION_AXIS_X("I"),
	/**
	 * AxisAngle Rotation Y [J]
	 * @see OffsetData
	 */
	ROTATION_AXIS_Y("J"),
	/**
	 * AxisAngle Rotation Z [K]
	 * @see OffsetData
	 */
	ROTATION_AXIS_Z("K"),
	/**
	 * Size Width []
	 * @see SizeData
	 */
	SIZE_W(""),
	/**
	 * Size Height [x]
	 * @see SizeData
	 */
	SIZE_H("x"),
	/**
	 * Texture X [u]
	 * @see TextureData
	 */
	TEXTURE_X("u"),
	/**
	 * Texture Y [v]
	 * @see TextureData
	 */
	TEXTURE_Y("v"),
	/**
	 * Texture Width [w]
	 * @see TextureData
	 */
	TEXTURE_W("w"),
	/**
	 * Texture Height [h]
	 * @see TextureData
	 */
	TEXTURE_H("h"),
	/**
	 * Texture Split Width [c]
	 * @see TextureData
	 */
	TEXTURE_SPLIT_W("c"),
	/**
	 * Texture Split Height [s]
	 * @see TextureData
	 */
	TEXTURE_SPLIT_H("s"),
	/**
	 * Texture Opacity [o]
	 * @see TextureData
	 */
	TEXTURE_OPACITY("o"),
	/**
	 * Texture Repeat [r]
	 * @see TextureData
	 */
	TEXTURE_REPEAT("r"),
	/**
	 * Texture Mipmap [m]
	 * @see TextureData
	 */
	TEXTURE_MIPMAP("m"),
	/**
	 * Texture Lighting [l]
	 * @see TextureData
	 */
	TEXTURE_LIGHTING("l"),
	/**
	 * Texture Blend Src [b]
	 * @see TextureData
	 */
	TEXTURE_BLEND_SRC("b"),
	/**
	 * Texture Blend Dst [d]
	 * @see TextureData
	 */
	TEXTURE_BLEND_DST("d"),
	/**
	 * Texture Light X [f]
	 * @see TextureData
	 */
	TEXTURE_LIGHT_X("f"),
	/**
	 * Texture Light Y [g]
	 * @see TextureData
	 */
	TEXTURE_LIGHT_Y("g"),
	_reserved_e("e"),
	_reserved_E("E");
	;
	public final @Nonnull String id;

	private PropSyntax(@Nonnull final String identifier) {
		this.id = identifier;
	}

	static {
		// Bug finding
		final Map<String, PropSyntax> checkcache = Maps.newHashMap();
		final PropSyntax[] allprops = PropSyntax.values();
		for (final PropSyntax newprop : allprops) {
			final PropSyntax cacheprop = checkcache.get(newprop.id);
			if (cacheprop!=null)
				throw new IllegalStateException("conflicting sign syntax: ["+newprop.id+"]("+newprop.name()+"&"+cacheprop.name()+") // Oops! It's a mod bug! Please report us!");
			checkcache.put(newprop.id, newprop);
		}
	}
}
