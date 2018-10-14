package net.teamfruit.signpic.http.shortening;

import javax.annotation.Nullable;

import net.teamfruit.signpic.http.ICommunicate;

public interface IShortener extends ICommunicate {
	@Nullable
	String getShortLink();
}
