package com.kamesuta.mc.bnnwidget;

import static org.lwjgl.opengl.GL11.*;

import java.util.BitSet;

import org.lwjgl.opengl.EXTFramebufferObject;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.render.OpenGL;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;

public class StencilClip {
	public static final StencilClip instance = new StencilClip();
	private int layer = 0;

	private StencilClip() {
	}

	public void startCropping() {
		if (this.layer<=0) {
			OpenGL.glEnable(GL_STENCIL_TEST);
			OpenGL.glClear(GL_STENCIL_BUFFER_BIT);
		}
		// layer
		this.layer++;

		// stencil mode on
		OpenGL.glStencilOp(GL_INCR, GL_KEEP, GL_KEEP);
		OpenGL.glColorMask(false, false, false, false);
		OpenGL.glDepthMask(false);

		// draw where pattern has been drawn
		OpenGL.glStencilFunc(GL_EQUAL, this.layer, 0xff);
		OpenGL.glStencilMask(0xff);
	}

	public void endCropping() {
		if (this.layer<0)
			throw new IllegalStateException("Not Clipping");
		else {
			// stencil mode off
			OpenGL.glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
			OpenGL.glColorMask(true, true, true, true);
			OpenGL.glDepthMask(true);

			// draw where pattern has been drawn
			OpenGL.glStencilFunc(GL_LEQUAL, this.layer, 0xff);
			OpenGL.glStencilMask(0xff);
		}
	}

	public void end() {
		if (this.layer>0) {
			// layer
			this.layer--;

			// draw where pattern has been drawn
			OpenGL.glStencilFunc(GL_LEQUAL, this.layer, 0xff);
			OpenGL.glStencilMask(0xff);
		}
		if (this.layer<=0) {
			OpenGL.glClear(GL_STENCIL_BUFFER_BIT);
			OpenGL.glDisable(GL_STENCIL_TEST);
		}
	}

	public void clipArea(final Area a) {
		startCropping();
		WGui.draw(a);
		endCropping();
	}

	public static void init() {
		if (!Boolean.parseBoolean(System.getProperty("forge.forceDisplayStencil", "false")))
			try {
				if (
					!(ReflectionHelper.findField(OpenGlHelper.class, "field_153212_w").getInt(null)==2&&
							EXTFramebufferObject.glCheckFramebufferStatusEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT)!=EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT)
				) {
					ReflectionHelper.findField(ForgeHooksClient.class, "stencilBits").setInt(null, 8);
					ReflectionHelper.<BitSet, MinecraftForgeClient> getPrivateValue(MinecraftForgeClient.class, null, "stencilBits").set(0, 8);
				}
			} catch (final Throwable e) {
				Reference.logger.info("Failed to enable stencil buffer", e);
			}
	}
}
