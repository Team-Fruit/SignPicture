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

import org.objectweb.asm.Type;

public class MappedType {
	private final @Nonnull String clsName;

	public static @Nonnull MappedType of(final @Nonnull Class<?> cls) {
		return new MappedType(cls.getName());
	}

	public static @Nonnull MappedType of(final @Nonnull String clsName) {
		return new MappedType(clsName);
	}

	private MappedType(final @Nonnull String clsName) {
		this.clsName = VisitorHelper.getMappedName(clsName.replace('.', '/'));
	}

	public @Nonnull String name() {
		return this.clsName;
	}

	public @Nonnull Type type() {
		return Type.getObjectType(this.clsName);
	}
}