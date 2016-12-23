package com.kamesuta.mc.bnnwidget;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.render.OpenGL;

public class WRenderer {
	public static void startShape() {
		OpenGL.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		OpenGL.glDisable(GL_LIGHTING);
		OpenGL.glEnable(GL_BLEND);
		OpenGL.glDisable(GL_TEXTURE_2D);
	}

	public static void startTexture() {
		OpenGL.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		OpenGL.glDisable(GL_LIGHTING);
		OpenGL.glEnable(GL_BLEND);
		OpenGL.glEnable(GL_TEXTURE_2D);
	}
}
