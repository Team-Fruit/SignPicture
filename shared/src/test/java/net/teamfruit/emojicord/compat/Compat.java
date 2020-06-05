package net.teamfruit.emojicord.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

#if MC_12_LATER
#if MC_14_LATER
import net.minecraft.client.renderer.texture.TextureUtil;
#else
import com.mojang.blaze3d.platform.TextureUtil;
#endif
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraftforge.versions.forge.ForgeVersion;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.versions.mcp.MCPVersion;
#else
import net.minecraftforge.common.ForgeVersion;
import net.minecraft.client.renderer.texture.TextureUtil;
#endif

#if MC_12_LATER
#elif MC_7_LATER
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
#else
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
#endif

public class Compat {
	public static @Nonnull Minecraft getMinecraft() {
		#if MC_12_LATER
		return Minecraft.getInstance();
		#else
		return FMLClientHandler.instance().getClient();
		#endif
	}

	public static class CompatI18n {
		public static String format(final String format, final Object... args) {
			return net.minecraft.client.resources.I18n.format(format, args);
		}

		public static boolean hasKey(final String key) {
			#if MC_7_LATER
			return net.minecraft.client.resources.I18n.hasKey(key);
			#else
			return net.minecraft.util.StatCollector.canTranslate(key);
			#endif
		}

		@SuppressWarnings("deprecation")
		public static String translateToLocal(final String text) {
			#if MC_12_LATER
			return hasKey(text) ? format(text) : text;
			#elif MC_7_LATER
			return net.minecraft.util.text.translation.I18n.translateToLocal(text);
			#else
			return net.minecraft.util.StatCollector.translateToLocal(text);
			#endif
		}
	}

	public static class CompatTexture {
		public static void uploadTexture(Supplier<Integer> genTextureId, final InputStream image) throws IOException {
			final boolean blur = true;
			final boolean clamp = false;

			#if MC_12_LATER
			try (
					NativeImage nativeimage = NativeImage.read(image);
			) {
				TextureUtil.prepareImage(genTextureId.get(), 0, nativeimage.getWidth(), nativeimage.getHeight());
				nativeimage.uploadTextureSub(0, 0, 0, 0, 0, nativeimage.getWidth(), nativeimage.getHeight(), blur, clamp, false #if MC_14_LATER , true #endif );
			}
			#else
			final BufferedImage bufferedimage = #if MC_7_LATER TextureUtil.readBufferedImage #else ImageIO.read #endif (image);
			uploadTexture(genTextureId, bufferedimage);
			#endif
		}

		public static void uploadTexture(Supplier<Integer> genTextureId, final BufferedImage bufferedimage) throws IOException {
			final boolean blur = true;
			final boolean clamp = false;

			#if MC_12_LATER
			final int width = bufferedimage.getWidth();
			final int height = bufferedimage.getHeight();
			try (
					NativeImage nativeimage = new NativeImage(NativeImage.PixelFormat.RGBA, width, height, false);
			) {
				final int[] pixels = new int[width*height];
				bufferedimage.getRGB(0, 0, width, height, pixels, 0, width);

				for (int y = 0; y<height; y++)
					for (int x = 0; x<width; x++) {
						final int argb = pixels[y*width+x];
						final int alpha = 0xFF&argb>>24;
						final int red = 0xFF&argb>>16;
						final int green = 0xFF&argb>>8;
						final int blue = 0xFF&argb>>0;
						final int abgr = alpha<<24|blue<<16|green<<8|red<<0;

						// ABGR
						nativeimage.setPixelRGBA(x, y, abgr);
					}

				TextureUtil.prepareImage(genTextureId.get(), 0, nativeimage.getWidth(), nativeimage.getHeight());
				nativeimage.uploadTextureSub(0, 0, 0, 0, 0, nativeimage.getWidth(), nativeimage.getHeight(), blur, clamp, false #if MC_14_LATER , true #endif );
			}
			#else
			if (bufferedimage != null)
				TextureUtil.uploadTextureImageAllocate(genTextureId.get(), bufferedimage, blur, clamp);
			#endif
		}
	}

	public static class CompatBufferBuilder {
		public CompatBufferBuilder() {
		}
	}

	public static class CompatVersionChecker {
		public static void startVersionCheck(final String modId, final String modVersion, final String updateURL) {
		}

		public static #if (MC_7_LATER && !MC_12_LATER) ForgeVersion.CheckResult #else VersionChecker.CheckResult #endif getResult(final String modId) {
			#if MC_12_LATER
			final IModInfo container = ModList.get().getModContainerById(modId)
					.map(e -> e.getModInfo())
					.orElse(null);
			return VersionChecker.getResult(container);
			#elif MC_7_LATER
			final ModContainer container = Loader.instance().getIndexedModList().get(modId);
			return ForgeVersion.getResult(container);
			#else
			return VersionChecker.getResult();
			#endif
		}
	}

	public static class CompatMinecraftVersion {
		public static String getMinecraftVersion() {
			#if MC_12_LATER
			return MCPVersion.getMCVersion();
			#else
			return MinecraftForge.MC_VERSION;
			#endif
		}

		public static String getForgeVersion() {
			return ForgeVersion.getVersion();
		}
	}
}
