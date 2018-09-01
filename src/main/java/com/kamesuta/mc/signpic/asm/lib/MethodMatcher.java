/*
 * This class is from the OpenModsLib.
 * https://github.com/OpenMods/OpenModsLib
 *
 * Code Copyright (c) 2013 Open Mods
 * Code released under the MIT license
 * https://github.com/OpenMods/OpenModsLib/blob/master/LICENSE
 */
package com.kamesuta.mc.signpic.asm.lib;

import javax.annotation.Nonnull;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class MethodMatcher {
	private final @Nonnull String clsName;
	private final @Nonnull String description;
	private final @Nonnull RefName refname;;

	public MethodMatcher(final @Nonnull String clsName, final @Nonnull String description, final @Nonnull RefName refname) {
		this.clsName = clsName;
		this.description = description;
		this.refname = refname;
	}

	public MethodMatcher(final @Nonnull MappedType cls, final @Nonnull String description, final @Nonnull RefName refname) {
		this(cls.name(), description, refname);
	}

	public boolean match(final @Nonnull String methodName, final @Nonnull String methodDesc) {
		if (methodName.equals(this.refname.mcpName()))
			return true;
		if (!VisitorHelper.useSrgNames())
			return false;
		final String unmappedDesc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(methodDesc);
		if (!unmappedDesc.equals(this.description))
			return false;
		final String unmappedName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(this.clsName, methodName, methodDesc);
		return unmappedName.equals(this.refname.srgName());
	}

	@Override
	public @Nonnull String toString() {
		return String.format("Matcher: %s.%s %s", this.clsName, this.refname, this.description);
	}
}