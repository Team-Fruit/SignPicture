/*
 * This class is from the OpenModsLib.
 * https://github.com/OpenMods/OpenModsLib
 *
 * Code Copyright (c) 2013 Open Mods
 * Code released under the MIT license
 * https://github.com/OpenMods/OpenModsLib/blob/master/LICENSE
 */
package com.kamesuta.mc.signpic.asm.lib;

import org.objectweb.asm.Type;

public class MappedType {

	private final String clsName;

	public static MappedType of(final Class<?> cls) {
		return new MappedType(cls.getName());
	}

	public static MappedType of(final String clsName) {
		return new MappedType(clsName);
	}

	private MappedType(final String clsName) {
		this.clsName = VisitorHelper.getMappedName(clsName.replace('.', '/'));
	}

	public String name() {
		return this.clsName;
	}

	public Type type() {
		return Type.getObjectType(this.clsName);
	}

}