package com.kamesuta.mc.guiwidget;

import static org.lwjgl.opengl.GL11.*;

import java.util.BitSet;

import org.lwjgl.opengl.EXTFramebufferObject;

import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.signpic.Reference;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;

public class StencilClip {
	public static final StencilClip instance = new StencilClip();
	private ClipState state = ClipState.NONE;

	private StencilClip() {}

	public void startMasking() {
		if (this.state != ClipState.NONE) {
			throw new IllegalStateException("Already clipping!");
		} else {
			this.state = ClipState.MASK;
			glEnable(GL_STENCIL_TEST);
			glClear(GL_DEPTH_BUFFER_BIT);
			glStencilFunc(GL_NEVER, 1, 0xFF);
			glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);  // draw 1s on test fail (always)

			glColorMask(false, false, false, false);
			glDepthMask(false);

			// draw stencil pattern
			glStencilMask(0xFF);
			glClear(GL_STENCIL_BUFFER_BIT);  // needs mask=0xFF
		}
	}

	public void startClippingWithMask() {
		if (this.state != ClipState.MASK) {
			throw new IllegalStateException("No mask!");
		} else {
			this.state = ClipState.CLIP;
			glColorMask(true, true, true, true);
			glDepthMask(true);
			glStencilMask(0x00);
			// draw only where stencil's value is 1
			glStencilFunc(GL_EQUAL, 1, 0xFF);
			glEnable(GL_BLEND);
		}
	}

	public void clip() {
		if (this.state != ClipState.CLIP) {
			throw new IllegalStateException("Not clipping!");
		} else {
			this.state = ClipState.NONE;
			//glClear(GL_DEPTH_BUFFER_BIT);
			glDisable(GL_STENCIL_TEST);
		}
	}

	public void startClippingWithArea(final Area a) {
		startMasking();
		WGui.drawRect(a);
		startClippingWithMask();
	}

	public static void init() {
		if (!Boolean.parseBoolean(System.getProperty("forge.forceDisplayStencil", "false"))) {
			try {
				if (!(ReflectionHelper.findField(OpenGlHelper.class, "field_153212_w").getInt(null)==2 &&
						EXTFramebufferObject.glCheckFramebufferStatusEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT)!=EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT)) {
					final int i = 8;
					ReflectionHelper.findField(ForgeHooksClient.class, "stencilBits").setInt(null, i);
					final BitSet stencilBits = ReflectionHelper.getPrivateValue(MinecraftForgeClient.class, null, "stencilBits");
					stencilBits.set(0, i);
				}
			} catch (final Throwable e) {
				Reference.logger.info("Failed to enable stencil buffer", e);
			}
		}
	}

	private enum ClipState {
		MASK,
		CLIP,
		NONE,
	}
}
