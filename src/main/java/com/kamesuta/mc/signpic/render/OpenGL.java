package com.kamesuta.mc.signpic.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;

public class OpenGL {
	public static void glEnable(final int attrib) {
		switch (attrib) {
			case GL11.GL_ALPHA_TEST:
				GlStateManager.enableAlpha();
				break;
			case GL11.GL_BLEND:
				GlStateManager.enableBlend();
				break;
			case GL11.GL_CULL_FACE:
				GlStateManager.enableCull();
				break;
			case GL11.GL_DEPTH_TEST:
				GlStateManager.enableDepth();
				break;
			case GL11.GL_FOG:
				GlStateManager.enableFog();
				break;
			case GL11.GL_LIGHTING:
				GlStateManager.enableLighting();
				break;
			case GL11.GL_NORMALIZE:
				GlStateManager.enableNormalize();
				break;
			case GL11.GL_POLYGON_OFFSET_FILL:
				GlStateManager.enablePolygonOffset();
				break;
			case GL12.GL_RESCALE_NORMAL:
				GlStateManager.enableRescaleNormal();
				break;
			case GL11.GL_TEXTURE_2D:
				GlStateManager.enableTexture2D();
				break;
			default:
				GL11.glEnable(attrib);
		}
	}

	public static void glDisable(final int attrib) {
		switch (attrib) {
			case GL11.GL_ALPHA_TEST:
				GlStateManager.disableAlpha();
				break;
			case GL11.GL_BLEND:
				GlStateManager.disableBlend();
				break;
			case GL11.GL_CULL_FACE:
				GlStateManager.disableCull();
				break;
			case GL11.GL_DEPTH_TEST:
				GlStateManager.disableDepth();
				break;
			case GL11.GL_FOG:
				GlStateManager.disableFog();
				break;
			case GL11.GL_LIGHTING:
				GlStateManager.disableLighting();
				break;
			case GL11.GL_NORMALIZE:
				GlStateManager.disableNormalize();
				break;
			case GL11.GL_POLYGON_OFFSET_FILL:
				GlStateManager.disablePolygonOffset();
				break;
			case GL12.GL_RESCALE_NORMAL:
				GlStateManager.disableRescaleNormal();
				break;
			case GL11.GL_TEXTURE_2D:
				GlStateManager.disableTexture2D();
				break;
			default:
				GL11.glDisable(attrib);
		}
	}

	public static void glHint(final int target, final int mode) {
		GL11.glHint(target, mode);
	}

	public static void glAlphaFunc(final int func, final float ref) {
		GlStateManager.alphaFunc(func, ref);
		//		GL11.glAlphaFunc(func, ref);
	}

	public static void glBlendFunc(final int sfactor, final int dfactor) {
		GlStateManager.blendFunc(sfactor, dfactor);
		//		GL11.glBlendFunc(sfactor, dfactor);
	}

	public static void glBlendFuncSeparate(final int srcFactor, final int dstFactor, final int srcFactorAlpha, final int dstFactorAlpha) {
		GlStateManager.tryBlendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
		//		OpenGlHelper.glBlendFunc(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
	}

	public static void glCallList(final int list) {
		GlStateManager.callList(list);
		//		GL11.glCallList(list);
	}

	public static void glClear(final int mask) {
		GlStateManager.clear(mask);
		//		GL11.glClear(mask);
	}

	public static void glClearColor(
			final float red, final float green, final float blue,
			final float alpha) {
		GlStateManager.clearColor(red, green, blue, alpha);
		//		GL11.glClearColor(red, green, blue, alpha);
	}

	public static void glClearDepth(final double depth) {
		GlStateManager.clearDepth(depth);
		//		GL11.glClearDepth(depth);
	}

	public static void glColor3f(final float red, final float green, final float blue) {
		GlStateManager.color(red, green, blue, 1.0F);
		//		GL11.glColor3f(red, green, blue);
	}

