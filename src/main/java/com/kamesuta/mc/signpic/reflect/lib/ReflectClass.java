package com.kamesuta.mc.signpic.reflect.lib;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.asm.lib.RefName;

public class ReflectClass<T> {
	private final @Nullable Class<T> refClazz;

	public @Nullable Class<T> getReflectClass() {
		return this.refClazz;
	}

	private ReflectClass(final @Nullable Class<T> refClazz) {
		this.refClazz = refClazz;
	}

	public @Nonnull <F> ReflectField<T, F> getFieldFromType(final @Nullable ModifierMatcher matcher, final @Nonnull Class<F> type) {
		return ReflectField.getFieldFromType(this, matcher, type);
	}

	public @Nonnull <F> ReflectField<T, F> getFieldFromName(final @Nonnull RefName refName, final @Nullable ModifierMatcher matcher, final @Nonnull Class<F> type) {
		return ReflectField.getFieldFromName(this, refName, matcher, type);
	}

	public @Nonnull <F> ReflectMethod<T, F> getMethodFromType(final @Nullable ModifierMatcher matcher, final @Nonnull Class<F> returnType, final @Nonnull Class<?>... paramsType) {
		return ReflectMethod.getMethodFromType(this, matcher, returnType, paramsType);
	}

	public @Nonnull <F> ReflectMethod<T, F> getMethodFromName(final @Nonnull RefName refName, final @Nullable ModifierMatcher matcher, final @Nonnull Class<F> returnType, final @Nonnull Class<?>... paramsType) {
		return ReflectMethod.getMethodFromName(this, refName, matcher, returnType, paramsType);
	}

	public static @Nonnull <F> ReflectClass<F> fromClass(final @Nullable Class<F> refClazz) {
		return new ReflectClass<F>(refClazz);
	}
}