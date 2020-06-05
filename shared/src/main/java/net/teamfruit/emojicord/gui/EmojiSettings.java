package net.teamfruit.emojicord.gui;

#if MC_12_LATER
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
#else
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
#endif

import net.minecraft.client.gui.FontRenderer;
import net.teamfruit.emojicord.*;
import net.teamfruit.emojicord.compat.Compat;
import net.teamfruit.emojicord.compat.Compat.CompatI18n;
import net.teamfruit.emojicord.compat.Compat.CompatVersionChecker;
import net.teamfruit.emojicord.compat.OpenGL;
import net.teamfruit.emojicord.emoji.DiscordEmojiIdDictionary;
import net.teamfruit.emojicord.emoji.Models.EmojiDiscordList;
import net.teamfruit.emojicord.util.MathHelper;

#if MC_12_LATER
import net.minecraftforge.fml.VersionChecker;
#elif !MC_7_LATER
import net.teamfruit.emojicord.compat.VersionChecker;
#else
import net.minecraftforge.common.ForgeVersion;
#endif

public class EmojiSettings implements IChatOverlay {
	public static Runnable showSettings;
	public static Runnable onWindowActive;

	public final #if MC_12_LATER ChatScreen #else GuiChat #endif chatScreen;
	public final #if MC_12_LATER TextFieldWidget #else GuiTextField #endif inputField;
	public final FontRenderer font;
	public int mouseX, mouseY;
	private EmojiSettingMenu settingMenu;
	private final Rectangle2d rectScreen;
	private EmojiAdding addGui;

	private interface EmojiAdding {
		boolean isClosing();

		default boolean isApplyPreferred() {
			return false;
		}

		default void onWindowFocus() {
		}

		void onApplying();

		String getDescription();

		String getClosingDescription();

		String getApplyPreferredDescription();

		default void onOK() {
		}

		default void onCancel() {
		}
	}

	public EmojiSettings(final #if MC_12_LATER ChatScreen #else GuiChat #endif chatScreen) {
		this.chatScreen = chatScreen;
		this.font = Compat.getMinecraft(). #if MC_10 fontRendererObj #else fontRenderer #endif;
		this.inputField = chatScreen.inputField;
		this.rectScreen = new Rectangle2d(0, 0, this.chatScreen.width, this.chatScreen.height);

		showSettings = this::show;
	}

	@Override
	public boolean onDraw() {
		if (this.settingMenu != null)
			this.settingMenu.onDraw();

		return false;
	}

	@Override
	public boolean onMouseClicked(final int button) {
		return this.settingMenu != null && this.settingMenu.onMouseClicked(button);
	}

	@Override
	public boolean onMouseScroll(final double scrollDelta) {
		return this.settingMenu != null && this.settingMenu.onMouseScroll(scrollDelta);
	}

	@Override
	public boolean onMouseInput(final int mouseX, final int mouseY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		return false;
	}

	@Override
	public boolean onCharTyped(final char typed, final int keycode) {
		return this.settingMenu != null && this.settingMenu.onCharTyped(typed, keycode);
	}

	@Override
	public boolean onKeyPressed(final int keycode) {
		return this.settingMenu != null && this.settingMenu.onKeyPressed(keycode);
	}

	@Override
	public void onTick() {
		if (this.settingMenu != null)
			this.settingMenu.onTick();
	}

	public void show() {
		final int width = 200;
		final int height = 180;

		this.settingMenu = new EmojiSettingMenu(this.chatScreen.width / 2, this.chatScreen.height / 2, width, height);
	}

	public void hide() {
		this.settingMenu = null;
	}

	private class EmojiSettingMenu {
		private final Rectangle2d rectangle;
		private final Rectangle2d rectTop;
		private final Rectangle2d rectLogo;
		private final Rectangle2d rectTopRight;
		private final Rectangle2d rectName;
		private final Rectangle2d rectBottom;
		private final Rectangle2d rectButton1;
		private final Rectangle2d rectButton2;
		private final Rectangle2d rectButton3;
		private final Rectangle2d rectMain;
		private final Rectangle2d rectUpdate;

		private boolean focused = true;
		private final #if MC_7_LATER && !MC_12_LATER ForgeVersion.CheckResult #else VersionChecker.CheckResult #endif update;

