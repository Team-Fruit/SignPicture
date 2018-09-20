package com.kamesuta.mc.signpic.handler;

import java.util.List;

import javax.annotation.Nonnull;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Timer;

import com.google.common.collect.Lists;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.CoreEvent;
import com.kamesuta.mc.signpic.CoreInvoke;
import com.kamesuta.mc.signpic.compat.Compat.CompatInputEvent;
import com.kamesuta.mc.signpic.compat.Compat.CompatKeyRegistrar;
import com.kamesuta.mc.signpic.gui.GuiHub;
import com.kamesuta.mc.signpic.gui.GuiIngameScreenShot;
import com.kamesuta.mc.signpic.gui.GuiMain;
import com.kamesuta.mc.signpic.gui.GuiWindowScreenShot;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;

public class KeyHandler {
	public static final @Nonnull Key keySignPicture = new Key.AbstractKey() {
		@Override
		public int getCode() {
			return KeyHandler.Keys.KEY_BINDING_GUI.binding.getKeyCode();
		}
	};

	public static final @Nonnull Key keyScreenShotFull = new Key.FixedKey(Keyboard.KEY_O);
	public static final @Nonnull Key keyScreenShot = new Key.FixedKey(Keyboard.KEY_P);
	public static final @Nonnull Key keySwingScreenShot = new Key.FixedKey(Keyboard.KEY_I);

	public static interface Key {
		int getCode();

		boolean isKeyPressed();

		@Nonnull
		String getName();

		public static abstract class AbstractKey implements Key {
			@Override
			public boolean isKeyPressed() {
				return Keyboard.isKeyDown(getCode());
			}

			@Override
			public @Nonnull String getName() {
				return Keyboard.getKeyName(getCode());
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
			public void onKeyInput(final @Nonnull CompatInputEvent event, final @Nonnull KeyBinding binding) {
				if (WFrame.getCurrent()==null&&binding.isPressed())
					WFrame.displayFrame(new GuiHub(WFrame.getCurrent()));
			}
		},
		;

		public final @Nonnull KeyBinding binding;

		private Keys(final @Nonnull KeyBinding binding) {
			this.binding = binding;
		}

		public abstract void onKeyInput(@Nonnull CompatInputEvent event, @Nonnull KeyBinding binding);
	}

	public static void init() {
		for (final Keys key : Keys.values())
			CompatKeyRegistrar.registerKeyBinding(key.binding);
	}

	public static final @Nonnull KeyHandler instance = new KeyHandler();

	private @Nonnull Timer signpickeypressed;

	private KeyHandler() {
		this.signpickeypressed = new Timer();
		this.signpickeypressed.pause();
	}

	@CoreEvent
	public void onKeyInput(final @Nonnull CompatInputEvent event) {
		for (final Keys key : Keys.values())
			key.onKeyInput(event, key.binding);
	}

	@CoreInvoke
	public boolean onGuiKeyInput(final @Nonnull GuiScreen screen) {
		final WFrame widget = WFrame.getWidget(screen);
		final float time = this.signpickeypressed.getTime();
		if (!(widget instanceof GuiIngameScreenShot||widget instanceof GuiWindowScreenShot))
			if (keySignPicture.isKeyPressed()) {
				if (!keyHook(screen)) {
					this.signpickeypressed.resume();
					return true;
				}
				clearInput();
			} else if (!(widget instanceof GuiMain)&&time>.35f) {
				WFrame.displayFrame(new GuiMain(screen));
				clearInput();
			}
		this.signpickeypressed.reset();
		this.signpickeypressed.pause();
		return false;
	}

	public boolean keyHook(final @Nonnull GuiScreen screen) {
		if (keyScreenShot.isKeyPressed())
			WFrame.displayFrame(new GuiIngameScreenShot(screen));
		else if (keyScreenShotFull.isKeyPressed()) {
			final GuiIngameScreenShot shot = new GuiIngameScreenShot(screen);
			WFrame.displayFrame(shot);
			shot.takeFullScreenshot();
		} else if (keySwingScreenShot.isKeyPressed())
			WFrame.displayFrame(new GuiWindowScreenShot(screen));
		else
			return false;
		return true;
	}

	public void clearInput() {
		if (Mouse.isCreated())
			while (Mouse.next()) {
			}

		if (Keyboard.isCreated())
			while (Keyboard.next()) {
			}
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
