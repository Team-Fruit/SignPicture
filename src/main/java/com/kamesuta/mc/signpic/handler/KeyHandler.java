package com.kamesuta.mc.signpic.handler;

import org.lwjgl.input.Keyboard;

import com.kamesuta.mc.signpic.Client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.settings.KeyBinding;

public class KeyHandler {
	public static KeyHandler INSTANCE = new KeyHandler();

	private static final KeyBinding KEY_BINDING_GUI = new KeyBinding("signpic.key.gui", Keyboard.KEY_BACKSLASH, "signpic.key.category");

	public static final KeyBinding[] KEY_BINDINGS = new KeyBinding[] {
			KEY_BINDING_GUI
	};

	private KeyHandler() {
	}

	public void init() {
		for (final KeyBinding keyBinding : KEY_BINDINGS) {
			ClientRegistry.registerKeyBinding(keyBinding);
		}
	}

	@SubscribeEvent
	public void onKeyInput(final InputEvent event) {
		if (Client.mc.currentScreen == null) {
			if (KEY_BINDING_GUI.isPressed()) {
				Client.openEditor();
			}
		}
	}
}
