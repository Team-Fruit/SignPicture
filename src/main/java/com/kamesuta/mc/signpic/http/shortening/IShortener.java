package com.kamesuta.mc.signpic.http.shortening;

import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.http.ICommunicate;

public interface IShortener extends ICommunicate {
	@Nullable
	String getShortLink();
}
