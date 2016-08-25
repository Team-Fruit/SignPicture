package com.kamesuta.mc.guiwidget;

import static org.lwjgl.opengl.GL11.*;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class WGui {
	public static final Minecraft mc = FMLClientHandler.instance().getClient();
	public static final render =
			public static final ResourceLocation widget = new ResourceLocation("textures/gui/widgets.png");
	public static final ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
	public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
	public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");

	public static void drawHorizontalLine(final float x1, final float x2, final float y, final int color)
	{
		drawRect(Math.min(x1, x2), y, Math.max(x1, x2) + 1, y + 1, color);
	}

	public static void drawVerticalLine(final float x, final float y1, final float y2, final int color)
	{
		drawRect(x, Math.min(y1, y2) + 1, x + 1, Math.max(y1, y2), color);
	}

	/**
	 * Draws a solid color rectangle with the specified coordinates and color. Args: x1, y1, x2, y2, color
	 */
	public static void drawRect(final float x1, final float y1, final float x2, final float y2, final int color)
	{
		final float a = (color >> 24 & 255) / 255.0F;
		final float r = (color >> 16 & 255) / 255.0F;
		final float g = (color >> 8 & 255) / 255.0F;
		final float b = (color & 255) / 255.0F;
		final Tessellator tessellator = Tessellator.instance;
		glEnable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		glColor4f(r, g, b, a);
		tessellator.startDrawingQuads();
		tessellator.addVertex(Math.min(x1, x2), Math.max(y1, y2), 0.0D);
		tessellator.addVertex(Math.max(x1, x2), Math.max(y1, y2), 0.0D);
		tessellator.addVertex(Math.max(x1, x2), Math.min(y1, y2), 0.0D);
		tessellator.addVertex(Math.min(x1, x2), Math.min(y1, y2), 0.0D);
		tessellator.draw();
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
	}

	/**
	 * Draws a rectangle with a vertical gradient between the specified colors.
	 */
	public static void drawGradientRect(final float x1, final float y1, final float x2, final float y2, final int color1, final int color2)
	{
		final float a1 = (color1 >> 24 & 255) / 255.0F;
		final float r1 = (color1 >> 16 & 255) / 255.0F;
		final float g1 = (color1 >> 8 & 255) / 255.0F;
		final float b1 = (color1 & 255) / 255.0F;
		final float a2 = (color2 >> 24 & 255) / 255.0F;
		final float r2 = (color2 >> 16 & 255) / 255.0F;
		final float g2 = (color2 >> 8 & 255) / 255.0F;
		final float b2 = (color2 & 255) / 255.0F;
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glDisable(GL_ALPHA_TEST);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		glShadeModel(GL_SMOOTH);
		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(r1, g1, b1, a1);
		tessellator.addVertex(x2, y1, 0);
		tessellator.addVertex(x1, y1, 0);
		tessellator.setColorRGBA_F(r2, g2, b2, a2);
		tessellator.addVertex(x1, y2, 0);
		tessellator.addVertex(x2, y2, 0);
		tessellator.draw();
		glShadeModel(GL_FLAT);
		glDisable(GL_BLEND);
		glEnable(GL_ALPHA_TEST);
		glEnable(GL_TEXTURE_2D);
	}

	/**
	 * Renders the specified text to the screen, center-aligned.
	 */
	public static void drawCenteredString(final FontRenderer font, final String text, final float x, final float y, final int color)
	{
		glPushMatrix();
		glTranslatef(x, y, 0f);
		font.drawStringWithShadow(text, - font.getStringWidth(text) / 2, 0, color);
		glPopMatrix();
	}

	/**
	 * Renders the specified text to the screen.
	 */
	public static void drawString(final FontRenderer font, final String text, final float x, final float y, final int color)
	{
		glPushMatrix();
		glTranslatef(x, y, 0f);
		font.drawStringWithShadow(text, 0, 0, color);
		glPopMatrix();
	}

	/**
	 * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
	 */
	public static void drawTexturedModalRect(final float x, final float y, final float u, final float v, final float width, final float height)
	{
		final float f = 0.00390625F;
		final float f1 = 0.00390625F;
		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + 0, y + height, 0, u + 0 * f, v + height * f1);
		tessellator.addVertexWithUV(x + width, y + height, 0, u + width * f, v + height * f1);
		tessellator.addVertexWithUV(x + width, y + 0, 0, u + width * f, v + 0 * f1);
		tessellator.addVertexWithUV(x + 0, y + 0, 0, u + 0 * f, v + 0 * f1);
		tessellator.draw();
	}

	public static void drawTexturedModelRectFromIcon(final float x, final float y, final IIcon image, final float width, final float height)
	{
		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + 0, y + height, 0, image.getMinU(), image.getMaxV());
		tessellator.addVertexWithUV(x + width, y + height, 0, image.getMaxU(), image.getMaxV());
		tessellator.addVertexWithUV(x + width, y + 0, 0, image.getMaxU(), image.getMinV());
		tessellator.addVertexWithUV(x + 0, y + 0, 0, image.getMinU(), image.getMinV());
		tessellator.draw();
	}

	public static void drawTexturedModelRect(final float x, final float y, final float u, final float v, final float width, final float height, final float divu, final float divv)
	{
		final float mulu = 1.0F / divu;
		final float mulv = 1.0F / divv;
		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x, y + height, 0.0D, u * mulu, (v + height) * mulv);
		tessellator.addVertexWithUV(x + width, y + height, 0.0D, (u + width) * mulu, (v + height) * mulv);
		tessellator.addVertexWithUV(x + width, y, 0.0D, (u + width) * mulu, v * mulv);
		tessellator.addVertexWithUV(x, y, 0.0D, u * mulu, v * mulv);
		tessellator.draw();
	}

	public static void drawTexturedModelRect(final float x, final float y, final float u, final float v, final float uwidth, final float vheight, final float width, final float height, final float divu, final float divv)
	{
		final float mulu = 1.0F / divu;
		final float mulv = 1.0F / divv;
		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x, y + height, 0.0D, u * mulu, (v + vheight) * mulv);
		tessellator.addVertexWithUV(x + width, y + height, 0.0D, (u + uwidth) * mulu, (v + vheight) * mulv);
		tessellator.addVertexWithUV(x + width, y, 0.0D, (u + uwidth) * mulu, v * mulv);
		tessellator.addVertexWithUV(x, y, 0.0D, u * mulu, v * mulv);
		tessellator.draw();
	}
}
