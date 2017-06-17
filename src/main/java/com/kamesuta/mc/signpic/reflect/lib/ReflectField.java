package com.kamesuta.mc.signpic.reflect.lib;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.asm.lib.RefName;

public class ReflectField<T, S> {
	public final @Nullable Field refField;

	public @Nullable Field getReflectField() {
		return this.refField;
	}

	private ReflectField(final @Nullable Field refField) {
		this.refField = refField;
	}

	public @Nullable S get(@Nullable final T instance) {
		final Field field = getReflectField();
		if (field!=null)
			try {
				@SuppressWarnings("unchecked")
				final S o = (S) field.get(instance);
				return o;
			} catch (final Exception e) {
			}
		return null;
	}

	public boolean set(@Nullable final T instance, @Nullable final S value) {
		final Field field = getReflectField();
		if (field!=null)
			try {
				field.set(instance, value);
				return true;
			} catch (final Exception e) {
			}
		return false;
	}

	public static @Nonnull <F, G> ReflectField<F, G> getFieldFromType(final @Nonnull ReflectClass<F> refClass, final @Nullable ModifierMatcher matcher, final @Nonnull Class<G> type) {
		final Class<F> refClazz = refClass.getReflectClass();
		Field refFielz = null;
		if (refClazz!=null)
			b: {
				try {
					final Field[] fields = refClazz.getDeclaredFields();
					for (final Field field : fields)
						if (type.equals(field.getType())&&(matcher==null||matcher.match(field.getModifiers()))) {
							field.setAccessible(true);
							refFielz = field;
							break b;
						}
				} catch (final Throwable e) {
				}
			}
		return new ReflectField<F, G>(refFielz);
	}

	public static @Nonnull <F, G> ReflectField<F, G> getFieldFromName(final @Nonnull ReflectClass<F> refClass, final @Nonnull RefName refName, final @Nullable ModifierMatcher matcher, final @Nonnull Class<G> type) {
		final Class<F> refClazz = refClass.getReflectClass();
		Field refFielz = null;
		if (refClazz!=null)
			b: {
				try {
					final Field field = refClazz.getDeclaredField(refName.name());
					if (type.equals(field.getType())&&(matcher==null||matcher.match(field.getModifiers()))) {
						field.setAccessible(true);
						refFielz = field;
						break b;
					}
				} catch (final Throwable e) {
				}
			}
		return new ReflectField<F, G>(refFielz);
	}
}
