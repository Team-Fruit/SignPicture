package net.teamfruit.emojicord.gui;

import org.lwjgl.opengl.GL11;

import net.teamfruit.emojicord.compat.CompatBaseVertex;
import net.teamfruit.emojicord.compat.CompatVertex;
import net.teamfruit.emojicord.compat.OpenGL;

public interface IChatOverlay {
	default boolean onDraw() {
		return false;
	}

	default boolean onMouseClicked(final int button) {
		return false;
	}

	default boolean onMouseReleased(final int button) {
		return false;
	}

	default boolean onMouseScroll(final double scrollDelta) {
		return false;
	}

	default boolean onMouseInput(final int mouseX, final int mouseY) {
		return false;
	}

	default boolean onCharTyped(final char typed, final int modifier) {
		return false;
	}

	default boolean onKeyPressed(final int keycode) {
		return false;
	}

	default void onTick() {
	}

	public static void fill(int x1, int y1, int x2, int y2, final int color) {
		if (x1<x2) {
			final int i = x1;
			x1 = x2;
			x2 = i;
		}

		if (y1<y2) {
			final int j = y1;
			y1 = y2;
			y2 = j;
		}

		final float f3 = (color>>24&255)/255.0F;
		final float f = (color>>16&255)/255.0F;
		final float f1 = (color>>8&255)/255.0F;
		final float f2 = (color&255)/255.0F;
		final CompatBaseVertex t = CompatVertex.getTessellator();
		OpenGL.glEnable(GL11.GL_BLEND);
		OpenGL.glDisable(GL11.GL_TEXTURE_2D);
		OpenGL.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		OpenGL.glColor4f(f, f1, f2, f3);
		t.begin(GL11.GL_QUADS);
		t.pos(x1, y2, 0.0D);
		t.pos(x2, y2, 0.0D);
		t.pos(x2, y1, 0.0D);
		t.pos(x1, y1, 0.0D);
		t.draw();
		OpenGL.glEnable(GL11.GL_TEXTURE_2D);
		OpenGL.glDisable(GL11.GL_BLEND);
	}

	public static void fill(final Rectangle2d rect, final int color) {
		fill(rect.getX(), rect.getY(), rect.getX()+rect.getWidth(), rect.getY()+rect.getHeight(), color);
	}
}
