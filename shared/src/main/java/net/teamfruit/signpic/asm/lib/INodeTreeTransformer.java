package net.teamfruit.signpic.asm.lib;

import org.objectweb.asm.tree.ClassNode;

import java.util.function.Function;

public interface INodeTreeTransformer extends Function<ClassNode, ClassNode>, INodeTransformer {
    // 引数のnodeは書き換えても構いません。
    @Override
    public abstract ClassNode apply(ClassNode node);
}
