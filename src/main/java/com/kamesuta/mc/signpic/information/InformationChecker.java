package com.kamesuta.mc.signpic.information;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.information.Info.Version;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public final class InformationChecker {

	public static boolean doneChecking = false;
	public static boolean triedToWarnPlayer = false;
	public static Version onlineVersion;
	public static Version stableVersion;
	public static Version unstableVersion;

	public static boolean startedDownload = false;
	public static boolean downloadedFile = false;

	public void init() {
		new ThreadInformationChecker();
		FMLCommonHandler.instance().bus().register(this);
	}

	@SubscribeEvent
	public void onTick(final ClientTickEvent event) {
		final EntityPlayer player = Client.mc.thePlayer;
		if(doneChecking && event.phase == Phase.END && player != null && !triedToWarnPlayer) {
			if (!StringUtils.equals(Reference.VERSION, "${version}")) {
				try {
					final String[] client = Reference.VERSION.split("\\.");
					if (client.length>=2) {
						final int clientBuild1 = Integer.parseInt(client[0]);
						final int clientBuild2 = Integer.parseInt(client[1]);

						boolean betaneedupdate = false;
						if (unstableVersion!=null && unstableVersion.version!=null) {
							if (client.length>=4 &&StringUtils.equals(client[3], "beta")) {
								final String[] beta = unstableVersion.version.split("\\.");
								betaneedupdate = true;
								if (beta.length>=3) {
									if (NumberUtils.isNumber(beta[0]) && NumberUtils.isNumber(beta[1])) {
										final int betaBuild1 = NumberUtils.toInt(beta[0]);
										final int betaBuild2 = NumberUtils.toInt(beta[1]);
										if (betaBuild1 > clientBuild1 || (betaBuild1 == clientBuild1 && betaBuild2 > clientBuild2)) {
											if (NumberUtils.isNumber(beta[2]) && NumberUtils.isNumber(client[2])) {
												final int betaBuild3 = NumberUtils.toInt(beta[2]);
												betaneedupdate = betaBuild3 > NumberUtils.toInt(client[2]);
											}
										}
									}
								}
							}
						}
						if (betaneedupdate)
							onlineVersion = unstableVersion;
						else
							onlineVersion = stableVersion;

						if (onlineVersion!=null && onlineVersion.version!=null) {
							final String[] online = onlineVersion.version.split("\\.");
							if (online.length>=2) {
								boolean needupdate = false;
								if (!betaneedupdate) {
									final int onlineBuild1 = Integer.parseInt(online[0]);
									final int onlineBuild2 = Integer.parseInt(online[1]);
									needupdate = onlineBuild1 > clientBuild1 || (onlineBuild1 == clientBuild1 && onlineBuild2 > clientBuild2);
								}

								if(betaneedupdate || needupdate) {
									if (!StringUtils.isEmpty(onlineVersion.message))
										player.addChatComponentMessage(new ChatComponentText(onlineVersion.message));
									player.addChatComponentMessage(new ChatComponentTranslation("signpic.versioning.outdated", Reference.VERSION, onlineVersion.version));
									final IChatComponent component = IChatComponent.Serializer.func_150699_a(StatCollector.translateToLocal("signpic.versioning.updateMessage"));
									player.addChatComponentMessage(component);
								}
							}
						}
					}
				} catch (final NumberFormatException e) {
					Reference.logger.warn(String.format("failed to check version: invaild version: client[%s], online[%s]", Reference.VERSION, (onlineVersion!=null)?onlineVersion:"-"));
				}
			}
			triedToWarnPlayer = true;
		}
	}

}