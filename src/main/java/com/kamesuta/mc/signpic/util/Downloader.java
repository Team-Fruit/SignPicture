package com.kamesuta.mc.signpic.util;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
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

import com.kamesuta.mc.signpic.Reference;

public class Downloader {
	public final PoolingHttpClientConnectionManager manager;
	public HttpClient client;

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
								@Override public boolean isTrusted(final X509Certificate[] chain, final String authType)
										throws CertificateException {
									return true;
								}
							})
					.build();
			final SSLConnectionSocketFactory sslConnectionSocketFactory =
					new SSLConnectionSocketFactory(
							sslContext,
							SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			registry =
					RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory())
					.register("https", sslConnectionSocketFactory)
					.build();
		} catch (final NoSuchAlgorithmException e) {
		} catch (final KeyManagementException e) {
		} catch (final KeyStoreException e) {
		}

		if (registry != null)
			this.manager = new PoolingHttpClientConnectionManager(registry);
		else
			this.manager = new PoolingHttpClientConnectionManager();

		final RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(15 * 1000)
				.setSocketTimeout(15 * 1000)
				.build();

		final List<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("Accept-Charset","utf-8"));
		headers.add(new BasicHeader("Accept-Language","ja, en;q=0.8"));
		headers.add(new BasicHeader("User-Agent", Reference.MODID));

		this.client = HttpClientBuilder.create()
				.setConnectionManager(this.manager)
				.setDefaultRequestConfig(requestConfig)
				.setDefaultHeaders(headers)
				.build();
	}
}