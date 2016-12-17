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

public class MethodMatcher {
	private final String clsName;
	private final String description;
	private final String srgName;
	private final String mcpName;

	public MethodMatcher(final String clsName, final String description, final String mcpName, final String srgName) {
		this.clsName = clsName;
		this.description = description;
		this.srgName = srgName;
		this.mcpName = mcpName;
	}

	public MethodMatcher(final MappedType cls, final String description, final String mcpName, final String srgName) {
		this(cls.name(), description, mcpName, srgName);
	}

	public boolean match(final String methodName, final String methodDesc) {
		if (methodName.equals(this.mcpName))
			return true;
		if (!VisitorHelper.useSrgNames())
			return false;
		final String unmappedDesc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(methodDesc);
		if (!unmappedDesc.equals(this.description))
			return false;
		final String unmappedName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(this.clsName, methodName, methodDesc);
		return unmappedName.equals(this.srgName);
	}

	@Override
	public String toString() {
		return String.format("Matcher: %s.[%s,%s] %s", this.clsName, this.srgName, this.mcpName, this.description);
	}

}