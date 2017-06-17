package com.kamesuta.mc.signpic.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;

import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.Reference;

public class Downloader {
	public static @Nonnull Downloader downloader = new Downloader();

	public final @Nonnull PoolingHttpClientConnectionManager manager;
	public @Nonnull HttpClient client;

	public Downloader() {
		Registry<ConnectionSocketFactory> registry = null;
		try {
			final SSLContext sslContext = new SSLContextBuilder()
					.loadTrustMaterial(
							null,
							new TrustSelfSignedStrategy())
					.loadTrustMaterial(
							null,
							new TrustStrategy() {
								@Override
								public boolean isTrusted(final @Nullable X509Certificate[] chain, final @Nullable String authType)
										throws CertificateException {
									return true;
								}
							})
					.build();
			final SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
					sslContext,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER) {
				@Override
				protected void prepareSocket(final @Nullable SSLSocket socket) throws IOException {
					if (socket!=null) {
						final String[] enabledCipherSuites = socket.getEnabledCipherSuites();

						final List<String> asList = new ArrayList<String>(Arrays.asList(enabledCipherSuites));

						asList.remove("TLS_DHE_RSA_WITH_AES_128_CBC_SHA");
						asList.remove("SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA");
						asList.remove("TLS_DHE_RSA_WITH_AES_256_CBC_SHA");

						final String[] array = asList.toArray(new String[0]);
						socket.setEnabledCipherSuites(array);
					}
				};
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
		if (Config.getConfig().communicateDLTimedout.get()>0) {
			requestConfig.setConnectTimeout(Config.getConfig().communicateDLTimedout.get());
			requestConfig.setSocketTimeout(Config.getConfig().communicateDLTimedout.get());
		}

		final List<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("Accept-Charset", "utf-8"));
		headers.add(new BasicHeader("Accept-Language", "ja, en;q=0.8"));
		headers.add(new BasicHeader("User-Agent", Reference.MODID));

		this.client = HttpClientBuilder.create()
				.setConnectionManager(this.manager)
				.setDefaultRequestConfig(requestConfig.build())
				.setDefaultHeaders(headers)
				.build();
	}
}