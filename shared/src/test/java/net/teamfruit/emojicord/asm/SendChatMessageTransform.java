package net.teamfruit.emojicord.asm;

import java.util.function.Supplier;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.teamfruit.emojicord.asm.lib.ASMValidate;
import net.teamfruit.emojicord.asm.lib.ClassName;
import net.teamfruit.emojicord.asm.lib.DescHelper;
import net.teamfruit.emojicord.asm.lib.INodeTreeTransformer;
import net.teamfruit.emojicord.asm.lib.MethodMatcher;
import net.teamfruit.emojicord.compat.CompatBaseVersion;
import net.teamfruit.emojicord.compat.CompatVersion;

public class SendChatMessageTransform implements INodeTreeTransformer {
	@Override
	public ClassName getClassName() {
		if (CompatVersion.version().older(CompatBaseVersion.V7))
			return ClassName.of("net.minecraft.client.gui.GuiChat");
		if (CompatVersion.version().older(CompatBaseVersion.V13))
			return ClassName.of("net.minecraft.client.gui.GuiScreen");
		else
			return ClassName.of("net.minecraft.client.gui.screen.Screen");
	}

	@Override
	public ClassNode apply(final ClassNode node) {
		final ASMValidate validator = ASMValidate.create(getSimpleName());
		validator.test("sendChatMessage", CompatVersion.version().older(CompatBaseVersion.V10));

		if (CompatVersion.version().older(CompatBaseVersion.V10)) {
			final MethodMatcher matcher = ((Supplier<MethodMatcher>) () -> {
				if (CompatVersion.version().older(CompatBaseVersion.V7))
					return new MethodMatcher(getClassName(), DescHelper.toDescMethod(void.class, ClassName.of("java.lang.String")), ASMDeobfNames.GuiChatSendChatMessage);
				else
					return new MethodMatcher(getClassName(), DescHelper.toDescMethod(void.class, ClassName.of("java.lang.String"), boolean.class), ASMDeobfNames.GuiScreenSendChatMessage);
			}).get();
			node.methods.stream().filter(matcher).forEach(method -> {
				{
					/*
					 0  aload_1 [text]
					 1  invokestatic net.teamfruit.emojicord.compat.ClientChatEvent.onClientSendMessage(java.lang.String) : java.lang.String [61]
					 4  astore_1 [text]
					*/
					final InsnList insertion = new InsnList();
					insertion.add(new VarInsnNode(Opcodes.ALOAD, 1));
					insertion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClassName.of("net.teamfruit.emojicord.compat.ClientChatEvent").getBytecodeName(), "onClientSendMessage", DescHelper.toDescMethod(ClassName.of("java.lang.String"), ClassName.of("java.lang.String")), false));
					insertion.add(new VarInsnNode(Opcodes.ASTORE, 1));
					method.instructions.insert(insertion);
					validator.check("sendChatMessage");
				}
			});
		}

		validator.validate();
		return node;
	}
}