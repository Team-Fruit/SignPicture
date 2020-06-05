package net.teamfruit.emojicord.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;

import net.teamfruit.emojicord.compat.Compat;

public class Downloader {
	public static final int timeout = 15000;
	public static final @Nonnull String useragent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36";

	public static final @Nonnull Downloader downloader = new Downloader();

	public final @Nonnull PoolingHttpClientConnectionManager manager;
	public @Nonnull CloseableHttpClient client;

	public Downloader() {
		Registry<ConnectionSocketFactory> registry = null;
		try {
			final SSLContext sslContext = new SSLContextBuilder()
					.loadTrustMaterial(
							null,
							new TrustSelfSignedStrategy())
					.loadTrustMaterial(
							null,
							(final @Nullable X509Certificate[] chain, final @Nullable String authType) -> true)
					.build();
			final SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
					sslContext,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER) {
				@Override
				protected void prepareSocket(final @Nullable SSLSocket socket) throws IOException {
					if (socket!=null) {
						final String[] enabledCipherSuites = socket.getEnabledCipherSuites();

						final List<String> asList = new ArrayList<>(Arrays.asList(enabledCipherSuites));

						asList.remove("TLS_DHE_RSA_WITH_AES_128_CBC_SHA");
						asList.remove("SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA");
						asList.remove("TLS_DHE_RSA_WITH_AES_256_CBC_SHA");

						final String[] array = asList.toArray(new String[0]);
						socket.setEnabledCipherSuites(array);
					}
				}
			};

			registry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory())
					.register("https", sslConnectionSocketFactory)
					.build();
		} catch (final NoSuchAlgorithmException e) {
		} catch (final KeyManagementException e) {
		} catch (final KeyStoreException e) {
		}

		if (registry!=null)
			this.manager = new PoolingHttpClientConnectionManager(registry);
		else
			this.manager = new PoolingHttpClientConnectionManager();

		final Builder requestConfig = RequestConfig.custom();

		if (timeout>0) {
			requestConfig.setConnectTimeout(timeout);
			requestConfig.setSocketTimeout(timeout);
		}

		final Proxy proxy = Compat.getMinecraft().getProxy();
		if (proxy!=null&&!Proxy.NO_PROXY.equals(proxy)) {
			final SocketAddress saddr = proxy.address();
			if (saddr instanceof InetSocketAddress) {
				final InetSocketAddress inetsaddr = (InetSocketAddress) saddr;
				final HttpHost httpproxy = new HttpHost(inetsaddr.getAddress(), inetsaddr.getPort());
				requestConfig.setProxy(httpproxy);
			}
		}

		final List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader("Accept-Charset", "utf-8"));
		headers.add(new BasicHeader("Accept-Language", "ja, en;q=0.8"));
		headers.add(new BasicHeader("User-Agent", useragent));

		this.client = HttpClientBuilder.create()
				.setConnectionManager(this.manager)
				.setDefaultRequestConfig(requestConfig.build())
				.setDefaultHeaders(headers)
				.build();
	}
}