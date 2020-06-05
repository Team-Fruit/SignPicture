package net.teamfruit.bnnwidget.component;

import javax.annotation.Nonnull;

import net.minecraft.util.ResourceLocation;
import net.teamfruit.bnnwidget.WEvent;
import net.teamfruit.bnnwidget.WPanel;
import net.teamfruit.bnnwidget.compat.OpenGL;
import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.position.Point;
import net.teamfruit.bnnwidget.position.R;
import net.teamfruit.bnnwidget.render.RenderOption;
import net.teamfruit.bnnwidget.render.WRenderer;

/**
 * Minecraftデザインのパネルコンポーネントです。
 *
 * @author TeamFruit
 */
public class MPanel extends WPanel {
	/**
	 * BnnWidget同封のMinecraftデザイン、パネルです。
	 */
	public static final @Nonnull ResourceLocation background = new ResourceLocation("bnnwidget", "textures/gui/background.png");

	public MPanel(final @Nonnull R position) {
		super(position);
	}

	@Override
	public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
		final Area a = getGuiPosition(pgp);
		final float op = getGuiOpacity(popacity);

		WRenderer.startTexture();
		OpenGL.glColor4f(1.0F, 1.0F, 1.0F, op);
		texture().bindTexture(background);
		drawBack(a);

		super.draw(ev, pgp, p, frame, popacity, opt);
	}

	/**
	 * 背景を描画します
	 * @param a 絶対座標
	 */
	public static void drawBack(final @Nonnull Area a) {
		drawTextureModal(Area.size(a.x1(), a.y1(), a.w()/2, a.h()/2), null, Area.size(0, 0, a.w()/2, a.h()/2));
		drawTextureModal(Area.size(a.x1()+a.w()/2, a.y1(), a.w()/2, a.h()/2), null, Area.size(256-a.w()/2, 0, a.w()/2, a.h()/2));
		drawTextureModal(Area.size(a.x1(), a.y1()+a.h()/2, a.w()/2, a.h()/2), null, Area.size(0, 256-a.h()/2, a.w()/2, a.h()/2));
		drawTextureModal(Area.size(a.x1()+a.w()/2, a.y1()+a.h()/2, a.w()/2, a.h()/2), null, Area.size(256-a.w()/2, 256-a.h()/2, a.w()/2, a.h()/2));
	}
}
