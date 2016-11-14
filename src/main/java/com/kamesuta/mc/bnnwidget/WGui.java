package com.kamesuta.mc.bnnwidget;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.position.Area;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.EnumChatFormatting;

public class WGui extends Gui {
	public static final Minecraft mc;
	public static final Tessellator t = Tessellator.instance;
	public static final StencilClip clip = StencilClip.instance;
	static {
		mc = FMLClientHandler.instance().getClient();
	}

	public static void drawTexture(final float vx, final float vy, final float vw, final float vh, final float tx, final float ty, final float tw, final float th) {
		t.startDrawingQuads();
		t.addVertexWithUV(vx, vy+vh, 0, tx, ty+th);
		t.addVertexWithUV(vx+vw, vy+vh, 0, tx+tw, ty+th);
		t.addVertexWithUV(vx+vw, vy, 0, tx+tw, ty);
		t.addVertexWithUV(vx, vy, 0, tx, ty);
		t.draw();
	}

	public static void drawTexture(final Area vertex, final Area textrue) {
		drawTexture(vertex.x1(), vertex.y1(), vertex.w(), vertex.h(), textrue.x1(), textrue.y1(), textrue.w(), textrue.h());
	}

	public static void drawTexture(final float vx, final float vy, final float vw, final float vh) {
		drawTexture(vx, vy, vw, vh, 0, 0, 1, 1);
	}

	public static void drawTexture(final Area vertex) {
		drawTexture(vertex.x1(), vertex.y1(), vertex.w(), vertex.h(), 0, 0, 1, 1);
	}

	public static void draw(final float x1, final float y1, final float x2, final float y2, final int mode) {
		t.startDrawing(mode);
		t.addVertex(x1, y1, 0);
		t.addVertex(x1, y2, 0);
		t.addVertex(x2, y2, 0);
		t.addVertex(x2, y1, 0);
		t.draw();
	}

	public static void draw(final Area p, final int mode) {
		draw(p.x1(), p.y1(), p.x2(), p.y2(), mode);
	}

	private static int fontcolor;

	public static void fontColor(final int color) {
		WGui.fontcolor = color;
	}

	public static void fontColor(final int r, final int g, final int b, final int a) {
		fontColor((a&0xFF)<<24|
				(r&0xFF)<<16|
				(g&0xFF)<<8|
				(b&0xFF)<<0);
	}

	public static void fontColor(final float r, final float g, final float b, final float a) {
		fontColor((int) r*256, (int) g*256, (int) b*256, (int) a*256);
	}

	public static void fontColor(final Color color) {
		fontColor(color.getRGB());
	}

	private static void resetFontColor() {
		WGui.fontcolor = 0xff000000;
	}

	public static TextureManager texture() {
		return mc.renderEngine;
	}

	public static FontRenderer font() {
		return mc.fontRenderer;
	}

	public static void drawString(final String text, final float x, final float y, final float w, final float h, final Align align, final boolean shadow) {
		glPushMatrix();
		align.translate(text, x, y, w, h);
		font().drawString(text, 0, 0, fontcolor, shadow);
		glPopMatrix();
		resetFontColor();
	}

	public static void drawString(final String text, final Area a, final Align align, final boolean shadow) {
		drawString(text, a.x1(), a.y1(), a.w(), a.h(), align, shadow);
	}

	public static enum Align {
		LEFT {
			@Override
			public void translate(final String text, final float x, final float y, final float w, final float h) {
				glTranslatef(x, y+(h-font().FONT_HEIGHT)/2f, 0);
			}
		},
		CENTER {
			@Override
			public void translate(final String text, final float x, final float y, final float w, final float h) {
				glTranslatef(x+(w-getStringWidth(text))/2, y+(h-font().FONT_HEIGHT)/2f, 0);
			}
		},
		RIGHT {
			@Override
			public void translate(final String text, final float x, final float y, final float w, final float h) {
				glTranslatef(x-getStringWidth(text), y+(h-font().FONT_HEIGHT)/2f, 0);
			}
		},
		;
		protected abstract void translate(String text, float x, float y, float w, float h);
	}

	public static int getStringWidth(final String s) {
		if (StringUtils.isEmpty(s))
			return 0;
		return font().getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(s));
	}
}
