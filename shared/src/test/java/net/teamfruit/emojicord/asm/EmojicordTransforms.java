package net.teamfruit.emojicord.asm;

import net.teamfruit.emojicord.asm.lib.INodeTreeTransformer;

public class EmojicordTransforms {
    public static final INodeTreeTransformer transformers[] = {
            new SendChatMessageTransform(),
            new GuiTextFieldTransform(),
            new FontRendererTransform(),
            new GuiScreenInputEventTransform(),
    };

    public static String getSimpleClassName(Object object) {
        return object.getClass().getSimpleName();
    }
}
