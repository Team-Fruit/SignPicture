package com.kamesuta.mc.guiwidget;

import org.lwjgl.opengl.GL11;

import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.relative.RCommon;

import net.minecraft.client.renderer.Tessellator;
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

	public void draw(final Area p, final int mode) {
		final Tessellator t = Tessellator.instance;
		t.startDrawing(mode);
		t.addVertex(p.x1(), p.y1(), 0);
		t.addVertex(p.x1(), p.y2(), 0);
		t.addVertex(p.x2(), p.y2(), 0);
		t.addVertex(p.x2(), p.y1(), 0);
		t.draw();
	}

	public void translate(final Area p) {
		GL11.glTranslatef(p.anc_x(), p.anc_y(), 0f);
	}
}
