package com.kamesuta.mc.signpic.image.meta;

import java.util.Set;

public abstract class MetaMovie<E, B> implements MetaParser, MetaBuilder<E, B> {
	private Set<String> identifier;

	public MetaMovie(final Set<String> identifier) {
		this.identifier = identifier;
	}

	public Set<String> getIdentifier() {
		return this.identifier;
	}

	public MetaMovie<E, B> register() {

		return this;
	}
}
