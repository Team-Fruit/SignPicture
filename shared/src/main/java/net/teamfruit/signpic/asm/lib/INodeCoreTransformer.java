package net.teamfruit.signpic.asm.lib;

import org.objectweb.asm.ClassVisitor;

import javax.annotation.Nonnull;

public interface INodeCoreTransformer extends INodeTransformer {
    @Nonnull
    ClassVisitor createVisitor(@Nonnull String name, @Nonnull ClassVisitor cv) throws StopTransforming;
}
