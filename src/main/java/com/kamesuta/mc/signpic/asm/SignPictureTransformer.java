package com.kamesuta.mc.signpic.asm;

import javax.annotation.Nullable;

import net.minecraft.launchwrapper.IClassTransformer;

public class SignPictureTransformer implements IClassTransformer {

	@Override
	public @Nullable byte[] transform(final @Nullable String name, final @Nullable String transformedName, final @Nullable byte[] bytes) {
		if (bytes==null||name==null||transformedName==null)
			return bytes;

		//TODO

		return bytes;
	}
}