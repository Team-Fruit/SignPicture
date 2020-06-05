package net.teamfruit.emojicord.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.teamfruit.emojicord.asm.lib.ASMValidate;
import net.teamfruit.emojicord.asm.lib.ClassName;
import net.teamfruit.emojicord.asm.lib.DescHelper;
import net.teamfruit.emojicord.asm.lib.INodeTreeTransformer;
import net.teamfruit.emojicord.asm.lib.MethodMatcher;
import net.teamfruit.emojicord.asm.lib.VisitorHelper;
import net.teamfruit.emojicord.compat.CompatBaseVersion;
import net.teamfruit.emojicord.compat.CompatVersion;

public class GuiScreenInputEventTransform implements INodeTreeTransformer {
	@Override
	public ClassName getClassName() {
		if (CompatVersion.version().older(CompatBaseVersion.V13))
			return ClassName.of("net.minecraft.client.gui.GuiScreen");
		else
			return ClassName.of("net.minecraft.client.gui.screen.Screen");
	}

	@Override
	public ClassNode apply(final ClassNode node) {
		final ASMValidate validator = ASMValidate.create(getSimpleName());
		validator.test("handleInput", CompatVersion.version().older(CompatBaseVersion.V7));
		validator.test("handleInput.onMouseInput", CompatVersion.version().older(CompatBaseVersion.V7));
		validator.test("handleInput.onKeyboardInput", CompatVersion.version().older(CompatBaseVersion.V7));

		if (CompatVersion.version().older(CompatBaseVersion.V7)) {
			final MethodMatcher matcher = new MethodMatcher(getClassName(), DescHelper.toDescMethod(void.class), ASMDeobfNames.GuiScreenHandleInput);
			node.methods.stream().filter(matcher).forEach(method -> {
				{
					/*
					  0  invokestatic org.lwjgl.input.Mouse.isCreated() : boolean [15]
					  3  ifeq 26
					  6  goto 20
					
					  9  aload_0 [this]
					 10  invokestatic net.teamfruit.emojicord.compat.MouseInputEvent$Pre.onMouseInput(net.minecraft.client.gui.GuiScreen) : boolean [21]
					 13  ifne 20
					
					 16  aload_0 [this]
					 17  invokevirtual net.teamfruit.emojicord.asm.GuiScreenTest.handleMouseInput() : void [27]
					 20  invokestatic org.lwjgl.input.Mouse.next() : boolean [30]
					 23  ifne 9
					 26  invokestatic org.lwjgl.input.Keyboard.isCreated() : boolean [33]
					 29  ifeq 52
					 32  goto 46
					
					 35  aload_0 [this]
					 36  invokestatic net.teamfruit.emojicord.compat.KeyboardInputEvent$Pre.onKeyboardInput(net.minecraft.client.gui.GuiScreen) : boolean [36]
					 39  ifne 46
					
					 42  aload_0 [this]
					 43  invokevirtual net.teamfruit.emojicord.asm.GuiScreenTest.handleKeyboardInput() : void [41]
					 46  invokestatic org.lwjgl.input.Keyboard.next() : boolean [44]
					 49  ifne 35
					 52  return
					*/
					{
						final MethodMatcher matcher0 = new MethodMatcher(getClassName(), DescHelper.toDescMethod(void.class), ASMDeobfNames.GuiScreenHandleMouseInput);
						VisitorHelper.stream(method.instructions).filter(matcher0.insnMatcher()).forEach(marker -> {
							final LabelNode label = new LabelNode();
							{
								final InsnList insertion = new InsnList();
								insertion.add(new VarInsnNode(Opcodes.ALOAD, 0));
								insertion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClassName.of("net.teamfruit.emojicord.compat.MouseInputEvent$Pre").getBytecodeName(), "onMouseInput", DescHelper.toDescMethod(boolean.class, getClassName()), false));
								insertion.add(new JumpInsnNode(Opcodes.IFNE, label));
								method.instructions.insertBefore(marker.getPrevious(), insertion);
							}
							{
								final InsnList insertion = new InsnList();
								insertion.add(label);
								method.instructions.insert(marker, insertion);
							}
							validator.check("handleInput.onMouseInput");
						});
					}
					{
						final MethodMatcher matcher0 = new MethodMatcher(getClassName(), DescHelper.toDescMethod(void.class), ASMDeobfNames.GuiScreenHandleKeyboardInput);
						VisitorHelper.stream(method.instructions).filter(matcher0.insnMatcher()).forEach(marker -> {
							final LabelNode label = new LabelNode();
							{
								final InsnList insertion = new InsnList();
								insertion.add(new VarInsnNode(Opcodes.ALOAD, 0));
								insertion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClassName.of("net.teamfruit.emojicord.compat.KeyboardInputEvent$Pre").getBytecodeName(), "onKeyboardInput", DescHelper.toDescMethod(boolean.class, getClassName()), false));
								insertion.add(new JumpInsnNode(Opcodes.IFNE, label));
								method.instructions.insertBefore(marker.getPrevious(), insertion);
							}
							{
								final InsnList insertion = new InsnList();
								insertion.add(label);
								method.instructions.insert(marker, insertion);
							}
							validator.check("handleInput.onKeyboardInput");
						});
					}
					validator.check("handleInput");
				}
			});
		}

		validator.validate();
		return node;
	}
}