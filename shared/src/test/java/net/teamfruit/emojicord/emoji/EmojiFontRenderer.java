package net.teamfruit.emojicord.emoji;

#if MC_14_LATER
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
#endif
#if MC_12_LATER
import net.minecraft.client.gui.fonts.IGlyph;
import net.minecraft.client.gui.fonts.TexturedGlyph;
#endif
#if MC_10_LATER
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix4f;
#endif
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.teamfruit.emojicord.CoreInvoke;
import net.teamfruit.emojicord.EmojicordConfig;
import net.teamfruit.emojicord.EmojicordScope;
import net.teamfruit.emojicord.compat.Compat;
import net.teamfruit.emojicord.compat.CompatBaseVertex;
import net.teamfruit.emojicord.compat.CompatVertex;
import net.teamfruit.emojicord.compat.OpenGL;
import net.teamfruit.emojicord.emoji.EmojiContext.EmojiContextAttribute;
import net.teamfruit.emojicord.emoji.EmojiText.EmojiTextElement;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.EnumSet;

@CoreInvoke
public class EmojiFontRenderer {
	@CoreInvoke
	public static boolean shadow;
	@CoreInvoke
	public static int index;

	private static EmojiContext CurrentContext;

	@CoreInvoke
	public static String updateEmojiContext(final String text) {
		if (EmojicordConfig.spec.isAvailable() && EmojicordConfig.RENDER.renderEnabled.get()) {
			final EnumSet<EmojiContextAttribute> attributes = EnumSet.noneOf(EmojiContextAttribute.class);
			final StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
			if (EmojicordScope.instance.checkIsInput(stacks))
				attributes.add(EmojiContextAttribute.CHAT_INPUT);
			if (EmojicordScope.instance.checkIsMessage(stacks))
				attributes.add(EmojiContextAttribute.CHAT_MESSAGE);
			CurrentContext = EmojiContext.EmojiContextCache.instance.getContext(text, attributes);
			return CurrentContext.text;
		}
		CurrentContext = null;
		return text;
	}

	/*
	public static void main(final String... args) {
		Log.log.info("Start");
		final int count = 1_000_000;
		Log.log.info("Count: "+count);
		final long time = System.nanoTime();
		for (int i = 0; i<count; i++)
			Thread.currentThread().getStackTrace();
		final long diff = System.nanoTime()-time;
		Log.log.info("Finish");
		Log.log.info(diff/1e9);
		Log.log.info(diff/1e9/count);
	}
	*/

	#if MC_12_LATER
	public static abstract class CompatGlyph implements IGlyph {
		public final float width;
		public final float height;

		public CompatGlyph(final float width, final float height) {
			this.width = width;
			this.height = height;
		}

		@Override
		public float getAdvance() {
			return this.width;
		}

		@Override
		public float getBoldOffset() {
			return 0;
		}

		@Override
		public float getShadowOffset() {
			return 0;
		}
	}

	@CoreInvoke
	public static @Nullable EmojiGlyph getEmojiGlyph(final char c, final int index) {
		if (CurrentContext!=null) {
			final EmojiTextElement emojiElement = CurrentContext.emojis.get(index);
			if (emojiElement!=null) {
				final EmojiId emojiId = emojiElement.id;
				if (emojiId!=null)
					return new EmojiGlyph(emojiId);
			}
		}
		return null;
	}

	@CoreInvoke
	public static class EmojiGlyph extends CompatGlyph {
		public static final float GlyphWidth = 10;
		public static final float GlyphHeight = 10;

		private final EmojiId emojiId;

		public EmojiGlyph(final EmojiId emojiId) {
			super(GlyphWidth, GlyphHeight);
			this.emojiId = emojiId;
		}

		@CoreInvoke
		public EmojiTexturedGlyph getTexturedGlyph() {
			return new EmojiTexturedGlyph(this.emojiId);
		}
	}

