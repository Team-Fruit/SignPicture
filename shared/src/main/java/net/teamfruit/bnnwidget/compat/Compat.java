package net.teamfruit.bnnwidget.compat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class Compat {
	public static PositionedSoundRecord createClickSound() {
		return PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F);
	}

	public static class CompatGuiTextField extends GuiTextField {
		public CompatGuiTextField() {
			super(1, getFontRenderer().getFontRendererObj(), 0, 0, 0, 0);
		}

		public int getX() {
			return this.x;
		}

		public void setX(final int value) {
			this.x = value;
		}

		public int getY() {
			return this.y;
		}

		public void setY(final int value) {
			this.y = value;
		}
	}

	public static abstract class CompatFontRendererBase extends FontRenderer {
		public CompatFontRendererBase(final GameSettings gameSettingsIn, final ResourceLocation location, final TextureManager textureManagerIn, final boolean unicode) {
			super(gameSettingsIn, location, textureManagerIn, unicode);
		}

		@Override
		public int drawStringWithShadow(@Nullable final String str, final float x, final float y, final int color) {
			return drawStringWithShadowCompat(str, (int) x, (int) y, color);
		}

		protected abstract int drawStringWithShadowCompat(@Nullable final String str, final float x, final float y, final int color);

		@Override
		public int drawString(@Nullable final String str, final float x, final float y, final int color, final boolean shadow) {
			return drawStringCompat(str, (int) x, (int) y, color, shadow);
		}

		protected abstract int drawStringCompat(@Nullable final String str, final float x, final float y, final int color, final boolean shadow);

		@Override
		public int getWordWrappedHeight(final String str, final int maxLength) {
			return getWordWrappedHeightCompat(str, maxLength);
		}

		public abstract int getWordWrappedHeightCompat(final String str, final int maxLength);
	}

	public static @Nonnull Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}

	public static @Nonnull CompatFontRenderer getFontRenderer() {
		return new CompatFontRenderer(getMinecraft().fontRenderer);
	}

	public static class CompatFontRenderer {
		private final FontRenderer font;

		public CompatFontRenderer(final FontRenderer font) {
			this.font = font;
		}

		public int drawString(final String msg, final float x, final float y, final int color, final boolean shadow) {
			return this.font.drawString(msg, x, y, color, shadow);
		}

		public int drawString(final String msg, final float x, final float y, final int color) {
			return drawString(msg, x, y, color, false);
		}

		public int drawStringWithShadow(final String msg, final float x, final float y, final int color) {
			return drawString(msg, x, y, color, true);
		}

		public int getStringWidth(final @Nullable String s) {
			return this.font.getStringWidth(s);
		}

		public int getStringWidthWithoutFormattingCodes(final @Nullable String s) {
			return getStringWidth(TextFormatting.getTextWithoutFormattingCodes(s));
		}

		public FontRenderer getFontRendererObj() {
			return this.font;
		}
	}

	private static class WVertexImpl implements WVertex {
		public static final @Nonnull Tessellator t = Tessellator.getInstance();
		public static final @Nonnull BufferBuilder w = t.getBuffer();

		public WVertexImpl() {
		}

		@Override
		public void draw() {
			endVertex();
			t.draw();
		}

		@Override
		public @Nonnull WVertex begin(final int mode) {
			w.begin(mode, DefaultVertexFormats.POSITION);
			init();
			return this;
		}

		@Override
		public @Nonnull WVertex beginTexture(final int mode) {
			w.begin(mode, DefaultVertexFormats.POSITION_TEX);
			init();
			return this;
		}

		private void init() {
			this.stack = false;
		}

		private boolean stack;

		@Override
		public @Nonnull WVertex pos(final double x, final double y, final double z) {
			endVertex();
			w.pos(x, y, z);
			this.stack = true;
			return this;
		}

		@Override
		public @Nonnull WVertex tex(final double u, final double v) {
			w.tex(u, v);
			return this;
		}

		@Override
		public @Nonnull WVertex color(final float red, final float green, final float blue, final float alpha) {
			return this.color((int) (red*255.0F), (int) (green*255.0F), (int) (blue*255.0F), (int) (alpha*255.0F));
		}

		@Override
		public @Nonnull WVertex color(final int red, final int green, final int blue, final int alpha) {
			w.putColorRGBA(0, red, green, blue, alpha);
			return this;
		}

		@Override
		public @Nonnull WVertex normal(final float nx, final float ny, final float nz) {
			w.normal(nx, ny, nz);
			return this;
		}

		@Override
		public void setTranslation(final double x, final double y, final double z) {
			w.setTranslation(x, y, z);
		}

		private void endVertex() {
			if (this.stack) {
				this.stack = false;
				w.endVertex();
			}
		}
	}

	public static @Nonnull WVertex getWVertex() {
		return new WVertexImpl();
	}
}
