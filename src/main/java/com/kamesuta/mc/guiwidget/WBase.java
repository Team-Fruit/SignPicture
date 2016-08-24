package com.kamesuta.mc.guiwidget;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;

import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.Coord;
import com.kamesuta.mc.guiwidget.position.RArea;
import com.kamesuta.mc.guiwidget.position.RCommon;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public abstract class WBase extends WComponent {
	protected static final ResourceLocation guiTex = new ResourceLocation("textures/gui/widgets.png");

	protected RCommon position;

	public WBase(final RCommon position) {
		this.position = position;
	}

	public RCommon getGuiRelative() {
		return this.position;
	}

	public Area getGuiPosition(final Area pgp) {
		return pgp.child(getGuiRelative());
	}

	public static void draw(final Area p, final int mode) {
		final Tessellator t = Tessellator.instance;
		t.startDrawing(mode);
		t.addVertex(p.x1(), p.y1(), 0);
		t.addVertex(p.x1(), p.y2(), 0);
		t.addVertex(p.x2(), p.y2(), 0);
		t.addVertex(p.x2(), p.y1(), 0);
		t.draw();
	}

	public static RArea horizontalLine = new RArea(Coord.left(0), Coord.top(0), Coord.right(0), Coord.top(1));
	public static void drawHorizontalLine(final Area a)
	{
		drawRect(a.child(horizontalLine));
	}

	public static RArea verticalLine = new RArea(Coord.left(0), Coord.top(0), Coord.left(1), Coord.bottom(0));
	public static void drawVerticalLine(final Area a)
	{
		drawRect(a.child(verticalLine));
	}

	public static void drawRect(final Area a)
	{
		final Tessellator tessellator = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		tessellator.startDrawingQuads();
		tessellator.addVertex(a.x1(), a.y2(), .0);
		tessellator.addVertex(a.x2(), a.y2(), .0);
		tessellator.addVertex(a.x2(), a.y1(), .0);
		tessellator.addVertex(a.x1(), a.y1(), .0);
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	/**
	 * Draws a rectangle with a vertical gradient between the specified colors.
	 */
	public static void drawGradientRect(final Area a, final int color1, final int color2)
	{
		final float f = (color1 >> 24 & 255) / 255.0F;
		final float f1 = (color1 >> 16 & 255) / 255.0F;
		final float f2 = (color1 >> 8 & 255) / 255.0F;
		final float f3 = (color1 & 255) / 255.0F;
		final float f4 = (color2 >> 24 & 255) / 255.0F;
		final float f5 = (color2 >> 16 & 255) / 255.0F;
		final float f6 = (color2 >> 8 & 255) / 255.0F;
		final float f7 = (color2 & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(f1, f2, f3, f);
		tessellator.addVertex(a.x2(), a.y1(), 0f);
		tessellator.addVertex(a.x1(), a.y1(), 0f);
		tessellator.setColorRGBA_F(f5, f6, f7, f4);
		tessellator.addVertex(a.x1(), a.y2(), 0f);
		tessellator.addVertex(a.x2(), a.y2(), 0f);
		tessellator.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	/**
	 * Renders the specified text to the screen, center-aligned.
	 */
	public void drawCenteredString(final FontRenderer font, final String text, final Area a, final int color)
	{
		glPushMatrix();
		glTranslatef(a.x1(), a.x2(), 0f);
		font.drawStringWithShadow(text, - font.getStringWidth(text) / 2, 0, color);
		glPopMatrix();
	}

	/**
	 * Renders the specified text to the screen.
	 */
	public void drawString(final FontRenderer font, final String text, final Area a, final int color)
	{
		glPushMatrix();
		glTranslatef(a.x1(), a.x2(), 0f);
		font.drawStringWithShadow(text, 0, 0, color);
		glPopMatrix();
	}

	/**
	 * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
	 */
	public void drawTexturedModalRect(final Area a, final Area texture)
	{
		final float f = 0.00390625F;
		final float f1 = 0.00390625F;
		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(a.x1(), a.y2(), 0, texture.x1() * f, texture.y2() * f1);
		tessellator.addVertexWithUV(a.x2(), a.y2(), 0, texture.x2() * f, texture.y2() * f1);
		tessellator.addVertexWithUV(a.x2(), a.y1(), 0, texture.x2() * f, texture.y1() * f1);
		tessellator.addVertexWithUV(a.x1(), a.y1(), 0, texture.x1() * f, texture.y1() * f1);
		tessellator.draw();
	}

	public void drawTexturedModelRectFromIcon(final Area a, final float x, final float y, final IIcon icon, final float w, final float h)
	{
		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(a.x1(), a.y2(), 0, icon.getMinU(), icon.getMaxV());
		tessellator.addVertexWithUV(a.x2(), a.y2(), 0, icon.getMaxU(), icon.getMaxV());
		tessellator.addVertexWithUV(a.x2(), a.y1(), 0, icon.getMaxU(), icon.getMinV());
		tessellator.addVertexWithUV(a.x1(), a.y1(), 0, icon.getMinU(), icon.getMinV());
		tessellator.draw();
	}

	public void translate(final Area p) {
		GL11.glTranslatef(p.x1(), p.y1(), 0f);
	}
}