	@CoreInvoke
	public static class EmojiTexturedGlyph extends TexturedGlyph {
		public EmojiTexturedGlyph(final ResourceLocation texture, final float width, final float height) {
			super( #if MC_14_LATER RenderType.getText(texture), RenderType.getTextSeeThrough(texture) #else texture #endif , 0, 1, 0, 1, 0, width, 0+3, height+3);
		}

		public EmojiTexturedGlyph(final EmojiId emojiId) {
			this(EmojiObject.EmojiObjectCache.instance.getEmojiObject(emojiId).loadAndGetResourceLocation(), EmojiGlyph.GlyphWidth, EmojiGlyph.GlyphHeight);
		}

		#if MC_14_LATER
		@Override
		public void render(boolean italic, float x, float y, Matrix4f matrix, IVertexBuilder vbuilder, float red, float green, float blue, float alpha, int packedLight) {
			if (!shadow)
				super.render(italic, x, y, matrix, vbuilder, 1, 1, 1, alpha, packedLight);
		}
		#else
		@Override
		public void render(final TextureManager textureManager, final boolean hasShadow, final float x, final float y, final BufferBuilder vbuilder, final float red, final float green, final float blue, final float alpha) {
			if (!shadow)
				super.render(textureManager, hasShadow, x, y, vbuilder, 1, 1, 1, alpha);
		}
		#endif
	}
	#else
	@CoreInvoke
	public static boolean renderEmojiChar(final char c, final boolean italic, final float x, final float y, final float red, final float green, final float blue, final float alpha) {
		if (CurrentContext != null) {
			final EmojiTextElement emojiElement = CurrentContext.emojis.get(index);
			if (emojiElement != null) {
				final EmojiId emojiId = emojiElement.id;
				if (emojiId != null) {
					final EmojiObject emoji = EmojiObject.EmojiObjectCache.instance.getEmojiObject(emojiId);
					if (!shadow) {
						Compat.getMinecraft().getTextureManager().bindTexture(emoji.loadAndGetResourceLocation());
						renderEmoji(emoji, x, y, red, green, blue, alpha);
					}
					return c == EmojiContext.EMOJI_REPLACE_CHARACTOR;
				}
			}
		}
		return false;
	}

	public static void renderEmoji(final EmojiObject emoji, final float x, final float y, final float red, final float green, final float blue, final float alpha) {
		final float textureSize = 16.0F;
		final float textureX = 0.0F / textureSize;
		final float textureY = 0.0F / textureSize;
		final float textureOffset = 16.0F / textureSize;
		final float size = 10.0F;
		final float offsetY = 1.0F;
		final float offsetX = 0.0F;

		OpenGL.glPushAttrib();

		final int rgba = OpenGL.glGetColorRGBA();
		OpenGL.glColor4f(1.0F, 1.0F, 1.0F, (OpenGL.glGetColorRGBA() >> 24 & 0xff) / 256f);

		//OpenGL.glEnable(GL11.GL_BLEND);
		//OpenGL.glEnable(GL11.GL_ALPHA_TEST);

		//OpenGL.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		//OpenGL.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		//OpenGL.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		//OpenGL.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		final CompatBaseVertex bufferbuilder = CompatVertex.getTessellator();
		bufferbuilder.beginTexture(GL11.GL_QUADS);
		bufferbuilder.pos(x - offsetX, y - offsetY, 0.0F).tex(textureX, textureY);
		bufferbuilder.pos(x - offsetX, y + size - offsetY, 0.0F).tex(textureX, textureY + textureOffset);
		bufferbuilder.pos(x - offsetX + size, y + size - offsetY, 0.0F).tex(textureX + textureOffset, textureY + textureOffset);
		bufferbuilder.pos(x - offsetX + size, y - offsetY, 0.0F).tex(textureX + textureOffset, textureY);
		bufferbuilder.draw();
		//OpenGL.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		//OpenGL.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

		//OpenGL.glDisable(GL11.GL_ALPHA_TEST);
		//OpenGL.glDisable(GL11.GL_BLEND);

		OpenGL.glColorRGBA(rgba);

		OpenGL.glPopAttrib();
	}
	#endif
}
