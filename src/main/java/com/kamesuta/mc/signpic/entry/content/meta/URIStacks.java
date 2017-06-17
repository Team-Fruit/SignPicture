package com.kamesuta.mc.signpic.entry.content.meta;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class URIStacks {
	private final @Nonnull URI end;
	private final @Nonnull List<URI> uristack;

	protected URIStacks(final @Nonnull URI end, final @Nonnull List<URI> uristack) {
		this.end = end;
		this.uristack = uristack;
	}

	public @Nonnull URI getEndPoint() {
		return this.end;
	}

	/**
	 * First - Endpoint,
	 * Last - Basepoint
	 */
	public @Nonnull List<URI> getRedirectTrace() {
		return this.uristack;
	}

	public @Nonnull String getMetaString() {
		final StringBuilder stb = new StringBuilder();
		for (final URI uri : this.uristack) {
			final String frag = uri.getFragment();
			final String[] metas = StringUtils.substringsBetween(frag, "(=", "=)");
			if (metas!=null)
				for (final String meta : metas)
					stb.append(meta);
		}
		return stb.toString();
	}

	public static @Nonnull URIStacks from(final @Nonnull URI base, final @Nullable List<URI> redirects) {
		URI end;
		LinkedList<URI> reds;
		if (redirects!=null&&!redirects.isEmpty()) {
			reds = Lists.newLinkedList(redirects);
			end = reds.getLast();
			Collections.reverse(reds);
		} else {
			reds = Lists.newLinkedList();
			end = base;
		}
		reds.add(base);
		return new URIStacks(end, reds);
	}
}
