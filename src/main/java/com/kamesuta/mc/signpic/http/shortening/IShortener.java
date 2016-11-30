package com.kamesuta.mc.signpic.http.shortening;

import com.kamesuta.mc.signpic.http.ICommunicate;

public interface IShortener extends ICommunicate {
	String getShortLink();
}
