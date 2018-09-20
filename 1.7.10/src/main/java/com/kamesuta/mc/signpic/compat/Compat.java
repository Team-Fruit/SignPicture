package com.kamesuta.mc.signpic.compat;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;

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
			return FMLClientHandler.instance().getClient();
		}

		public static @Nonnull MinecraftServer getMinecraftServer() {
			return FMLCommonHandler.instance().getMinecraftServerInstance();
		}

		public static @Nonnull FontRenderer getFontRenderer() {
			return getMinecraft().fontRenderer;
		}

		public static @Nonnull World getWorld() {
			return getMinecraft().theWorld;
		}

		public static @Nonnull CompatGameSettings getSettings() {
			return new CompatGameSettings(getMinecraft().gameSettings);
		}
	}

	public static class CompatGameSettings {
		private final GameSettings settings;

		public CompatGameSettings(final GameSettings settings) {
			this.settings = settings;
		}

		public int getAnisotropicFiltering() {
			return this.settings.anisotropicFiltering;
		}
	}

	public static class CompatMovingObjectPosition {
		private final MovingObjectPosition movingPos;

		public CompatMovingObjectPosition(final MovingObjectPosition movingPos) {
			this.movingPos = movingPos;
		}

		public static @Nullable CompatMovingObjectPosition getMovingPos() {
			final MovingObjectPosition movingPos = CompatMinecraft.getMinecraft().objectMouseOver;
			return movingPos==null ? null : new CompatMovingObjectPosition(movingPos);
		}

		public @Nullable CompatBlockPos getMovingBlockPos() {
			return new CompatBlockPos(this.movingPos.blockX, this.movingPos.blockY, this.movingPos.blockZ);
		}

		public CompatEnumFacing getSideHit() {
			return CompatEnumFacing.fromFacingId(this.movingPos.sideHit);
		}
	}

	public static class CompatBlockPos {
		private final int x;
		private final int y;
		private final int z;

		public CompatBlockPos(final int x, final int y, final int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public int getX() {
			return this.x;
		}

		public int getY() {
			return this.y;
		}

		public int getZ() {
			return this.z;
		}

		public @Nullable TileEntity getTile() {
			return CompatMinecraft.getWorld().getTileEntity(this.x, this.y, this.z);
		}

		public @Nullable Block getBlock() {
			return CompatMinecraft.getWorld().getBlock(this.x, this.y, this.z);
		}

		public @Nonnull CompatBlockPos offset(final CompatEnumFacing facing) {
			if (facing==null)
				return this;
			return fromCoords(getX()+facing.offsetX, getY()+facing.offsetY, getZ()+facing.offsetZ);
		}

		public static @Nonnull CompatBlockPos fromCoords(final int xCoord, final int yCoord, final int zCoord) {
			return new CompatBlockPos(xCoord, yCoord, zCoord);
		}

		public static @Nonnull CompatBlockPos getTileEntityPos(@Nonnull final TileEntity tile) {
			return new CompatBlockPos(tile.xCoord, tile.yCoord, tile.zCoord);
		}

		public void setTileEntityPos(@Nonnull final TileEntity tile) {
			tile.xCoord = getX();
			tile.yCoord = getY();
			tile.zCoord = getZ();
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
			init(new CompatFMLInitializationEvent(event));
		}

		public static class CompatFMLPostInitializationEvent {
			private CompatFMLPostInitializationEvent(final FMLPostInitializationEvent event) {
			}
		}

		public abstract void postInit(final @Nonnull CompatFMLPostInitializationEvent event);

		public void postInit(final @Nonnull FMLPostInitializationEvent event) {
			postInit(new CompatFMLPostInitializationEvent(event));
		}
	}

	public static class CompatItemSignRendererRegistrar {
		public static void bindTileEntitySpecialRenderer(final Class<? extends TileEntity> tileEntityClass, final TileEntitySpecialRenderer specialRenderer) {
			ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, specialRenderer);
		}

		public static void registerPreInit(@Nonnull final CompatItemSignRenderer renderer) {

		}

		public static void registerInit(@Nonnull final CompatItemSignRenderer renderer) {
			MinecraftForgeClient.registerItemRenderer(Items.sign, renderer);
		}
	}

	public static abstract class CompatTileEntitySignRenderer extends TileEntitySignRenderer {
		public void renderBaseTileEntityAt(final @Nullable TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final int destroy) {
			super.renderTileEntityAt(tile, x, y, z, partialTicks);
		}

		public abstract void renderTileEntityAt(final @Nullable TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final int destroy);

		@Override
		public void renderTileEntityAt(final @Nullable TileEntitySign tile, final double x, final double y, final double z, final float partialTicks) {
			renderTileEntityAt(tile, x, y, z, partialTicks, -1);
		}

		@Override
		public void renderTileEntityAt(final @Nullable TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
			if (tile instanceof TileEntitySign)
				renderTileEntityAt((TileEntitySign) tile, x, y, z, partialTicks);
		}
	}

	public static class CompatWorld {
		private final World world;

		public CompatWorld(final World world) {
			this.world = world;
		}

		public static CompatWorld getWorld() {
			return new CompatWorld(CompatMinecraft.getWorld());
		}

		public World getWorldObj() {
			return this.world;
		}

		public int getLightFor(final CompatBlockPos pos) {
			return this.world.getLightBrightnessForSkyBlocks(pos.getX(), pos.getY(), pos.getZ(), 0);
		}

		public CompatBlock getBlock(final CompatBlockPos pos) {
			return new CompatBlock(this.world.getBlock(pos.getX(), pos.getY(), pos.getZ()));
		}
	}

	public static class CompatBlock {
		private final Block block;

		public CompatBlock(final Block block) {
			this.block = block;
		}

		public Block getBlockObj() {
			return this.block;
		}

		public boolean canPlaceBlockAt(final CompatWorld world, final CompatBlockPos pos) {
			return this.block.canPlaceBlockAt(world.getWorldObj(), pos.getX(), pos.getY(), pos.getX());
		}
	}

	public static class CompatGuiNewChat {
		public static int getChatWidth(final GuiNewChat chat) {
			return chat.func_146228_f();
		}

		public static float getChatScale(final GuiNewChat chat) {
			return chat.func_146244_h();
		}
	}

	public static class CompatTextComponent {
		public final IChatComponent component;

		public CompatTextComponent(final IChatComponent component) {
			this.component = component;
		}

		public @Nonnull List<ClickEvent> getLinksFromChat() {
			final List<ClickEvent> list = Lists.newLinkedList();
			getLinksFromChat0(list, this.component);
			return list;
		}

		private void getLinksFromChat0(final @Nonnull List<ClickEvent> list, final @Nonnull IChatComponent pchat) {
			final List<?> chats = pchat.getSiblings();
			for (final Object o : chats) {
				final IChatComponent chat = (IChatComponent) o;
				final ClickEvent ev = chat.getChatStyle().getChatClickEvent();
				if (ev!=null&&ev.getAction()==ClickEvent.Action.OPEN_URL)
					list.add(ev);
				getLinksFromChat0(list, chat);
			}
		}

		public CompatTextComponent setChatStyle(final CompatTextStyle style) {
			this.component.setChatStyle(style.style);
			return this;
		}

		public String getUnformattedText() {
			return this.component==null ? null : this.component.getUnformattedText();
		}

		public static CompatTextComponent jsonToComponent(final String json) {
			return new CompatTextComponent(IChatComponent.Serializer.func_150699_a(json));
		}

		public static CompatTextComponent fromText(final String text) {
			return new CompatTextComponent(new ChatComponentText(text));
		}

		public static CompatTextComponent fromTranslation(final String text, final Object... params) {
			return new CompatTextComponent(new ChatComponentTranslation(text, params));
		}

		public void sendClient() {
			CompatMinecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(this.component);
		}

		public void sendClientWithId(final int id) {
			CompatMinecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(this.component, id);
		}

		public void sendPlayer(final @Nonnull ICommandSender target) {
			target.addChatMessage(this.component);
		}

		public void sendBroadcast() {
			final ServerConfigurationManager sender = CompatMinecraft.getMinecraftServer().getConfigurationManager();
			sender.sendChatMsg(this.component);
		}
	}

	public static class CompatTextStyle {
		public final ChatStyle style;

		public CompatTextStyle(final ChatStyle style) {
			this.style = style;
		}

		public CompatTextStyle setColor(final CompatTextFormatting format) {
			this.style.setColor(format.format);
			return this;
		}

		public static CompatTextStyle create() {
			return new CompatTextStyle(new ChatStyle());
		}
	}

	public static class CompatChatLine {
		public static CompatTextComponent getChatComponent(final ChatLine line) {
			return new CompatTextComponent(line.func_151461_a());
		}
	}

	public static enum CompatTextFormatting {
		BLACK(EnumChatFormatting.BLACK),
		DARK_BLUE(EnumChatFormatting.DARK_BLUE),
		DARK_GREEN(EnumChatFormatting.DARK_GREEN),
		DARK_AQUA(EnumChatFormatting.DARK_AQUA),
		DARK_RED(EnumChatFormatting.DARK_RED),
		DARK_PURPLE(EnumChatFormatting.DARK_PURPLE),
		GOLD(EnumChatFormatting.GOLD),
		GRAY(EnumChatFormatting.GRAY),
		DARK_GRAY(EnumChatFormatting.DARK_GRAY),
		BLUE(EnumChatFormatting.BLUE),
		GREEN(EnumChatFormatting.GREEN),
		AQUA(EnumChatFormatting.AQUA),
		RED(EnumChatFormatting.RED),
		LIGHT_PURPLE(EnumChatFormatting.LIGHT_PURPLE),
		YELLOW(EnumChatFormatting.YELLOW),
		WHITE(EnumChatFormatting.WHITE),
		OBFUSCATED(EnumChatFormatting.OBFUSCATED),
		BOLD(EnumChatFormatting.BOLD),
		STRIKETHROUGH(EnumChatFormatting.STRIKETHROUGH),
		UNDERLINE(EnumChatFormatting.UNDERLINE),
		ITALIC(EnumChatFormatting.ITALIC),
		RESET(EnumChatFormatting.RESET),
		;

		public final EnumChatFormatting format;

		private CompatTextFormatting(final EnumChatFormatting format) {
			this.format = format;
		}
	}

	public static class CompatC12PacketUpdateSign {
		public static C12PacketUpdateSign create(final CompatBlockPos pos, final List<CompatTextComponent> clines) {
			final List<String> lines = Lists.transform(clines, input -> {
				return input==null ? null : input.component.getUnformattedText();
			});
			return new C12PacketUpdateSign(pos.getX(), pos.getY(), pos.getZ(), lines.toArray(new String[lines.size()]));
		}
	}

	public static class CompatC17PacketCustomPayload {
		public static C17PacketCustomPayload create(final String channel, final String data) {
			return new C17PacketCustomPayload(channel, data.getBytes(Charsets.UTF_8));
		}
	}

	public static abstract class CompatTileEntitySign {
		public static List<CompatTextComponent> getSignText(final TileEntitySign tile) {
			return Lists.transform(Lists.newArrayList(tile.signText), t -> CompatTextComponent.fromText(t));
		}

		public static void setSignText(final TileEntitySign tile, final List<CompatTextComponent> clines) {
			final List<String> lines = Lists.transform(clines, t -> t==null ? null : t.getUnformattedText());
			final Iterator<String> itr = lines.iterator();
			for (int i = 0; i<tile.signText.length; i++)
				tile.signText[i] = itr.hasNext() ? itr.next() : "";
		}
	}

	public static enum CompatEnumFacing {
		DOWN(0, -1, 0, EnumFacing.DOWN),
		UP(0, 1, 0, EnumFacing.UP),
		NORTH(0, 0, -1, EnumFacing.NORTH),
		SOUTH(0, 0, 1, EnumFacing.SOUTH),
		WEST(-1, 0, 0, EnumFacing.WEST),
		EAST(1, 0, 0, EnumFacing.EAST);

		public final int offsetX;
		public final int offsetY;
		public final int offsetZ;
		private final EnumFacing facing;

		private CompatEnumFacing(final int offsetX, final int offsetY, final int offsetZ, final EnumFacing facing) {
			this.offsetX = offsetX;
			this.offsetY = offsetY;
			this.offsetZ = offsetZ;
			this.facing = facing;
		}

		public int getIndex() {
			return ordinal();
		}

		public static @Nonnull CompatEnumFacing fromFacing(@Nonnull final EnumFacing facing) {
			for (final CompatEnumFacing cfacing : values())
				if (facing==cfacing.facing)
					return cfacing;
			return DOWN;
		}

		public static @Nonnull CompatEnumFacing fromFacingId(final int facing) {
			final CompatEnumFacing[] cfacings = values();
			if (0<=facing&&facing<cfacings.length)
				return cfacings[facing];
			return DOWN;
		}
	}

	public static class CompatTextureUtil {
		public static void processPixelValues(final int[] pixel, final int displayWidth, final int displayHeight) {
			TextureUtil.func_147953_a(pixel, displayWidth, displayHeight);
		}

		public static void allocateTextureImpl(final int id, final int miplevel, final int width, final int height, final float anisotropicFiltering) {
			TextureUtil.allocateTextureImpl(id, miplevel, width, height, anisotropicFiltering);
		}
	}

	public static class CompatGuiConfig extends GuiConfig {
		public CompatGuiConfig(final GuiScreen parentScreen, final List<CompatConfigElement> configElements, final String modID, final boolean allRequireWorldRestart, final boolean allRequireMcRestart, final String title) {
			super(parentScreen, CompatConfigElement.getConfigElements(configElements), modID, allRequireWorldRestart, allRequireMcRestart, GuiConfig.getAbridgedConfigPath(title));
		}
	}

	public static class CompatConfigElement {
		@SuppressWarnings("rawtypes")
		public final IConfigElement element;

		@SuppressWarnings("rawtypes")
		public CompatConfigElement(final IConfigElement element) {
			this.element = element;
		}

		@SuppressWarnings("rawtypes")
		public static List<IConfigElement> getConfigElements(final List<CompatConfigElement> elements) {
			return Lists.transform(elements, t -> t==null ? null : t.element);
		}

		public static CompatConfigElement fromCategory(final ConfigCategory category) {
			return new CompatConfigElement(new ConfigElement<>(category));
		}

		public static CompatConfigElement fromProperty(final Property prop) {
			return new CompatConfigElement(new ConfigElement<>(prop));
		}
	}

	public static class CompatRenderGameOverlayEvent {
		private final RenderGameOverlayEvent event;

		public CompatRenderGameOverlayEvent(final RenderGameOverlayEvent event) {
			this.event = event;
		}

		public static class Post extends CompatRenderGameOverlayEvent {
			public Post(final RenderGameOverlayEvent event) {
				super(event);
			}
		}

		public ScaledResolution getResolution() {
			return this.event.resolution;
		}

		public CompatElementType getType() {
			return CompatElementType.getType(this.event.type);
		}

		public float getPartialTicks() {
			return this.event.partialTicks;
		}

		public static enum CompatElementType {
			CHAT,
			OTHER,
			;

			public static CompatElementType getType(final ElementType type) {
				if (type==ElementType.CHAT)
					return CompatElementType.CHAT;
				return CompatElementType.OTHER;
			}
		}
	}

	public static abstract class CompatModGuiFactory implements IModGuiFactory {
		@Override
		public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
			return null;
		}

		@Override
		public RuntimeOptionGuiHandler getHandlerFor(final RuntimeOptionCategoryElement element) {
			return null;
		}
	}

	public static class CompatClientTickEvent {
	}
}
