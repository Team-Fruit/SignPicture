package net.teamfruit.emojicord.asm.lib;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.objectweb.asm.tree.ClassNode;

import net.teamfruit.emojicord.compat.CompatFMLDeobfuscatingRemapper;

public class ClassMatcher implements Predicate<ClassNode> {
	private final @Nonnull ClassName clsName;
	private final @Nonnull String mappedClassName;
	private final @Nonnull String unmappedClassName;

	public ClassMatcher(final @Nonnull ClassName clsName) {
		this.clsName = clsName;
		this.mappedClassName = this.clsName.getBytecodeName();
		this.unmappedClassName = CompatFMLDeobfuscatingRemapper.unmap(this.mappedClassName);
	}

	public boolean match(final @Nonnull String className) {
		return this.unmappedClassName.equals(className)||this.mappedClassName.equals(className);
	}

	@Override
	public boolean test(final ClassNode node) {
		return match(node.name);
	}

	@Override
	public @Nonnull String toString() {
		return String.format("Class Matcher: %s", this.mappedClassName);
	}
}