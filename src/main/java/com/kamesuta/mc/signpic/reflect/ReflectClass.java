package com.kamesuta.mc.signpic.reflect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.launchwrapper.Launch;

public class ReflectClass<T> {
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