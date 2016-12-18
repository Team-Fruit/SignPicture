package com.kamesuta.mc.signpic.asm;

import net.minecraft.launchwrapper.IClassTransformer;

public class SignPictureTransformer implements IClassTransformer {

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
		if (bytes==null)
			return bytes;

		// 1.8.9 comming soon

		return bytes;
	}
}