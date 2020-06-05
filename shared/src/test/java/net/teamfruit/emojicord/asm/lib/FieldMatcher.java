package net.teamfruit.emojicord.asm.lib;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;

import net.teamfruit.emojicord.compat.CompatFMLDeobfuscatingRemapper;

public class FieldMatcher implements Predicate<FieldNode> {
	private final @Nonnull ClassName clsName;
	private final @Nonnull String description;
	private final @Nonnull RefName refname;;

	public FieldMatcher(final @Nonnull ClassName clsName, final @Nonnull String description, final @Nonnull RefName refname) {
		this.clsName = clsName;
		this.description = description;
		this.refname = refname;
	}

	public boolean match(final @Nonnull String fieldName, final @Nonnull String fieldDesc) {
		if (CompatFMLDeobfuscatingRemapper.useMcpNames())
			return fieldName.equals(this.refname.mcpName())&&fieldDesc.equals(this.description);
		final String srgDesc = CompatFMLDeobfuscatingRemapper.mapDesc(fieldDesc);
		if (!srgDesc.equals(this.description))
			return false;
		final String srgFieldName = CompatFMLDeobfuscatingRemapper.mapFieldName(CompatFMLDeobfuscatingRemapper.unmap(this.clsName.getBytecodeName()), fieldName, fieldDesc);
		return srgFieldName.equals(this.refname.srgName());
	}

	@Override
	public boolean test(final FieldNode node) {
		return match(node.name, node.desc);
	}

	public Predicate<AbstractInsnNode> insnMatcher() {
		return node -> node instanceof FieldInsnNode&&match(((FieldInsnNode) node).name, ((FieldInsnNode) node).desc);
	}

	@Override
	public @Nonnull String toString() {
		return String.format("Field Matcher: %s.%s %s", this.clsName.getBytecodeName(), this.refname, this.description);
	}
}