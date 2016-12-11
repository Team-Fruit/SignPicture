package com.kamesuta.mc.signpic.image.meta;

public interface MetaParser extends IComposable {
	boolean parse(String src, String key, String value);

	MetaParser reset();
}