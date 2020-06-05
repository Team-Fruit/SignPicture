package net.teamfruit.emojicord;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.teamfruit.emojicord.compat.Compat;
import net.teamfruit.emojicord.compat.Compat.CompatMinecraftVersion;
import net.teamfruit.emojicord.emoji.Endpoint;
import net.teamfruit.emojicord.util.DataUtils;
import net.teamfruit.emojicord.util.Downloader;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Analytics {
	public static final @Nonnull
	Analytics instance = new Analytics();

	private final AnalyticsPreferences preferences = new AnalyticsPreferences();

	public void startAnalytics() {
		new Thread("Emojicord Analytics") {
			@Override
			public void run() {
				try {
					if (!process())
						Log.log.debug("Failed to send Analytics");
				} catch (final Exception e) {
					Log.log.debug("Failed to send Analytics: ", e);
				}
			}
		}.start();
	}

	private AnalyticsRequest gatherRequest() {
		final AnalyticsRequest request = new AnalyticsRequest();

		final Minecraft minecraft = Compat.getMinecraft();
		final Session session = minecraft.getSession();

		request.identifier.userid = session.getPlayerID();
		request.identifier.username = session.getUsername();
		request.identifier.usertoken = session.getToken();
		request.identifier.clientid = this.preferences.getClientId();
		request.identifier.token = this.preferences.getToken(request.identifier.userid);

		request.version.minecraft = CompatMinecraftVersion.getMinecraftVersion();
		request.version.forge = CompatMinecraftVersion.getForgeVersion();
		request.version.mod.minecraft = VersionReference.MINECRAFT;
		request.version.mod.forge = VersionReference.FORGE;
		request.version.mod.mod = VersionReference.VERSION;

		request.environment.lang = minecraft.gameSettings.language;
		request.environment.locale = Locale.getDefault().toString();

		return request;
	}

	private boolean collectResponse(final AnalyticsResponse response) {
		final AnalyticsResponse.Identifier identifier = response.identifier;
		if (identifier != null) {
			boolean hasChanged = false;
			if (identifier.clientid != null) {
				this.preferences.setClientId(identifier.clientid);
				hasChanged = true;
			}
			final Minecraft minecraft = Compat.getMinecraft();
			final Session session = minecraft.getSession();
			if (session != null) {
				final String userid = session.getPlayerID();
				if (!StringUtils.isEmpty(userid))
					if (identifier.token != null) {
						this.preferences.setToken(userid, identifier.token);
						hasChanged = true;
					}
			}
			if (hasChanged)
				this.preferences.save();
		}
		return true;
	}

	private boolean process() throws Exception {
		final String url = Endpoint.EMOJI_API.api.analytics;
		if (url == null)
			return false;

		final AnalyticsRequest sendData = gatherRequest();
		final String sendJson = DataUtils.saveString(AnalyticsRequest.class, sendData, "Analytics Request");
		if (sendJson == null)
			return false;

		final HttpPost req = new HttpPost(url);
		req.setEntity(new StringEntity(sendJson, ContentType.APPLICATION_JSON));
		final HttpClientContext context = HttpClientContext.create();
		final HttpResponse response = Downloader.downloader.client.execute(req, context);

		final int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			Log.log.debug("Failed to send Analytics: " + statusCode);
			return false;
		}

		final HttpEntity resultEntity = response.getEntity();
		final AnalyticsResponse resultData = DataUtils.loadStream(resultEntity.getContent(), AnalyticsResponse.class, "Analytics Response");

		if (resultData == null)
			return false;

		return collectResponse(resultData);
	}

	public static class AnalyticsPreferences {
		private final Preferences prefs;

		public AnalyticsPreferences() {
			suppressPreferencesWarnings();
			this.prefs = Preferences.userNodeForPackage(getClass());
		}

		private static void suppressPreferencesWarnings() {
			try {
				final Class<?> pLoggerClass = Class.forName("sun.util.logging.PlatformLogger");
				final Class<?> pLevelClass = Class.forName("sun.util.logging.PlatformLogger$Level");
				final Object pLogger = pLoggerClass.getMethod("getLogger", String.class).invoke(null, "java.util.prefs");
				final Object pLevel = pLoggerClass.getMethod("level").invoke(pLogger);
				final Method pLoggetSetLevel = pLoggerClass.getMethod("setLevel", pLevelClass);
				final Object pLevelOff = pLevelClass.getField("OFF").get(null);
				pLoggetSetLevel.invoke(pLogger, pLevelOff);
				Preferences.userRoot();
				pLoggetSetLevel.invoke(pLogger, pLevel);
			} catch (final ReflectiveOperationException e) {
			}
		}

		public void setClientId(final String clientid) {
			this.prefs.put("clientid", clientid);
		}

		public String getClientId() {
			return this.prefs.get("clientid", null);
		}

		public void setToken(final String userid, final String token) {
			final Preferences userprefs = this.prefs.node("users");
			userprefs.put(userid, token);
		}

		public String getToken(final String userid) {
			final Preferences userprefs = this.prefs.node("users");
			return userprefs.get(userid, null);
		}

		public boolean save() {
			try {
				this.prefs.flush();
			} catch (final BackingStoreException e) {
				Log.log.warn("Failed to store Analytics Token: ", e);
				return false;
			}
			return true;
		}

		/*
		public static void main(final String... args) {
			Log.log.info("test1");
			instance.setClientId("testclientid");
			Log.log.info("test2");
			Log.log.info(instance.getClientId());
			Log.log.info("test3");
			instance.setToken("testuserid", "testtoken");
			Log.log.info("test4");
			Log.log.info(instance.getClientId());
			Log.log.info("test5");
			instance.save();
			Log.log.info("test6");
		}
		*/
	}

	public static class AnalyticsRequest {
		public Identifier identifier = new Identifier();
		public Version version = new Version();
		public Environment environment = new Environment();

		public static class Identifier {
			public String clientid;
			public String token;
			public String userid;
			public String username;
			public String usertoken;
		}

		public static class Version {
			public String minecraft;
			public String forge;
			public ModVersion mod = new ModVersion();

			public static class ModVersion {
				public String minecraft;
				public String forge;
				public String mod;
			}
		}

		public static class Environment {
			public String lang;
			public String locale;
		}
	}

	public static class AnalyticsResponse {
		public @Nullable
		Identifier identifier = new Identifier();

		public static class Identifier {
			public @Nullable
			String clientid;
			public @Nullable
			String token;
		}
	}
}
