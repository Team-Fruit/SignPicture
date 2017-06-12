package com.kamesuta.mc.signpic.reflect.lib;

import java.lang.reflect.Method;
import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.asm.lib.RefName;

public class ReflectMethod<T, S> {
	public final @Nullable Method refMethod;

	public @Nullable Method getReflectMethod() {
		return this.refMethod;
	}

	private ReflectMethod(final @Nullable Method refMethod) {
		this.refMethod = refMethod;
	}

	public @Nullable S invoke(final @Nullable T instance, final @Nullable Object... params) {
		final Method method = getReflectMethod();
		if (method!=null)
			try {
				@SuppressWarnings("unchecked")
				final S o = (S) method.invoke(instance, params);
				return o;
			} catch (final Exception e) {
			}
		return null;
	}

	public static @Nonnull <F, G> ReflectMethod<F, G> getMethodFromType(final @Nonnull ReflectClass<F> refClass, final @Nullable ModifierMatcher matcher, final @Nonnull Class<G> returnType, final @Nonnull Class<?>... paramsType) {
		final Class<F> refClazz = refClass.getReflectClass();
		Method refMethoz = null;
		if (refClazz!=null)
			b: {
				try {
					final Method[] methods = refClazz.getDeclaredMethods();
					for (final Method method : methods)
						if (returnType.equals(method.getReturnType())&&Arrays.equals(paramsType, method.getParameterTypes())&&(matcher==null||matcher.match(method.getModifiers()))) {
							method.setAccessible(true);
							refMethoz = method;
							break b;
						}
				} catch (final Throwable e) {
				}
			}
		return new ReflectMethod<F, G>(refMethoz);
	}

	public static @Nonnull <F, G> ReflectMethod<F, G> getMethodFromName(final @Nonnull ReflectClass<F> refClass, final @Nonnull RefName refName, final @Nullable ModifierMatcher matcher, final @Nonnull Class<G> returnType, final @Nonnull Class<?>... paramsType) {
		final Class<F> refClazz = refClass.getReflectClass();
		Method refMethoz = null;
		if (refClazz!=null)
			b: {
				try {
					final Method method = refClazz.getDeclaredMethod(refName.name(), paramsType);
					if (returnType.equals(method.getReturnType())&&(matcher==null||matcher.match(method.getModifiers()))) {
						method.setAccessible(true);
						refMethoz = method;
						break b;
					}
				} catch (final Throwable e) {
				}
			}
		return new ReflectMethod<F, G>(refMethoz);
	}
}