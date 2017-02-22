package com.kamesuta.mc.signpic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

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

		public @Nonnull <F> ReflectField<T, F> getFieldFromName(final @Nonnull String mcpName, final @Nonnull String srgName, final @Nonnull Class<F> type) {
			return ReflectField.getFieldFromName(this, mcpName, srgName, type);
		}

		public @Nonnull <F> ReflectMethod<T, F> getMethodFromType(final @Nonnull Class<F> returnType, final @Nonnull Class<?>... paramsType) {
			return ReflectMethod.getMethodFromType(this, returnType, paramsType);
		}

		public @Nonnull <F> ReflectMethod<T, F> getMethodFromName(final @Nonnull String mcpName, final @Nonnull String srgName, final @Nonnull Class<F> returnType, final @Nonnull Class<?>... paramsType) {
			return ReflectMethod.getMethodFromName(this, mcpName, srgName, returnType, paramsType);
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

		public static @Nonnull <F, G> ReflectField<F, G> getFieldFromName(final @Nonnull ReflectClass<F> refClass, final @Nonnull String mcpName, final @Nonnull String srgName, final @Nonnull Class<G> type) {
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
	}

	public static class ReflectMethod<T, S> {
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

		public static @Nonnull <F, G> ReflectMethod<F, G> getMethodFromType(final @Nonnull ReflectClass<F> refClass, final @Nonnull Class<G> returnType, final @Nonnull Class<?>... paramsType) {
			Method refMethoz = null;
			if (refClass.refClazz!=null)
				b: {
					try {
						final Method[] methods = refClass.refClazz.getDeclaredMethods();
						for (final Method method : methods)
							if (returnType.equals(method.getReturnType())&&Arrays.equals(paramsType, method.getParameterTypes())) {
								method.setAccessible(true);
								refMethoz = method;
								break b;
							}
					} catch (final Throwable e) {
					}
				}
			return new ReflectMethod<F, G>(refMethoz);
		}

		public static @Nonnull <F, G> ReflectMethod<F, G> getMethodFromName(final @Nonnull ReflectClass<F> refClass, final @Nonnull String mcpName, final @Nonnull String srgName, final @Nonnull Class<G> returnType, final @Nonnull Class<?>... paramsType) {
			Method refMethoz = null;
			if (refClass.refClazz!=null)
				b: {
					try {
						final Method method = refClass.refClazz.getMethod(ReflectClass.useSrgNames() ? srgName : mcpName, paramsType);
						if (returnType.equals(method.getReturnType())) {
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
}
