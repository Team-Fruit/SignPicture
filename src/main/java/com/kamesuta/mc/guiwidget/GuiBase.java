package com.kamesuta.mc.guiwidget;

import org.lwjgl.opengl.GL11;

import com.kamesuta.mc.guiwidget.position.IPositionAbsolute;
import com.kamesuta.mc.guiwidget.position.relative.IPositionRelative;

import net.minecraft.client.renderer.Tessellator;

public abstract class GuiBase extends GuiComponent {
	protected IPositionRelative position;

	public GuiBase(final IPositionRelative position) {
		this.position = position;
	}

	public IPositionRelative getGuiRelative() {
		return this.position;
	}

	public GuiPosition getGuiPosition(final GuiPosition pgp) {
		return pgp.child(getGuiRelative());
	}

	public void draw(final IPositionAbsolute p, final int mode) {
		final Tessellator t = Tessellator.instance;
		t.startDrawing(mode);
		t.addVertex(p.x1(), p.y1(), 0);
		t.addVertex(p.x1(), p.y2(), 0);
		t.addVertex(p.x2(), p.y2(), 0);
		t.addVertex(p.x2(), p.y1(), 0);
		t.draw();
	}

	public void translate(final IPositionAbsolute p) {
		GL11.glTranslatef(p.x(), p.y(), 0f);
	}
}
