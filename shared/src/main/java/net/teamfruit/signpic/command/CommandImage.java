package net.teamfruit.signpic.command;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;
import net.teamfruit.bnnwidget.WFrame;
import net.teamfruit.signpic.Client;
import net.teamfruit.signpic.compat.Compat.CompatCommandBase;
import net.teamfruit.signpic.gui.GuiMain;
import net.teamfruit.signpic.mode.CurrentMode;

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
			final String joint = CompatCommandBase.buildString(sender, args, 0);
			GuiMain.setContentId(joint);
			CurrentMode.instance.setState(CurrentMode.State.PREVIEW, false);
			if (!WFrame.isCurrentInstanceOf(GuiMain.class))
				Client.openEditor();
		}
	}
}