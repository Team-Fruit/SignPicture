/*
 * This class is from the OpenModsLib.
 * https://github.com/OpenMods/OpenModsLib
 *
 * Code Copyright (c) 2013 Open Mods
 * Code released under the MIT license
 * https://github.com/OpenMods/OpenModsLib/blob/master/LICENSE
 */
package com.kamesuta.mc.signpic.asm.lib;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class FieldMatcher {
	private final String clsName;
	private final String description;
	private final String srgName;
	private final String mcpName;

	public FieldMatcher(final String clsName, final String description, final String mcpName, final String srgName) {
		this.clsName = clsName;
		this.description = description;
		this.srgName = srgName;
		this.mcpName = mcpName;
	}

	public boolean match(final String fieldName, final String fieldDesc) {
		if (!fieldDesc.equals(this.description))
			return false;
		if (fieldName.equals(this.mcpName))
			return true;
		if (!VisitorHelper.useSrgNames())
			return false;
		final String unmappedName = FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(this.clsName, fieldName, fieldDesc);
		return unmappedName.equals(this.srgName);
	}
}