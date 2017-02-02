package com.kamesuta.mc.signpic.gui;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.handler.KeyHandler;

import net.minecraft.client.gui.GuiScreen;

public class GuiHub extends WFrame {
	public static final Key keySignPicture = new Key.AbstractKey() {
		@Override
		public int getCode() {
			return KeyHandler.Keys.KEY_BINDING_GUI.binding.getKeyCode();
		}
	};

	public static final Key keyScreenShotFull = new Key.FixedKey(Keyboard.KEY_O);
	public static final Key keyScreenShot = new Key.FixedKey(Keyboard.KEY_P);
	public static final Key keySwingScreenShot = new Key.FixedKey(Keyboard.KEY_I);

	public GuiHub(final @Nullable GuiScreen parent) {
		super(parent);
	}

	public GuiHub() {
	}

	{
		setGuiPauseGame(false);
	}

	@Override
	protected void initWidget() {
		add(new WPanel(new R()) {
			private boolean close;

			@Override
			public void update(final WEvent ev, final Area pgp, final Point p) {
				if (this.close) {
					if (mc.currentScreen==GuiHub.this)
						requestClose();
				} else if (!keySignPicture.isKeyPressed()) {
					this.close = true;
					Client.openEditor();
				}
				super.update(ev, pgp, p);
			}

			@Override
			public boolean keyTyped(final WEvent ev, final Area pgp, final Point p, final char c, final int keycode) {
				if (!this.close) {
					if (keycode==keyScreenShot.getCode()) {
						this.close = true;
						mc.displayGuiScreen(new GuiIngameScreenShot(mc.currentScreen));
					} else if (keycode==keyScreenShotFull.getCode()) {
						this.close = true;
						final GuiIngameScreenShot shot = new GuiIngameScreenShot(mc.currentScreen);
						mc.displayGuiScreen(shot);
						shot.takeFullScreenshot();
					} else if (keycode==keySwingScreenShot.getCode()) {
						this.close = true;
						mc.displayGuiScreen(new GuiWindowScreenShot(mc.currentScreen));
					}
					;
				}
				return super.keyTyped(ev, pgp, p, c, keycode);
			}
		});
	}

	public static interface Key {
		int getCode();

		boolean isKeyPressed();

		public static abstract class AbstractKey implements Key {
			@Override
			public boolean isKeyPressed() {
				return Keyboard.isKeyDown(getCode());
			}
		}

		public static class FixedKey extends AbstractKey {
			protected int code;

			public FixedKey(final int code) {
				this.code = code;
			}

			@Override
			public int getCode() {
				return code;
			}
		}
	}
}
