package com.kamesuta.mc.guiwidget.component;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.guiwidget.WBase;
import com.kamesuta.mc.guiwidget.WEvent;
import com.kamesuta.mc.guiwidget.position.Point;
import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.relative.RCommon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class MButton extends WBase {
	public String text;
	public String actionCommand;
	private boolean isEnabled = true;
	public boolean visible = true;

	public MButton(final RCommon position, final String text) {
		super(position);
		this.text = text;
	}

	public void setText(final String s) {
		this.text = s;
	}

	public boolean isEnabled() {
		return this.isEnabled;
	}

	public void setEnabled(final boolean b) {
		this.isEnabled = b;
	}

	@Override
	public void mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
		final Area abs = getGuiPosition(pgp);
		if ((this.isEnabled) && (abs.pointInside(p))) {
			if (onClicked(ev, pgp, p, button)) {
				if (this.actionCommand != null)
					ev.eventDispatch(this.actionCommand, Integer.valueOf(button));
				Minecraft.getMinecraft().getSoundHandler()
				.playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
			}
		}
	}

	protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
		return true;
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		if (!this.visible) {
			return;
		}
		drawButtonTex(ev, pgp, p, frame);
		if (this.text != null) {
			drawText(ev, pgp, p, frame);
		}
	}

	protected void drawButtonTex(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area abs = getGuiPosition(pgp);
		glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(guiTex);
		final int state = getButtonTex(ev, pgp, p, frame);
		drawTexturedModalRect(abs.x1(), abs.y1(), 0, 46 + state * 20, abs.w() / 2, abs.h() / 2);
		drawTexturedModalRect(abs.x1() + abs.w() / 2, abs.anc_y(), 200 - abs.w() / 2, 46 + state * 20, abs.w() / 2,
				abs.h() / 2);
		drawTexturedModalRect(abs.x1(), abs.y1() + abs.h() / 2, 0, 46 + state * 20 + 20 - abs.h() / 2,
				abs.w() / 2, abs.h() / 2);
		drawTexturedModalRect(abs.x1() + abs.w() / 2, abs.y1() + abs.h() / 2, 200 - abs.w() / 2,
				46 + state * 20 + 20 - abs.h() / 2, abs.w() / 2, abs.h() / 2);
	}

	public int getButtonTex(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area abs = getGuiPosition(pgp);
		return abs.pointInside(p) ? 2 : !this.isEnabled ? 0 : 1;
	}

	public void drawText(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area abs = getGuiPosition(pgp);
		drawCenteredString(mc.fontRenderer, this.text, abs.anc_x() + abs.w() / 2, abs.anc_y() + (abs.h() - 8) / 2,
				getTextColour(ev, pgp, p, frame));
	}

	public int getTextColour(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area abs = getGuiPosition(pgp);
		return abs.pointInside(p) ? -96 : !this.isEnabled ? -6250336 : -2039584;
	}

	public MButton setActionCommand(final String string) {
		this.actionCommand = string;
		return this;
	}
}
