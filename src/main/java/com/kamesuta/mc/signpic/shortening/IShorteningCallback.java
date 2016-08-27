package com.kamesuta.mc.signpic.shortening;

public interface IShorteningCallback {

	void onShorteningDone(String shorturl);

	void onShorteningError(String message, Throwable e);

}
