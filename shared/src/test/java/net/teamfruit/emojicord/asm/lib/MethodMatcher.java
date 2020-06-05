package net.teamfruit.emojicord.asm.lib;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import net.teamfruit.emojicord.compat.CompatFMLDeobfuscatingRemapper;

public class MethodMatcher implements Predicate<MethodNode> {
	private final @Nonnull ClassName clsName;
	private final @Nonnull String description;
	private final @Nonnull RefName refname;;

	public MethodMatcher(final @Nonnull ClassName clsName, final @Nonnull String description, final @Nonnull RefName refname) {
		this.clsName = clsName;
		this.description = description;
		this.refname = refname;
	}

	public boolean match(final @Nonnull String methodName, final @Nonnull String methodDesc) {
		if (CompatFMLDeobfuscatingRemapper.useMcpNames())
			return methodName.equals(this.refname.mcpName())&&methodDesc.equals(this.description);
		final String srgMethodDesc = CompatFMLDeobfuscatingRemapper.mapMethodDesc(methodDesc);
		if (!srgMethodDesc.equals(this.description))
			return false;
		final String srgMethodName = CompatFMLDeobfuscatingRemapper.mapMethodName(CompatFMLDeobfuscatingRemapper.unmap(this.clsName.getBytecodeName()), methodName, methodDesc);
		return srgMethodName.equals(this.refname.srgName());
	}

	@Override
	public boolean test(final MethodNode node) {
		return match(node.name, node.desc);
	}

	public Predicate<AbstractInsnNode> insnMatcher() {
		return node -> node instanceof MethodInsnNode&&match(((MethodInsnNode) node).name, ((MethodInsnNode) node).desc);
	}

	@Override
	public @Nonnull String toString() {
		return String.format("Mathod Matcher: %s.%s %s", this.clsName.getBytecodeName(), this.refname, this.description);
	}
}