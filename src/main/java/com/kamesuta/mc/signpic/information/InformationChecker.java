package com.kamesuta.mc.signpic.information;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.handler.CoreEvent;
import com.kamesuta.mc.signpic.util.ChatBuilder;

import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import net.minecraft.entity.player.EntityPlayer;

public final class InformationChecker {
	public static InfoState state = new InfoState();

	public static class InfoState {
		public Info.Version onlineVersion;
		public Info.Version stableVersion;
		public Info.Version unstableVersion;
		public Info.PrivateMsg privateMsg;

		public boolean doneChecking = false;
		public boolean triedToWarnPlayer = false;
		public boolean startedDownload = false;
		public boolean downloadedFile = false;

		public void onTick() {
			final EntityPlayer player = Client.mc.thePlayer;
			if(this.doneChecking && player != null && !this.triedToWarnPlayer) {
				final String lang = Client.mc.gameSettings.language;
				if (!StringUtils.equals(Reference.VERSION, "${version}")) {
					try {
						final String[] client = Reference.VERSION.split("\\.");
						if (client.length>=2) {
							final int clientBuild1 = Integer.parseInt(client[0]);
							final int clientBuild2 = Integer.parseInt(client[1]);

							boolean betaneedupdate = false;
							if (this.unstableVersion!=null && this.unstableVersion.version!=null) {
								if (client.length>=4 &&StringUtils.equals(client[3], "beta")) {
									if (NumberUtils.isNumber(client[2])) {
										final int clientBuild3 = NumberUtils.toInt(client[2]);
										final String[] beta = this.unstableVersion.version.split("\\.");
										if (beta.length>=4 &&StringUtils.equals(beta[3], "beta")) {
											if (NumberUtils.isNumber(beta[0]) && NumberUtils.isNumber(beta[1]) && NumberUtils.isNumber(beta[2])) {
												final int betaBuild1 = NumberUtils.toInt(beta[0]);
												final int betaBuild2 = NumberUtils.toInt(beta[1]);
												final int betaBuild3 = NumberUtils.toInt(beta[2]);
												betaneedupdate = (betaBuild1 > clientBuild1) ||
														(betaBuild1 == clientBuild1 && betaBuild2 > clientBuild2) ||
														(betaBuild1 == clientBuild1 && betaBuild2 == clientBuild2 && betaBuild3 > clientBuild3);
											}
										}
									}
								}
							}
							if (betaneedupdate)
								this.onlineVersion = this.unstableVersion;
							else
								this.onlineVersion = this.stableVersion;

							if (this.onlineVersion!=null && this.onlineVersion.version!=null) {
								final String[] online = this.onlineVersion.version.split("\\.");
								if (online.length>=2) {
									boolean needupdate = false;
									if (!betaneedupdate) {
										final int onlineBuild1 = Integer.parseInt(online[0]);
										final int onlineBuild2 = Integer.parseInt(online[1]);
										needupdate = (onlineBuild1 > clientBuild1) || (onlineBuild1 == clientBuild1 && onlineBuild2 > clientBuild2);
									}

									if(betaneedupdate || needupdate) {
										if (this.onlineVersion.message_local!=null && this.onlineVersion.message_local.containsKey(lang))
											ChatBuilder.create(this.onlineVersion.message_local.get(lang)).chatClient();
										else if (!StringUtils.isEmpty(this.onlineVersion.message))
											ChatBuilder.create(this.onlineVersion.message).chatClient();
										ChatBuilder.create("signpic.versioning.outdated").setParams(Reference.VERSION, this.onlineVersion.version).useTranslation().chatClient();
										ChatBuilder.create("signpic.versioning.updateMessage").useTranslation().useJson().chatClient();;
									}
								}
							}
						}
					} catch (final NumberFormatException e) {
						Reference.logger.warn(String.format("failed to check version: invaild version: client[%s], online[%s]", Reference.VERSION, (this.onlineVersion!=null)?this.onlineVersion:"-"));
					}
				}
				if (this.privateMsg!=null) {
					final ChatBuilder ctb = new ChatBuilder();
					if (this.privateMsg.message_local!=null && this.privateMsg.message_local.containsKey(lang))
						ctb.setText(this.privateMsg.message_local.get(lang));
					else if (!StringUtils.isEmpty(this.privateMsg.message))
						ctb.setText(this.privateMsg.message);
					if (this.privateMsg.json)
						ctb.useJson();
					ctb.chatClient();
				}
				this.triedToWarnPlayer = true;
			}
		}
	}

	public static void init() {
		new ThreadInformationChecker();
	}

	@CoreEvent
	public void onTick(final ClientTickEvent event) {
		if(event.phase==Phase.END) {
			state.onTick();
		}
	}
}