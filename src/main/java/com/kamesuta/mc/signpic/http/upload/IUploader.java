package com.kamesuta.mc.signpic.http.upload;

import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.http.ICommunicate;

public interface IUploader extends ICommunicate {
	@Nullable
	String getLink();
}
