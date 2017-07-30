package test;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Test189 {
	public static TestRoot root = new TestRoot();
	public static String title = "Test189";
	public static Minecraft minecraft1 = Minecraft.getMinecraft();
	public static Minecraft minecraft2 = FMLClientHandler.instance().getClient();
	public static MinecraftServer minecraftserver1 = MinecraftServer.getServer();
	public static MinecraftServer minecraftserver2 = FMLCommonHandler.instance().getMinecraftServerInstance();
}