package test;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class Test1710 {
	public static TestRoot root = new TestRoot();
	public static String title = "Test1710";
	public static Minecraft minecraft1 = Minecraft.getMinecraft();
	public static Minecraft minecraft2 = FMLClientHandler.instance().getClient();
	public static MinecraftServer minecraftserver1 = MinecraftServer.getServer();
	public static MinecraftServer minecraftserver2 = FMLCommonHandler.instance().getMinecraftServerInstance();
}
