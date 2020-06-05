package net.teamfruit.emojicord.emoji;

#if MC_12_LATER
import net.minecraft.resources.IResourceManager;
#else
import net.minecraft.client.resources.IResourceManager;
#endif

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalNotification;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.madgag.gif.fmsware.GifDecoder;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.util.ResourceLocation;
import net.teamfruit.emojicord.Log;
import net.teamfruit.emojicord.compat.Compat;
import net.teamfruit.emojicord.compat.Compat.CompatTexture;
import net.teamfruit.emojicord.util.Downloader;
import net.teamfruit.emojicord.util.Timer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.message.BasicHeader;

import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EmojiObject {
	public static final ResourceLocation loading_texture = new ResourceLocation("emojicord", "textures/26a0.png");
	public static final ResourceLocation error_texture = new ResourceLocation("emojicord", "textures/26d4.png");

	private static final @Nonnull
	ExecutorService threadpool = Executors.newCachedThreadPool(
			new ThreadFactoryBuilder().setNameFormat("emojicord-emoji-%d").setDaemon(true).build());

	private final EmojiId id;
	private boolean deleteOldTexture;
	private SimpleTexture img;
	private ResourceLocation resourceLocation;

	public EmojiObject(final EmojiId id) {
		this.resourceLocation = loading_texture;
		this.id = id;
	}

	private void checkLoad() {
		if (this.img == null) {
			this.img = new DownloadImageData(this.id.getCache(), this.id.getRemote(), loading_texture);
			this.resourceLocation = this.id.getResourceLocation();
			Compat.getMinecraft().getTextureManager().loadTexture(this.resourceLocation, this.img);
		}
	}

	public ResourceLocation getResourceLocation() {
		return this.resourceLocation;
	}

	public ResourceLocation loadAndGetResourceLocation() {
		checkLoad();
		if (this.deleteOldTexture) {
			this.img.deleteGlTexture();
			this.deleteOldTexture = false;
		}
		return this.resourceLocation;
	}

	public void delete() {
		if (this.img != null) {
			this.img.deleteGlTexture();
			this.deleteOldTexture = false;
		}
	}

	public static class InfinityIterator<T> implements Iterator<T> {
		private final Iterable<T> iterable;
		private Iterator<T> iterator;

		public InfinityIterator(final Iterable<T> iterable) {
			this.iterable = iterable;
			this.iterator = iterable.iterator();
		}

		@Override
		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		@Override
		public T next() {
			final T next = this.iterator.next();
			if (!this.iterator.hasNext())
				this.iterator = this.iterable.iterator();
			return next;
		}
	}

	public class DownloadImageData extends SimpleTexture {
		private final File cacheFile;
		private final String imageUrl;
		private final ResourceLocation textureResourceLocation;

		private CompletableFuture<byte[]> downloading;
		private boolean textureUploaded;

		private byte[] rawData;
		private BufferedImage imageData;
		private List<Pair<Integer, BufferedImage>> animationData;

		private List<Pair<Integer, SimpleTexture>> animation;

		private Timer timer = new Timer();
		private Iterator<Pair<Integer, SimpleTexture>> animationIterator;
		private Pair<Integer, SimpleTexture> current;

		public DownloadImageData(
				final File cacheFileIn, final String imageUrlIn,
				final ResourceLocation textureResourceLocation
		) {
			super(textureResourceLocation);
			this.cacheFile = cacheFileIn;
			this.imageUrl = imageUrlIn;
			this.textureResourceLocation = textureResourceLocation;
		}

		private void checkTextureUploaded() {
			if (!this.textureUploaded)
				if (this.imageData != null && this.animationData != null) {
					try {
						deleteGlTexture();
						CompatTexture.uploadTexture(super::getGlTextureId, this.imageData);
						this.animation = this.animationData.stream().map(e -> {
							final SimpleTexture t = new SimpleTexture(this.textureResourceLocation);
							try {
								CompatTexture.uploadTexture(t::getGlTextureId, e.getRight());
							} catch (final IOException e1) {
								throw new UncheckedIOException(e1);
							}
							return Pair.of(e.getLeft(), t);
						}).collect(Collectors.toList());
						this.animationIterator = new InfinityIterator<>(this.animation);
					} catch (final IOException | UncheckedIOException e) {
						Log.log.warn("Failed to load texture: ", e);
					}
					this.imageData = null;
					this.animationData = null;
					this.textureUploaded = true;
				} else if (this.rawData != null) {
					try {
						deleteGlTexture();
						CompatTexture.uploadTexture(super::getGlTextureId, new ByteArrayInputStream(this.rawData));
					} catch (final IOException e) {
						Log.log.warn("Failed to load texture: ", e);
					}
					this.rawData = null;
					this.textureUploaded = true;
				}
		}

		@Override
		public int getGlTextureId() {
			checkTextureUploaded();
			if (this.animationIterator != null && this.animationIterator.hasNext()) {
				if (this.current == null)
					this.current = this.animationIterator.next();
				Timer.tick();
				final int currentId = this.current.getRight().getGlTextureId();
				final float t = this.timer.getTime() - this.current.getLeft() * 1e-3f;
				if (t > 0) {
					this.current = null;
					this.timer.set(0);
				}
				return currentId;
			}
			return super.getGlTextureId();
		}

		@Override
		public void loadTexture(final IResourceManager resourceManager) throws IOException {
			if (this.textureUploaded)
				return;
			if (this.textureLocation != null)
				super.loadTexture(resourceManager);
			if (this.downloading == null) {
				CompletableFuture<byte[]> dataFuture;
				if (this.cacheFile == null)
					dataFuture = CompletableFuture.completedFuture(null);
				else {
					final CompletableFuture<File> cacheFuture = this.cacheFile.isFile()
							? CompletableFuture.completedFuture(this.cacheFile)
							: CompletableFuture.supplyAsync(downloadTextureFromServer(this.cacheFile), threadpool);
					dataFuture = cacheFuture.thenApplyAsync(cacheFile -> {
						try {
							return IOUtils.toByteArray(FileUtils.openInputStream(cacheFile));
						} catch (final IOException ioexception) {
							throw new UncheckedIOException(ioexception);
						}
					}, threadpool);
				}
				final CompletableFuture<byte[]> statusFuture = dataFuture.exceptionally(e -> {
					Log.log.warn("Failed to load texture: ", e);
					return null;
				});
				statusFuture.thenAcceptAsync(data -> {
					if (data == null) {
						EmojiObject.this.resourceLocation = EmojiObject.error_texture;
						EmojiObject.this.deleteOldTexture = true;
					} else {
						final GifDecoder d = new GifDecoder();
						if (d.read(new ByteArrayInputStream(data)) == GifDecoder.STATUS_OK) {
							this.imageData = d.getImage();
							this.animationData = IntStream.range(0, d.getFrameCount()).mapToObj(i -> Pair.of(d.getDelay(i), d.getFrame(i))).collect(Collectors.toList());
						} else
							this.rawData = data;
					}
				}, threadpool);
			}
		}

		protected Supplier<File> downloadTextureFromServer(final File cacheFile) {
			return () -> {
				CloseableHttpResponse response = null;
				try {
					final HttpUriRequest req = new HttpGet(DownloadImageData.this.imageUrl);
					req.setHeaders(new Header[]{
							new BasicHeader("User-Agent",
									"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36"),
							new BasicHeader("Accept", "*/*"),
							new BasicHeader("Accept-Encoding", ""),
							new BasicHeader("Accept-Language", "ja,en-US;q=0.9,en;q=0.8"),
					});
					final HttpClientContext context = HttpClientContext.create();
					response = Downloader.downloader.client.execute(req, context);
					final HttpEntity entity = response.getEntity();

					final int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode != HttpStatus.SC_OK)
						throw new IOException("Invalid Status Code: " + statusCode);

					FileUtils.copyInputStreamToFile(entity.getContent(),
							DownloadImageData.this.cacheFile);
					return cacheFile;
				} catch (final IOException exception) {
					throw new UncheckedIOException(exception);
				} finally {
					IOUtils.closeQuietly(response);
				}
			};
		}
	}

	public static class EmojiObjectCache {
		public static final long EMOJI_LIFETIME_SEC = 60;

		public static final EmojiObjectCache instance = new EmojiObjectCache();

		private EmojiObjectCache() {
		}

		private final LoadingCache<EmojiId, EmojiObject> EMOJI_ID_MAP = CacheBuilder.newBuilder()
				.expireAfterAccess(EMOJI_LIFETIME_SEC, TimeUnit.SECONDS)
				.removalListener(
						(final RemovalNotification<EmojiId, EmojiObject> notification) -> {
							//Log.log.info("deleted");
							final EmojiObject nvalue = notification.getValue();
							if (nvalue != null)
								nvalue.delete();
						})
				.build(new CacheLoader<EmojiId, EmojiObject>() {
					@Override
					public EmojiObject load(final EmojiId key) throws Exception {
						return new EmojiObject(key);
					}
				});

		public @Nonnull
		EmojiObject getEmojiObject(final @Nonnull EmojiId name) {
			return this.EMOJI_ID_MAP.getUnchecked(name);
		}
	}
}