package com.kamesuta.mc.signpic;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.launchwrapper.Launch;

public class ReflectionManager {

	public static class ReflectClass<T> {
		private final @Nullable Class<T> refClazz;

		public @Nullable Class<T> getReflectClass() {
			return this.refClazz;
		}

		private ReflectClass(final @Nullable Class<T> refClazz) {
			this.refClazz = refClazz;
		}

		public @Nonnull <F> ReflectField<T, F> getFieldFromType(final @Nonnull Class<F> type) {
			return ReflectField.getFieldFromType(this, type);
		}

		public @Nonnull <F> ReflectField<T, F> getFieldFromName(final @Nonnull Class<F> type, final @Nonnull String mcpName, final @Nonnull String srgName) {
			return ReflectField.getFieldFromName(this, type, mcpName, srgName);
		}

		public static boolean useSrgNames() {
			final Boolean deobfuscated = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
			return deobfuscated==null||!deobfuscated;
		}

		public static @Nonnull <F> ReflectClass<F> fromClass(final @Nullable Class<F> refClazz) {
			return new ReflectClass<F>(refClazz);
		}
	}

	public static class ReflectField<T, S> {
		public final @Nullable Field refField;

		public @Nullable Field getReflectField() {
			return this.refField;
		}

		private ReflectField(final @Nullable Field refField) {
			this.refField = refField;
		}

		public static @Nonnull <F, G> ReflectField<F, G> getFieldFromType(final @Nonnull ReflectClass<F> refClass, final @Nonnull Class<G> type) {
			Field refFielz = null;
			if (refClass.refClazz!=null)
				b: {
					try {
						final Field[] fields = refClass.refClazz.getDeclaredFields();
						for (final Field field : fields)
							if (type.equals(field.getType())) {
								field.setAccessible(true);
								refFielz = field;
								break b;
							}
					} catch (final Throwable e) {
					}
				}
			return new ReflectField<F, G>(refFielz);
		}

		public static @Nonnull <F, G> ReflectField<F, G> getFieldFromName(final @Nonnull ReflectClass<F> refClass, final @Nonnull Class<G> type, final @Nonnull String mcpName, final @Nonnull String srgName) {
			Field refFielz = null;
			if (refClass.refClazz!=null)
				b: {
					try {
						final Field field = refClass.refClazz.getField(ReflectClass.useSrgNames() ? srgName : mcpName);
						if (type.equals(field.getType())) {
							field.setAccessible(true);
							refFielz = field;
							break b;
						}
					} catch (final Throwable e) {
					}
				}
			return new ReflectField<F, G>(refFielz);
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
	}
}
