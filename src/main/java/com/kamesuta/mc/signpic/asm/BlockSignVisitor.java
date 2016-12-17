package com.kamesuta.mc.signpic.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.asm.lib.DescHelper;
import com.kamesuta.mc.signpic.asm.lib.MethodMatcher;

public class BlockSignVisitor extends ClassVisitor {
	private static class HookMethodVisitor extends MethodVisitor {
		public HookMethodVisitor(final MethodVisitor mv) {
			super(Opcodes.ASM5, mv);
		}

		@Override
		public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
			super.visitFieldInsn(opcode, "net/minecraft/tileentity/TileEntity", "INFINITE_EXTENT_AABB", desc);
			Log.info("BlockSign patch applied. You can see the sign from any angle");
		}
	}

	private final MethodMatcher matcher;

	public BlockSignVisitor(final String obfClassName, final ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
		this.matcher = new MethodMatcher(obfClassName, DescHelper.toDesc("net.minecraft.util.math.AxisAlignedBB", "net.minecraft.block.state.IBlockState", "net.minecraft.world.World", "net.minecraft.util.math.BlockPos"), "getCollisionBoundingBox", "func_180646_a");
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
		final MethodVisitor parent = super.visitMethod(access, name, desc, signature, exceptions);
		return this.matcher.match(name, desc) ? new HookMethodVisitor(parent) : parent;
	}
}