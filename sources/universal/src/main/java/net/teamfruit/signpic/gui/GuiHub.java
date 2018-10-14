package net.teamfruit.signpic.gui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.teamfruit.bnnwidget.WEvent;
import net.teamfruit.bnnwidget.WFrame;
import net.teamfruit.bnnwidget.WPanel;
import net.teamfruit.bnnwidget.component.FontScaledLabel;
import net.teamfruit.bnnwidget.font.WFont;
import net.teamfruit.bnnwidget.motion.Easings;
import net.teamfruit.bnnwidget.motion.Motion;
import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.position.Coord;
import net.teamfruit.bnnwidget.position.Point;
import net.teamfruit.bnnwidget.position.R;
import net.teamfruit.bnnwidget.render.WGui.Align;
import net.teamfruit.bnnwidget.var.V;
import net.teamfruit.bnnwidget.var.VMotion;
import net.teamfruit.signpic.handler.KeyHandler;

public class GuiHub extends WFrame {
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
		add(new WPanel(new R(Coord.right(10), Coord.top(5), Coord.left(10), Coord.bottom(5))) {
			protected @Nonnull VMotion o = V.pm(0f).add(Motion.blank(1.5f).setAfter(() -> {
				add(new FontScaledLabel(new R(Coord.top(0), Coord.height(9)), WFont.fontRenderer).setText(I18n.format("signpic.over.hub.usage.takerange", KeyHandler.keyScreenShot.getName())).setAlign(Align.LEFT).setShadow(true));
				add(new FontScaledLabel(new R(Coord.top(9), Coord.height(9)), WFont.fontRenderer).setText(I18n.format("signpic.over.hub.usage.takeall", KeyHandler.keyScreenShotFull.getName())).setAlign(Align.LEFT).setShadow(true));
				add(new FontScaledLabel(new R(Coord.top(18), Coord.height(9)), WFont.fontRenderer).setText(I18n.format("signpic.over.hub.usage.takerangewindow", KeyHandler.keySwingScreenShot.getName())).setAlign(Align.LEFT).setShadow(true));
				add(new FontScaledLabel(new R(Coord.top(27), Coord.height(9)), WFont.fontRenderer).setText(I18n.format("signpic.over.hub.usage.release")).setAlign(Align.LEFT).setShadow(true));
			})).add(Easings.easeLinear.move(1f, 1f)).start();

			{
				this.opacity = this.o;
			}

			@Override
			protected void initWidget() {

			}
		});
	}

	@Override
	public void update(final WEvent ev, final Area pgp, final Point p) {
		KeyHandler.instance.keyHook(getScreen());
		if (KeyHandler.keySignPicture.isKeyPressed())
			KeyHandler.instance.keyHook(getScreen());
		else
			WFrame.displayFrame(new GuiMain(getScreen()));
		if (!ev.isCurrent())
			requestClose();
		super.update(ev, pgp, p);
	}
}
