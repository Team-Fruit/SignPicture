package com.kamesuta.mc.signpic.asm;

import javax.annotation.Nullable;

import net.minecraft.launchwrapper.IClassTransformer;

public class SignPictureTransformer implements IClassTransformer {
	@Override
	public @Nullable byte[] transform(final @Nullable String name, final @Nullable String transformedName, final @Nullable byte[] bytes) {
		if (bytes==null)
			return bytes;

		// 1.8.9 comming soon

		return bytes;
	}
}