package com.kamesuta.mc.signpic.command;

import javax.annotation.Nonnull;

import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.gui.GuiMain;
import com.kamesuta.mc.signpic.mode.CurrentMode;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandImage extends SubCommand {
	public CommandImage() {
		super("image");
		addChildCommand(new CommandImageOpen());
		setPermLevel(PermLevel.EVERYONE);
	}

	private static class CommandImageOpen extends SubCommand {
		private CommandImageOpen() {
			super("open");
			setPermLevel(PermLevel.EVERYONE);
		}

		@Override
		public void processSubCommand(final @Nonnull ICommandSender sender, final @Nonnull String[] args) {
			final String joint = CommandBase.func_82360_a(sender, args, 0);
			GuiMain.setContentId(joint);
			CurrentMode.instance.setState(CurrentMode.State.PREVIEW, false);
			if (!(WFrame.getCurrent() instanceof GuiMain))
				Client.openEditor();
		}
	}
}