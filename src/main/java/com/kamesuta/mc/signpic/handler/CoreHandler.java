package com.kamesuta.mc.signpic.handler;

import org.lwjgl.util.Timer;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.entry.EntryManager;
import com.kamesuta.mc.signpic.entry.EntrySlot;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.gui.OverlayFrame;
import com.kamesuta.mc.signpic.information.InformationChecker;
import com.kamesuta.mc.signpic.render.SignPicRender;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

public class CoreHandler {
	public final Config configHandler = Config.instance;
	public final KeyHandler keyHandler = new KeyHandler();
	public final SignHandler signHandler = new SignHandler();
	public final EntryManager signEntryManager = EntryManager.instance;
	public final ContentManager contentManager = ContentManager.instance;
	public final SignPicRender renderHandler = new SignPicRender();
	public final OverlayFrame overlayHandler = OverlayFrame.instance;
	public final InformationChecker informationHandler = InformationChecker.instance;

	public void init() {
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
		KeyHandler.init();
		SignHandler.init();
		this.informationHandler.init();
	}

	@SubscribeEvent
	public void onKeyInput(final InputEvent event) {
		this.keyHandler.onKeyInput(event);
	}

	@SubscribeEvent
	public void onRenderTick(final TickEvent.RenderTickEvent event) {
		Timer.tick();
	}

	@SubscribeEvent
	public void onSign(final GuiOpenEvent event) {
		this.signHandler.onSign(event);
	}

	@SubscribeEvent
	public void onClick(final MouseEvent event) {
		this.signHandler.onClick(event);
	}

	@SubscribeEvent
	public void onRender(final RenderWorldLastEvent event) {
		this.renderHandler.onRender(event);
	}

	@SubscribeEvent()
	public void onDraw(final RenderGameOverlayEvent.Post event) {
		this.renderHandler.onDraw(event);
		this.overlayHandler.onDraw(event);
	}

	@SubscribeEvent()
	public void onDraw(final GuiScreenEvent.DrawScreenEvent.Post event) {
		this.overlayHandler.onDraw(event);
	}

	@SubscribeEvent
	public void onText(final RenderGameOverlayEvent.Text event) {
		this.renderHandler.onText(event);
	}

	@SubscribeEvent
	public void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		this.configHandler.onConfigChanged(eventArgs);
	}

	@SubscribeEvent
	public void onTick(final ClientTickEvent event) {
		if (event.phase==Phase.END) {
			Client.startSection("signpic_load");
			this.signEntryManager.onTick();
			this.contentManager.onTick();
			this.overlayHandler.onTick(event);
			this.informationHandler.onTick(event);
			EntrySlot.Tick();
			Client.endSection();
		}
	}
}
