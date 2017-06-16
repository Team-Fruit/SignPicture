package com.kamesuta.mc.signpic.asm.lib;

import javax.annotation.Nonnull;

import net.minecraft.launchwrapper.Launch;

public class RefName {
	private final @Nonnull String mcpName;
	private final @Nonnull String srgName;

	protected RefName(final @Nonnull String mcpName, final @Nonnull String srgName) {
		this.mcpName = mcpName;
		this.srgName = srgName;
	}

	public @Nonnull String name() {
		return useSrgNames() ? this.srgName : this.mcpName;
	}

	public @Nonnull String mcpName() {
		return this.mcpName;
	}

	public @Nonnull String srgName() {
		return this.srgName;
	}

	@Override
	public @Nonnull String toString() {
		return String.format("[%s,%s]", this.mcpName, this.srgName);
	}

	public static boolean useMcpNames() {
		return !useSrgNames();
	}

	public static boolean useSrgNames() {
		final Boolean deobfuscated = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
		return deobfuscated==null||!deobfuscated;
	}

	public static @Nonnull RefName deobName(final @Nonnull String mcpName, final @Nonnull String srgName) {
		return new RefName(mcpName, srgName);
	}

	public static @Nonnull RefName name(final @Nonnull String name) {
		return new RefName(name, name);
	}
}
