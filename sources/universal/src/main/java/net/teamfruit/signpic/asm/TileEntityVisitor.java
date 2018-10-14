package net.teamfruit.signpic.asm;

import javax.annotation.Nullable;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import net.teamfruit.signpic.asm.lib.ClassName;
import net.teamfruit.signpic.asm.lib.DescHelper;
import net.teamfruit.signpic.asm.lib.MethodMatcher;

public class TileEntityVisitor extends ClassVisitor {
	private static class HookMethodVisitor extends MethodVisitor {
		public HookMethodVisitor(final @Nullable MethodVisitor mv) {
			super(Opcodes.ASM5, mv);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			/*
			 * 	 0  aload_0 [this]
			 *   1  instanceof net.minecraft.tileentity.TileEntitySign [3]
			 *   4  ifeq 11
			 *   7  getstatic net.minecraft.tileentity.TileEntity.INFINITE_EXTENT_AABB : net.minecraft.util.math.AxisAlignedBB [16]
			 *  10  areturn
			 *  11  aload_0 [this]
			 */
			visitVarInsn(Opcodes.ALOAD, 0);
			visitTypeInsn(Opcodes.INSTANCEOF, ClassName.of("net.minecraft.tileentity.TileEntitySign").getBytecodeName());
			final Label skipReturn = new Label();
			visitJumpInsn(Opcodes.IFEQ, skipReturn);
			visitFieldInsn(Opcodes.GETSTATIC, ClassName.of("net.minecraft.tileentity.TileEntity").getBytecodeName(), "INFINITE_EXTENT_AABB", DescHelper.toDesc(ClassName.of("net.minecraft.util.math.AxisAlignedBB")));
			visitInsn(Opcodes.ARETURN);
			visitLabel(skipReturn);
		}
	}

	private final MethodMatcher matcher;

	public TileEntityVisitor(final String obfClassName, final ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
		this.matcher = new MethodMatcher(ClassName.fromBytecodeName(obfClassName), DescHelper.toDescMethod(ClassName.of("net.minecraft.util.math.AxisAlignedBB")), ASMDeobfNames.TileEntityGetRenderBoundingBox);
	}

	@Override
	public @Nullable MethodVisitor visitMethod(final int access, final @Nullable String name, final @Nullable String desc, final @Nullable String signature, final @Nullable String[] exceptions) {
		final MethodVisitor parent = super.visitMethod(access, name, desc, signature, exceptions);
		if (name==null||desc==null)
			return parent;
		return this.matcher.match(name, desc) ? new HookMethodVisitor(parent) : parent;
	}
}