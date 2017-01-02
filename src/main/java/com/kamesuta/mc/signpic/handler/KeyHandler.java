package com.kamesuta.mc.signpic.handler;

import java.util.List;

import javax.annotation.Nonnull;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.CoreEvent;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyHandler {
	public static enum Keys {
		KEY_BINDING_GUI(new KeyBinding("signpic.key.gui", Keyboard.KEY_BACKSLASH, "signpic.key.category")) {
			@Override
			public void onKeyInput(final @Nonnull InputEvent event, final @Nonnull KeyBinding binding) {
				if (Client.mc.currentScreen==null&&binding.isPressed())
					Client.openEditor();
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
