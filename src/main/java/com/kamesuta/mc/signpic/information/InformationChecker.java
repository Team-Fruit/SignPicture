package com.kamesuta.mc.signpic.information;

import com.kamesuta.mc.signpic.Reference;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public final class InformationChecker {

	public static boolean doneChecking = false;
	public static String onlineVersion;
	public static boolean triedToWarnPlayer = false;
	public static String onlineVersionRemote;
	public static String onlineVersionLocal;

	public static boolean startedDownload = false;
	public static boolean downloadedFile = false;

	public void init() {
		new ThreadInformationChecker();
		FMLCommonHandler.instance().bus().register(this);
	}

	@SubscribeEvent
	public void onTick(final ClientTickEvent event) {
		if(doneChecking && event.phase == Phase.END && FMLClientHandler.instance().getClient().thePlayer != null && !triedToWarnPlayer) {
			if(onlineVersion!=null && !onlineVersion.isEmpty()) {
				if (!Reference.VERSION.equals("${version}")) {
					final EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
					final String[] online = onlineVersion.split("\\.");
					final String[] client = Reference.VERSION.split("\\.");
					if (online.length>=2 && client.length>=2) {
						try {
							final int onlineBuild1 = Integer.parseInt(online[0]);
							final int onlineBuild2 = Integer.parseInt(online[1]);
							final int clientBuild1 = Integer.parseInt(client[0]);
							final int clientBuild2 = Integer.parseInt(client[1]);

							if(onlineBuild1 > clientBuild1 || onlineBuild2 > clientBuild2) {
								player.addChatComponentMessage(new ChatComponentTranslation("signpic.versioning.outdated", Reference.VERSION, onlineVersion));
								final IChatComponent component = IChatComponent.Serializer.func_150699_a(StatCollector.translateToLocal("signpic.versioning.updateMessage"));
								player.addChatComponentMessage(component);
							}
						} catch (final NumberFormatException e) {
							Reference.logger.warn(String.format("failed to check version: invaild version: client[%s], online[%s]", Reference.VERSION, onlineVersion));
						}
					}
				}
			}
			triedToWarnPlayer = true;
		}
	}

}