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

public class FieldMatcher {
	private final @Nonnull ClassName clsName;
	private final @Nonnull String description;
	private final @Nonnull RefName refname;;

	public FieldMatcher(final @Nonnull ClassName clsName, final @Nonnull String description, final @Nonnull RefName refname) {
		this.clsName = clsName;
		this.description = description;
		this.refname = refname;
	}

	public boolean match(final @Nonnull String fieldName, final @Nonnull String fieldDesc) {
		if (!fieldDesc.equals(this.description))
			return false;
		if (fieldName.equals(this.refname.mcpName()))
			return true;
		if (!VisitorHelper.useSrgNames())
			return false;
		final String unmappedName = CompatFMLDeobfuscatingRemapper.mapFieldName(this.clsName.getBytecodeName(), fieldName, fieldDesc);
		return unmappedName.equals(this.refname.srgName());
	}

	@Override
	public @Nonnull String toString() {
		return String.format("Field Matcher: %s.%s %s", this.clsName.getBytecodeName(), this.refname, this.description);
	}
}