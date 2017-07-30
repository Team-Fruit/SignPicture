package test;

import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class TestRoot {
	public static String title = "TestRoot";
	public static LaunchClassLoader launch = Launch.classLoader;
	public static Minecraft minecraft1 = Minecraft.getMinecraft();
}