		public EmojiSettingMenu(final int posX, final int posY, final int width, final int height) {
			this.rectangle = new Rectangle2d(posX - width / 2, posY - height / 2, width, height);
			this.rectTop = new Rectangle2d(this.rectangle.getX(), this.rectangle.getY(), this.rectangle.getWidth(), 57);
			this.rectLogo = new Rectangle2d(this.rectTop.getX(), this.rectTop.getY(), 10 * 5, this.rectTop.getHeight()).inner(0, 4, 0, 0);
			this.rectTopRight = this.rectTop.inner(this.rectLogo.getX() + this.rectLogo.getWidth() - this.rectTop.getX(), 0, 0, 0);
			this.rectName = this.rectTopRight.inner(10, 20, 0, 0);
			this.rectBottom = new Rectangle2d(this.rectangle.getX(), this.rectangle.getY() + this.rectangle.getHeight() - 52, this.rectangle.getWidth(), 52);
			final Rectangle2d rectButton1Rect = new Rectangle2d(this.rectBottom.getX(), this.rectBottom.getY(), this.rectBottom.getWidth(), 20);
			final Rectangle2d rectButton2Rect = new Rectangle2d(this.rectBottom.getX(), rectButton1Rect.getY() + rectButton1Rect.getHeight(), this.rectBottom.getWidth(), 15);
			final Rectangle2d rectButton3Rect = new Rectangle2d(this.rectBottom.getX(), rectButton2Rect.getY() + rectButton2Rect.getHeight(), this.rectBottom.getWidth(), 15);
			this.rectButton1 = rectButton1Rect.inner(3, 2, 3, 1);
			this.rectButton2 = rectButton2Rect.inner(3, 1, 3, 1);
			this.rectButton3 = rectButton3Rect.inner(3, 1, 3, 1);
			this.rectMain = new Rectangle2d(this.rectangle.getX(), this.rectTop.getY() + this.rectTop.getHeight(), this.rectangle.getWidth(), this.rectBottom.getY() - (this.rectTop.getY() + this.rectTop.getHeight()));
			this.rectUpdate = new Rectangle2d(this.rectName.getX(), this.rectName.getY(), this.rectName.getWidth() - 5, this.rectName.getHeight() - 5);

			if (EmojicordConfig.UPDATE.showUpdate.get())
				this.update = CompatVersionChecker.getResult(Reference.MODID);
			else
				this.update = null;
		}

