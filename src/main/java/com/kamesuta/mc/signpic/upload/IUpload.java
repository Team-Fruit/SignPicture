package com.kamesuta.mc.signpic.upload;

public interface IUpload<RES extends IUploadResponse<? extends IUploadResult>> {
	RES upload();
}
