package com.kamesuta.mc.signpic.http;

public interface ICommunicate<T> {
	ICommunicateResponse<T> communicate();
}