		public boolean onDraw() {
			IChatOverlay.fill(EmojiSettings.this.rectScreen, 0x77000000);
			IChatOverlay.fill(this.rectangle, 0xFF36393F);
			IChatOverlay.fill(this.rectTop, 0xFF202225);

			{
				OpenGL.glPushMatrix();
				OpenGL.glTranslatef(this.rectLogo.getX(), this.rectLogo.getY() + 2, 0);
				OpenGL.glScalef(5, 5, 1);
				EmojiSettings.this.font.drawString("<:emojicord:631339297886175295>", 0, 0, 0xFFFFFFFF);
				OpenGL.glPopMatrix();
			}

			if (this.update != null && this.update.status == #if MC_7_LATER && !MC_12_LATER ForgeVersion #else VersionChecker #endif .Status.OUTDATED) {
				{
					final String name = Reference.NAME;
					EmojiSettings.this.font.drawString(name, this.rectName.getX(), this.rectName.getY() - 15, 0xFFFFFFFF);
					EmojiSettings.this.font.drawString("by TeamFruit", this.rectName.getX() + 5 + EmojiSettings.this.font.getStringWidth(name), this.rectName.getY() - 15, 0xFF777777);
				}
				{
					final boolean b = this.rectUpdate.contains(EmojiSettings.this.mouseX, EmojiSettings.this.mouseY);
					final double t = (Math.sin(System.currentTimeMillis() / 200d) + 1) / 2;
					IChatOverlay.fill(this.rectUpdate, 0xFAA61A | (b ? 0xFF : (int) MathHelper.lerp(0x77, 0xFF, (float) t)) << 24);
					final String text1 = CompatI18n.format("emojicord.gui.settings.update", this.update.target);
					EmojiSettings.this.font.drawString(text1, this.rectUpdate.getX() + this.rectUpdate.getWidth() / 2 - EmojiSettings.this.font.getStringWidth(text1) / 2, this.rectUpdate.getY() + 7, 0xFFFFFFFF);
					final String text2 = CompatI18n.format("emojicord.gui.settings.update.click", this.update.target);
					EmojiSettings.this.font.drawString(text2, this.rectUpdate.getX() + this.rectUpdate.getWidth() / 2 - EmojiSettings.this.font.getStringWidth(text2) / 2, this.rectUpdate.getY() + 18, 0xFFFFFFFF);
				}
			} else {
				{
					OpenGL.glPushMatrix();
					OpenGL.glTranslatef(this.rectName.getX(), this.rectName.getY(), 0);
					OpenGL.glScalef(1.5f, 1.5f, 1);
					EmojiSettings.this.font.drawString(Reference.NAME, 0, 0, 0xFFFFFFFF);
					OpenGL.glPopMatrix();
				}
				EmojiSettings.this.font.drawString("by TeamFruit", this.rectName.getX() + 10, this.rectName.getY() + 15, 0xFF777777);
			}

			if (EmojiSettings.this.addGui == null) {
				{
					final Rectangle2d rectInner = this.rectMain.inner(2, 2, 2, 2);
					#if MC_12_LATER float #else int #endif posY = rectInner.getY() + 2;
					EmojiSettings.this.font.drawString(CompatI18n.format("emojicord.gui.settings.menu.packs"), rectInner.getX() + 2, posY, 0xFF777777);
					posY += 13;
					for (final EmojiDiscordList group : DiscordEmojiIdDictionary.instance.groups)
						if (posY + 20 > rectInner.getY() + rectInner.getHeight()) {
							EmojiSettings.this.font.drawString(CompatI18n.format("emojicord.gui.settings.menu.more"), rectInner.getX() + 12, posY, 0xFF777777);
							break;
						} else {
							EmojiSettings.this.font.drawString(group.name, rectInner.getX() + 12, posY, 0xFFFFFFFF);
							posY += 13;
						}
				}
				{
					IChatOverlay.fill(this.rectButton1, this.rectButton1.contains(EmojiSettings.this.mouseX, EmojiSettings.this.mouseY) ? 0xFF3CA374 : 0xFF43B581);
					final String text = CompatI18n.format("emojicord.gui.settings.menu.button.web");
					EmojiSettings.this.font.drawString(text, this.rectButton1.getX() + this.rectButton1.getWidth() / 2
							- EmojiSettings.this.font.getStringWidth(text) / 2, this.rectButton1.getY() + 5, 0xFFFFFFFF);
				}

				{
					IChatOverlay.fill(this.rectButton2, this.rectButton2.contains(EmojiSettings.this.mouseX, EmojiSettings.this.mouseY) ? 0xFF677BC4 : 0xFF7289DA);
					final String text = CompatI18n.format("emojicord.gui.settings.menu.button.manual");
					EmojiSettings.this.font.drawString(text, this.rectButton2.getX() + this.rectButton2.getWidth() / 2
							- EmojiSettings.this.font.getStringWidth(text) / 2, this.rectButton2.getY() + 2, 0xFFFFFFFF);
				}

				{
					IChatOverlay.fill(this.rectButton3, this.rectButton3.contains(EmojiSettings.this.mouseX, EmojiSettings.this.mouseY) ? 0xFF62666D : 0xFF72767D);
					final String text = CompatI18n.format("emojicord.gui.settings.menu.button.done");
					EmojiSettings.this.font.drawString(text, this.rectButton3.getX() + this.rectButton3.getWidth() / 2
							- EmojiSettings.this.font.getStringWidth(text) / 2, this.rectButton3.getY() + 2, 0xFFFFFFFF);
				}
			} else if (!EmojiSettings.this.addGui.isClosing()) {
				if (!EmojiSettings.this.addGui.isApplyPreferred()) {
					{
						OpenGL.glPushMatrix();
						OpenGL.glTranslatef(this.rectMain.getX() + this.rectMain.getWidth() / 2 - 10 * 5 / 2, this.rectMain.getY(), 0);
						OpenGL.glScalef(5, 5, 1);
						EmojiSettings.this.font.drawString("<:info:633136157626204181>", 0, 0, 0xFFFFFFFF);
						OpenGL.glPopMatrix();
					}

					{
						final Rectangle2d rectInner = this.rectMain.inner(2, 10 * 5 + 2, 2, 2);
						#if MC_12_LATER float #else int #endif posY = 0;
						for (final String desc : EmojiSettings.this.font.wrapFormattedStringToWidth(EmojiSettings.this.addGui.getDescription(), rectInner.getWidth() - 2).split("\n")) {
							EmojiSettings.this.font.drawString(desc, rectInner.getX() + 2, rectInner.getY() + 2 + posY, 0xFF777777);
							posY += 12;
						}
					}

					{
						IChatOverlay.fill(this.rectButton3, this.rectButton3.contains(EmojiSettings.this.mouseX, EmojiSettings.this.mouseY) ? 0xFFD84040 : 0xFFF04747);
						final String text = CompatI18n.format("emojicord.gui.settings.waiting.button.cancel");
						EmojiSettings.this.font.drawString(text, this.rectButton3.getX() + this.rectButton3.getWidth() / 2
								- EmojiSettings.this.font.getStringWidth(text) / 2, this.rectButton3.getY() + 2, 0xFFFFFFFF);
					}
				} else {
					{
						OpenGL.glPushMatrix();
						OpenGL.glTranslatef(this.rectMain.getX() + this.rectMain.getWidth() / 2 - 10 * 5 / 2, this.rectMain.getY(), 0);
						OpenGL.glScalef(5, 5, 1);
						EmojiSettings.this.font.drawString("<:check:633136145122983957>", 0, 0, 0xFFFFFFFF);
						OpenGL.glPopMatrix();
					}

					{
						final Rectangle2d rectInner = this.rectMain.inner(2, 10 * 5 + 2, 2, 2);
						#if MC_12_LATER float #else int #endif posY = 0;
						for (final String desc : EmojiSettings.this.font.wrapFormattedStringToWidth(EmojiSettings.this.addGui.getApplyPreferredDescription(), rectInner.getWidth() - 2).split("\n")) {
							EmojiSettings.this.font.drawString(desc, rectInner.getX() + 2, rectInner.getY() + 2 + posY, 0xFF777777);
							posY += 12;
						}
					}

					{
						IChatOverlay.fill(this.rectButton3, this.rectButton3.contains(EmojiSettings.this.mouseX, EmojiSettings.this.mouseY) ? 0xFF3CA374 : 0xFF43B581);
						final String text = CompatI18n.format("emojicord.gui.settings.completed.button.done");
						EmojiSettings.this.font.drawString(text, this.rectButton3.getX() + this.rectButton3.getWidth() / 2
								- EmojiSettings.this.font.getStringWidth(text) / 2, this.rectButton3.getY() + 2, 0xFFFFFFFF);
					}
				}
			} else {
				{
					OpenGL.glPushMatrix();
					OpenGL.glTranslatef(this.rectMain.getX() + this.rectMain.getWidth() / 2 - 10 * 5 / 2, this.rectMain.getY(), 0);
					OpenGL.glScalef(5, 5, 1);
					EmojiSettings.this.font.drawString("<:warning:633136170250928151>", 0, 0, 0xFFFFFFFF);
					OpenGL.glPopMatrix();
				}

				{
					final Rectangle2d rectInner = this.rectMain.inner(2, 10 * 5 + 2, 2, 2);
					#if MC_12_LATER float #else int #endif posY = 0;
					for (final String desc : EmojiSettings.this.font.wrapFormattedStringToWidth(EmojiSettings.this.addGui.getClosingDescription(), rectInner.getWidth() - 2).split("\n")) {
						EmojiSettings.this.font.drawString(desc, rectInner.getX() + 2, rectInner.getY() + 2 + posY, 0xFF777777);
						posY += 12;
					}
				}

				{
					IChatOverlay.fill(this.rectButton2, this.rectButton2.contains(EmojiSettings.this.mouseX, EmojiSettings.this.mouseY) ? 0xFFD84040 : 0xFFF04747);
					final String text = CompatI18n.format("emojicord.gui.settings.aborting.button.ok");
					EmojiSettings.this.font.drawString(text, this.rectButton2.getX() + this.rectButton2.getWidth() / 2
							- EmojiSettings.this.font.getStringWidth(text) / 2, this.rectButton2.getY() + 2, 0xFFFFFFFF);
				}

				{
					IChatOverlay.fill(this.rectButton3, this.rectButton3.contains(EmojiSettings.this.mouseX, EmojiSettings.this.mouseY) ? 0xFF62666D : 0xFF72767D);
					final String text = CompatI18n.format("emojicord.gui.settings.aborting.button.cancel");
					EmojiSettings.this.font.drawString(text, this.rectButton3.getX() + this.rectButton3.getWidth() / 2
							- EmojiSettings.this.font.getStringWidth(text) / 2, this.rectButton3.getY() + 2, 0xFFFFFFFF);
				}
			}

			return false;
		}

