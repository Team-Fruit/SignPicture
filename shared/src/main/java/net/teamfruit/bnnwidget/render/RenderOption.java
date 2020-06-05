package net.teamfruit.bnnwidget.render;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;

public class RenderOption {

	private @Nullable Map<String, Object> data = null;

	public @Nonnull Map<String, Object> getData() {
		if (this.data!=null)
			return this.data;
		return this.data = Maps.newHashMap();
	}

	@SuppressWarnings("unchecked")
	public @Nullable <T> T get(final @Nonnull String key, final @Nonnull Class<T> clazz) {
		final Object obj = getData().get(key);
		if (clazz.isInstance(obj))
			return (T) obj;
		return null;
	}

	public void put(final @Nonnull String key, final @Nonnull Object obj) {
		getData().put(key, obj);
	}

}
