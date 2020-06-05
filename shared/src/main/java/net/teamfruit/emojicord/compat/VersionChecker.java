package net.teamfruit.emojicord.compat;

#if !MC_7_LATER
import static net.teamfruit.emojicord.compat.VersionChecker.Status.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;

import cpw.mods.fml.common.versioning.ComparableVersion;
import net.minecraftforge.common.MinecraftForge;

public class VersionChecker {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final int MAX_HTTP_REDIRECTS = Integer.getInteger("http.maxRedirects", 20);

	public enum Status {
		PENDING(),
		FAILED(),
		UP_TO_DATE(),
		OUTDATED(3, true),
		AHEAD(),
		BETA(),
		BETA_OUTDATED(6, true);

		final int sheetOffset;
		final boolean draw, animated;

		Status() {
			this(0, false, false);
		}

		Status(final int sheetOffset) {
			this(sheetOffset, true, false);
		}

		Status(final int sheetOffset, final boolean animated) {
			this(sheetOffset, true, animated);
		}

		Status(final int sheetOffset, final boolean draw, final boolean animated) {
			this.sheetOffset = sheetOffset;
			this.draw = draw;
			this.animated = animated;
		}

		public int getSheetOffset() {
			return this.sheetOffset;
		}

		public boolean shouldDraw() {
			return this.draw;
		}

		public boolean isAnimated() {
			return this.animated;
		}

	}

	public static class CheckResult {
		@Nonnull
		public final Status status;
		@Nullable
		public final ComparableVersion target;
		@Nullable
		public final Map<ComparableVersion, String> changes;
		@Nullable
		public final String url;

		private CheckResult(@Nonnull final Status status, @Nullable final ComparableVersion target, @Nullable final Map<ComparableVersion, String> changes, @Nullable final String url) {
			this.status = status;
			this.target = target;
			this.changes = changes==null ? Collections.<ComparableVersion, String> emptyMap() : Collections.unmodifiableMap(changes);
			this.url = url;
		}
	}

	public static void startVersionCheck(final String modId, final String modVersion, final String updateURL) {
		new Thread("Emojicord Version Check") {
			@Override
			public void run() {
				process(modId, modVersion, updateURL);
			}

			/**
			 * Opens stream for given URL while following redirects
			 */
			private InputStream openUrlStream(final URL url) throws IOException {
				URL currentUrl = url;
				for (int redirects = 0; redirects<MAX_HTTP_REDIRECTS; redirects++) {
					final URLConnection c = currentUrl.openConnection();
					if (c instanceof HttpURLConnection) {
						final HttpURLConnection huc = (HttpURLConnection) c;
						huc.setInstanceFollowRedirects(false);
						final int responseCode = huc.getResponseCode();
						if (responseCode>=300&&responseCode<=399)
							try {
								final String loc = huc.getHeaderField("Location");
								currentUrl = new URL(currentUrl, loc);
								continue;
							} finally {
								huc.disconnect();
							}
					}

					return c.getInputStream();
				}
				throw new IOException("Too many redirects while trying to fetch "+url);
			}

			private void process(final String modId, final String modVersion, final String updateURL) {
				Status status = PENDING;
				ComparableVersion target = null;
				Map<ComparableVersion, String> changes = null;
				String display_url = null;
				try {
					final URL url = new URL(updateURL);
					LOGGER.info("[{}] Starting version check at {}", modId, url.toString());

					final InputStream con = openUrlStream(url);
					final String data = new String(ByteStreams.toByteArray(con), StandardCharsets.UTF_8);
					con.close();

					LOGGER.debug("[{}] Received version check data:\n{}", modId, data);

					@SuppressWarnings("unchecked")
					final Map<String, Object> json = new Gson().fromJson(data, Map.class);
					@SuppressWarnings("unchecked")
					final Map<String, String> promos = (Map<String, String>) json.get("promos");
					display_url = (String) json.get("homepage");

					final String mcVersion = MinecraftForge.MC_VERSION;
					final String rec = promos.get(mcVersion+"-recommended");
					final String lat = promos.get(mcVersion+"-latest");
					final ComparableVersion current = new ComparableVersion(modVersion);

					if (rec!=null) {
						final ComparableVersion recommended = new ComparableVersion(rec);
						final int diff = recommended.compareTo(current);

						if (diff==0)
							status = UP_TO_DATE;
						else if (diff<0) {
							status = AHEAD;
							if (lat!=null) {
								final ComparableVersion latest = new ComparableVersion(lat);
								if (current.compareTo(latest)<0) {
									status = OUTDATED;
									target = latest;
								}
							}
						} else {
							status = OUTDATED;
							target = recommended;
						}
					} else if (lat!=null) {
						final ComparableVersion latest = new ComparableVersion(lat);
						if (current.compareTo(latest)<0)
							status = BETA_OUTDATED;
						else
							status = BETA;
						target = latest;
					} else
						status = BETA;

					LOGGER.info("[{}] Found status: {} Current: {} Target: {}", modId, status, current, target);

					changes = new LinkedHashMap<>();
					@SuppressWarnings("unchecked")
					final Map<String, String> tmp = (Map<String, String>) json.get(mcVersion);
					if (tmp!=null) {
						final List<ComparableVersion> ordered = new ArrayList<>();
						for (final String key : tmp.keySet()) {
							final ComparableVersion ver = new ComparableVersion(key);
							if (ver.compareTo(current)>0&&(target==null||ver.compareTo(target)<1))
								ordered.add(ver);
						}
						Collections.sort(ordered);

						for (final ComparableVersion ver : ordered)
							changes.put(ver, tmp.get(ver.toString()));
					}
				} catch (final Exception e) {
					LOGGER.warn("Failed to process update information", e);
					status = FAILED;
				}
				results = Optional.of(new CheckResult(status, target, changes, display_url));
			}
		}.start();
	}

	private static Optional<CheckResult> results = Optional.empty();
	private static final CheckResult PENDING_CHECK = new CheckResult(PENDING, null, null, null);

	public static CheckResult getResult() {
		return results.orElse(PENDING_CHECK);
	}

}
#endif
