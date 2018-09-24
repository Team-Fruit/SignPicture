package com.kamesuta.mc.signpic;

import javax.annotation.Nonnull;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Timer;

import com.kamesuta.mc.signpic.compat.CompatEvents.CompatConfigChangedEvent;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatGuiOpenEvent;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatGuiScreenEvent;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatHandler;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatInputEvent;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatItemTooltipEvent;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatModelBakeEvent;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatModelRegistryEvent;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatMouseEvent;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatRenderGameOverlayEvent;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatRenderWorldLastEvent;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatTextureStitchEvent;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatTickEvent;
import com.kamesuta.mc.signpic.entry.EntryManager;
import com.kamesuta.mc.signpic.entry.EntrySlot;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.gui.GuiTask;
import com.kamesuta.mc.signpic.gui.OverlayFrame;
import com.kamesuta.mc.signpic.handler.KeyHandler;
import com.kamesuta.mc.signpic.handler.SignHandler;
import com.kamesuta.mc.signpic.http.Communicator;
import com.kamesuta.mc.signpic.http.ICommunicate;
import com.kamesuta.mc.signpic.http.ICommunicateCallback;
import com.kamesuta.mc.signpic.information.Informations;
import com.kamesuta.mc.signpic.render.SignPicRender;
import com.kamesuta.mc.signpic.state.Progressable;
import com.kamesuta.mc.signpic.state.State;

public class CoreHandler extends CompatHandler {
	public final @Nonnull Config configHandler = Config.getConfig();
	public final @Nonnull KeyHandler keyHandler = KeyHandler.instance;
	public final @Nonnull SignHandler signHandler = new SignHandler();
	public final @Nonnull EntryManager signEntryManager = EntryManager.instance;
	public final @Nonnull ContentManager contentManager = ContentManager.instance;
	public final @Nonnull SignPicRender renderHandler = new SignPicRender();
	public final @Nonnull OverlayFrame overlayHandler = OverlayFrame.instance;
	public final @Nonnull Informations informationHandler = Informations.instance;
	public final @Nonnull Apis apiHandler = Apis.instance;

	public void init() {
		registerHandler();
		KeyHandler.init();
		SignHandler.init();
		this.informationHandler.init();
		this.apiHandler.init();
	}

	@Override
	@CoreEvent
	public void onKeyInput(final @Nonnull CompatInputEvent event) {
		this.keyHandler.onKeyInput(event);
	}

	@Override
	@CoreEvent
	public void onRenderTick(final @Nonnull CompatTickEvent.CompatRenderTickEvent event) {
		Timer.tick();
	}

	@Override
	@CoreEvent
	public void onSign(final @Nonnull CompatGuiOpenEvent event) {
		this.signHandler.onSign(event);
	}

	@Override
	@CoreEvent
	public void onClick(final @Nonnull CompatMouseEvent event) {
		this.signHandler.onClick(event);
	}

	@Override
	@CoreEvent
	public void onTooltip(final @Nonnull CompatItemTooltipEvent event) {
		this.signHandler.onTooltip(event);
	}

	@Override
	@CoreEvent
	public void onRender(final @Nonnull CompatRenderWorldLastEvent event) {
		this.renderHandler.onRender(event);
	}

	@Override
	@CoreEvent()
	public void onDraw(final @Nonnull CompatRenderGameOverlayEvent.CompatPost event) {
		this.renderHandler.onDraw(event);
		this.overlayHandler.onDraw(event);
	}

	@Override
	@CoreEvent()
	public void onDraw(final @Nonnull CompatGuiScreenEvent.CompatDrawScreenEvent.CompatPost event) {
		this.overlayHandler.onDraw(event);
		this.signHandler.onDraw(event);
	}

	@Override
	@CoreEvent
	public void onText(final @Nonnull CompatRenderGameOverlayEvent.CompatText event) {
		this.renderHandler.onText(event);
	}

	@Override
	@CoreEvent
	public void onConfigChanged(final @Nonnull CompatConfigChangedEvent.CompatOnConfigChangedEvent event) {
		this.configHandler.onConfigChanged(event);
	}

	@Override
	@CoreEvent
	public void onResourceReloaded(final @Nonnull CompatTextureStitchEvent.CompatPost event) {
		this.contentManager.onResourceReloaded(event);
	}

	@Override
	@CoreEvent
	public void onTick(final @Nonnull CompatTickEvent.CompatClientTickEvent event) {
		if (event.getTickPhase()==CompatTickEvent.CompatPhase.END) {
			Client.startSection("signpic_load");
			debugKey();
			// this.keyHandler.onTick();
			this.signEntryManager.onTick();
			this.signHandler.onTick();
			this.contentManager.onTick();
			this.overlayHandler.onTick(event);
			this.informationHandler.onTick(event);
			EntrySlot.Tick();
			Client.endSection();
		}
	}

	@CoreEvent
	@Override
	public void onModelRegistry(final CompatModelRegistryEvent event) {
		Client.itemRenderer.registerModelRegistry(event);
	}

	@Override
	@CoreEvent
	public void onModelBakeEvent(final @Nonnull CompatModelBakeEvent event) {
		Client.itemRenderer.registerModelBakery(event);
	}

	private boolean debugKey;

	private void debugKey() {
		if (Keyboard.isKeyDown(Keyboard.KEY_I)&&Keyboard.isKeyDown(Keyboard.KEY_O)&&Keyboard.isKeyDown(Keyboard.KEY_P)) {
			if (!this.debugKey)
				debug();
			this.debugKey = true;
		} else
			this.debugKey = false;
	}

	private void debug() {
		// Client.openEditor();
		// Reference.logger.info("try to delete: "+Client.location.modFile.getName());
		// Client.deleteMod();
		// Client.notice("Debug Message!", 3f);
		final DebugCommunicate debug = new DebugCommunicate();
		debug.getState().getMeta().put(GuiTask.HighlightPanel, true);
		Communicator.instance.communicate(debug);
		// Log.log.info(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName("net/minecraft/client/gui/GuiNewChat", "resetScroll", DescHelper.toDesc(void.class, new Object[0])));
		// Log.log.info(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName("net/minecraft/client/gui/GuiNewChat", "resetScroll", DescHelper.toDesc(void.class, new Object[0])));
	}

	static class DebugCommunicate implements ICommunicate, Progressable {
		private final @Nonnull State state = new State();
		{
			this.state.setName("Debug Progress").getProgress().setOverall(10);
		}

		@Override
		public void cancel() {
		}

		@Override
		public @Nonnull State getState() {
			return this.state;
		}

		@Override
		public void communicate() {
			try {
				for (int i = 0; i<10; i++) {
					Thread.sleep(100);
					this.state.getProgress().setDone(i+1);
				}
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setCallback(final @Nonnull ICommunicateCallback callback) {
		}
	}
}
