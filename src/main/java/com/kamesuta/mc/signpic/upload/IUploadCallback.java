package com.kamesuta.mc.signpic.upload;

public interface IUploadCallback<RES extends IUploadResponse<? extends IUploadResult>> {
	void onDone(RES res);
}
