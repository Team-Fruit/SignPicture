package com.kamesuta.mc.signpic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kamesuta.mc.signpic.gui.OverlayFrame;

public class Log {
	public static Logger logger = LogManager.getLogger(Reference.MODID);

	public static void error(final Object message) {
		logger.error(message);
	}

	public static void error(final Object message, final Throwable e) {
		logger.error(message, e);
	}

	public static void warn(final Object message) {
		logger.warn(message);
	}

	public static void warn(final Object message, final Throwable e) {
		logger.warn(message, e);
	}

	public static void info(final Object message) {
		logger.info(message);
	}

	public static void info(final Object message, final Throwable e) {
		logger.info(message, e);
	}

	public static void debugerror(final Object message) {
		if (Config.instance.debugLog.get())
			logger.error("[DEBUG] "+message);
	}

	public static void debugerror(final Object message, final Throwable e) {
		if (Config.instance.debugLog.get())
			logger.error("[DEBUG] "+message, e);
	}

	public static void debugwarn(final Object message) {
		if (Config.instance.debugLog.get())
			logger.warn("[DEBUG] "+message);
	}

	public static void debugwarn(final Object message, final Throwable e) {
		if (Config.instance.debugLog.get())
			logger.warn("[DEBUG] "+message, e);
	}

	public static void debuginfo(final Object message) {
		if (Config.instance.debugLog.get())
			logger.info("[DEBUG] "+message);
	}

	public static void debuginfo(final Object message, final Throwable e) {
		if (Config.instance.debugLog.get())
			logger.info("[DEBUG] "+message, e);
	}

	@SuppressWarnings("deprecation")
	public static void notice(final Object notice, final float duration) {
		OverlayFrame.instance.pane.addNotice1(notice.toString(), duration);
		debuginfo(notice);
	}

	@SuppressWarnings("deprecation")
	public static void notice(final String notice, final Throwable e, final float duration) {
		OverlayFrame.instance.pane.addNotice1(notice, duration);
		debuginfo(notice, e);
	}

	public static void notice(final String notice) {
		notice(notice, 2f);
	}

	public static void notice(final String notice, final Throwable e) {
		notice(notice, e, 2f);
	}
}
