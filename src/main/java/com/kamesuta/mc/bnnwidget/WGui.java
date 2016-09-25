package com.kamesuta.mc.bnnwidget;

import static org.lwjgl.opengl.GL11.*;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.RArea;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

public class WGui extends Gui {
	public static final Minecraft mc;
	public static final Tessellator t = Tessellator.instance;
	public static final StencilClip clip = StencilClip.instance;
	static {
		mc = FMLClientHandler.instance().getClient();
	}

	public static TextureManager texture() {
		return mc.renderEngine;
	}

	public static FontRenderer font() {
		return mc.fontRenderer;
	}

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
		glEnable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		glColor4f(r, g, b, a);
		t.startDrawingQuads();
		t.addVertex(Math.min(x1, x2), Math.max(y1, y2), 0.0D);
		t.addVertex(Math.max(x1, x2), Math.max(y1, y2), 0.0D);
		t.addVertex(Math.max(x1, x2), Math.min(y1, y2), 0.0D);
		t.addVertex(Math.min(x1, x2), Math.min(y1, y2), 0.0D);
		t.draw();
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
		t.startDrawingQuads();
		t.setColorRGBA_F(r1, g1, b1, a1);
		t.addVertex(x2, y1, 0);
		t.addVertex(x1, y1, 0);
		t.setColorRGBA_F(r2, g2, b2, a2);
		t.addVertex(x1, y2, 0);
		t.addVertex(x2, y2, 0);
		t.draw();
		glShadeModel(GL_FLAT);
		glDisable(GL_BLEND);
		glEnable(GL_ALPHA_TEST);
		glEnable(GL_TEXTURE_2D);
	}

	/**
	 * Renders the specified text to the screen, center-aligned.
	 */
	public static void drawCenteredString(final String text, final float x, final float y, final int color)
	{
		glPushMatrix();
		glTranslatef(x, y, 0f);
		font().drawStringWithShadow(text, - font().getStringWidth(text) / 2, 0, color);
		glPopMatrix();
	}

	/**
	 * Renders the specified text to the screen.
	 */
	public static void drawString(final String text, final float x, final float y, final int color)
	{
		glPushMatrix();
		glTranslatef(x, y, 0f);
		font().drawStringWithShadow(text, 0, 0, color);
		glPopMatrix();
	}

	/**
	 * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
	 */
	public static void drawTexturedModalRect(final float x, final float y, final float u, final float v, final float width, final float height)
	{
		final float f = 0.00390625F;
		final float f1 = 0.00390625F;
		t.startDrawingQuads();
		t.addVertexWithUV(x + 0, y + height, 0, (u + 0) * f, (v + height) * f1);
		t.addVertexWithUV(x + width, y + height, 0, (u + width) * f, (v + height) * f1);
		t.addVertexWithUV(x + width, y + 0, 0, (u + width) * f, (v + 0) * f1);
		t.addVertexWithUV(x + 0, y + 0, 0, (u + 0) * f, (v + 0) * f1);
		t.draw();
	}

	public static void drawTexturedModalRectFromIcon(final float x, final float y, final IIcon image, final float width, final float height)
	{
		t.startDrawingQuads();
		t.addVertexWithUV(x + 0, y + height, 0, image.getMinU(), image.getMaxV());
		t.addVertexWithUV(x + width, y + height, 0, image.getMaxU(), image.getMaxV());
		t.addVertexWithUV(x + width, y + 0, 0, image.getMaxU(), image.getMinV());
		t.addVertexWithUV(x + 0, y + 0, 0, image.getMinU(), image.getMinV());
		t.draw();
	}

	public static void drawTexturedModalRect(final float x, final float y, final float u, final float v, final float width, final float height, final float divu, final float divv)
	{
		final float mulu = 1.0F / divu;
		final float mulv = 1.0F / divv;
		t.startDrawingQuads();
		t.addVertexWithUV(x, y + height, 0.0D, u * mulu, (v + height) * mulv);
		t.addVertexWithUV(x + width, y + height, 0.0D, (u + width) * mulu, (v + height) * mulv);
		t.addVertexWithUV(x + width, y, 0.0D, (u + width) * mulu, v * mulv);
		t.addVertexWithUV(x, y, 0.0D, u * mulu, v * mulv);
		t.draw();
	}

	public static void drawTexturedModalRect(final float x, final float y, final float u, final float v, final float uwidth, final float vheight, final float width, final float height, final float divu, final float divv)
	{
		final float mulu = 1.0F / divu;
		final float mulv = 1.0F / divv;
		t.startDrawingQuads();
		t.addVertexWithUV(x, y + height, 0.0D, u * mulu, (v + vheight) * mulv);
		t.addVertexWithUV(x + width, y + height, 0.0D, (u + uwidth) * mulu, (v + vheight) * mulv);
		t.addVertexWithUV(x + width, y, 0.0D, (u + uwidth) * mulu, v * mulv);
		t.addVertexWithUV(x, y, 0.0D, u * mulu, v * mulv);
		t.draw();
	}

	public static void draw(final Area p, final int mode) {
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
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		t.startDrawingQuads();
		t.addVertex(a.x1(), a.y2(), .0);
		t.addVertex(a.x2(), a.y2(), .0);
		t.addVertex(a.x2(), a.y1(), .0);
		t.addVertex(a.x1(), a.y1(), .0);
		t.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void drawRect(final Area a, final int color)
	{
		drawRect(a.x1(), a.y1(), a.x2(), a.y2(), color);
	}

	/**
	 * Draws a rectangle with a vertical gradient between the specified colors.
	 */
	public static void drawGradientRect(final Area a, final int color1, final int color2)
	{
		drawGradientRect(a.x1(), a.y1(), a.x2(), a.y2(), color1, color2);
	}

	/**
	 * Renders the specified text to the screen, center-aligned.
	 */
	public static void drawCenteredString(final String text, final Area a, final int color)
	{
		drawCenteredString(text, a.x1(), a.y1(), color);
	}

	/**
	 * Renders the specified text to the screen.
	 */
	public static void drawString(final String text, final Area a, final int color)
	{
		drawString(text, a.x1(), a.y1(), color);
	}

	/**
	 * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
	 */
	public static void drawTexturedModalRect(final Area a, final float u, final float v)
	{
		drawTexturedModalRect(a.x1(), a.y1(), u, v, a.w(), a.h());
	}

	public static void drawTexturedModalRect(final Area a)
	{
		drawTexturedModalRect(a.x1(), a.y1(), 0, 0, 1, 1, a.w(), a.h(), 1, 1);
	}

	/**
	 * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
	 */
	public static void drawTexturedModalRect(final Area a, final Area texture)
	{
		final float f = 0.00390625F;
		final float f1 = 0.00390625F;
		t.startDrawingQuads();
		t.addVertexWithUV(a.x1(), a.y2(), 0, texture.x1() * f, texture.y2() * f1);
		t.addVertexWithUV(a.x2(), a.y2(), 0, texture.x2() * f, texture.y2() * f1);
		t.addVertexWithUV(a.x2(), a.y1(), 0, texture.x2() * f, texture.y1() * f1);
		t.addVertexWithUV(a.x1(), a.y1(), 0, texture.x1() * f, texture.y1() * f1);
		t.draw();
	}

	public static void drawTexturedModalRectFromIcon(final Area a, final IIcon icon)
	{
		drawTexturedModalRectFromIcon(a.x1(), a.y1(), icon, a.w(), a.h());
	}

	public static void translate(final Area p) {
		GL11.glTranslatef(p.x1(), p.y1(), 0f);
	}

	public static void drawString(final String text, final int x, final int y, final int colour, final boolean shadow) {
		if (shadow)
			font().drawStringWithShadow(text, x, y, colour);
		else
			font().drawString(text, x, y, colour);
	}

	public static void drawString(final String text, final int x, final int y, final int colour) {
		drawString(text, x, y, colour, true);
	}

	public static void drawStringC(final String text, final int x, final int y, final int w, final int h, final int colour, final boolean shadow) {
		drawString(text, x + (w - getStringWidth(text)) / 2, y + (h - 8) / 2, colour, shadow);
	}

	public static void drawStringC(final String text, final int x, final int y, final int w, final int h, final int colour) {
		drawStringC(text, x, y, w, h, colour, true);
	}

	public static void drawStringC(final String text, final int x, final int y, final int colour, final boolean shadow) {
		drawString(text, x - getStringWidth(text) / 2, y, colour, shadow);
	}

	public static void drawStringC(final String text, final int x, final int y, final int colour) {
		drawStringC(text, x, y, colour, true);
	}

	public static void drawStringR(final String text, final int x, final int y, final int colour, final boolean shadow) {
		drawString(text, x - getStringWidth(text), y, colour, shadow);
	}

	public static void drawStringR(final String text, final int x, final int y, final int colour) {
		drawStringR(text, x, y, colour, true);
	}

	public static int getStringWidth(final String s) {
		if (StringUtils.isEmpty(s))
			return 0;
		return font().getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(s));
	}
}
