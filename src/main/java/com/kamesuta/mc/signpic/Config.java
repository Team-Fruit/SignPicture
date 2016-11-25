package com.kamesuta.mc.signpic;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;

public final class Config extends Configuration {
	public static Config instance;

	private final File configFile;
	public boolean updatable;

	public String signpicDir = "";

	public int imageWidthLimit = 512;
	public int imageHeightLimit = 512;
	public boolean imageAnimationGif = true;

	public int entryGCtick = 15*20;

	public int communicateThreads = 3;

	public int contentLoadThreads = 3;
	public int contentMaxByte = 32*1024*1024;
	public int contentGCtick = 15 * 20;
	public int contentLoadTick = 0;
	public int contentSyncTick = 0;

	public boolean informationNotice = true;
	public boolean informationJoinBeta = false;
	public boolean informationUpdateGui = true;
	public boolean informationTryNew = false;

	public boolean multiplayPAAS = true;
	/** Fastest time "possible" estimate for an empty sign. */
	public int multiplayPAASMinEditTime = 150;
	/** Minimum time needed to add one extra line (not the first). */
	public int multiplayPAASMinLineTime = 50;
	/** Minimum time needed to type a character. */
	public int multiplayPAASMinCharTime = 50;

	public boolean renderOverlayPanel = true;
	public boolean renderGuiOverlay = true;
	public boolean renderUseMipmap = true;
	public boolean renderMipmapTypeNearest = false;
	public float renderSeeOpacity = .5f;
	public float renderPreviewFixedOpacity = .7f;
	public float renderPreviewFloatedOpacity = .7f*.7f;

	public String apiType = "";
	public String apiKey = "";

	public Config( final File configFile ) {
		super( configFile );
		this.configFile = configFile;

		this.signpicDir = get("General", "SignpicDir", this.signpicDir).setRequiresMcRestart(true).getString();

		this.imageWidthLimit = get( "Image", "WidthLimit", this.imageWidthLimit ).setRequiresMcRestart(true).getInt( this.imageWidthLimit );
		this.imageHeightLimit = get( "Image", "HeightLimit", this.imageHeightLimit ).setRequiresMcRestart(true).getInt( this.imageHeightLimit );
		this.imageAnimationGif = get( "Image", "AnimateGif", this.imageAnimationGif ).setRequiresMcRestart(true).getBoolean( this.imageAnimationGif );

		addCustomCategoryComment("Entry", "Entry(sign text parse cache) Management");

		addCustomCategoryComment("Content", "Content Data Management");

		this.informationNotice = get( "Version", "Notice", this.informationNotice ).setRequiresMcRestart(true).getBoolean( this.informationNotice );

		final Property joinBeta = get( "Version", "JoinBeta", this.informationJoinBeta );
		final String[] v = StringUtils.split(Reference.VERSION, "\\.");
		if (v.length>=4 &&StringUtils.equals(v[3], "beta")) {
			this.informationJoinBeta = true;
			joinBeta.set(true);
		}
		this.informationJoinBeta = joinBeta.setRequiresMcRestart(true).getBoolean( this.informationJoinBeta );

		addCustomCategoryComment("Multiplay.PreventAntiAutoSign", "Prevent from Anti-AutoSign Plugin such as NoCheatPlus. (ms)");

		addCustomCategoryComment("Api.Upload", "Api Upload Settings");

		changeableSync();

		this.updatable = true;
	}

