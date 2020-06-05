package net.teamfruit.emojicord.compat;

#if MC_12_LATER
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
#else
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
#endif
import net.minecraft.client.gui.FontRenderer;
import net.teamfruit.emojicord.CoreInvoke;

public class CompatGui {

	public static class CompatScreen {
		public static boolean hasShiftDown() {
			#if MC_12_LATER
			return Screen.hasShiftDown();
			#else
			return GuiScreen.isShiftKeyDown();
			#endif
		}
	}

	public static class CompatTextFieldWidget {
		public static int getInsertPos( #if MC_12_LATER TextFieldWidget #else GuiTextField #endif textField, FontRenderer font, final int start) {
			#if MC_12_LATER
			return textField.func_195611_j(start);
			#else
			final String text = textField.getText();
			int x = textField. #if MC_12_OR_LATER x #else xPosition #endif ;
			if (start > text.length())
				return x;
			return x + font.getStringWidth(text.substring(0, start));
			#endif
		}

		public static void setSuggestion( #if MC_12_LATER TextFieldWidget #else GuiTextField #endif textField, final String string) {
			#if MC_12_LATER
			textField.setSuggestion(string);
			#else
			try {
				textField.getClass().getField("suggestion").set(textField, string);
			} catch (final ReflectiveOperationException e) {
				throw new RuntimeException("Could not set suggestion: ", e);
			}
			#endif
		}

		@CoreInvoke
		public static void renderSuggestion(final FontRenderer font, final boolean flag, final String suggestion, final int posX, final int posY) {
			if (!flag && suggestion != null)
				font.drawStringWithShadow(suggestion, posX - 1, posY, 0xFF808080);
		}
	}

}
