package com.kamesuta.mc.signpic.handler;

import java.util.List;

import javax.annotation.Nonnull;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.CoreEvent;
import com.kamesuta.mc.signpic.gui.GuiHub;
import com.kamesuta.mc.signpic.gui.GuiIngameScreenShot;
import com.kamesuta.mc.signpic.gui.GuiMain;
import com.kamesuta.mc.signpic.gui.GuiWindowScreenShot;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.KeyBinding;

public class KeyHandler {
	public static final Key keySignPicture = new Key.AbstractKey() {
		@Override
		public int getCode() {
			return KeyHandler.Keys.KEY_BINDING_GUI.binding.getKeyCode();
		}
	};

	public static final Key keyScreenShotFull = new Key.FixedKey(Keyboard.KEY_O);
	public static final Key keyScreenShot = new Key.FixedKey(Keyboard.KEY_P);
	public static final Key keySwingScreenShot = new Key.FixedKey(Keyboard.KEY_I);

	public static interface Key {
		int getCode();

		boolean isKeyPressed();

		public static abstract class AbstractKey implements Key {
			@Override
			public boolean isKeyPressed() {
				return Keyboard.isKeyDown(getCode());
			}
		}

		public static class FixedKey extends AbstractKey {
			protected int code;

			public FixedKey(final int code) {
				this.code = code;
			}

			@Override
			public int getCode() {
				return code;
			}
		}
	}

	public static enum Keys {
		KEY_BINDING_GUI(new KeyBinding("signpic.key.gui", Keyboard.KEY_BACKSLASH, "signpic.key.category")) {
			@Override
			public void onKeyInput(final @Nonnull InputEvent event, final @Nonnull KeyBinding binding) {
				if (Client.mc.currentScreen==null&&binding.isPressed())
					Client.mc.displayGuiScreen(new GuiHub(Client.mc.currentScreen));
			}
		},
		;

		public final @Nonnull KeyBinding binding;

		private Keys(final @Nonnull KeyBinding binding) {
			this.binding = binding;
		}

		public abstract void onKeyInput(@Nonnull InputEvent event, @Nonnull KeyBinding binding);
	}

	public static void init() {
		for (final Keys key : Keys.values())
			ClientRegistry.registerKeyBinding(key.binding);
	}

	private boolean firstkeypressed;
	private boolean keypressed;

	@CoreEvent
	public void onTick() {
		if (Keyboard.isKeyDown(Keys.KEY_BINDING_GUI.binding.getKeyCode())) {
			this.firstkeypressed = true;
			if (!this.keypressed) {
				this.keypressed = true;
				if (Keyboard.isKeyDown(keyScreenShot.getCode()))
					WRenderer.mc.displayGuiScreen(new GuiIngameScreenShot(WFrame.getCurrent()));
				else if (Keyboard.isKeyDown(keyScreenShotFull.getCode())) {
					final GuiIngameScreenShot shot = new GuiIngameScreenShot(WFrame.getCurrent());
					WRenderer.mc.displayGuiScreen(shot);
					shot.takeFullScreenshot();
				} else if (Keyboard.isKeyDown(keySwingScreenShot.getCode()))
					WRenderer.mc.displayGuiScreen(new GuiWindowScreenShot(WFrame.getCurrent()));
				else
					this.keypressed = false;
			}
		} else if (this.firstkeypressed) {
			this.firstkeypressed = false;
			if (!this.keypressed&&(WRenderer.mc.currentScreen instanceof GuiHub||WRenderer.mc.currentScreen instanceof GuiMainMenu))
				WRenderer.mc.displayGuiScreen(new GuiMain(WFrame.getParentOrThis()));
			this.keypressed = false;
		}
	}

	@CoreEvent
	public void onKeyInput(final @Nonnull InputEvent event) {
		for (final Keys key : Keys.values())
			key.onKeyInput(event, key.binding);
	}

	public static @Nonnull List<KeyBinding> getKeyConflict(final @Nonnull KeyBinding binding) {
		final List<KeyBinding> conflict = Lists.newLinkedList();
		if (binding.getKeyCode()!=0) {
			final KeyBinding[] akeybinding = Client.mc.gameSettings.keyBindings;
			final int l1 = akeybinding.length;
			for (int i2 = 0; i2<l1; ++i2) {
				final KeyBinding keybinding = akeybinding[i2];
				if (keybinding!=binding&&keybinding.getKeyCode()==binding.getKeyCode())
					conflict.add(keybinding);
			}
		}
		return conflict;
	}
}
