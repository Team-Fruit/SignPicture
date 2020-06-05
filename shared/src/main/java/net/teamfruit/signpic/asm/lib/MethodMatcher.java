/*
 * This class is from the OpenModsLib.
 * https://github.com/OpenMods/OpenModsLib
 *
 * Code Copyright (c) 2013 Open Mods
 * Code released under the MIT license
 * https://github.com/OpenMods/OpenModsLib/blob/master/LICENSE
 */
package net.teamfruit.signpic.asm.lib;

import javax.annotation.Nonnull;

import net.teamfruit.signpic.compat.Compat.CompatFMLDeobfuscatingRemapper;

public class MethodMatcher {
	private final @Nonnull ClassName clsName;
	private final @Nonnull String description;
	private final @Nonnull RefName refname;;

	public MethodMatcher(final @Nonnull ClassName clsName, final @Nonnull String description, final @Nonnull RefName refname) {
		this.clsName = clsName;
		this.description = description;
		this.refname = refname;
	}

	public boolean match(final @Nonnull String methodName, final @Nonnull String methodDesc) {
		if (methodName.equals(this.refname.mcpName()))
			return true;
		if (!VisitorHelper.useSrgNames())
			return false;
		final String unmappedDesc = CompatFMLDeobfuscatingRemapper.mapMethodDesc(methodDesc);
		if (!unmappedDesc.equals(this.description))
			return false;
		final String unmappedName = CompatFMLDeobfuscatingRemapper.mapMethodName(this.clsName.getBytecodeName(), methodName, methodDesc);
		return unmappedName.equals(this.refname.srgName());
	}

	@Override
	public @Nonnull String toString() {
		return String.format("Mathod Matcher: %s.%s %s", this.clsName.getBytecodeName(), this.refname, this.description);
	}
}