package com.kamesuta.mc.signpic.reflect;

import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.Sets;

public class ModifierMatcher {
	private @Nonnull Set<Integer> accept = Sets.newHashSet();
	private @Nonnull Set<Integer> deny = Sets.newHashSet();

	public @Nonnull ModifierMatcher accept(final int modifier) {
		this.accept.add(modifier);
		this.deny.remove(modifier);
		return this;
	}

	public @Nonnull ModifierMatcher deny(final int modifier) {
		this.accept.remove(modifier);
		this.deny.add(modifier);
		return this;
	}

	public @Nonnull ModifierMatcher ignore(final int modifier) {
		this.accept.remove(modifier);
		this.deny.remove(modifier);
		return this;
	}

	public boolean match(final int modifiers) {
		for (final int acc : this.accept)
			if (!isMatchModifier(modifiers, acc))
				return false;
		for (final int den : this.deny)
			if (isMatchModifier(modifiers, den))
				return false;
		return true;
	}

	private boolean isMatchModifier(final int modifiers, final int modifier) {
		return (modifiers&modifier)!=0;
	}
}
