package com.kamesuta.mc.signpic;

import java.lang.reflect.Field;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import net.minecraft.launchwrapper.Launch;

public class ReflectionManager {

	private static final @Nonnull FieldManager field = new FieldManager();

	public ReflectionManager() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public static class ReflectClass {
		public final @Nullable Class<?> refClazz;

		private ReflectClass(final @Nullable Class<?> refClazz) {
			this.refClazz = refClazz;
		}

		public <T> ReflectField<T> getFieldFromType(final @Nonnull Class<T> type) {
			Field refFielz = null;
			if (this.refClazz!=null)
				b: {
					try {
						final Field[] fields = this.refClazz.getDeclaredFields();
						for (final Field field : fields)
							if (type.equals(field.getType())) {
								field.setAccessible(true);
								refFielz = field;
								break b;
							}
					} catch (final Throwable e) {
					}
				}
			return new ReflectField<T>(refFielz);
		}

		public <T> ReflectField<T> getFieldFromName(final @Nonnull Class<T> type, final @Nonnull String mcpName, final @Nonnull String srgName) {
			Field refFielz = null;
			if (this.refClazz!=null)
				b: {
					try {
						final Field field = this.refClazz.getField(useSrgNames() ? srgName : mcpName);
						if (type.equals(field.getType())) {
							field.setAccessible(true);
							refFielz = field;
							break b;
						}
					} catch (final Throwable e) {
					}
				}
			return new ReflectField<T>(refFielz);
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
			final ReflectClass other = (ReflectClass) obj;
			if (this.refClazz!=null) {
				if (!this.refClazz.equals(other.refClazz))
					return false;
			} else if (other.refClazz!=null)
				return false;
			return true;
		}

		public @Nonnull ReflectClass fromClass(final @Nullable Class<?> refClazz) {
			return new ReflectClass(refClazz);
		}

		public @Nonnull ReflectClass fromObject(final @Nullable Object object) {
			return fromClass(object!=null ? object.getClass() : null);
		}
	}

	public static class ReflectField<T> {
		public final @Nullable Field refField;

		private ReflectField(final @Nullable Field refField) {
			this.refField = refField;
		}

		public static <F> ReflectField<F> getFieldFromType(final @Nonnull ReflectClass refClass, final @Nonnull Class<F> type) {
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
			return new ReflectField<F>(refFielz);
		}

		public static <F> ReflectField<F> getFieldFromName(final @Nonnull ReflectClass refClass, final @Nonnull Class<F> type, final @Nonnull String mcpName, final @Nonnull String srgName) {
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
			return new ReflectField<F>(refFielz);
		}

		public @Nullable T get(@Nullable final Object instance) {
			if (this.refField!=null)
				try {
					@SuppressWarnings("unchecked")
					final T o = (T) this.refField.get(instance);
					return o;
				} catch (final Exception e) {
				}
			return null;
		}

		public boolean set(@Nullable final Object instance, @Nullable final T value) {
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
