package net.teamfruit.signpic.http.upload;

import javax.annotation.Nullable;

import net.teamfruit.signpic.http.ICommunicate;

public interface IUploader extends ICommunicate {
	@Nullable
	String getLink();
}