	public static void glColor4f(final float red, final float green, final float blue, final float alpha) {
		GlStateManager.color(red, green, blue, alpha);
		//		GL11.glColor4f(red, green, blue, alpha);
	}

	public static void glColor4i(final int red, final int green, final int blue, final int alpha) {
		glColor4f(red/255f, green/255f, blue/255f, alpha/255f);
	}

	public static void glColor4ub(final byte red, final byte green, final byte blue, final byte alpha) {
		glColor4i(red&0xff, green&0xff, blue&0xff, alpha&0xff);
		//		GL11.glColor4ub(red, green, blue, alpha);
	}

	public static void glColor(final Color color) {
		glColor4i(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public static void glColor(final org.lwjgl.util.Color color) {
		glColor4i(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public static void glColorMask(final boolean red, final boolean green, final boolean blue, final boolean alpha) {
		GlStateManager.colorMask(red, green, blue, alpha);
		//		GL11.glColorMask(red, green, blue, alpha);
	}

	public static void glColorMaterial(final int face, final int mode) {
		GlStateManager.colorMaterial(face, mode);
		//		GL11.glColorMaterial(face, mode);
	}

	public static void glCullFace(final int mode) {
		//		GlStateManager.cullFace(mode);
		if (mode==GlStateManager.CullFace.BACK.mode)
			GlStateManager.cullFace(GlStateManager.CullFace.BACK);
		else if (mode==GlStateManager.CullFace.FRONT.mode)
			GlStateManager.cullFace(GlStateManager.CullFace.FRONT);
		else if (mode==GlStateManager.CullFace.FRONT_AND_BACK.mode)
			GlStateManager.cullFace(GlStateManager.CullFace.FRONT_AND_BACK);
		//		GL11.glCullFace(mode);
	}

	public static void glDepthFunc(final int func) {
		GlStateManager.depthFunc(func);
		//		GL11.glDepthFunc(func);
	}

	public static void glDepthMask(final boolean flag) {
		GlStateManager.depthMask(flag);
		//		GL11.glDepthMask(flag);
	}

	public static void glGetFloat(final int pname, final FloatBuffer params) {
		GlStateManager.getFloat(pname, params);
		//		GL11.glGetFloat(pname, params);
	}

	public static void glLoadIdentity() {
		GlStateManager.loadIdentity();
		//		GL11.glLoadIdentity();
	}

	public static void glLogicOp(final int opcode) {
		GlStateManager.colorLogicOp(opcode);
		//		GL11.glLogicOp(opcode);
	}

	public static void glMatrixMode(final int mode) {
		GlStateManager.matrixMode(mode);
		//		GL11.glMatrixMode(mode);
	}

	public static void glMultMatrix(final FloatBuffer m) {
		GlStateManager.multMatrix(m);
		//		GL11.glMultMatrix(m);
	}

	public static void glOrtho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
		GlStateManager.ortho(left, right, bottom, top, zNear, zFar);
		//		GL11.glOrtho(left, right, bottom, top, zNear, zFar);
	}

	public static void glPolygonOffset(final float factor, final float units) {
		GlStateManager.doPolygonOffset(factor, units);
		//		GL11.glPolygonOffset(factor, units);
	}

	public static void glPopAttrib() {
		GlStateManager.popAttrib();
		//		GL11.glPopAttrib();
	}

	public static void glPopMatrix() {
		GlStateManager.popMatrix();
		//		GL11.glPopMatrix();
	}

	public static void glPushAttrib() {
		GlStateManager.pushAttrib();
		//		GL11.glPushAttrib(8256);
	}

	public static void glPushMatrix() {
		GlStateManager.pushMatrix();
		//		GL11.glPushMatrix();
	}

	public static void glRotatef(final float angle, final float x, final float y, final float z) {
		GlStateManager.rotate(angle, x, y, z);
		//		GL11.glRotatef(angle, x, y, z);
	}

	public static void glScaled(final double x, final double y, final double z) {
		GlStateManager.scale(x, y, z);
		//		GL11.glScaled(x, y, z);
	}

	public static void glScalef(final float x, final float y, final float z) {
		GlStateManager.scale(x, y, z);
		//		GL11.glScalef(x, y, z);
	}

	public static void glSetActiveTextureUnit(final int texture) {
		GlStateManager.setActiveTexture(texture);
		//		OpenGlHelper.setActiveTexture(texture);
	}

	public static void glShadeModel(final int mode) {
		GlStateManager.shadeModel(mode);
		//		GL11.glShadeModel(mode);
	}

	public static void glTranslated(final double x, final double y, final double z) {
		GlStateManager.translate(x, y, z);
		//		GL11.glTranslated(x, y, z);
	}

	public static void glTranslatef(final float x, final float y, final float z) {
		GlStateManager.translate(x, y, z);
		//		GL11.glTranslatef(x, y, z);
	}

	public static void glViewport(final int x, final int y, final int width, final int height) {
		GlStateManager.viewport(x, y, width, height);
		//		GL11.glViewport(x, y, width, height);
	}

	public static void glBegin(final int mode) {
		GL11.glBegin(mode);
	}

	public static void glEnd() {
		GL11.glEnd();
	}

	public static int glGenTextures() {
		return GlStateManager.generateTexture();
		// return GL11.glGenTextures();
	}

	public static int glGetTexLevelParameteri(final int target, final int level, final int pname) {
		return GL11.glGetTexLevelParameteri(target, level, pname);
	}

	public static int glGetTexParameteri(final int target, final int pname) {
		return GL11.glGetTexParameteri(target, pname);
	}

	public static void glNormal3f(final float nx, final float ny, final float nz) {
		GL11.glNormal3f(nx, ny, nz);
	}

	public static void glPushAttrib(final int mask) {
		GL11.glPushAttrib(mask);
	}

	public static void glTexImage2D(final int target, final int level, final int internalFormat, final int width, final int height, final int border, final int format, final int type, final IntBuffer pixels) {
		GL11.glTexImage2D(target, level, internalFormat, width, height, border, format, type, pixels);
	}

	public static void glTexParameteri(final int target, final int pname, final int param) {
		GL11.glTexParameteri(target, pname, param);
	}

	public static void glVertex2f(final float x, final float y) {
		GL11.glVertex2f(x, y);
	}

	public static void glVertex3f(final float x, final float y, final float z) {
		GL11.glVertex3f(x, y, z);
	}

	public static void glVertex(final Vec3d vertex) {
		GL11.glVertex3f((float) vertex.xCoord, (float) vertex.yCoord, (float) vertex.zCoord);
	}

	public static void glLineWidth(final float width) {
		GL11.glLineWidth(width);
	}

	public static void glStencilFunc(final int func, final int ref, final int mask) {
		GL11.glStencilFunc(func, ref, mask);
	}

	public static void glStencilMask(final int mask) {
		GL11.glStencilMask(mask);
	}

	public static void glStencilOp(final int fail, final int zfail, final int zpass) {
		GL11.glStencilOp(fail, zfail, zpass);
	}

	private static ContextCapabilities capabilities;

	public static boolean openGl30() {
		if (capabilities==null)
			capabilities = GLContext.getCapabilities();
		return capabilities!=null&&capabilities.OpenGL30;
	}

	public static void glGenerateMipmap(final int target) {
		GL30.glGenerateMipmap(target);
	}

	public static void glBindTexture(final int target, final int texture) {
		if (target==GL11.GL_TEXTURE_2D)
			GlStateManager.bindTexture(texture);
		else
			GL11.glBindTexture(target, texture);
	}

	public static void glDeleteTextures(final int texture) {
		GlStateManager.deleteTexture(texture);
	}
}