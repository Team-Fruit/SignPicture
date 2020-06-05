package net.teamfruit.signpic;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.teamfruit.signpic.Reference;
import net.teamfruit.signpic.gui.OverlayFrame;

public class Log {
	public static @Nonnull Logger log = LogManager.getLogger(Reference.MODID);

	@SuppressWarnings("deprecation")
	public static void notice(final @Nonnull Object notice, final float duration) {
		OverlayFrame.instance.pane.addNotice1(notice.toString(), duration);
		log.debug(notice);
	}

	@SuppressWarnings("deprecation")
	public static void notice(final @Nonnull String notice, final @Nonnull Throwable e, final float duration) {
		OverlayFrame.instance.pane.addNotice1(notice, duration);
		log.debug(notice, e);
	}

	public static void notice(final @Nonnull String notice) {
		notice(notice, 2f);
	}

	public static void notice(final @Nonnull String notice, final @Nonnull Throwable e) {
		notice(notice, e, 2f);
	}
}
