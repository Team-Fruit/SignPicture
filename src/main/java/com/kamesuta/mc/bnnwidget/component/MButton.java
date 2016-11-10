package com.kamesuta.mc.bnnwidget.component;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.render.RenderHelper;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class MButton extends WBase {
	public static final ResourceLocation button = new ResourceLocation("signpic", "textures/gui/buttons.png");

	public String text;
	public String actionCommand;
	private boolean isEnabled = true;

	public MButton(final R position, final String text) {
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
	public boolean mouseClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
		final Area abs = getGuiPosition(pgp);
		if (abs.pointInside(p)) {
			if (isEnabled()) {
			if (onClicked(ev, pgp, p, button)) {
				if (this.actionCommand != null)
					ev.eventDispatch(this.actionCommand, Integer.valueOf(button));
				mc.getSoundHandler()
				.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			}
		}
			return true;
	}
		return false;
	}

	protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
		return true;
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity) {
		drawButtonTex(ev, pgp, p, frame);
		if (this.text != null) {
			drawText(ev, pgp, p, frame);
		}
	}

	protected void drawButtonTex(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area a = getGuiPosition(pgp);
		RenderHelper.startTexture();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		texture().bindTexture(button);
		final int state = getButtonTex(ev, pgp, p, frame);

		drawTexturedModalRect(a.x1(), a.y1(), 0, state * 80, a.w() / 2, a.h() / 2);
		drawTexturedModalRect(a.x1() + a.w() / 2, a.y1(), 256 - a.w() / 2, state * 80, a.w() / 2, a.h() / 2);
		drawTexturedModalRect(a.x1(), a.y1() + a.h() / 2, 0, state * 80 + 80 - a.h() / 2, a.w() / 2, a.h() / 2);
		drawTexturedModalRect(a.x1() + a.w() / 2, a.y1() + a.h() / 2, 256 - a.w() / 2, state * 80 + 80 - a.h() / 2, a.w() / 2, a.h() / 2);
	}

	public int getButtonTex(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area abs = getGuiPosition(pgp);
		return !isEnabled() ? 0 : abs.pointInside(p) ? 2 : 1;
	}

	public void drawText(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area abs = getGuiPosition(pgp);
		RenderHelper.startTexture();
		drawCenteredString(this.text, abs.x1() + abs.w() / 2, abs.y1() + (abs.h() - 8) / 2,
				getTextColour(ev, pgp, p, frame));
	}

	public int getTextColour(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area abs = getGuiPosition(pgp);
		return abs.pointInside(p) ? -96 : !isEnabled() ? -6250336 : -2039584;
	}

	public MButton setActionCommand(final String string) {
		this.actionCommand = string;
		return this;
	}
}
