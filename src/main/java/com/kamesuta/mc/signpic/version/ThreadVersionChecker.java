package com.kamesuta.mc.signpic.version;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.kamesuta.mc.signpic.Reference;

import net.minecraftforge.common.MinecraftForge;

public class ThreadVersionChecker extends Thread {

	public ThreadVersionChecker() {
		setName("Sign Picture Version Checker Thread");
		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		try {
			final URL url = new URL("https://raw.githubusercontent.com/Kamesuta/SignPicture/master/version/" + MinecraftForge.MC_VERSION + ".txt");
			final BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
			VersionChecker.onlineVersion = r.readLine();
			VersionChecker.onlineVersionURL = r.readLine();
			VersionChecker.onlineVersionLocal = r.readLine();
			r.close();
		} catch(final Exception e) {
			Reference.logger.warn("Could not check version information", e);
		}
		VersionChecker.doneChecking = true;
	}
}