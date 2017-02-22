package com.kamesuta.mc.signpic;

import java.lang.reflect.Field;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import net.minecraft.launchwrapper.Launch;

public class ReflectionManager {

	public static class ReflectClass<T> {
		public final @Nullable Class<T> refClazz;

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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime*result+(this.refClazz!=null ? this.refClazz.hashCode() : 0);
			return result;
		}

		@Override
		public boolean equals(@Nullable final Object obj) {
			if (this==obj)
				return true;
			if (obj==null)
				return false;
			if (!(obj instanceof ReflectClass))
				return false;
			final ReflectClass<?> other = (ReflectClass<?>) obj;
			if (this.refClazz!=null) {
				if (!this.refClazz.equals(other.refClazz))
					return false;
			} else if (other.refClazz!=null)
				return false;
			return true;
		}

		public static @Nonnull <F> ReflectClass<F> fromClass(final @Nullable Class<F> refClazz) {
			return new ReflectClass<F>(refClazz);
		}
	}

	public static class ReflectField<T, S> {
		public final @Nullable Field refField;

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
			if (this.refField!=null)
				try {
					@SuppressWarnings("unchecked")
					final S o = (S) this.refField.get(instance);
					return o;
				} catch (final Exception e) {
				}
			return null;
		}

		public boolean set(@Nullable final T instance, @Nullable final S value) {
			if (this.refField!=null)
				try {
					this.refField.set(instance, value);
					return true;
				} catch (final Exception e) {
				}
			return false;
		}
	}

	public static class FieldManager {
		private Cache<Class<?>, Class<?>, Field> cache = new Cache<Class<?>, Class<?>, Field>();

		public @Nullable Field getFieldFromType(@Nonnull final Class<?> baseclass, @Nonnull final Class<?> type) {
			final Field cachedfield = this.cache.get(baseclass, type);
			if (cachedfield!=null)
				return cachedfield;
			try {
				final Field[] fields = baseclass.getDeclaredFields();
				for (final Field field : fields)
					if (type.equals(field.getType())) {
						field.setAccessible(true);
						this.cache.put(baseclass, type, field);
						return field;
					}
			} catch (final Exception e) {
			}
			return null;
		}

		public @Nullable <T> T getObjectFromType(@Nonnull final Class<?> baseclass, @Nonnull final Class<T> type, @Nullable final Object instance) {
			try {
				final Field field = getFieldFromType(baseclass, type);
				if (field!=null)
					return (T) field.get(instance);
			} catch (final Exception e) {
			}
			return null;
		}
	}

	protected static class Cache<X, Y, Z> {
		private @Nonnull Map<X, Map<Y, Z>> xyz = Maps.newHashMap();

		public void put(@Nullable final X x, @Nullable final Y y, @Nullable final Z z) {
			Map<Y, Z> yz = this.xyz.get(x);
			if (yz==null) {
				yz = Maps.newHashMap();
				this.xyz.put(x, yz);
			}
			yz.put(y, z);
		}

		public @Nullable Z get(@Nullable final X x, @Nullable final Y y) {
			final Map<Y, Z> yz = this.xyz.get(x);
			if (yz!=null)
				return yz.get(y);
			return null;
		}
	}
}
