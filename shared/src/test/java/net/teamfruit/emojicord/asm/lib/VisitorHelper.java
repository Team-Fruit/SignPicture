package net.teamfruit.emojicord.asm.lib;

import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.teamfruit.emojicord.compat.CompatFMLDeobfuscatingRemapper;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;

public class VisitorHelper {
	public static ClassNode read(final @Nonnull byte[] bytes, final int readerFlags) {
		Validate.notNull(bytes);
		final ClassReader cr = new ClassReader(bytes);
		final ClassNode node = new ClassNode(Opcodes.ASM5);
		cr.accept(node, readerFlags);
		return node;
	}

	public static byte[] write(final @Nonnull ClassNode node, final int writerFlags) {
		Validate.notNull(node);
		// ASMライブラリのクラスでのgetClass().getClassLoader()はForgeのクラスを見つけることができない可能性があります。
		// インナークラスを作成してgetClass().getClassLoader()をLaunchClassLoaderにしましょう。
		final ClassWriter cw = new ClassWriter(writerFlags) {
			@Override
			protected String getCommonSuperClass(final @Nullable String type1, final @Nullable String type2) {
				if (type1==null||type2==null)
					throw new NullPointerException();
				Class<?> c, d;
				final ClassLoader classLoader = ClassWriter.class.getClassLoader();
				try {
					try {
						c = Class.forName(ClassName.of(type1).getName(), false, classLoader);
						d = Class.forName(ClassName.of(type2).getName(), false, classLoader);
					} catch (final ClassNotFoundException e) {
						final ClassLoader launchClassLoader = getClass().getClassLoader();
						try {
							c = Class.forName(ClassName.of(CompatFMLDeobfuscatingRemapper.map(type1)).getName(), false, launchClassLoader);
							d = Class.forName(ClassName.of(CompatFMLDeobfuscatingRemapper.map(type2)).getName(), false, launchClassLoader);
						} catch (final ClassNotFoundException e1) {
							throw new RuntimeException(String.format("ClassLoader: %s, LaunchClassLoader: %s", e.toString(), e1.toString()));
						}
					}
				} catch (final Exception e) {
						throw new RuntimeException(e.toString());
				}
				if (c.isAssignableFrom(d))
					return type1;
				if (d.isAssignableFrom(c))
					return type2;
				if (c.isInterface()||d.isInterface())
					return ClassName.of("java.lang.Object").getBytecodeName();
				else {
					do
						c = c.getSuperclass();
					while (!c.isAssignableFrom(d));
					return ClassName.of(c.getName()).getBytecodeName();
				}
			}
		};
		node.accept(cw);
		return cw.toByteArray();
	}

	public static <T> Stream<AbstractInsnNode> stream(final InsnList instructions) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(instructions.iterator(), 0), false);
	}

	// Note:
	// ASM Core API to Tree API
	// Regex: super\.visit(.*)Insn\(Opcodes\.(.+)\);
	// Replace: insertion.add(new $1InsnNode(Opcodes.$2));

	// ASM Core API
	public static ClassNode apply(@Nonnull ClassNode node, final @Nonnull INodeCoreTransformer context) {
		final ClassNode cw = new ClassNode(Opcodes.ASM5);
		final ClassVisitor mod = context.createVisitor(node.name, cw);
		try {
			node.accept(mod);
			node = cw;
		} catch (final StopTransforming e) {
		}
		return node;
	}

	// ASM Core API
	public static ClassNode apply(@Nonnull ClassNode node, final @Nonnull INodeCoreTransformer context, final @Nonnull Logger logger) {
		final String name = context.getSimpleName();
		final String rawname = ClassName.of(node.name).getName();
		logger.info(String.format("Patching %s (class: %s)", name, rawname));
		node = apply(node, context);
		logger.debug(String.format("Finished Patching %s (class: %s)", name, rawname));
		return node;
	}

	// ASM Tree API
	public static ClassNode transform(@Nonnull ClassNode node, final @Nonnull INodeTreeTransformer context) {
		node = Validate.notNull(context.apply(node));
		return node;
	}

	// ASM Tree API
	public static ClassNode transform(@Nonnull ClassNode node, final @Nonnull INodeTreeTransformer context, final @Nonnull Logger logger) {
		final String name = context.getSimpleName();
		final String rawname = ClassName.of(node.name).getName();
		logger.info(String.format("Patching %s (class: %s)", name, rawname));
		node = transform(node, context);
		logger.debug(String.format("Finished Patching %s (class: %s)", name, rawname));
		return node;
	}
}