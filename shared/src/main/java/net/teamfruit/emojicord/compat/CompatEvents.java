package net.teamfruit.emojicord.compat;

#if !MC_12_LATER
import net.minecraft.client.gui.GuiScreen;
#endif

#if MC_12_LATER
import net.minecraftforge.eventbus.api.Cancelable;
#elif MC_7_LATER
import net.minecraftforge.fml.common.eventhandler.Cancelable;
#else
import cpw.mods.fml.common.eventhandler.Cancelable;
#endif

import net.minecraftforge.client.event.GuiScreenEvent;

public class CompatEvents {
	public static class CompatGuiScreenEvent {
	    #if !MC_12_LATER
		@Cancelable
		public static class MouseClickedEvent extends GuiScreenEvent {
			private final int button;

			public MouseClickedEvent(final GuiScreen screen, final int button) {
				super(screen);
				this.button = button;
			}

			public int getButton() {
				return this.button;
			}

			public static class Pre extends MouseClickedEvent {
				public Pre(final GuiScreen screen, final int button) {
					super(screen, button);
				}
			}
		}

		@Cancelable
		public static class MouseReleasedEvent extends GuiScreenEvent {
			private final int button;

			public MouseReleasedEvent(final GuiScreen screen, final int button) {
				super(screen);
				this.button = button;
			}

			public int getButton() {
				return this.button;
			}

			public static class Pre extends MouseReleasedEvent {
				public Pre(final GuiScreen screen, final int button) {
					super(screen, button);
				}
			}
		}

		@Cancelable
		public static class MouseScrollEvent extends GuiScreenEvent {
			private final double scrollDelta;

			public MouseScrollEvent(final GuiScreen screen, final double scrollDelta) {
				super(screen);
				this.scrollDelta = scrollDelta;
			}

			public double getScrollDelta() {
				return this.scrollDelta;
			}

			public static class Pre extends MouseScrollEvent {
				public Pre(final GuiScreen screen, final double scrollDelta) {
					super(screen, scrollDelta);
				}
			}
		}

		@Cancelable
		public static class KeyboardCharTypedEvent extends GuiScreenEvent {
			private final char codePoint;
			private final int modifiers;

			public KeyboardCharTypedEvent(final GuiScreen screen, final char codePoint, final int modifiers) {
				super(screen);
				this.codePoint = codePoint;
				this.modifiers = modifiers;
			}

			public char getCodePoint() {
				return this.codePoint;
			}

			public int getModifiers() {
				return this.modifiers;
			}

			public static class Pre extends KeyboardCharTypedEvent {
				public Pre(final GuiScreen screen, final char codePoint, final int modifiers) {
					super(screen, codePoint, modifiers);
				}
			}
		}

		@Cancelable
		public static class KeyboardKeyPressedEvent extends GuiScreenEvent {
			private final int keycode;

			public KeyboardKeyPressedEvent(final GuiScreen screen, final int keycode) {
				super(screen);
				this.keycode = keycode;
			}

			public int getKeyCode() {
				return this.keycode;
			}

			public static class Pre extends KeyboardKeyPressedEvent {
				public Pre(final GuiScreen screen, final int keycode) {
					super(screen, keycode);
				}
			}
		}
        #endif
	}
}
