package net.teamfruit.signpic.asm;

import net.teamfruit.signpic.asm.lib.INodeCoreTransformer;

public class SignPictureTransforms {
    public static final INodeCoreTransformer transformers[] = {
            new TileEntityTransform(),
            new GuiScreenBookTransform(),
            new GuiNewChatTransform(),
            new GuiScreenTransform(),
    };

    public static String getSimpleClassName(Object object) {
        return object.getClass().getSimpleName();
    }
}
