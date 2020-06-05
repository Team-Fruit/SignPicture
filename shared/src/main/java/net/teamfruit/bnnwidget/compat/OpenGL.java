package net.teamfruit.bnnwidget.compat;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.annotation.Nullable;

import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;

/**
 * OpenGL操作のラッパーです。
 * <p>
 * 主にMinecraftのバージョン間の差異を吸収します。
 *
 * @author TeamFruit
 */
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

	public static boolean glIsEnabled(final int attrib) {
		return GL11.glIsEnabled(attrib);
	}

	public static boolean glEnabled(final int attrib) {
		if (!glIsEnabled(attrib)) {
			glEnable(attrib);
			return true;
		}
		return false;
	}

	public static boolean glDisabled(final int attrib) {
		if (glIsEnabled(attrib)) {
			glEnable(attrib);
			return true;
		}
		return false;
	}

	public static void glHint(final int target, final int mode) {
		GL11.glHint(target, mode);
	}

	public static void glAlphaFunc(final int func, final float ref) {
		GlStateManager.alphaFunc(func, ref);
		// GL11.glAlphaFunc(func, ref);
	}

	public static void glBlendFunc(final int sfactor, final int dfactor) {
		GlStateManager.blendFunc(sfactor, dfactor);
		// GL11.glBlendFunc(sfactor, dfactor);
	}

	public static void glBlendFuncSeparate(final int srcFactor, final int dstFactor, final int srcFactorAlpha, final int dstFactorAlpha) {
		GlStateManager.tryBlendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
		// OpenGlHelper.glBlendFunc(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
	}

	public static void glCallList(final int list) {
		GlStateManager.callList(list);
		// GL11.glCallList(list);
	}

	public static void glClear(final int mask) {
		GlStateManager.clear(mask);
		// GL11.glClear(mask);
	}

	public static void glClearColor(
			final float red, final float green, final float blue,
			final float alpha
	) {
		GlStateManager.clearColor(red, green, blue, alpha);
		// GL11.glClearColor(red, green, blue, alpha);
	}

	public static void glClearDepth(final double depth) {
		GlStateManager.clearDepth(depth);
		// GL11.glClearDepth(depth);
	}

	public static void glColor3f(final float red, final float green, final float blue) {
		GlStateManager.color(red, green, blue, 1.0F);
		// GL11.glColor3f(red, green, blue);
	}

	public static void glColor4f(final float red, final float green, final float blue, final float alpha) {
		GlStateManager.color(red, green, blue, alpha);
		// GL11.glColor4f(red, green, blue, alpha);
	}

	public static void glColor4i(final int red, final int green, final int blue, final int alpha) {
		glColor4f(red/255f, green/255f, blue/255f, alpha/255f);
	}

	public static void glColor4ub(final byte red, final byte green, final byte blue, final byte alpha) {
		glColor4i(red&0xff, green&0xff, blue&0xff, alpha&0xff);
		// GL11.glColor4ub(red, green, blue, alpha);
	}

	public static void glColorRGB(final int rgb) {
		final int value = 0xff000000|rgb;
		glColor4i(value>>16&0xff, value>>8&0xff, value>>0&0xff, value>>24&0xff);
	}

	public static void glColorRGBA(final int rgba) {
		glColor4i(rgba>>16&0xff, rgba>>8&0xff, rgba>>0&0xff, rgba>>24&0xff);
	}

	public static void glColor(final Color color) {
		glColor4i(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public static void glColor(final org.lwjgl.util.Color color) {
		glColor4i(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	private static FloatBuffer buf = GLAllocation.createDirectFloatBuffer(16);

	private static int toColorCode(final int r, final int g, final int b, final int a) {
		return (a&0xff)<<24|(r&0xff)<<16|(g&0xff)<<8|(b&0xff)<<0;
	}

	private static int toColorCode(final float r, final float g, final float b, final float a) {
		return toColorCode((int) (r*255+.5f), (int) (g*255+.5f), (int) (b*255+.5f), (int) (a*255+.5f));
	}

	public static int glGetColorRGBA() {
		buf.clear();
		GL11.glGetFloat(GL11.GL_CURRENT_COLOR, buf);
		final float r = buf.get(0);
		final float g = buf.get(1);
		final float b = buf.get(2);
		final float a = buf.get(3);
		return toColorCode(r, g, b, a);
	}

	public static Color glGetColor() {
		buf.clear();
		GL11.glGetFloat(GL11.GL_CURRENT_COLOR, buf);
		final float r = Math.min(1f, buf.get(0));
		final float g = Math.min(1f, buf.get(1));
		final float b = Math.min(1f, buf.get(2));
		final float a = Math.min(1f, buf.get(3));
		return new Color(r, g, b, a);
	}

	public static org.lwjgl.util.Color glGetLwjglColor() {
		buf.clear();
		GL11.glGetFloat(GL11.GL_CURRENT_COLOR, buf);
		final float r = buf.get(0);
		final float g = buf.get(1);
		final float b = buf.get(2);
		final float a = buf.get(3);
		return new org.lwjgl.util.Color((int) (r*255+0.5)&0xff, (int) (g*255+0.5)&0xff, (int) (b*255+0.5)&0xff, (int) (a*255+0.5)&0xff);
	}

	public static void glColorMask(final boolean red, final boolean green, final boolean blue, final boolean alpha) {
		GlStateManager.colorMask(red, green, blue, alpha);
		// GL11.glColorMask(red, green, blue, alpha);
	}

	public static void glColorMaterial(final int face, final int mode) {
		GlStateManager.colorMaterial(face, mode);
		// GL11.glColorMaterial(face, mode);
	}

	public static void glCullFace(final int mode) {
		// GlStateManager.cullFace(mode);
		if (mode==GlStateManager.CullFace.BACK.mode)
			GlStateManager.cullFace(GlStateManager.CullFace.BACK);
		else if (mode==GlStateManager.CullFace.FRONT.mode)
			GlStateManager.cullFace(GlStateManager.CullFace.FRONT);
		else if (mode==GlStateManager.CullFace.FRONT_AND_BACK.mode)
			GlStateManager.cullFace(GlStateManager.CullFace.FRONT_AND_BACK);
		// GL11.glCullFace(mode);
	}

	public static void glDepthFunc(final int func) {
		GlStateManager.depthFunc(func);
		// GL11.glDepthFunc(func);
	}

	public static void glDepthMask(final boolean flag) {
		GlStateManager.depthMask(flag);
		// GL11.glDepthMask(flag);
	}

	public static void glGetFloat(final int pname, final FloatBuffer params) {
		GlStateManager.getFloat(pname, params);
		// GL11.glGetFloat(pname, params);
	}

	public static void glLoadIdentity() {
		GlStateManager.loadIdentity();
		// GL11.glLoadIdentity();
	}

	public static void glLogicOp(final int opcode) {
		GlStateManager.colorLogicOp(opcode);
		// GL11.glLogicOp(opcode);
	}

	public static void glMatrixMode(final int mode) {
		GlStateManager.matrixMode(mode);
		// GL11.glMatrixMode(mode);
	}

	public static void glMultMatrix(final FloatBuffer m) {
		GlStateManager.multMatrix(m);
		// GL11.glMultMatrix(m);
	}

	public static void glOrtho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
		GlStateManager.ortho(left, right, bottom, top, zNear, zFar);
		// GL11.glOrtho(left, right, bottom, top, zNear, zFar);
	}

	public static void glPolygonOffset(final float factor, final float units) {
		GlStateManager.doPolygonOffset(factor, units);
		// GL11.glPolygonOffset(factor, units);
	}

	public static void glPopAttrib() {
		GlStateManager.popAttrib();
		// GL11.glPopAttrib();
	}

	public static void glPopMatrix() {
		GlStateManager.popMatrix();
		// GL11.glPopMatrix();
	}

	public static void glPushAttrib() {
		GlStateManager.pushAttrib();
		// GL11.glPushAttrib(8256);
	}

	public static void glPushMatrix() {
		GlStateManager.pushMatrix();
		// GL11.glPushMatrix();
	}

	public static void glRotatef(final float angle, final float x, final float y, final float z) {
		GlStateManager.rotate(angle, x, y, z);
		// GL11.glRotatef(angle, x, y, z);
	}

	public static void glScaled(final double x, final double y, final double z) {
		GlStateManager.scale(x, y, z);
		// GL11.glScaled(x, y, z);
	}

	public static void glScalef(final float x, final float y, final float z) {
		GlStateManager.scale(x, y, z);
		// GL11.glScalef(x, y, z);
	}

	public static void glSetActiveTextureUnit(final int texture) {
		GlStateManager.setActiveTexture(texture);
		// OpenGlHelper.setActiveTexture(texture);
	}

	public static void glShadeModel(final int mode) {
		GlStateManager.shadeModel(mode);
		// GL11.glShadeModel(mode);
	}

	public static void glTranslated(final double x, final double y, final double z) {
		GlStateManager.translate(x, y, z);
		// GL11.glTranslated(x, y, z);
	}

	public static void glTranslatef(final float x, final float y, final float z) {
		GlStateManager.translate(x, y, z);
		// GL11.glTranslatef(x, y, z);
	}

	public static void glViewport(final int x, final int y, final int width, final int height) {
		GlStateManager.viewport(x, y, width, height);
		// GL11.glViewport(x, y, width, height);
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

	private static @Nullable ContextCapabilities capabilities;

	public static boolean openGl30() {
		if (capabilities==null)
			capabilities = GLContext.getCapabilities();
		final ContextCapabilities cap = capabilities;
		return cap!=null&&cap.OpenGL30;
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
		// TextureUtil.deleteTexture(texture);
	}
}