package net.teamfruit.emojicord.asm.lib;

import javax.annotation.Nonnull;

public class ClassName {
	private final @Nonnull String clsName;

	public static @Nonnull ClassName of(final @Nonnull String clsName) {
		return new ClassName(clsName);
	}

	private ClassName(final @Nonnull String clsName) {
		this.clsName = clsName;
	}

	public @Nonnull String getBytecodeName() {
		return SourcecodeToBytecodeName(this.clsName);
	}

	public @Nonnull String getName() {
		return BytecodeToSourcecodeName(this.clsName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime*result+this.clsName.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this==obj)
			return true;
		if (obj==null)
			return false;
		if (!(obj instanceof ClassName))
			return false;
		final ClassName other = (ClassName) obj;
		if (!this.clsName.equals(other.clsName))
			return false;
		return true;
	}

	/**
	 * replace {@code .} to {@code /}
	 */
	private static @Nonnull String SourcecodeToBytecodeName(final @Nonnull String clsName) {
		return clsName.replace('.', '/');
	}

	/**
	 * replace {@code /} to {@code .}
	 */
	private static @Nonnull String BytecodeToSourcecodeName(final @Nonnull String clsName) {
		return clsName.replace('/', '.');
	}
}