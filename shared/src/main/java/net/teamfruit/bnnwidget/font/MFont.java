package net.teamfruit.bnnwidget.font;

import javax.annotation.Nonnull;

import net.minecraft.client.gui.FontRenderer;
import net.teamfruit.bnnwidget.compat.Compat.CompatFontRenderer;
import net.teamfruit.bnnwidget.compat.OpenGL;

public class MFont implements WFont {
	private final @Nonnull CompatFontRenderer font;

	public MFont(final @Nonnull FontRenderer font) {
		this.font = new CompatFontRenderer(font);
	}

	@Override
	public void drawString(final FontPosition p) {
		final String text = p.getText();
		final float scale = p.getScale();
		final float width = this.font.getStringWidth(text)*scale;
		final float height = this.font.getFontRendererObj().FONT_HEIGHT*scale;
		float x;
		switch (p.getAlign()) {
			default:
			case LEFT:
				x = p.getX();
				break;
			case CENTER:
				x = p.getX()-width/2;
				break;
			case RIGHT:
				x = p.getX()-width;
				break;
		}
		float y;
		switch (p.getVAlign()) {
			default:
			case TOP:
				y = p.getY();
				break;
			case MIDDLE:
				y = p.getY()-height/2;
				break;
			case BOTTOM:
				y = p.getY()-height;
				break;
		}
		final int color = OpenGL.glGetColorRGBA();
		OpenGL.glColorRGBA(0xffffffff);
		OpenGL.glPushMatrix();
		OpenGL.glTranslatef(x, y, 0);
		OpenGL.glScalef(scale, scale, 1);
		this.font.drawString(p.getText(), 0, 0, color, false);
		OpenGL.glPopMatrix();
		OpenGL.glColorRGBA(color);
	}

	@Override
	public float getWidth(final FontPosition p) {
		return this.font.getStringWidthWithoutFormattingCodes(p.getText())*p.getScale();
	}

	@Override
	public float getHeight(final FontPosition p) {
		return this.font.getFontRendererObj().FONT_HEIGHT*p.getScale();
	}
}
