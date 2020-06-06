package net.teamfruit.signpic.asm;

import net.teamfruit.signpic.Log;
import net.teamfruit.signpic.asm.lib.*;
import net.teamfruit.signpic.compat.CompatTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SignPictureTransformer extends CompatTransformer #if MC_12_LATER implements cpw.mods.modlauncher.api.ITransformer<ClassNode> #endif {
    @Override
    public ClassNode read(@Nonnull final byte[] bytes) {
        return VisitorHelper.read(bytes, ClassReader.SKIP_FRAMES);
    }

    @Override
    public byte[] write(@Nonnull final ClassNode node) {
        return VisitorHelper.write(node, ClassWriter.COMPUTE_FRAMES);
    }

    public final INodeCoreTransformer[] transformers = SignPictureTransforms.transformers;

    private final Set<String> transformerNames = Stream.of(this.transformers).map(INodeTransformer::getClassName).map(ClassName::getName).collect(Collectors.toSet());

    @Override
    public ClassNode transform(final ClassNode input, final CompatTransformerVotingContext context) {
        try {
            for (final INodeCoreTransformer transformer : this.transformers)
                if (transformer.getMatcher().test(input))
                    return VisitorHelper.apply(input, transformer, Log.log);
        } catch (final Exception e) {
            throw new RuntimeException("Could not transform: ", e);
        }

        return input;
    }

    public static final DeferredTransform intelliInputDeferred = new DeferredTransform(SignPictureTransformer.class.getName(), "com.tsoft_web.IntelliInput.asm.IntelliInputTransformer");

    public static final DeferredTransform[] deferredTransforms = {
            intelliInputDeferred,
    };

    @Override
    public DeferredTransform[] deferredTransforms() {
        return deferredTransforms;
    }

    @Override
    public Set<String> targetNames() {
        return this.transformerNames;
    }
}