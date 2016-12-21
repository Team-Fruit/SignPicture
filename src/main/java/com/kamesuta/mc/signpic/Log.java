package com.kamesuta.mc.signpic;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.spi.AbstractLogger;

import com.kamesuta.mc.signpic.gui.OverlayFrame;

public class Log {
	public static Logger log = LogManager.getLogger(Reference.MODID);
	public static Logger dev = new DevLogger(log);

	private static class DevLogger extends AbstractLogger {
		private Logger logger;

		public DevLogger(@Nonnull final Logger logger) {
			this.logger = logger;
		}

		private boolean isEnabled() {
			Validate.notNull(Config.instance, "internal error: debug logger can be used after config loaded!");
			return Config.instance.debugLog.get();
		}

		@Override
		protected boolean isEnabled(final Level level, final Marker marker, final Message data, final Throwable t) {
			return isEnabled();
		}

		@Override
		protected boolean isEnabled(final Level level, final Marker marker, final Object data, final Throwable t) {
			return isEnabled();
		}

		@Override
		protected boolean isEnabled(final Level level, final Marker marker, final String data) {
			return isEnabled();
		}

		@Override
		protected boolean isEnabled(final Level level, final Marker marker, final String data, final Object... p1) {
			return isEnabled();
		}

		@Override
		protected boolean isEnabled(final Level level, final Marker marker, final String data, final Throwable t) {
			return isEnabled();
		}

		@Override
		public void log(final Marker marker, final String fqcn, final Level level, final Message data, final Throwable t) {
			if (isEnabled())
				this.logger.log(level, marker, "[DEBUG] "+data.getFormattedMessage(), t);
		}
	}

	@SuppressWarnings("deprecation")
	public static void notice(final Object notice, final float duration) {
		OverlayFrame.instance.pane.addNotice1(notice.toString(), duration);
		if (dev!=null)
			dev.info(notice);
	}

	@SuppressWarnings("deprecation")
	public static void notice(final String notice, final Throwable e, final float duration) {
		OverlayFrame.instance.pane.addNotice1(notice, duration);
		if (dev!=null)
			dev.info(notice, e);
	}

	public static void notice(final String notice) {
		notice(notice, 2f);
	}

	public static void notice(final String notice, final Throwable e) {
		notice(notice, e, 2f);
	}
}