		public boolean onMouseClicked(final int button) {
			if (EmojiSettings.this.addGui != null) {
				if (!EmojiSettings.this.addGui.isClosing()) {
					if (this.rectButton3.contains(EmojiSettings.this.mouseX, EmojiSettings.this.mouseY)) {
						EmojiSettings.this.addGui.onApplying();
						return true;
					}
				} else {
					if (this.rectButton2.contains(EmojiSettings.this.mouseX, EmojiSettings.this.mouseY)) {
						EmojiSettings.this.addGui.onOK();
						return true;
					}
					if (this.rectButton3.contains(EmojiSettings.this.mouseX, EmojiSettings.this.mouseY)) {
						EmojiSettings.this.addGui.onCancel();
						return true;
					}
				}
			} else {
				if (this.update != null && this.update.status == #if MC_7_LATER && !MC_12_LATER ForgeVersion #else VersionChecker #endif .Status.OUTDATED)
					if (this.rectUpdate.contains(EmojiSettings.this.mouseX, EmojiSettings.this.mouseY)) {
						if (this.update.url != null)
							OSUtils.getOSType().openURI(this.update.url);
						return true;
					}

				if (this.rectButton3.contains(EmojiSettings.this.mouseX, EmojiSettings.this.mouseY) || !this.rectangle.contains(EmojiSettings.this.mouseX, EmojiSettings.this.mouseY)) {
					hide();
					return true;
				}
				if (this.rectButton1.contains(EmojiSettings.this.mouseX, EmojiSettings.this.mouseY)) {
					EmojiSettings.this.addGui = new WebAdding();
					return true;
				}
				if (this.rectButton2.contains(EmojiSettings.this.mouseX, EmojiSettings.this.mouseY)) {
					EmojiSettings.this.addGui = new ManualAdding();
					return true;
				}
			}
			return true;
		}

		public boolean onMouseScroll(final double scrollDelta) {
			return false;
		}

		public boolean onCharTyped(final char typed, final int keycode) {
			return EmojiSettings.this.addGui != null;
		}

		public boolean onKeyPressed(final int keycode) {
			return EmojiSettings.this.addGui != null;
		}

		public void onTick() {
			if (EmojiSettings.this.addGui != null) {
				final boolean lastFocused = this.focused;
				this.focused = Compat.getMinecraft(). #if MC_12_LATER isGameFocused() #else inGameHasFocus #endif ;
				if (this.focused != lastFocused)
					EmojiSettings.this.addGui.onWindowFocus();
			}
		}

		private class WebAdding implements EmojiAdding {
			private boolean closing;
			private boolean changed;

			public WebAdding() {
				EmojicordWeb.instance.open();
			}

			@Override
			public boolean isClosing() {
				return this.closing;
			}

			@Override
			public boolean isApplyPreferred() {
				if (!this.changed)
					this.changed = EmojicordWeb.instance.pollCallbacked();
				return this.changed;
			}

			@Override
			public void onApplying() {
				this.closing = true;
				if (this.changed)
					onOK();
			}

			@Override
			public String getDescription() {
				return CompatI18n.format("emojicord.gui.settings.waiting.desc.web").replace("\\n", "\n");
			}

			@Override
			public String getClosingDescription() {
				return CompatI18n.format("emojicord.gui.settings.aborting.desc.web").replace("\\n", "\n");
			}

			@Override
			public String getApplyPreferredDescription() {
				return CompatI18n.format("emojicord.gui.settings.completed.desc.web").replace("\\n", "\n");
			}

			@Override
			public void onOK() {
				this.closing = false;
				//EmojicordWeb.instance.close();
				DiscordEmojiIdDictionary.instance.loadAll();
				EmojiSettings.this.addGui = null;
			}

			@Override
			public void onCancel() {
				this.closing = false;
			}
		}

		private class ManualAdding implements EmojiAdding {
			private boolean closing;
			private boolean changed;

			public ManualAdding() {
				ClientProxy.eventHandler.hasDictionaryDirectoryChanged();

				OSUtils.getOSType().openFile(DiscordEmojiIdDictionary.instance.getDictionaryDirectory());
			}

			@Override
			public boolean isClosing() {
				return this.closing;
			}

			@Override
			public boolean isApplyPreferred() {
				if (!this.changed)
					this.changed = ClientProxy.eventHandler.hasDictionaryDirectoryChanged();
				return this.changed;
			}

			@Override
			public void onApplying() {
				this.closing = true;
				if (this.changed)
					onOK();
			}

			@Override
			public String getDescription() {
				return CompatI18n.format("emojicord.gui.settings.waiting.desc.manual").replace("\\n", "\n");
			}

			@Override
			public String getClosingDescription() {
				return CompatI18n.format("emojicord.gui.settings.aborting.desc.manual").replace("\\n", "\n");
			}

			@Override
			public String getApplyPreferredDescription() {
				return CompatI18n.format("emojicord.gui.settings.completed.desc.manual").replace("\\n", "\n");
			}

			@Override
			public void onOK() {
				this.closing = false;
				DiscordEmojiIdDictionary.instance.loadAll();
				EmojiSettings.this.addGui = null;
			}

			@Override
			public void onCancel() {
				this.closing = false;
			}
		}
	}
}
