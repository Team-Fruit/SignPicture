package com.kamesuta.mc.signpic;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.handler.CoreEvent;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public final class Config extends Configuration {
	public static Config instance;

	private final File configFile;
	public boolean updatable;

	public int imageWidthLimit = 512;
	public int imageHeightLimit = 512;
	public boolean imageAnimationGif = true;

	public int entryGCtick = 15 * 20;

	public int communicateThreads = 3;

	public int contentLoadThreads = 3;
	public int contentMaxByte = 0;
	public int contentGCtick = 15 * 20;
	public int contentLoadTick = 0;
	public int contentSyncTick = 0;

	public boolean informationNotice = true;
	public boolean informationJoinBeta = false;

	public boolean multiplayPAAS = true;
	/** Fastest time "possible" estimate for an empty sign. */
	public int multiplayPAASMinEditTime = 150;
	/** Minimum time needed to add one extra line (not the first). */
	public int multiplayPAASMinLineTime = 50;
	/** Minimum time needed to type a character. */
	public int multiplayPAASMinCharTime = 50;

	public float renderSeeOpacity = .5f;
	public float renderPreviewFixedOpacity = .7f;
	public float renderPreviewFloatedOpacity = .7f;

	public Config( final File configFile ) {
		super( configFile );
		this.configFile = configFile;

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

		addCustomCategoryComment("MultiplayPreventAntiAutoSign", "Prevent from Anti-AutoSign Plugin such as NoCheatPlus. (ms)");

		changeableSync();

		this.updatable = true;
	}

	private void changeableSync() {
		this.entryGCtick = get( "Entry", "GCDelayTick", this.entryGCtick ).getInt( this.entryGCtick );

		this.communicateThreads = addComment(get( "Http", "HttpThreads", this.communicateThreads ), "parallel processing number such as Downloading").setRequiresMcRestart(true).getInt( this.communicateThreads );

		this.contentLoadThreads = addComment(get( "Content", "LoadThreads", this.contentLoadThreads ), "parallel processing number such as Image Loading").setRequiresMcRestart(true).getInt( this.contentLoadThreads );
		this.contentMaxByte = addComment(get( "Content", "MaxByte", this.contentMaxByte ), "limit of size before downloading").getInt( this.contentMaxByte );
		this.contentGCtick = addComment(get( "Content", "GCDelayTick", this.contentGCtick ), "delay ticks of Garbage Collection").getInt( this.contentGCtick );
		this.contentLoadTick = addComment(get( "Content", "LoadStartIntervalTick", this.contentLoadTick ), "ticks of Load process starting delay (Is other threads, it does not disturb the operation) such as Downloading, File Loading...").getInt( this.contentLoadTick );
		this.contentSyncTick = addComment(get( "Content", "SyncLoadIntervalTick", this.contentSyncTick ), "ticks of Sync process interval (A drawing thread, affects the behavior. Please increase the value if the operation is heavy.) such as Gl Texture Uploading").getInt( this.contentSyncTick );

		this.multiplayPAAS = get( "MultiplayPreventAntiAutoSign", "Enable", this.multiplayPAAS ).getBoolean( this.multiplayPAAS );
		this.multiplayPAASMinEditTime = get( "MultiplayPreventAntiAutoSign", "minEditTime", this.multiplayPAASMinEditTime ).getInt( this.multiplayPAASMinEditTime );
		this.multiplayPAASMinLineTime = get( "MultiplayPreventAntiAutoSign", "minLineTime", this.multiplayPAASMinLineTime ).getInt( this.multiplayPAASMinLineTime );
		this.multiplayPAASMinCharTime = get( "MultiplayPreventAntiAutoSign", "minCharTime", this.multiplayPAASMinCharTime ).getInt( this.multiplayPAASMinCharTime );

		this.renderSeeOpacity = (float) get( "Render", "ViewSignOpacity", this.renderSeeOpacity ).getDouble( this.renderSeeOpacity );
		this.renderPreviewFixedOpacity = (float) get( "Render", "PreviewFixedSignOpacity", this.renderPreviewFixedOpacity ).getDouble( this.renderPreviewFixedOpacity );
		this.renderPreviewFloatedOpacity = (float) get( "Render", "PreviewFloatedSignOpacity", this.renderPreviewFloatedOpacity ).getDouble( this.renderPreviewFloatedOpacity );
	}

	private Property addComment(final Property prop, final String comment) {
		prop.comment = comment;
		return prop;
	}

	@Override
	public void save()
	{
		if( hasChanged() )
		{
			super.save();
		}
	}

	@CoreEvent
	public void onConfigChanged( final ConfigChangedEvent.OnConfigChangedEvent eventArgs )
	{
		if( StringUtils.equals(eventArgs.modID, Reference.MODID) )
		{
			changeableSync();

			if( this.updatable )
			{
				save();
			}
		}
	}

	public String getFilePath()
	{
		return this.configFile.getPath();
	}
}