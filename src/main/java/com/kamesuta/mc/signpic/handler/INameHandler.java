package com.kamesuta.mc.signpic.handler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.entry.EntryId;

import net.minecraft.client.gui.GuiScreen;

public interface INameHandler {
	void reset();

	boolean onOpen(@Nullable GuiScreen gui, @Nonnull EntryId currentId);

	void onTick();

	void onDraw(@Nullable GuiScreen gui);
}
