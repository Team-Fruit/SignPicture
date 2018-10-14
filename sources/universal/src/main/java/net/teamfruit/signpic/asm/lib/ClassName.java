package net.teamfruit.signpic.asm.lib;

import javax.annotation.Nonnull;

import net.teamfruit.signpic.compat.Compat.CompatFMLDeobfuscatingRemapper;

public class ClassName {
	private final @Nonnull String clsName;

	public static @Nonnull ClassName of(final @Nonnull String clsName) {
		return new ClassName(clsName);
	}

	public static @Nonnull ClassName fromBytecodeName(final @Nonnull String clsName) {
		return new ClassName(BytecodeToSourcecodeName(clsName));
	}

	private ClassName(final @Nonnull String clsName) {
		this.clsName = clsName;
	}

	public @Nonnull ClassName toMappedName() {
		return fromBytecodeName(BytecodeToMappedName(getBytecodeName()));
	}

	public @Nonnull String getBytecodeName() {
		return SourcecodeToBytecodeName(getName());
	}

	public @Nonnull String getName() {
		return this.clsName;
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

	public static @Nonnull String BytecodeToMappedName(final @Nonnull String clsName) {
		return VisitorHelper.useSrgNames() ? CompatFMLDeobfuscatingRemapper.unmap(clsName) : clsName;
	}

	/**
	 * replace {@code .} to {@code /}
	 */
	public static @Nonnull String SourcecodeToBytecodeName(final @Nonnull String clsName) {
		return clsName.replace('.', '/');
	}

	/**
	 * replace {@code /} to {@code .}
	 */
	public static @Nonnull String BytecodeToSourcecodeName(final @Nonnull String clsName) {
		return clsName.replace('/', '.');
	}
}