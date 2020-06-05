package net.teamfruit.emojicord.asm.lib;

import javax.annotation.Nonnull;

import org.objectweb.asm.ClassVisitor;

public interface INodeCoreTransformer extends INodeTransformer {
	@Nonnull
	ClassVisitor createVisitor(@Nonnull String name, @Nonnull ClassVisitor cv) throws StopTransforming;
}
