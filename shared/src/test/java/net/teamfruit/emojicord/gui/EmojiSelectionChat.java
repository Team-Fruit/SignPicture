package net.teamfruit.emojicord.gui;

#if MC_12_LATER
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraftforge.fml.VersionChecker;
#elif MC_7_LATER
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.common.ForgeVersion;
#else
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.teamfruit.emojicord.compat.VersionChecker;
#endif

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import net.teamfruit.emojicord.EmojicordConfig;
import net.teamfruit.emojicord.OSUtils;
import net.teamfruit.emojicord.Reference;
import net.teamfruit.emojicord.compat.Compat;
import net.teamfruit.emojicord.compat.Compat.CompatI18n;
import net.teamfruit.emojicord.compat.Compat.CompatVersionChecker;
import net.teamfruit.emojicord.compat.CompatGui;
import net.teamfruit.emojicord.emoji.*;
import net.teamfruit.emojicord.util.MathHelper;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class EmojiSelectionChat implements IChatOverlay {
	public final #if MC_12_LATER ChatScreen #else GuiChat #endif chatScreen;
	public final #if MC_12_LATER TextFieldWidget #else GuiTextField #endif inputField;
	public final FontRenderer font;
	public int mouseX, mouseY;
	private EmojiSelectionList selectionList;
	private final Rectangle2d emojiButton;

	private final List<String> faces;
	private boolean onButton;
	private String face = ":smile:";
	private boolean enabled;

	public EmojiSelectionChat(final #if MC_12_LATER ChatScreen #else GuiChat #endif chatScreen) {
		this.chatScreen = chatScreen;
		this.font = Compat.getMinecraft(). #if MC_10 fontRendererObj #else fontRenderer #endif ;
		this.inputField = chatScreen.inputField;
		this.emojiButton = new Rectangle2d(this.chatScreen.width - 13, this.chatScreen.height - 13, 10, 10);

		this.enabled = !StandardEmojiIdPicker.instance.categories.isEmpty();

		this.faces = Lists.newArrayList();
		{
			final List<PickerGroup> standardCategories = StandardEmojiIdPicker.instance.categories;
			final List<PickerItem> people = standardCategories.stream().filter(e -> "PEOPLE".equalsIgnoreCase(e.name)).limit(1).flatMap(e -> e.items.stream()).collect(Collectors.toList());
			for (final PickerItem item : people) {
				this.faces.add(item.name);
				if (StringUtils.equals(":sleeping:", item.name))
					break;
			}
		}
	}

	@Override
	public boolean onDraw() {
		if (this.selectionList != null)
			this.selectionList.onDraw();
		else {
			final boolean onButtonLast = this.onButton;
			this.onButton = this.emojiButton.contains(this.mouseX, this.mouseY);
			if (this.onButton && !onButtonLast && !this.faces.isEmpty())
				this.face = this.faces.get(RandomUtils.nextInt(0, this.faces.size()));
		}

		if (this.enabled)
			this.font.drawString(this.face, this.emojiButton.getX(), this.emojiButton.getY(), 0xFFFFFF);
		else {
			this.font.drawString("\u2717", this.emojiButton.getX(), this.emojiButton.getY(), 0xFF0000);
			if (this.emojiButton.contains(this.mouseX, this.mouseY))
				#if MC_12_LATER chatScreen.renderTooltip #elif MC_7_LATER chatScreen.drawHoveringText #else chatScreen.func_146283_a #endif (Arrays.asList(CompatI18n.format("emojicord.chat.error.disabled").split("\\\\n|\n")), this.mouseX, this.mouseY);
		}
		return false;
	}

	@Override
	public boolean onMouseClicked(final int button) {
		if (this.selectionList == null && this.emojiButton.contains(this.mouseX, this.mouseY)) {
			if (this.enabled)
				show();
			return true;
		}
		return this.selectionList != null && this.selectionList.onMouseClicked(button);
	}

	@Override
	public boolean onMouseReleased(final int button) {
		return this.selectionList != null && this.selectionList.onMouseReleased(button);
	}

	@Override
	public boolean onMouseScroll(final double scrollDelta) {
		return this.selectionList != null && this.selectionList.onMouseScroll(scrollDelta);
	}

	@Override
	public boolean onMouseInput(final int mouseX, final int mouseY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		return false;
	}

	@Override
	public boolean onCharTyped(final char typed, final int keycode) {
		return this.selectionList != null && this.selectionList.onCharTyped(typed, keycode);
	}

	@Override
	public boolean onKeyPressed(final int keycode) {
		return this.selectionList != null && this.selectionList.onKeyPressed(keycode);
	}

	@Override
	public void onTick() {
		if (this.selectionList != null)
			this.selectionList.onTick();
	}

	public void show() {
		final int width = 8 + 14 * 10 - 4 + 8;
		final int height = width;

		//EmojiFrequently.instance.load(Locations.instance.getEmojicordDirectory());
		final List<PickerGroup> standardCategories = StandardEmojiIdPicker.instance.categories;
		final List<PickerGroup> frequently = Lists.newArrayList(EmojiFrequently.instance.getGroup());
		final List<PickerGroup> discordCategories = DiscordEmojiIdDictionary.instance.pickerGroups;
		final List<PickerGroup> categories = Stream.of(frequently.stream(), discordCategories.stream(), standardCategories.stream()).flatMap(stream -> stream).collect(Collectors.toList());
		final List<Pair<String, PickerGroup>> buttonCategories = ((Supplier<List<Pair<String, PickerGroup>>>) () -> {
			return Arrays.asList(
					Pair.of("<:frequently:630652521911943191>", frequently.stream().findFirst().map(e -> e.setTranslation("emojicord.gui.picker.standard.frequently")).orElse(null)),
					Pair.of("<:custom:630652548331864085>", discordCategories.stream().findFirst().orElse(null)),
					Pair.of("<:people:630652609807515658>", standardCategories.stream().filter(e -> "PEOPLE".equalsIgnoreCase(e.name)).findFirst().map(e -> e.setTranslation("emojicord.gui.picker.standard.people")).orElse(null)),
					Pair.of("<:nature:630652621497171979>", standardCategories.stream().filter(e -> "NATURE".equalsIgnoreCase(e.name)).findFirst().map(e -> e.setTranslation("emojicord.gui.picker.standard.nature")).orElse(null)),
					Pair.of("<:food:630652671510183956>", standardCategories.stream().filter(e -> "FOOD".equalsIgnoreCase(e.name)).findFirst().map(e -> e.setTranslation("emojicord.gui.picker.standard.food")).orElse(null)),
					Pair.of("<:activities:630652683480465408>", standardCategories.stream().filter(e -> "ACTIVITIES".equalsIgnoreCase(e.name)).findFirst().map(e -> e.setTranslation("emojicord.gui.picker.standard.activities")).orElse(null)),
					Pair.of("<:travel:630652707631267860>", standardCategories.stream().filter(e -> "TRAVEL".equalsIgnoreCase(e.name)).findFirst().map(e -> e.setTranslation("emojicord.gui.picker.standard.travel")).orElse(null)),
					Pair.of("<:objects:630652735083249664>", standardCategories.stream().filter(e -> "OBJECTS".equalsIgnoreCase(e.name)).findFirst().map(e -> e.setTranslation("emojicord.gui.picker.standard.objects")).orElse(null)),
					Pair.of("<:symbols:630652764955082752>", standardCategories.stream().filter(e -> "SYMBOLS".equalsIgnoreCase(e.name)).findFirst().map(e -> e.setTranslation("emojicord.gui.picker.standard.symbols")).orElse(null)),
					Pair.of("<:flags:630652781866385490>", standardCategories.stream().filter(e -> "FLAGS".equalsIgnoreCase(e.name)).findFirst().map(e -> e.setTranslation("emojicord.gui.picker.standard.flags")).orElse(null)));
		}).get();
		this.selectionList = new EmojiSelectionList(this.chatScreen.width, this.chatScreen.height - 12, width, height, categories, buttonCategories);
	}

	public void hide() {
		this.selectionList = null;
	}

	private class EmojiSelectionList implements IChatOverlay {
		private final Rectangle2d rectangle;
		private final Rectangle2d rectTop;
		private final Rectangle2d rectInput;
		private final Rectangle2d rectInputField;
		private final Rectangle2d rectInputButton;
		private final Rectangle2d rectBottom;
		private final Rectangle2d rectMain;
		private final Rectangle2d rectColorButton;
		private final Rectangle2d rectColor;
		private final Rectangle2d rectSettingButton;
		private final Rectangle2d rectUpdate;

		private final #if MC_7_LATER && !MC_12_LATER ForgeVersion.CheckResult #else VersionChecker.CheckResult #endif update;

		private final List<PickerGroup> baseCategories;
		private final List<Pair<String, PickerGroup>> buttonCategories;
		private List<PickerGroup> categories;

		private #if MC_12_LATER TextFieldWidget #else GuiTextField #endif searchField;
		private float scrollY;
		private int scrollY0;
		private int selectedGroupIndex = -1;
		private int selectedIndex = -1;
		private PickerItem selecting;
		private PickerGroup selectingGroupButton = null;
		private boolean colorShown;
		private int selectingColor = -1;
		private int selectedColor = -1;
		private boolean mouseDown;

		private EmojiSelectionList(final int posX, final int posY, final int width, final int height, final List<PickerGroup> categories, final List<Pair<String, PickerGroup>> buttonCategories) {
			this.rectangle = new Rectangle2d(posX - 3 - width, posY - 4 - height, width + 1, height + 1);
			this.selectedColor = EmojicordConfig.PICKER.skinTone.get();

			final int marginTop = 6;
			final int paddingTop = 2;
			final int bannerTopHeight = 12 + (marginTop + paddingTop) * 2;
			final int bannerBottomHeight = 16;
			final int colorButtonWidth = 12;
			final int settingButtonWidth = 12;
			final int searchButtonWidth = 12;
			this.rectTop = new Rectangle2d(this.rectangle.getX(), this.rectangle.getY(), this.rectangle.getWidth(), bannerTopHeight);
			this.rectInput = this.rectTop.inner(marginTop, marginTop, 4 + 2 + colorButtonWidth + settingButtonWidth, marginTop);
			this.rectInputField = this.rectInput.inner(paddingTop + 2, paddingTop + 2, searchButtonWidth, 0);
			this.rectInputButton = new Rectangle2d(this.rectInput.getX() + this.rectInput.getWidth() - searchButtonWidth, this.rectInput.getY(), searchButtonWidth, this.rectInput.getHeight());
			this.rectBottom = new Rectangle2d(this.rectangle.getX(), this.rectangle.getY() + this.rectangle.getHeight() - bannerBottomHeight, this.rectangle.getWidth(), bannerBottomHeight);
			this.rectMain = new Rectangle2d(this.rectangle.getX(), this.rectTop.getY() + this.rectTop.getHeight(), this.rectangle.getWidth(), this.rectBottom.getY() - (this.rectTop.getY() + this.rectTop.getHeight()));
			this.rectSettingButton = new Rectangle2d(this.rectTop.getX() + this.rectTop.getWidth() - colorButtonWidth - 4, this.rectInput.getY(), colorButtonWidth, this.rectInput.getHeight()).inner(0, 2, 0, 2);
			this.rectColorButton = new Rectangle2d(this.rectSettingButton.getX() - colorButtonWidth, this.rectInput.getY(), colorButtonWidth, this.rectInput.getHeight()).inner(0, 2, 0, 2);
			this.rectColor = new Rectangle2d(this.rectColorButton.getX(), this.rectColorButton.getY(), this.rectColorButton.getWidth(), 14 * 6);
			this.rectUpdate = new Rectangle2d(this.rectangle.getX(), this.rectangle.getY() - 15, this.rectangle.getWidth(), 15);
			this.baseCategories = categories;
			this.buttonCategories = buttonCategories;
			this.categories = categories;

			select(0, 0);
			this.searchField = new #if MC_12_LATER TextFieldWidget #else GuiTextField #endif ( #if MC_7_LATER && !MC_12_LATER -1, #endif EmojiSelectionChat.this.font, this.rectInputField.getX(), this.rectInputField.getY(), this.rectInputField.getWidth(), this.rectInputField.getHeight() #if MC_12_LATER , "Search Field" #endif );
			this.searchField.setMaxStringLength(256);
			this.searchField.setEnableBackgroundDrawing(false);
			this.searchField. #if MC_12_LATER setFocused2 #else setFocused #endif (true);
			//this.inputField.setText("");
			//this.inputField.setTextFormatter(this::formatMessage);
			//this.inputField.func_212954_a(this::func_212997_a);

			onTextChanged();

			if (EmojicordConfig.UPDATE.showUpdate.get())
				this.update = CompatVersionChecker.getResult(Reference.MODID);
			else
				this.update = null;
		}

		@Override
		public boolean onDraw() {
			IChatOverlay.fill(this.rectangle, 0xFFFFFFFF);

			final int row = 10;
			final int emojiSize = 10;
			final int emojiMargin = 2;

			final int paddingLeft = 8;
			final int spanX = 14;
			final int spanY = 14;
			final int titleSpanY = 4;
			final int titleSpanY2 = 12;

			final int scrollbarWidth = 6;

			{
				this.scrollY = MathHelper.lerp(this.scrollY, this.scrollY0, .5f);

				final int posX = this.rectMain.getX() + paddingLeft;
				int posY = this.rectMain.getY() + (int) this.scrollY;
				this.selecting = null;
				int groupIndex = 0;
				for (final PickerGroup group : this.categories) {
					posY += titleSpanY;
					if (this.rectMain.contains(this.rectMain.getX(), posY) || this.rectMain.contains(this.rectMain.getX() + this.rectMain.getWidth(), posY + titleSpanY2))
						EmojiSelectionChat.this.font.drawString(group.getTranslation(), posX, posY, 0xFFABABAB);
					posY += titleSpanY2;
					int index = 0;
					for (final PickerItem item : group.items) {
						final int ix = index % row;
						final int iy = index / row;
						final int px = posX + ix * spanX;
						final int py = posY + iy * spanY;
						final Rectangle2d rect = new Rectangle2d(px - emojiMargin, py - emojiMargin, emojiSize + emojiMargin * 2, emojiSize + emojiMargin * 2);
						if (this.rectMain.overlap(rect)) {
							if (this.selectedGroupIndex == groupIndex && this.selectedIndex == index)
								IChatOverlay.fill(rect, 0xFFEBEBEB);
							String tone = "";
							if (this.selectedColor > 0)
								if (EmojiId.StandardEmojiId.fromAlias(StringUtils.strip(item.name, ":") + ":skin-tone-" + this.selectedColor) != null)
									tone = ":skin-tone-" + this.selectedColor + ":";
							EmojiSelectionChat.this.font.drawString(item.name + tone, rect.getX() + emojiMargin, rect.getY() + emojiMargin, 0xFFFFFFFF);
							if (rect.contains(EmojiSelectionChat.this.mouseX, EmojiSelectionChat.this.mouseY)) {
								this.selecting = item;
								if (!(this.selectedGroupIndex == groupIndex && this.selectedIndex == index)) {
									this.selectedGroupIndex = groupIndex;
									this.selectedIndex = index;
									if (!StringUtils.isNotEmpty(this.searchField.getText()))
										CompatGui.CompatTextFieldWidget.setSuggestion(this.searchField, StringUtils.replace(item.text, ":", "§r:"));
								}
							}
						}
						++index;
					}
					posY += (group.items.size() / row + (group.items.size() % row > 0 ? 1 : 0)) * spanY;
					++groupIndex;
					if (posY > this.rectMain.getY() + this.rectMain.getHeight())
						break;
				}
				final int height = this.categories.stream().mapToInt(e -> titleSpanY + titleSpanY2 + ((e.items.size() - 1) / row + 1) * spanY).sum();
				this.scrollY0 = height <= this.rectMain.getHeight() ? 0 : -MathHelper.clamp(-this.scrollY0, 0, height - this.rectMain.getHeight());

				final Rectangle2d rectScroll0 = new Rectangle2d(this.rectMain.getX() + this.rectMain.getWidth() - scrollbarWidth, this.rectMain.getY(), scrollbarWidth, this.rectMain.getHeight());
				final Rectangle2d rectScroll = rectScroll0.inner(1, 2, 1, 2);
				final int scrollbarHeight = Math.min(rectScroll.getHeight(), rectScroll.getHeight() * rectScroll.getHeight() / height);
				IChatOverlay.fill(rectScroll, 0xFFEBEBEB);
				if (height > this.rectMain.getHeight()) {
					IChatOverlay.fill(new Rectangle2d(
							rectScroll.getX(),
							rectScroll.getY() + (int) MathHelper.lerp(0, rectScroll.getHeight() - scrollbarHeight, -this.scrollY / (height - this.rectMain.getHeight())),
							rectScroll.getWidth(),
							scrollbarHeight), 0xFFABABAB);
					if (rectScroll0.contains(EmojiSelectionChat.this.mouseX, EmojiSelectionChat.this.mouseY))
						if (this.mouseDown)
							this.scrollY0 = (int) -MathHelper.lerp(0, height - this.rectMain.getHeight(), ((float) EmojiSelectionChat.this.mouseY - rectScroll.getY()) / rectScroll.getHeight());
				}
			}

			IChatOverlay.fill(this.rectTop, 0xFFFFFFFF);
			IChatOverlay.fill(this.rectInput, 0xFFABABAB);
			IChatOverlay.fill(this.rectBottom, 0xFFFFFFFF);
			IChatOverlay.fill(this.rectTop.getX(), this.rectTop.getY() + this.rectTop.getHeight(), this.rectTop.getX() + this.rectTop.getWidth(), this.rectTop.getY() + this.rectTop.getHeight() + 1, 0xFFEBEBEB);
			IChatOverlay.fill(this.rectBottom.getX(), this.rectBottom.getY() - 1, this.rectBottom.getX() + this.rectBottom.getWidth(), this.rectBottom.getY(), 0xFFEBEBEB);

			final int colorOffset = 1;
			EmojiSelectionChat.this.font.drawString(":ok_hand:" + (this.selectedColor > 0 ? ":skin-tone-" + this.selectedColor + ":" : ""), this.rectColorButton.getX() + colorOffset, this.rectColorButton.getY() + colorOffset, 0xFFFFFFFF);
			if (this.colorShown) {
				IChatOverlay.fill(this.rectColor, 0xFFEFEFEF);

				final int posY = this.rectColor.getY();
				this.selectingColor = -1;

				final int[] colorIndex = Stream.of(IntStream.of(this.selectedColor), IntStream.rangeClosed(0, 5).filter(e -> e != this.selectedColor)).flatMapToInt(stream -> stream).toArray();

				for (int pindex = 0; pindex < 6; pindex++) {
					final int py = posY + pindex * spanY;
					final int index = colorIndex[pindex];
					final Rectangle2d rect = new Rectangle2d(this.rectColor.getX(), py, this.rectColor.getWidth(), spanY);
					EmojiSelectionChat.this.font.drawString(":ok_hand:" + (index > 0 ? ":skin-tone-" + index + ":" : ""), rect.getX() + colorOffset, rect.getY() + colorOffset, 0xFFFFFFFF);
					if (rect.contains(EmojiSelectionChat.this.mouseX, EmojiSelectionChat.this.mouseY))
						this.selectingColor = index;
				}
			}
			EmojiSelectionChat.this.font.drawString(":gear:", this.rectSettingButton.getX() + colorOffset, this.rectSettingButton.getY() + colorOffset, 0xFFFFFFFF);

			{
				PickerGroup currentGroup = null;
				{
					int height = 0;
					for (final PickerGroup group : this.categories) {
						height += titleSpanY + titleSpanY2 + ((group.items.size() - 1) / row + 1) * spanY;
						if (height + this.scrollY0 > 0) {
							currentGroup = group;
							break;
						}
					}
				}

				final int posX = this.rectBottom.getX() + paddingLeft;
				final int posY = this.rectBottom.getY() + 2;
				int index = 0;
				this.selectingGroupButton = null;
				for (final Pair<String, PickerGroup> group : this.buttonCategories) {
					final int px = posX + index * spanX;
					final Rectangle2d rect = new Rectangle2d(px, posY, emojiSize, emojiSize).outer(emojiMargin, emojiMargin, emojiMargin, emojiMargin);
					{
						if (this.selectingGroupButton != null && this.selectingGroupButton == group.getRight())
							IChatOverlay.fill(rect, 0xFFEBEBEB);
						if (currentGroup == group.getRight())
							IChatOverlay.fill(new Rectangle2d(rect.getX(), rect.getY() + rect.getHeight(), rect.getWidth(), 2), 0xFF7289DA);
						EmojiSelectionChat.this.font.drawString(group.getLeft(), rect.getX() + emojiMargin, rect.getY() + emojiMargin, 0xFFFFFFFF);
						if (rect.contains(EmojiSelectionChat.this.mouseX, EmojiSelectionChat.this.mouseY))
							//if (!(this.selectingGroupButton==index))
							this.selectingGroupButton = group.getRight();
					}
					++index;
				}
			}

			final float partialTicks = 0.066f;
			this.searchField. #if MC_12_LATER render(EmojiSelectionChat.this.mouseX, EmojiSelectionChat.this.mouseY, partialTicks) #else drawTextBox() #endif ;
			EmojiSelectionChat.this.font.drawString(this.searchField.getText().isEmpty() ? "<:search:631021534705877012>" : "<:close:631021519295741973>", this.rectInputButton.getX() + 1, this.rectInputButton.getY() + 3, 0xFFFFFFFF);

			if (this.update != null && this.update.status == #if MC_7_LATER && !MC_12_LATER ForgeVersion #else VersionChecker #endif .Status.OUTDATED) {
				IChatOverlay.fill(this.rectUpdate, 0xFF36393F);
				final String text1 = CompatI18n.format("emojicord.gui.picker.update", this.update.target);
				EmojiSelectionChat.this.font.drawString(text1, this.rectUpdate.getX() + 2, this.rectUpdate.getY() + 3, 0xFFFFFFFF);
			}

			return false;
		}

		@Override
		public boolean onMouseClicked(final int button) {
			if (button == 0)
				this.mouseDown = true;

			if (this.update != null && this.update.status == #if MC_7_LATER && !MC_12_LATER ForgeVersion #else VersionChecker #endif .Status.OUTDATED)
				if (this.rectUpdate.contains(EmojiSelectionChat.this.mouseX, EmojiSelectionChat.this.mouseY)) {
					if (this.update.url != null)
						OSUtils.getOSType().openURI(this.update.url);
					return true;
				}

			if (!this.rectangle.contains(EmojiSelectionChat.this.mouseX, EmojiSelectionChat.this.mouseY)) {
				hide();
				return true;
			}
			if (this.rectSettingButton.contains(EmojiSelectionChat.this.mouseX, EmojiSelectionChat.this.mouseY)) {
				if (EmojiSettings.showSettings != null) {
					hide();
					EmojiSettings.showSettings.run();
				}
				return true;
			}
			if (!this.colorShown) {
				if (this.rectColorButton.contains(EmojiSelectionChat.this.mouseX, EmojiSelectionChat.this.mouseY)) {
					this.colorShown = true;
					return true;
				}
			} else {
				if (this.rectColor.contains(EmojiSelectionChat.this.mouseX, EmojiSelectionChat.this.mouseY)) {
					if (this.selectingColor >= 0)
						this.selectedColor = this.selectingColor;
					EmojicordConfig.PICKER.skinTone.set(this.selectedColor);
					EmojicordConfig.spec.save();
				}
				this.colorShown = false;
				return true;
			}
			if (this.rectInputButton.contains(EmojiSelectionChat.this.mouseX, EmojiSelectionChat.this.mouseY)) {
				if (!this.searchField.getText().isEmpty())
					this.searchField.setText("");
				this.searchField. #if MC_12_LATER setFocused2 #else setFocused #endif (true);
				onTextChanged();
				return true;
			}
			#if MC_12_OR_LATER boolean b = #endif
			this.searchField.mouseClicked(mouseX, mouseY, button);
			#if !MC_12_OR_LATER
			boolean b = this.searchField.xPosition <= mouseX && mouseX <= this.searchField.xPosition + this.searchField.width
					&& this.searchField.yPosition <= mouseY && mouseY <= this.searchField.yPosition + this.searchField.height;
			#endif
			if (b)
				return true;
			if (this.rectMain.contains(EmojiSelectionChat.this.mouseX, EmojiSelectionChat.this.mouseY) && this.selecting != null) {
				String tone = "";
				if (this.selecting.id instanceof EmojiId.StandardEmojiId)
					if (this.selectedColor > 0)
						if (EmojiId.StandardEmojiId.fromAlias(StringUtils.strip(this.selecting.name, ":") + ":skin-tone-" + this.selectedColor) != null)
							tone = ":skin-tone-" + this.selectedColor + ":";
				EmojiSelectionChat.this.inputField.writeText(this.selecting.name + tone + " ");
				hide();
				return true;
			}
			if (this.rectBottom.contains(EmojiSelectionChat.this.mouseX, EmojiSelectionChat.this.mouseY) && this.selectingGroupButton != null) {
				{
					final int row = 10;
					final int spanY = 14;
					final int titleSpanY = 4;
					final int titleSpanY2 = 12;

					{
						int height = 0;
						for (final PickerGroup group : this.categories) {
							if (group == this.selectingGroupButton) {
								this.scrollY0 = -height;
								break;
							}
							height += titleSpanY + titleSpanY2 + ((group.items.size() - 1) / row + 1) * spanY;
						}
					}
				}
				return true;
			}
			return true;
		}

		@Override
		public boolean onMouseReleased(final int button) {
			if (button == 0)
				this.mouseDown = false;
			return true;
		}

		@Override
		public boolean onMouseScroll(final double scrollDelta) {
			if (this.rectMain.contains(EmojiSelectionChat.this.mouseX, EmojiSelectionChat.this.mouseY)) {
				this.scrollY0 += scrollDelta * 10;
				return true;
			}
			return false;
		}

		private void onTextChanged() {
			this.selectedGroupIndex = -1;
			this.selectedIndex = -1;
			if (StringUtils.isNotEmpty(this.searchField.getText())) {
				CompatGui.CompatTextFieldWidget.setSuggestion(this.searchField, "");
				final String searchText = StringUtils.strip(this.searchField.getText(), ":");
				final List<PickerItem> candidates = this.baseCategories.stream().flatMap(e -> e.items.stream()).filter(e -> e.alias.stream().anyMatch(s -> s.contains(searchText))).collect(Collectors.toList());
				this.categories = Lists.newArrayList(new PickerGroup("Search", candidates));
			} else {
				CompatGui.CompatTextFieldWidget.setSuggestion(this.searchField, CompatI18n.format("emojicord.gui.picker.search"));
				this.categories = this.baseCategories;
			}
		}

		@Override
		public boolean onCharTyped(final char typed, final int keycode) {
			if (this.searchField. #if MC_12_LATER charTyped #else textboxKeyTyped #endif (typed, keycode)) {
				onTextChanged();
				return true;
			}
			return false;
		}

		@Override
		public boolean onKeyPressed(final int keycode) {
			if (#if MC_12_LATER this.searchField.keyPressed(keycode, EmojiSelectionChat.this.mouseX, EmojiSelectionChat.this.mouseY) #else true #endif ) {
				onTextChanged();
				return true;
			}
			return false;
		}

		@Override
		public void onTick() {
			this.searchField. #if MC_12_LATER tick #else updateCursorCounter #endif ();
		}

		public void select(final int groupIndex, final int index) {
			if (this.categories.isEmpty())
				return;
			this.selectedGroupIndex = MathHelper.clamp(groupIndex, 0, this.categories.size() - 1);
			final List<PickerItem> list = this.categories.get(this.selectedGroupIndex).items;
			if (list.isEmpty())
				return;
			this.selectedIndex = MathHelper.clamp(index, 0, list.size() - 1);
		}
	}
}
