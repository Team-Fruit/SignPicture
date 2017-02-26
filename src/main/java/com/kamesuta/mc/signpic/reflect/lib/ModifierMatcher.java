package com.kamesuta.mc.signpic.reflect.lib;

import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.Sets;

public class ModifierMatcher {
	private @Nonnull Set<Integer> require = Sets.newHashSet();
	private @Nonnull Set<Integer> deny = Sets.newHashSet();

	public @Nonnull ModifierMatcher require(final int modifier) {
		this.require.add(modifier);
		this.deny.remove(modifier);
		return this;
	}

	public @Nonnull ModifierMatcher deny(final int modifier) {
		this.require.remove(modifier);
		this.deny.add(modifier);
		return this;
	}

	public @Nonnull ModifierMatcher ignore(final int modifier) {
		this.require.remove(modifier);
		this.deny.remove(modifier);
		return this;
	}

	public boolean match(final int modifiers) {
		for (final int req : this.require)
			if (!isMatchModifier(modifiers, req))
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
