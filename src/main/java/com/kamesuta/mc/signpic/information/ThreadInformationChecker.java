package com.kamesuta.mc.signpic.information;

import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.kamesuta.mc.signpic.Reference;

import net.minecraftforge.common.MinecraftForge;

public class ThreadInformationChecker extends Thread {

	public ThreadInformationChecker() {
		setName("Sign Picture Information Checker Thread");
		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		try {
			final URL url = new URL("https://raw.githubusercontent.com/Team-Fruit/SignPicture/master/info/info.json");
			final Info info = new Gson().fromJson(new JsonReader(new InputStreamReader(url.openStream())), Info.class);
			if (info!=null)
				if (info.versions!=null) {
					final Info.Version version = info.versions.get(MinecraftForge.MC_VERSION);
					if (version!=null) {
						InformationChecker.onlineVersion = version.version;
						InformationChecker.onlineVersionRemote = version.remote;
						InformationChecker.onlineVersionLocal = version.local;
					}
				}
		} catch(final Exception e) {
			Reference.logger.warn("Could not check version information", e);
		}
		InformationChecker.doneChecking = true;
	}
}