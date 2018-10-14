package net.teamfruit.signpic;

import javax.annotation.Nonnull;

public class Reference {
	public static final @Nonnull String MODID = "signpic";
	public static final @Nonnull String NAME = "SignPicture";
	public static final @Nonnull String VERSION = "${version}";
	public static final @Nonnull String FORGE = "${forgeversion}";
	public static final @Nonnull String MINECRAFT = "${mcversion}";
	public static final @Nonnull String PROXY_SERVER = "net.teamfruit.signpic.CommonProxy";
	public static final @Nonnull String PROXY_CLIENT = "net.teamfruit.signpic.ClientProxy";
	public static final @Nonnull String GUI_FACTORY = "net.teamfruit.signpic.gui.config.ConfigGuiFactory";
	public static final @Nonnull String TRANSFORMER = "net.teamfruit.signpic.asm.SignPictureTransformer";
}
