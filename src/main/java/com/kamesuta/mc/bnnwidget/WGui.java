package com.kamesuta.mc.bnnwidget;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.signpic.render.OpenGL;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.EnumChatFormatting;

public class WGui extends Gui {
	public static final @Nonnull Minecraft mc;
	public static final @Nonnull Tessellator t = WRenderer.t;
	// public static final StencilClip clip = StencilClip.instance;
	static {
		mc = FMLClientHandler.instance().getClient();
	}

	public static void drawTextureAbs(final float vx1, final float vy1, final float vx2, final float vy2, final float tx1, final float ty1, final float tx2, final float ty2) {
		t.startDrawingQuads();
		t.addVertexWithUV(vx1, vy2, 0, tx1, ty2);
		t.addVertexWithUV(vx2, vy2, 0, tx2, ty2);
		t.addVertexWithUV(vx2, vy1, 0, tx2, ty1);
		t.addVertexWithUV(vx1, vy1, 0, tx1, ty1);
		t.draw();
	}

	public static void drawTextureAbs(final float vx1, final float vy1, final float vx2, final float vy2) {
		drawTextureAbs(vx1, vy1, vx2, vy2, 0, 0, 1, 1);
	}

	public static void drawTextureSize(final float vx, final float vy, final float vw, final float vh, final float tx, final float ty, final float tw, final float th) {
		drawTextureAbs(vx, vy, vx+vw, vy+vh, tx, ty, tx+tw, ty+th);
	}

	public static void drawTextureSize(final float vx, final float vy, final float vw, final float vh) {
		drawTextureAbs(vx, vy, vx+vw, vy+vh, 0, 0, 1, 1);
	}

	public static void drawTexture(final @Nonnull Area vertex, final @Nonnull Area textrue) {
		drawTextureAbs(vertex.x1(), vertex.y1(), vertex.x2(), vertex.y2(), textrue.x1(), textrue.y1(), textrue.x2(), textrue.y2());
	}

	public static void drawTexture(final @Nonnull Area vertex) {
		drawTextureAbs(vertex.x1(), vertex.y1(), vertex.x2(), vertex.y2(), 0, 0, 1, 1);
	}

	public static void drawTextureModalAbs(final float vx1, final float vy1, final float vx2, final float vy2, final float tx1, final float ty1, final float tx2, final float ty2) {
		final float f = 0.00390625F;
		drawTextureAbs(vx1, vy1, vx2, vy2, f*tx1, f*ty1, f*tx2, f*ty2);
	}

	public static void drawTextureModalAbs(final float vx1, final float vy1, final float vx2, final float vy2) {
		drawTextureModalAbs(vx1, vy1, vx2, vy2, 0, 0, 1, 1);
	}

	public static void drawTextureModalSize(final float vx, final float vy, final float vw, final float vh, final float tx, final float ty, final float tw, final float th) {
		drawTextureModalAbs(vx, vy, vx+vw, vy+vh, tx, ty, tx+tw, ty+th);
	}

	public static void drawTextureModalSize(final float vx, final float vy, final float vw, final float vh) {
		drawTextureModalAbs(vx, vy, vx+vw, vy+vh, 0, 0, 1, 1);
	}

	public static void drawTextureModal(final @Nonnull Area vertex, final @Nonnull Area textrue) {
		drawTextureModalAbs(vertex.x1(), vertex.y1(), vertex.x2(), vertex.y2(), textrue.x1(), textrue.y1(), textrue.x2(), textrue.y2());
	}

	public static void drawTextureModal(final @Nonnull Area vertex) {
		drawTextureModalAbs(vertex.x1(), vertex.y1(), vertex.x2(), vertex.y2(), 0, 0, 1, 1);
	}

	public static void draw(final float x1, final float y1, final float x2, final float y2, final int mode) {
		t.startDrawing(mode);
		t.addVertex(x1, y2, 0);
		t.addVertex(x2, y2, 0);
		t.addVertex(x2, y1, 0);
		t.addVertex(x1, y1, 0);
		t.draw();
	}

	public static void draw(final @Nonnull Area p, final int mode) {
		draw(p.x1(), p.y1(), p.x2(), p.y2(), mode);
	}

	public static void draw(final float x1, final float y1, final float x2, final float y2) {
		draw(x1, y1, x2, y2, GL_QUADS);
	}

	public static void draw(final @Nonnull Area p) {
		draw(p, GL_QUADS);
	}

	private static int fontcolor;

	public static void fontColor(final int color) {
		WGui.fontcolor = color;
	}

	public static void fontColor(final int r, final int g, final int b, final int a) {
		fontColor(Math.max(a&0xff, 0x4)<<24|(r&0xFF)<<16|(g&0xFF)<<8|(b&0xFF)<<0);
	}

	public static void fontColor(final float r, final float g, final float b, final float a) {
		fontColor((int) (r*255+0.5), (int) (g*255+0.5), (int) (b*255+0.5), (int) (a*255+0.5));
	}

	public static void fontColor(final @Nonnull Color color) {
		fontColor(color.getRGB());
	}

	private static void resetFontColor() {
		WGui.fontcolor = 0xff000000;
	}

	public static @Nonnull TextureManager texture() {
		return mc.renderEngine;
	}

	public static @Nonnull FontRenderer font() {
		return mc.fontRenderer;
	}

	public static void drawString(final @Nonnull String text, final float x, final float y, final float w, final float h, final @Nonnull Align align, final @Nonnull VerticalAlign valign, final boolean shadow) {
		OpenGL.glPushMatrix();
		align.translate(text, x, w);
		valign.translate(text, y, h);
		font().drawString(text, 0, 0, fontcolor, shadow);
		OpenGL.glPopMatrix();
		resetFontColor();
	}

	public static void drawString(final @Nonnull String text, final @Nonnull Area a, final @Nonnull Align align, final @Nonnull VerticalAlign valign, final boolean shadow) {
		drawString(text, a.x1(), a.y1(), a.w(), a.h(), align, valign, shadow);
	}

	public static enum Align {
		LEFT {
			@Override
			protected void translate(final @Nonnull String text, final float x, final float w) {
				OpenGL.glTranslatef(x, 0, 0);
			}
		},
		CENTER {
			@Override
			protected void translate(final @Nonnull String text, final float x, final float w) {
				OpenGL.glTranslatef(x+(w-getStringWidth(text))/2, 0, 0);
			}
		},
		RIGHT {
			@Override
			protected void translate(final @Nonnull String text, final float x, final float w) {
				OpenGL.glTranslatef(x-getStringWidth(text), 0, 0);
			}
		},
		;
		protected abstract void translate(@Nonnull String text, float x, float w);
	}

	public static enum VerticalAlign {
		TOP {
			@Override
			protected void translate(final @Nonnull String text, final float y, final float h) {
				OpenGL.glTranslatef(0, y, 0);
			}
		},
		MIDDLE {
			@Override
			protected void translate(final @Nonnull String text, final float y, final float h) {
				OpenGL.glTranslatef(0, y+(h-font().FONT_HEIGHT)/2, 0);
			}
		},
		BOTTOM {
			@Override
			protected void translate(final @Nonnull String text, final float y, final float h) {
				OpenGL.glTranslatef(0, y+h-font().FONT_HEIGHT, 0);
			}
		},
		;
		protected abstract void translate(@Nonnull String text, float y, float h);
	}

	public static int getStringWidth(final @Nonnull String s) {
		if (StringUtils.isEmpty(s))
			return 0;
		return font().getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(s));
	}
}
