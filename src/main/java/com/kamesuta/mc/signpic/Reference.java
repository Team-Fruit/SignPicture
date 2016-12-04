package com.kamesuta.mc.signpic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reference {
	public static final String MODID = "signpic";
	public static final String NAME = "SignPicture";
	public static final String VERSION = "${version}";
	public static final String FORGE = "${forgeversion}";
	public static final String MINECRAFT = "${mcversion}";
	public static final String PROXY_SERVER = "com.kamesuta.mc.signpic.CommonProxy";
	public static final String PROXY_CLIENT = "com.kamesuta.mc.signpic.ClientProxy";
	public static final String GUI_FACTORY = "com.kamesuta.mc.signpic.gui.config.ConfigGuiFactory";

	public static Logger logger = LogManager.getLogger(Reference.MODID);
}
