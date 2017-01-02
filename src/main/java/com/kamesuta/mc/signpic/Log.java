package com.kamesuta.mc.signpic;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.spi.AbstractLogger;

import com.kamesuta.mc.signpic.gui.OverlayFrame;

public class Log {
	public static @Nonnull Logger log = LogManager.getLogger(Reference.MODID);
	public static @Nonnull Logger dev = new DevLogger(log);

	private static class DevLogger extends AbstractLogger {
		private Logger logger;

		public DevLogger(@Nonnull final Logger logger) {
			this.logger = logger;
		}

		private boolean isEnabled() {
			final Config cfg = Config.getConfig();
			Validate.notNull(cfg, "internal error: debug logger can be used after config loaded!");
			return Config.getConfig().debugLog.get();
		}

		@Override
		protected boolean isEnabled(final @Nullable Level level, final @Nullable Marker marker, final @Nullable Message data, final @Nullable Throwable t) {
			return isEnabled();
		}

		@Override
		protected boolean isEnabled(final @Nullable Level level, final @Nullable Marker marker, final @Nullable Object data, final @Nullable Throwable t) {
			return isEnabled();
		}

		@Override
		protected boolean isEnabled(final @Nullable Level level, final @Nullable Marker marker, final @Nullable String data) {
			return isEnabled();
		}

		@Override
		protected boolean isEnabled(final @Nullable Level level, final @Nullable Marker marker, final @Nullable String data, final @Nullable Object... p1) {
			return isEnabled();
		}

		@Override
		protected boolean isEnabled(final @Nullable Level level, final @Nullable Marker marker, final @Nullable String data, final @Nullable Throwable t) {
			return isEnabled();
		}

		@Override
		public void log(final @Nullable Marker marker, final @Nullable String fqcn, final @Nullable Level level, final @Nullable Message data, final @Nullable Throwable t) {
			if (isEnabled()&&data!=null)
				this.logger.log(level, marker, "[DEBUG] "+data.getFormattedMessage(), t);
		}
	}

	@SuppressWarnings("deprecation")
	public static void notice(final @Nonnull Object notice, final float duration) {
		OverlayFrame.instance.pane.addNotice1(notice.toString(), duration);
		dev.info(notice);
	}

	@SuppressWarnings("deprecation")
	public static void notice(final @Nonnull String notice, final @Nonnull Throwable e, final float duration) {
		OverlayFrame.instance.pane.addNotice1(notice, duration);
		dev.info(notice, e);
	}

	public static void notice(final @Nonnull String notice) {
		notice(notice, 2f);
	}

	public static void notice(final @Nonnull String notice, final @Nonnull Throwable e) {
		notice(notice, e, 2f);
	}
}
