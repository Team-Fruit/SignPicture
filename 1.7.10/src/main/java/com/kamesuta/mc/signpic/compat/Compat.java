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
import com.kamesuta.mc.signpic.CoreInvoke;

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
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
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
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
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

		public static @Nonnull CompatWorld getWorld() {
			return new CompatWorld(getMinecraft().theWorld);
		}

		public static @Nonnull CompatEntityPlayer getPlayer() {
			return new CompatEntityPlayer(getMinecraft().thePlayer);
		}

		public static @Nonnull CompatGameSettings getSettings() {
			return new CompatGameSettings(getMinecraft().gameSettings);
		}

		public static @Nullable CompatNetHandlerPlayClient getConnection() {
			final NetHandlerPlayClient connection = getMinecraft().getNetHandler();
			return connection!=null ? new CompatNetHandlerPlayClient(connection) : null;
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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime*result+this.x;
			result = prime*result+this.y;
			result = prime*result+this.z;
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this==obj)
				return true;
			if (obj==null)
				return false;
			if (!(obj instanceof CompatBlockPos))
				return false;
			final CompatBlockPos other = (CompatBlockPos) obj;
			if (this.x!=other.x)
				return false;
			if (this.y!=other.y)
				return false;
			if (this.z!=other.z)
				return false;
			return true;
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
			MinecraftForgeClient.registerItemRenderer(CompatItems.SIGN, renderer);
		}
	}

	public static class CompatKeyRegistrar {
		public static void registerKeyBinding(final KeyBinding key) {
			ClientRegistry.registerKeyBinding(key);
		}
	}

	public static abstract class CompatTileEntitySignRenderer extends TileEntitySignRenderer {
		public void renderBaseTileEntityAt(final @Nullable TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final int destroy) {
			super.renderTileEntityAt(tile, x, y, z, partialTicks);
		}

		public abstract void renderTileEntityAtCompat(final @Nullable TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final int destroy);

		@Override
		public void renderTileEntityAt(final @Nullable TileEntitySign tile, final double x, final double y, final double z, final float partialTicks) {
			renderTileEntityAtCompat(tile, x, y, z, partialTicks, -1);
		}

		@Override
		public void renderTileEntityAt(final @Nullable TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
			if (tile instanceof TileEntitySign)
				renderTileEntityAt((TileEntitySign) tile, x, y, z, partialTicks);
		}
	}

	public static class CompatEntityPlayer {
		private final EntityPlayer player;

		public CompatEntityPlayer(final EntityPlayer player) {
			this.player = player;
		}

		public EntityPlayer getPlayerObj() {
			return this.player;
		}

		public @Nullable ItemStack getHeldItemMainhand() {
			return this.player.getCurrentEquippedItem();
		}

		public @Nullable ItemStack getHeldItemOffhand() {
			return null;
		}
	}

	public static class CompatWorld {
		private final World world;

		public CompatWorld(final World world) {
			this.world = world;
		}

		public World getWorldObj() {
			return this.world;
		}

		public int getLightFor(final CompatBlockPos pos) {
			return this.world.getLightBrightnessForSkyBlocks(pos.getX(), pos.getY(), pos.getZ(), 0);
		}

		public CompatBlockState getBlockState(final CompatBlockPos pos) {
			return new CompatBlockState(this.world.getBlock(pos.getX(), pos.getY(), pos.getZ()));
		}

		public @Nullable TileEntity getTileEntity(final CompatBlockPos pos) {
			return this.world.getTileEntity(pos.getX(), pos.getY(), pos.getZ());
		}
	}

	public static class CompatBlockState {
		private final Block blockstate;

		public CompatBlockState(final Block blockstate) {
			this.blockstate = blockstate;
		}

		public CompatBlock getBlock() {
			return new CompatBlock(this.blockstate);
		}

		public Material getMaterial() {
			return this.blockstate.getMaterial();
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

	public static class CompatBlocks {
		public static final Block STANDING_SIGN = Blocks.standing_sign;
		public static final Block WALL_SIGN = Blocks.wall_sign;
	}

	public static class CompatItems {
		public static final Item SIGN = Items.sign;
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

		public @Nonnull List<CompatClickEvent> getLinksFromChat() {
			final List<CompatClickEvent> list = Lists.newLinkedList();
			getLinksFromChat0(list, this.component);
			return list;
		}

		private void getLinksFromChat0(final @Nonnull List<CompatClickEvent> list, final @Nonnull IChatComponent pchat) {
			final List<?> chats = pchat.getSiblings();
			for (final Object o : chats) {
				final IChatComponent chat = (IChatComponent) o;
				final ClickEvent ev = chat.getChatStyle().getChatClickEvent();
				if (ev!=null&&ev.getAction()==ClickEvent.Action.OPEN_URL)
					list.add(new CompatClickEvent(ev));
				getLinksFromChat0(list, chat);
			}
		}

		public CompatTextComponent setChatStyle(final CompatTextStyle style) {
			this.component.setChatStyle(style.style);
			return this;
		}

		public String getUnformattedText() {
			return this.component.getUnformattedText();
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

	public static class CompatClickEvent {
		private final ClickEvent event;

		public CompatClickEvent(final ClickEvent event) {
			this.event = event;
		}

		public String getValue() {
			return this.event.getValue();
		}

		public static CompatClickEvent create(final CompatAction action, final String text) {
			return new CompatClickEvent(new ClickEvent(action.action, text));
		}

		public static enum CompatAction {
			OPEN_URL(ClickEvent.Action.OPEN_URL),
			OPEN_FILE(ClickEvent.Action.OPEN_FILE),
			RUN_COMMAND(ClickEvent.Action.RUN_COMMAND),
			SUGGEST_COMMAND(ClickEvent.Action.SUGGEST_COMMAND),
			;

			public final ClickEvent.Action action;

			private CompatAction(final ClickEvent.Action action) {
				this.action = action;
			}
		}
	}

	public static class CompatHoverEvent {
		public final HoverEvent event;

		public CompatHoverEvent(final HoverEvent event) {
			this.event = event;
		}

		public static CompatHoverEvent create(final CompatAction action, final CompatTextComponent text) {
			return new CompatHoverEvent(new HoverEvent(action.action, text.component));
		}

		public static enum CompatAction {
			SHOW_TEXT(HoverEvent.Action.SHOW_TEXT),
			SHOW_ACHIEVEMENT(HoverEvent.Action.SHOW_ACHIEVEMENT),
			SHOW_ITEM(HoverEvent.Action.SHOW_ITEM),
			;

			public final HoverEvent.Action action;

			private CompatAction(final HoverEvent.Action action) {
				this.action = action;
			}
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

		public CompatTextStyle setChatHoverEvent(final CompatHoverEvent event) {
			this.style.setChatHoverEvent(event.event);
			return this;
		}

		public CompatTextStyle setChatClickEvent(final CompatClickEvent event) {
			this.style.setChatClickEvent(event.event);
			return this;
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

		@Override
		public String toString() {
			return this.format.toString();
		}
	}

	public static class CompatNetHandlerPlayClient {
		private final NetHandlerPlayClient connection;

		public CompatNetHandlerPlayClient(final NetHandlerPlayClient connection) {
			this.connection = connection;
		}

		public void sendPacket(final CompatPacket packet) {
			this.connection.addToSendQueue(packet.packet);
		}
	}

	public static class CompatPacket {
		public final Packet packet;

		public CompatPacket(final Packet packet) {
			this.packet = packet;
		}
	}

	public static class CompatC12PacketUpdateSign extends CompatPacket {
		public CompatC12PacketUpdateSign(final C12PacketUpdateSign packet) {
			super(packet);
		}

		public static CompatC12PacketUpdateSign create(final CompatBlockPos pos, final List<CompatTextComponent> clines) {
			final List<String> lines = Lists.transform(clines, input -> {
				return input==null ? null : input.component.getUnformattedText();
			});
			return new CompatC12PacketUpdateSign(new C12PacketUpdateSign(pos.getX(), pos.getY(), pos.getZ(), lines.toArray(new String[lines.size()])));
		}
	}

	public static class CompatC17PacketCustomPayload extends CompatPacket {
		public CompatC17PacketCustomPayload(final C17PacketCustomPayload packet) {
			super(packet);
		}

		public static CompatC17PacketCustomPayload create(final String channel, final String data) {
			return new CompatC17PacketCustomPayload(new C17PacketCustomPayload(channel, data.getBytes(Charsets.UTF_8)));
		}
	}

	public static class CompatTileEntitySign {
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
		public static final DynamicTexture missingTexture = TextureUtil.missingTexture;

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

	public static class CompatCommand {
		public static @Nonnull String getCommandName(final ICommand command) {
			return command.getCommandName();
		}

		@SuppressWarnings("unchecked")
		public static @Nullable List<String> getCommandAliases(final ICommand command) {
			return command.getCommandAliases();
		}

		public static @Nonnull String getCommandUsage(final ICommand command, final @Nullable ICommandSender sender) {
			return command.getCommandUsage(sender);
		}
	}

	public static class CompatCommandSender {
		public static boolean canCommandSenderUseCommand(final ICommandSender sender, final int level, final String name) {
			return sender.canCommandSenderUseCommand(level, name);
		}
	}

	public static abstract class CompatRootCommand extends CommandBase implements ICommand {
		@Override
		public @Nullable List<String> addTabCompletionOptions(final @Nullable ICommandSender sender, final @Nullable String[] args) {
			return addTabCompletionOptionCompat(sender, args);
		}

		public @Nullable abstract List<String> addTabCompletionOptionCompat(final @Nullable ICommandSender sender, final @Nullable String[] args);

		@Override
		public @Nonnull String getCommandName() {
			return getCommandNameCompat();
		}

		public abstract @Nonnull String getCommandNameCompat();

		@Override
		public @Nullable List<String> getCommandAliases() {
			return getCommandAliasesCompat();
		}

		public abstract @Nullable List<String> getCommandAliasesCompat();

		@Override
		public @Nonnull String getCommandUsage(final @Nullable ICommandSender sender) {
			return getCommandUsageCompat(sender);
		}

		public abstract @Nonnull String getCommandUsageCompat(final @Nullable ICommandSender sender);

		@Override
		public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
			processCommandCompat(sender, args);
		}

		public abstract void processCommandCompat(final @Nullable ICommandSender sender, final @Nullable String[] args) throws CommandException;
	}

	public static abstract class CompatSubCommand implements ICommand {
		@Override
		public @Nullable List<String> addTabCompletionOptions(final @Nullable ICommandSender sender, final @Nullable String[] args) {
			return addTabCompletionOptionCompat(sender, args);
		}

		public @Nullable abstract List<String> addTabCompletionOptionCompat(final @Nullable ICommandSender sender, final @Nullable String[] args);

		@Override
		public @Nonnull String getCommandName() {
			return getCommandNameCompat();
		}

		public abstract @Nonnull String getCommandNameCompat();

		@Override
		public @Nullable List<String> getCommandAliases() {
			return getCommandAliasesCompat();
		}

		public abstract @Nullable List<String> getCommandAliasesCompat();

		@Override
		public @Nonnull String getCommandUsage(final @Nullable ICommandSender sender) {
			return getCommandUsageCompat(sender);
		}

		public abstract @Nonnull String getCommandUsageCompat(final @Nullable ICommandSender sender);

		@Override
		public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
			processCommandCompat(sender, args);
		}

		public abstract void processCommandCompat(final @Nullable ICommandSender sender, final @Nullable String[] args) throws CommandException;

		@Override
		public boolean canCommandSenderUseCommand(final ICommandSender sender) {
			return canCommandSenderUseCommandCompat(sender);
		}

		public abstract boolean canCommandSenderUseCommandCompat(final @Nullable ICommandSender sender);

		public abstract int compare(final @Nullable ICommand command);

		public int compareTo(final @Nullable ICommand command) {
			return compare(command);
		}

		@Override
		public int compareTo(final @Nullable Object command) {
			if (command instanceof ICommand)
				return compare((ICommand) command);
			return 0;
		}
	}

	public static class CompatCommandBase {
		public static String buildString(final ICommandSender sender, final String[] args, final int startPos) {
			return CommandBase.func_82360_a(sender, args, startPos);
		}
	}

	public static class CompatMathHelper {
		public static int floor_float(final float value) {
			return MathHelper.floor_float(value);
		}

		public static int floor_double(final double value) {
			return MathHelper.floor_double(value);
		}
	}

	public static class CompatChatRender {
		public static abstract class CompatPicChatLine extends ChatLine {
			public static final @Nonnull CompatTextComponent dummytext = CompatTextComponent.fromText("");

			public CompatPicChatLine(final int updateCounterCreated, final int lineId) {
				super(updateCounterCreated, dummytext.component, lineId);
			}

			@CoreInvoke
			public @Nullable IChatComponent onClicked(final @Nonnull GuiNewChat chat, final int x) {
				final CompatTextComponent component = onClickedCompat(chat, x);
				if (component!=null)
					return component.component;
				return null;
			}

			public abstract @Nullable CompatTextComponent onClickedCompat(final @Nonnull GuiNewChat chat, final int x);
		}
	}

	public static class CompatI18n {
		public static String format(final String format, final Object... args) {
			return I18n.format(format, args);
		}

		public static String translateToLocal(final String text) {
			return StatCollector.translateToLocal(text);
		}
	}
}
