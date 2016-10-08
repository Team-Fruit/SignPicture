package com.kamesuta.mc.signpic.upload;

public interface IUploadResponse<T extends IUploadResult> {
	boolean isSuccess();

	Throwable getError();

	T getResult();
}
