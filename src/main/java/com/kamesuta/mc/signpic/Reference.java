package com.kamesuta.mc.signpic;

import javax.annotation.Nonnull;

public class Reference {
	public static final @Nonnull String MODID = "signpic";
	public static final @Nonnull String NAME = "SignPicture";
	public static final @Nonnull String VERSION = "${version}";
	public static final @Nonnull String FORGE = "${forgeversion}";
	public static final @Nonnull String MINECRAFT = "${mcversion}";
	public static final @Nonnull String PROXY_SERVER = "com.kamesuta.mc.signpic.CommonProxy";
	public static final @Nonnull String PROXY_CLIENT = "com.kamesuta.mc.signpic.ClientProxy";
	public static final @Nonnull String GUI_FACTORY = "com.kamesuta.mc.signpic.gui.config.ConfigGuiFactory";
}
