package com.kamesuta.mc.signpic.attr;

public interface IPropParser extends IPropComposable {
	boolean parse(String src, String key, String value);
}