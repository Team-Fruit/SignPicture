package net.teamfruit.signpic.handler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiScreen;
import net.teamfruit.signpic.entry.EntryId;

public interface INameHandler {
	void reset();

	boolean onOpen(@Nullable GuiScreen gui, @Nonnull EntryId currentId);

	void onTick();

	void onDraw(@Nullable GuiScreen gui);
}
