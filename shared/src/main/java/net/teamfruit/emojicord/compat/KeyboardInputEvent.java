package net.teamfruit.emojicord.compat;

#if !MC_7_LATER
import cpw.mods.fml.common.eventhandler.Cancelable;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.teamfruit.emojicord.CoreInvoke;

public class KeyboardInputEvent extends GuiScreenEvent {
	public KeyboardInputEvent(final GuiScreen gui) {
		super(gui);
	}

	@Cancelable
	public static class Pre extends KeyboardInputEvent {
		public Pre(final GuiScreen gui) {
			super(gui);
		}

		@CoreInvoke
		public static boolean onKeyboardInput(final GuiScreen screen) {
			final Pre event = new Pre(screen);
			return MinecraftForge.EVENT_BUS.post(event);
		}
	}
}
#endif