	private void changeableSync() {
		this.entryGCtick = get( "Entry", "GCDelayTick", this.entryGCtick ).getInt( this.entryGCtick );

		this.communicateThreads = addComment(get("Http", "HttpThreads", this.communicateThreads), "parallel processing number such as Downloading").setRequiresMcRestart(true).getInt(this.communicateThreads);

		this.contentLoadThreads = addComment(get("Content", "LoadThreads", this.contentLoadThreads), "parallel processing number such as Image Loading").setRequiresMcRestart(true).getInt(this.contentLoadThreads);
		this.contentMaxByte = addComment(get("Content", "MaxByte", this.contentMaxByte), "limit of size before downloading. 0 is infinity.").getInt(this.contentMaxByte);
		this.contentGCtick = addComment(get( "Content", "GCDelayTick", this.contentGCtick ), "delay ticks of Garbage Collection").getInt( this.contentGCtick );
		this.contentLoadTick = addComment(get("Content", "LoadStartIntervalTick", this.contentLoadTick), "ticks of Load process starting delay (Is other threads, it does not disturb the operation) such as Downloading, File Loading...").getInt(this.contentLoadTick);
		this.contentSyncTick = addComment(get( "Content", "SyncLoadIntervalTick", this.contentSyncTick ), "ticks of Sync process interval (A drawing thread, affects the behavior. Please increase the value if the operation is heavy.) such as Gl Texture Uploading").getInt( this.contentSyncTick );

		this.informationUpdateGui = get("Version", "UpdateGui", this.informationUpdateGui).getBoolean(this.informationUpdateGui);
		this.informationTryNew = get("Version", "TryNew", this.informationTryNew).getBoolean(this.informationTryNew);

		this.multiplayPAAS = get("Multiplay.PreventAntiAutoSign", "Enable", this.multiplayPAAS).getBoolean(this.multiplayPAAS);
		this.multiplayPAASMinEditTime = get("Multiplay.PreventAntiAutoSign.Time", "minEditTime", this.multiplayPAASMinEditTime).getInt(this.multiplayPAASMinEditTime);
		this.multiplayPAASMinLineTime = get("Multiplay.PreventAntiAutoSign.Time", "minLineTime", this.multiplayPAASMinLineTime).getInt(this.multiplayPAASMinLineTime);
		this.multiplayPAASMinCharTime = get("Multiplay.PreventAntiAutoSign.Time", "minCharTime", this.multiplayPAASMinCharTime).getInt(this.multiplayPAASMinCharTime);

		this.renderOverlayPanel = addComment(get("Render", "OverlayPanel", this.renderOverlayPanel), "Overlay signpic!online").getBoolean(this.renderOverlayPanel);
		this.renderGuiOverlay = addComment(get("Render", "GuiOverlay", this.renderGuiOverlay), "Overlay on GUI").getBoolean(this.renderGuiOverlay);
		this.renderUseMipmap = addComment(get("Render", "Mipmap", this.renderUseMipmap), "Require OpenGL 3.0 or later").getBoolean(this.renderUseMipmap);
		this.renderMipmapTypeNearest = addComment(get("Render", "MipmapTypeNearest", this.renderMipmapTypeNearest), "true = Nearest, false = Linear").getBoolean(this.renderMipmapTypeNearest);
		this.renderSeeOpacity = (float) get("Render.Opacity", "ViewSign", this.renderSeeOpacity).getDouble(this.renderSeeOpacity);
		this.renderPreviewFixedOpacity = (float) get("Render.Opacity", "PreviewFixedSign", this.renderPreviewFixedOpacity).getDouble(this.renderPreviewFixedOpacity);
		this.renderPreviewFloatedOpacity = (float) get("Render.Opacity", "PreviewFloatedSign", this.renderPreviewFloatedOpacity).getDouble(this.renderPreviewFloatedOpacity);

		this.apiType = get("Api.Upload", "Type", this.apiType).getString();
		this.apiKey = get("Api.Upload", "Key", this.apiKey).getString();
	}

	private Property addComment(final Property prop, final String comment) {
		prop.setComment(comment);
		return prop;
	}

	@Override
	public void save() {
		if( hasChanged() )
			super.save();
		changeableSync();
		}

	@CoreEvent
	public void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if( StringUtils.equals(eventArgs.getModID(), Reference.MODID) )
			if( this.updatable )
				save();
			}

	public String getFilePath() {
		return this.configFile.getPath();
	}
}