package com.kamesuta.mc.signpic.information;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.util.Downloader;

import net.minecraftforge.common.MinecraftForge;

public class ThreadInformationChecker extends Thread {

	public ThreadInformationChecker() {
		setName("Sign Picture Information Checker Thread");
		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		InputStream input = null;
		final Gson gson = new Gson();
		try {
			final HttpUriRequest req = new HttpGet(new URI("https://raw.githubusercontent.com/Team-Fruit/SignPicture/master/info/info.json"));
			final HttpResponse response = Downloader.downloader.client.execute(req);
			final HttpEntity entity = response.getEntity();
			input = entity.getContent();
			final Info info = gson.fromJson(new JsonReader(new InputStreamReader(input, CharEncoding.UTF_8)), Info.class);
			if (info!=null) {
				if (info.versions!=null) {
					InformationChecker.stableVersion = info.versions.get(MinecraftForge.MC_VERSION);
					InformationChecker.unstableVersion = info.versions.get(MinecraftForge.MC_VERSION + "-beta");
				}
				if (!StringUtils.isEmpty(info.private_msg)) {
					InputStream input1 = null;
					try {
						final HttpUriRequest req1 = new HttpGet(new URI(info.private_msg.replace("%name%", Client.name).replace("%id%", Client.id)));
						final HttpResponse response1 = Downloader.downloader.client.execute(req1);
						final HttpEntity entity1 = response1.getEntity();
						input1 = entity1.getContent();
						InformationChecker.privateMsg = gson.fromJson(new JsonReader(new InputStreamReader(input1, CharEncoding.UTF_8)), Info.PrivateMsg.class);
					} catch(final Exception e1) {
					} finally {
						IOUtils.closeQuietly(input1);
					}
				}
			}
		} catch(final Exception e) {
			Reference.logger.warn("Could not check version information", e);
		} finally {
			IOUtils.closeQuietly(input);
		}
		InformationChecker.doneChecking = true;
	}
}