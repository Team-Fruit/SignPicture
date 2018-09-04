package com.kamesuta.mc.signpic.compat;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public class Compat {
	public static class CompatFMLDeobfuscatingRemapper {
		public static @Nonnull String mapMethodDesc(@Nonnull final String desc) {
			return FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
		}

		public static @Nonnull String mapFieldName(@Nonnull final String owner, @Nonnull final String name, @Nonnull final String desc) {
			return FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(owner, name, desc);
		}

		public static @Nonnull String unmap(@Nonnull final String typeName) {
			return FMLDeobfuscatingRemapper.INSTANCE.unmap(typeName);
		}

		public static @Nonnull String mapMethodName(@Nonnull final String owner, @Nonnull final String name, @Nonnull final String desc) {
			return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc);
		}
	}

	public static class CompatMinecraft {
		public static @Nonnull Minecraft getMinecraft() {
			return Minecraft.getMinecraft();
		}

		public static @Nonnull FontRenderer getFontRenderer() {
			return getMinecraft().fontRenderer;
		}

		public static @Nonnull World getWorld() {
			return getMinecraft().theWorld;
		}
	}

	public static class MovePos {
		public int x;
		public int y;
		public int z;

		public MovePos(final int x, final int y, final int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public static @Nullable MovingObjectPosition getMovingPos() {
			return CompatMinecraft.getMinecraft().objectMouseOver;
		}

		public static @Nullable MovePos getBlockPos() {
			final MovingObjectPosition movingPos = getMovingPos();
			if (movingPos!=null)
				return new MovePos(movingPos.blockX, movingPos.blockY, movingPos.blockZ);
			return null;
		}

		public static @Nullable TileEntity getTile() {
			final MovePos movePos = getBlockPos();
			if (movePos!=null)
				return CompatMinecraft.getWorld().getTileEntity(movePos.x, movePos.y, movePos.z);
			return null;
		}

		public static @Nullable Block getBlock() {
			final MovePos movePos = getBlockPos();
			if (movePos!=null)
				return CompatMinecraft.getWorld().getBlock(movePos.x, movePos.y, movePos.z);
			return null;
		}
	}

	public static class CompatSoundHandler {
		public static void playSound(final @Nonnull ResourceLocation location, final float volume) {
			CompatMinecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(location, volume));
		}
	}

	public static abstract class CompatProxy {
		public static class CompatFMLPreInitializationEvent {
			private final @Nonnull FMLPreInitializationEvent event;

			private CompatFMLPreInitializationEvent(final FMLPreInitializationEvent event) {
				this.event = event;
			}

			public Logger getModLog() {
				return this.event.getModLog();
			}

			public File getSuggestedConfigurationFile() {
				return this.event.getSuggestedConfigurationFile();
			}

			public File getSourceFile() {
				return this.event.getSourceFile();
			}
		}

		public abstract void preInit(final @Nonnull CompatFMLPreInitializationEvent event);

		public void preInit(final @Nonnull FMLPreInitializationEvent event) {
			preInit(new CompatFMLPreInitializationEvent(event));
		}

		public static class CompatFMLInitializationEvent {
			private CompatFMLInitializationEvent(final FMLInitializationEvent event) {
			}
		}

		public abstract void init(final @Nonnull CompatFMLInitializationEvent event);

		public void init(final @Nonnull FMLInitializationEvent event) {
		}

		public static class CompatFMLPostInitializationEvent {
			private CompatFMLPostInitializationEvent(final FMLPostInitializationEvent event) {
			}
		}

		public abstract void postInit(final @Nonnull CompatFMLPostInitializationEvent event);

		public void postInit(final @Nonnull FMLPostInitializationEvent event) {
		}
	}

	public static class CompatItemSignRenderer {
		protected IItemRenderer itemRenderer;
	}

	public static class CompatItemSignRendererRegistrar {
		public static void bindTileEntitySpecialRenderer(final Class<? extends TileEntity> tileEntityClass, final TileEntitySpecialRenderer specialRenderer) {
			ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, specialRenderer);
		}

		public static void registerPreInit(@Nonnull final CompatItemSignRenderer renderer) {

		}

		public static void registerInit(@Nonnull final CompatItemSignRenderer renderer) {
			MinecraftForgeClient.registerItemRenderer(Items.sign, renderer.itemRenderer);
		}
	}
}
