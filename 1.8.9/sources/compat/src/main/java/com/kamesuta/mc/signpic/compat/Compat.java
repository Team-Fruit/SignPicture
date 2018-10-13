package com.kamesuta.mc.signpic.compat;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Lists;
import com.kamesuta.mc.signpic.CoreInvoke;

import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
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
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
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
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

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

		public static @Nonnull CompatFontRenderer getFontRenderer() {
			return new CompatFontRenderer(getMinecraft().fontRendererObj);
		}

		public static @Nullable CompatWorld getWorld() {
			final World world = getMinecraft().theWorld;
			if (world!=null)
				return new CompatWorld(world);
			return null;
		}

		public static @Nullable CompatEntityPlayer getPlayer() {
			final EntityPlayer player = getMinecraft().thePlayer;
			if (player!=null)
				return new CompatEntityPlayer(player);
			return null;
		}

		public static @Nonnull CompatGameSettings getSettings() {
			return new CompatGameSettings(getMinecraft().gameSettings);
		}

		public static @Nullable CompatNetHandlerPlayClient getConnection() {
			final NetHandlerPlayClient connection = getMinecraft().getNetHandler();
			return connection!=null ? new CompatNetHandlerPlayClient(connection) : null;
		}
	}

	public static class CompatFontRenderer {
		private final FontRenderer font;

		public CompatFontRenderer(final FontRenderer font) {
			this.font = font;
		}

		public int drawString(final String msg, final float x, final float y, final int color, final boolean shadow) {
			return this.font.drawString(msg, x, y, color, shadow);
		}

		public int drawString(final String msg, final float x, final float y, final int color) {
			return drawString(msg, x, y, color, false);
		}

		public int drawStringWithShadow(final String msg, final float x, final float y, final int color) {
			return drawString(msg, x, y, color, true);
		}

		public int getStringWidth(final @Nullable String s) {
			return this.font.getStringWidth(s);
		}

		public int getStringWidthWithoutFormattingCodes(final @Nullable String s) {
			return getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(s));
		}

		public FontRenderer getFontRendererObj() {
			return this.font;
		}
	}

	public static class CompatGameSettings {
		public CompatGameSettings(final GameSettings settings) {
		}

		public int getAnisotropicFiltering() {
			return 0;
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
			final BlockPos pos = this.movingPos.getBlockPos();
			if (pos!=null)
				return new CompatBlockPos(pos);
			return null;
		}

		public CompatEnumFacing getSideHit() {
			return CompatEnumFacing.fromFacing(this.movingPos.sideHit);
		}
	}

	public static class CompatBlockPos {
		public final @Nonnull BlockPos pos;

		public CompatBlockPos(final @Nonnull BlockPos pos) {
			Validate.notNull(pos, "MovePos needs position");
			this.pos = pos;
		}

		public int getX() {
			return this.pos.getX();
		}

		public int getY() {
			return this.pos.getY();
		}

		public int getZ() {
			return this.pos.getZ();
		}

		public @Nonnull CompatBlockPos offset(final CompatEnumFacing facing) {
			if (facing==null)
				return this;
			return fromCoords(getX()+facing.offsetX, getY()+facing.offsetY, getZ()+facing.offsetZ);
		}

		public static @Nonnull CompatBlockPos fromCoords(final int xCoord, final int yCoord, final int zCoord) {
			return new CompatBlockPos(new BlockPos(xCoord, yCoord, zCoord));
		}

		public static @Nonnull CompatBlockPos getTileEntityPos(@Nonnull final TileEntity tile) {
			return new CompatBlockPos(tile.getPos());
		}

		public void setTileEntityPos(@Nonnull final TileEntity tile) {
			tile.setPos(this.pos);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime*result+this.pos.hashCode();
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
			if (!this.pos.equals(other.pos))
				return false;
			return true;
		}
	}

	public static class CompatSoundHandler {
		public static void playSound(final @Nonnull ResourceLocation location, final float volume) {
			CompatMinecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(location, volume));
		}
	}

	public static class CompatItemSignRendererRegistrar {
		public static <T extends TileEntity> void bindTileEntitySpecialRenderer(final Class<T> tileEntityClass, final TileEntitySpecialRenderer<? super T> specialRenderer) {
			ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, specialRenderer);
		}

		public static void registerPreInit(@Nonnull final CompatItemSignRenderer renderer) {
			ModelLoader.setCustomModelResourceLocation(CompatItems.SIGN, 0, renderer.modelResourceLocation);
		}

		public static void registerInit(@Nonnull final CompatItemSignRenderer renderer) {

		}
	}

	public static class CompatKeyRegistrar {
		public static void registerKeyBinding(final KeyBinding key) {
			ClientRegistry.registerKeyBinding(key);
		}
	}

	public static abstract class CompatTileEntitySignRenderer extends TileEntitySignRenderer {
		public void renderBaseTileEntityAt(final @Nullable TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final int destroy, final float alpha) {
			super.renderTileEntityAt(tile, x, y, z, partialTicks, destroy);
		}

		public abstract void renderTileEntityAtCompat(final @Nullable TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final int destroy, final float alpha);

		@Override
		public void renderTileEntityAt(final @Nullable TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final int destroy) {
			renderTileEntityAtCompat(tile, x, y, z, partialTicks, destroy, 1f);
		}
	}

	public static class CompatTileEntityRendererDispatcher {
		public static void renderTileEntityAt(final @Nullable TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final int destroy, final float alpha) {
			TileEntityRendererDispatcher.instance.renderTileEntityAt(tile, x, y, z, partialTicks, destroy);
		}
	}

	public static class CompatEntityPlayer {
		private final EntityPlayer player;

		public CompatEntityPlayer(@Nonnull final EntityPlayer player) {
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

		public CompatWorld(@Nonnull final World world) {
			this.world = world;
		}

		public World getWorldObj() {
			return this.world;
		}

		public int getLightFor(final CompatBlockPos pos) {
			return this.world.getLightFor(EnumSkyBlock.SKY, pos.pos);
		}

		public CompatBlockState getBlockState(final CompatBlockPos pos) {
			return new CompatBlockState(this.world.getBlockState(pos.pos));
		}

		public @Nullable TileEntity getTileEntity(final CompatBlockPos pos) {
			return this.world.getTileEntity(pos.pos);
		}
	}

	public static class CompatBlockState {
		private final IBlockState blockstate;

		public CompatBlockState(final IBlockState blockstate) {
			this.blockstate = blockstate;
		}

		public IBlockState getBlockStateObj() {
			return this.blockstate;
		}

		public CompatBlock getBlock() {
			return new CompatBlock(this.blockstate.getBlock());
		}

		public Material getMaterial() {
			return this.blockstate.getBlock().getMaterial();
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
			return this.block.canPlaceBlockAt(world.getWorldObj(), pos.pos);
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
			return chat.getChatWidth();
		}

		public static float getChatScale(final GuiNewChat chat) {
			return chat.getChatScale();
		}
	}

	public static class CompatTextComponent {
		public static CompatTextComponent blank = fromText("");

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
			return new CompatTextComponent(IChatComponent.Serializer.jsonToComponent(json));
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
			return new CompatTextComponent(line.getChatComponent());
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
		public final Packet<? extends INetHandler> packet;

		public CompatPacket(final Packet<? extends INetHandler> packet) {
			this.packet = packet;
		}
	}

	public static class CompatC12PacketUpdateSign extends CompatPacket {
		public CompatC12PacketUpdateSign(final C12PacketUpdateSign packet) {
			super(packet);
		}

		public static CompatC12PacketUpdateSign create(final CompatBlockPos pos, final List<CompatTextComponent> clines) {
			final List<IChatComponent> lines = Lists.transform(clines, input -> {
				return input==null ? null : input.component;
			});
			return new CompatC12PacketUpdateSign(new C12PacketUpdateSign(pos.pos, lines.toArray(new IChatComponent[lines.size()])));
		}
	}

	public static class CompatC17PacketCustomPayload extends CompatPacket {
		public CompatC17PacketCustomPayload(final C17PacketCustomPayload packet) {
			super(packet);
		}

		public static CompatC17PacketCustomPayload create(final String channel, final String data) {
			return new CompatC17PacketCustomPayload(new C17PacketCustomPayload(channel, new PacketBuffer(Unpooled.buffer()).writeString(data)));
		}
	}

	public static class CompatTileEntitySign {
		public static List<CompatTextComponent> getSignText(final TileEntitySign tile) {
			return Lists.transform(Lists.newArrayList(tile.signText), t -> new CompatTextComponent(t));
		}

		public static void setSignText(final TileEntitySign tile, final List<CompatTextComponent> clines) {
			final List<IChatComponent> lines = Lists.transform(clines, t -> t==null ? null : t.component);
			final Iterator<IChatComponent> itr = lines.iterator();
			for (int i = 0; i<tile.signText.length; i++)
				tile.signText[i] = itr.hasNext() ? itr.next() : CompatTextComponent.blank.component;
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
			TextureUtil.processPixelValues(pixel, displayWidth, displayHeight);
		}

		public static void allocateTextureImpl(final int id, final int miplevel, final int width, final int height, final float anisotropicFiltering) {
			TextureUtil.allocateTextureImpl(id, miplevel, width, height);
		}
	}

	public static class CompatGuiConfig extends GuiConfig {
		public CompatGuiConfig(final GuiScreen parentScreen, final List<CompatConfigElement> configElements, final String modID, final boolean allRequireWorldRestart, final boolean allRequireMcRestart, final String title) {
			super(parentScreen, CompatConfigElement.getConfigElements(configElements), modID, allRequireWorldRestart, allRequireMcRestart, GuiConfig.getAbridgedConfigPath(title));
		}
	}

	public static class CompatConfigElement {
		public final IConfigElement element;

		public CompatConfigElement(final IConfigElement element) {
			this.element = element;
		}

		public static List<IConfigElement> getConfigElements(final List<CompatConfigElement> elements) {
			return Lists.transform(elements, t -> t==null ? null : t.element);
		}

		public static CompatConfigElement fromCategory(final ConfigCategory category) {
			return new CompatConfigElement(new ConfigElement(category));
		}

		public static CompatConfigElement fromProperty(final Property prop) {
			return new CompatConfigElement(new ConfigElement(prop));
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

		@Override
		public @Nullable Class<? extends GuiScreen> mainConfigGuiClass() {
			return mainConfigGuiClassCompat();
		}

		public abstract @Nullable Class<? extends GuiScreen> mainConfigGuiClassCompat();

		public abstract GuiScreen createConfigGuiCompat(GuiScreen parentScreen);
	}

	public static class CompatCommand {
		public static @Nonnull String getCommandName(final ICommand command) {
			return command.getCommandName();
		}

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
		public @Nullable List<String> addTabCompletionOptions(final @Nullable ICommandSender sender, final @Nullable String[] args, final @Nullable BlockPos pos) {
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
		public @Nullable List<String> addTabCompletionOptions(final @Nullable ICommandSender sender, final @Nullable String[] args, final @Nullable BlockPos pos) {
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

		@Override
		public int compareTo(final @Nullable ICommand command) {
			return compare(command);
		}
	}

	public static class CompatCommandBase {
		public static String buildString(final ICommandSender sender, final String[] args, final int startPos) {
			return CommandBase.buildString(args, startPos);
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

	public static class CompatModel {
	}

	public static class CompatBakedModel {
	}
}
